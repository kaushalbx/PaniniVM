package dev.panini.execution

import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.Procedure
import dev.panini.vyakaranam.ast.ProcedureModifiers
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Scope

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(
        override val text: String,
        val ukti: dev.panini.vyakaranam.ast.Ukti? = null,
        val isNishedha: Boolean = false,
    ) : PvmScriptStatement {
        val program: ProgramNode?
            get() = ukti?.body ?: PurvaparaPipelineCompiler.compile(text)
    }

    /**
     * A named kriyā definition using the संज्ञा-सूत्र pattern:
     *
     *     गुण् + ल्युट् + सुँ इति संज्ञा ।
     *         संख्या + अम् संख्या + अम् च गुण् + णिच् + लोट् + सिप् ।
     *     इति ॥
     *
     * [nameSegmented] is the segmented prātipadika form (e.g. "गुण् + ल्युट् + सुँ").
     * [body] contains the vākya sentences that form the procedure.
     */
    data class SamjnaDefinition(
        val procedure: Procedure,
        val body: List<Sentence>,
    ) : PvmScriptStatement {
        override val text: String get() = procedure.sourceText
        val nameSegmented: String get() = procedure.name
        val domainStem: String? get() = procedure.domain
        val isInternal: Boolean get() = procedure.modifiers.isInternal
        val isApavada: Boolean get() = procedure.modifiers.isApavada
        val isAntaranga: Boolean get() = procedure.modifiers.isAntaranga
        val isNitya: Boolean get() = procedure.modifiers.isNitya
    }

    /**
     * An अधिकार-सूत्र (governing domain scope declaration):
     *
     *     गणित + अम् इति अधिकारः ।
     */
    data class AdhikaraDefinition(
        val scope: Scope,
    ) : PvmScriptStatement {
        override val text: String get() = scope.sourceText
        val domainSegmented: String get() = scope.domain
    }
}

enum class PvmSourceKind {
    EMPTY,
    UTTERANCE,
    SCRIPT,
}

object PvmScript {

    fun classify(source: String): PvmSourceKind {
        val statements = parse(source)
        val loneDefinition = statements.singleOrNull() as? PvmScriptStatement.SamjnaDefinition
        if (loneDefinition != null && loneDefinition.body.isEmpty() && !hasExplicitDefinitionMarker(source)) {
            return PvmSourceKind.UTTERANCE
        }
        return classify(statements)
    }

    fun classify(statements: List<PvmScriptStatement>): PvmSourceKind = when {
        statements.isEmpty() -> PvmSourceKind.EMPTY
        statements.size == 1 && statements.single() is PvmScriptStatement.Sentence -> PvmSourceKind.UTTERANCE
        else -> PvmSourceKind.SCRIPT
    }

    private fun hasExplicitDefinitionMarker(source: String): Boolean =
        SamjnaDefinitionMarkerParser.hasExplicitMarker(source)

    fun parse(source: String): List<PvmScriptStatement> {
        val rawLines = source.lines()

        val samjnaDefinitions = mutableListOf<PvmScriptStatement.SamjnaDefinition>()
        val nonSamjnaLines = mutableListOf<String>()

        var inBlock = false
        var currentName = ""
        var currentBodyLines = mutableListOf<String>()
        var currentBlockText = mutableListOf<String>()

        for (line in rawLines) {
            val stripped = stripComment(line).trim()

            if (!inBlock) {
                val rawHeaderName = extractSamjnaHeaderName(stripped)
                if (rawHeaderName != null) {
                    inBlock = true
                    currentName = rawHeaderName
                    currentBodyLines = mutableListOf()
                    currentBlockText = mutableListOf(line)
                } else {
                    nonSamjnaLines += line
                }
            } else {
                currentBlockText += line
                val endsWithDoubleDanda = PvmBlockBoundary.closes(stripped)
                if (PvmBlockBoundary.carriesBody(stripped)) {
                    currentBodyLines += line
                }
                if (endsWithDoubleDanda) {
                    val bodyText = currentBodyLines
                        .map { stripComment(it).trim() }
                        .filter { it.isNotEmpty() }
                        .joinToString(" ")
                    val bodySentences = parseSentences(bodyText)

                    samjnaDefinitions += samjnaDefinition(currentName, bodySentences, currentBlockText)
                    inBlock = false
                }
            }
        }

        if (inBlock) {
            val bodyText = currentBodyLines
                .map { stripComment(it).trim() }
                .filter { it.isNotEmpty() }
                .joinToString(" ")
            val bodySentences = parseSentences(bodyText)

            samjnaDefinitions += samjnaDefinition(currentName, bodySentences, currentBlockText)
            inBlock = false
        }

        val adhikaraDefinitions = mutableListOf<PvmScriptStatement.AdhikaraDefinition>()
        val regularNonSamjnaLines = mutableListOf<String>()

        nonSamjnaLines.forEach { line ->
            val stripped = stripComment(line).trim()
            val adhikaraDomain = extractAdhikaraDomain(stripped)
            if (adhikaraDomain != null) {
                adhikaraDefinitions += PvmScriptStatement.AdhikaraDefinition(
                    scope = Scope(
                        sourceText = line,
                        domain = adhikaraDomain,
                    ),
                )
            } else {
                regularNonSamjnaLines += line
            }
        }

        val sanitizedLines = regularNonSamjnaLines
            .map { stripComment(it).trim() }
            .filter { it.isNotEmpty() }

        val sentences = if (sanitizedLines.isEmpty()) {
            emptyList()
        } else {
            parseSentences(sanitizedLines.joinToString(" "))
        }

        return samjnaDefinitions + adhikaraDefinitions + sentences
    }

    private fun samjnaDefinition(
        header: String,
        body: List<PvmScriptStatement.Sentence>,
        blockText: List<String>,
    ): PvmScriptStatement.SamjnaDefinition {
        val internalHeader = splitInternalHeader(header)
        val rawName = internalHeader.nominalSource
        val parsed = SamjnaDefinitionMarkerParser.qualifiers(rawName)
        val declarationSource = parsed?.declarationSource ?: rawName
        val methodHeader = TaddhitaStructEngine.detectMethodHeader(declarationSource)
        val cleanName = methodHeader?.second ?: declarationSource
        val qualifiers = parsed?.qualifiers.orEmpty()
        return PvmScriptStatement.SamjnaDefinition(
            procedure = Procedure(
                sourceText = blockText.joinToString("\n"),
                name = cleanName,
                domain = methodHeader?.first,
                body = body.mapNotNull(PvmScriptStatement.Sentence::program),
                modifiers = ProcedureModifiers(
                    isInternal = internalHeader.isInternal,
                    isApavada = SamjnaDefinitionQualifier.APAVADA in qualifiers,
                    isAntaranga = SamjnaDefinitionQualifier.ANTARANGA in qualifiers,
                    isNitya = SamjnaDefinitionQualifier.NITYA in qualifiers,
                ),
            ),
            body = body,
        )
    }

    internal fun isAdhikaraLine(line: String): Boolean {
        return AdhikaraHeaderParser.domain(line) != null
    }

    internal fun extractAdhikaraDomain(line: String): String? = AdhikaraHeaderParser.domain(line)

    internal fun extractSamjnaHeaderName(line: String): String? {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || isAdhikaraLine(trimmed)) return null

        SamjnaDefinitionMarkerParser.headerPrefix(trimmed)?.let { return it }

        val internalHeader = splitInternalHeader(trimmed)
        val nominalSource = internalHeader.nominalSource
        if (SamjnaHeaderIdentityParser.parse(nominalSource) == null) return null
        val ukti = parser.parseOrNull(nominalSource.trimEnd('।', '॥', ' ')) ?: return null
        val hasAccusative = ukti.vakyas.flatMap { it.padas }
            .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>()
            .any {
                dev.panini.core.SupAffix.fromUpadesha(it.sup.text)?.vibhakti ==
                    dev.panini.core.Vibhakti.DVITIYA
            }
        if (hasAccusative) return null
        val normalized = nominalSource.trimEnd('।', '॥', ' ').trim()
        return if (internalHeader.isInternal) "${internalHeader.marker} $normalized" else normalized
    }

    private fun stripComment(line: String): String {
        val hashIdx = line.indexOf('#')
        val slashIdx = line.indexOf("//")
        val commentIdx = when {
            hashIdx != -1 && slashIdx != -1 -> minOf(hashIdx, slashIdx)
            hashIdx != -1 -> hashIdx
            else -> slashIdx
        }
        return if (commentIdx != -1) line.substring(0, commentIdx) else line
    }

    private val parser = dev.panini.vyakaranam.parser.PaniniParser()

    private data class InternalHeader(
        val marker: String? = null,
        val nominalSource: String,
    ) {
        val isInternal: Boolean get() = marker != null
    }

    private fun splitInternalHeader(header: String): InternalHeader {
        val separator = header.indexOf(' ')
        if (separator <= 0) return InternalHeader(nominalSource = header.trim())
        val marker = header.substring(0, separator)
        val isInternal = MulaPratipadikaIdentity.fromText(marker) == MulaPratipadikaIdentity.ANTARANGA
        return if (isInternal) {
            InternalHeader(marker, header.substring(separator + 1).trim())
        } else {
            InternalHeader(nominalSource = header.trim())
        }
    }

    private fun parseSentences(joinedText: String): List<PvmScriptStatement.Sentence> {
        if (joinedText.isBlank()) return emptyList()
        val sentenceRegex = Regex("""[^।॥]+[।॥]*""")
        return sentenceRegex.findAll(joinedText)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() }
            .map { text ->
                val trimmed = text.trim()
                val ukti = parser.parseOrNull(trimmed)
                val isNishedha = ukti?.vakyas?.any { vakya ->
                    vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.AvyayaPada>()
                        .any { it.function == dev.panini.vyakaranam.ast.AvyayaFunction.NISHEDHA }
                } == true
                PvmScriptStatement.Sentence(text = text, ukti = ukti, isNishedha = isNishedha)
            }
            .toList()
    }
}
