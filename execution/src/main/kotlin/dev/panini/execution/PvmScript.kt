package dev.panini.execution

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(
        override val text: String,
        val ukti: dev.panini.vyakaranam.ast.Ukti? = null,
        val isNishedha: Boolean = false,
    ) : PvmScriptStatement

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
        val nameSegmented: String,
        val body: List<Sentence>,
        override val text: String,
        val domainStem: String? = null,
        val isInternal: Boolean = false,
        val isApavada: Boolean = false,
        val isAntaranga: Boolean = false,
        val isNitya: Boolean = false,
    ) : PvmScriptStatement

    /**
     * An अधिकार-सूत्र (governing domain scope declaration):
     *
     *     गणित + अम् इति अधिकारः ।
     */
    data class AdhikaraDefinition(
        val domainSegmented: String,
        override val text: String,
    ) : PvmScriptStatement
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
                    domainSegmented = adhikaraDomain,
                    text = line,
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
        val isInternal = INTERNAL_PREFIXES.any(header::startsWith)
        val rawName = INTERNAL_PREFIXES.fold(header) { name, prefix -> name.removePrefix(prefix) }.trim()
        val parsed = SamjnaDefinitionMarkerParser.qualifiers(rawName)
        val declarationSource = parsed?.declarationSource ?: rawName
        val methodHeader = TaddhitaStructEngine.detectMethodHeader(declarationSource)
        val cleanName = methodHeader?.second ?: declarationSource
        val qualifiers = parsed?.qualifiers.orEmpty()
        return PvmScriptStatement.SamjnaDefinition(
            nameSegmented = cleanName,
            body = body,
            text = blockText.joinToString("\n"),
            domainStem = methodHeader?.first,
            isInternal = isInternal,
            isApavada = SamjnaDefinitionQualifier.APAVADA in qualifiers,
            isAntaranga = SamjnaDefinitionQualifier.ANTARANGA in qualifiers,
            isNitya = SamjnaDefinitionQualifier.NITYA in qualifiers,
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

        val internalPrefix = INTERNAL_PREFIXES.firstOrNull(trimmed::startsWith)
        val nominalSource = internalPrefix?.let(trimmed::removePrefix)?.trim() ?: trimmed
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
        return if (internalPrefix == null) normalized else "$internalPrefix$normalized"
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

    private val INTERNAL_PREFIXES = listOf("अन्तरङ्गा ", "अन्तरङ्ग ")

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
