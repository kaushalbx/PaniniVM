package dev.panini.execution

import dev.panini.execution.binding.NumeralAstNormalizer
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.PrakriyaModifiers
import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.invocations
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(
        override val text: String,
        val ukti: dev.panini.vyakaranam.ast.Ukti? = null,
        val isNishedha: Boolean = false,
        val semantics: PvmSentenceSemantics = PvmSentenceSemantics.Executable,
    ) : PvmScriptStatement {
        val program: ProgramNode?
            get() = ukti?.body
    }

    /**
     * A named reusable prakriyā declaration:
     *
     *     गुण् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
     *         संख्या + अम् संख्या + अम् च गुण् + णिच् + लोट् + सिप् ।
     *     इति ॥
     *
     * [nameSegmented] is the segmented prātipadika form (e.g. "गुण् + ल्युट् + सुँ").
     * [body] contains the vākya sentences that form the prakriyā.
     */
    data class PrakriyaDefinition(
        val prakriya: Prakriya,
        val body: List<Sentence>,
    ) : PvmScriptStatement {
        override val text: String get() = prakriya.sourceText
        val nameSegmented: String get() = prakriya.name
        val domainStem: String? get() = prakriya.domain
        val isInternal: Boolean get() = prakriya.modifiers.visibility == PrakriyaVisibility.INTERNAL
        val isApavada: Boolean get() = prakriya.modifiers.precedence == PrakriyaPrecedence.APAVADA
        val isAntaranga: Boolean get() = prakriya.modifiers.precedence == PrakriyaPrecedence.ANTARANGA
        val isNitya: Boolean get() = prakriya.modifiers.precedence == PrakriyaPrecedence.NITYA
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

    /** One inclusive numeric range governing subsequent executable sentences. */
    data class RangeDefinition(
        val range: SanskritValue.Range,
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
        val loneDefinition = statements.singleOrNull() as? PvmScriptStatement.PrakriyaDefinition
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
        PrakriyaDefinitionMarkerParser.hasExplicitMarker(source)

    fun parse(source: String): List<PvmScriptStatement> {
        val rawLines = source.lines()

        val prakriyaDefinitions = mutableListOf<PvmScriptStatement.PrakriyaDefinition>()
        val nonPrakriyaLines = mutableListOf<String>()

        var inBlock = false
        var currentName = ""
        var currentBodyLines = mutableListOf<String>()
        var currentBlockText = mutableListOf<String>()

        for (line in rawLines) {
            val stripped = stripComment(line).trim()

            if (!inBlock) {
                val rawHeaderName = extractPrakriyaHeaderName(stripped)
                if (rawHeaderName != null) {
                    inBlock = true
                    currentName = rawHeaderName
                    currentBodyLines = mutableListOf()
                    currentBlockText = mutableListOf(line)
                } else {
                    nonPrakriyaLines += line
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

                    prakriyaDefinitions += prakriyaDefinition(currentName, bodySentences, currentBlockText)
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

            prakriyaDefinitions += prakriyaDefinition(currentName, bodySentences, currentBlockText)
            inBlock = false
        }

        val adhikaraDefinitions = mutableListOf<PvmScriptStatement.AdhikaraDefinition>()
        val rangeDefinitions = mutableListOf<PvmScriptStatement.RangeDefinition>()
        val regularNonPrakriyaLines = mutableListOf<String>()

        nonPrakriyaLines.forEach { line ->
            val stripped = stripComment(line).trim()
            val range = extractRangeDefinition(stripped)
            val adhikaraDomain = extractAdhikaraDomain(stripped)
            if (range != null) {
                rangeDefinitions += PvmScriptStatement.RangeDefinition(range, line)
            } else if (adhikaraDomain != null) {
                adhikaraDefinitions += PvmScriptStatement.AdhikaraDefinition(
                    scope = Scope(
                        sourceText = line,
                        domain = adhikaraDomain,
                    ),
                )
            } else {
                regularNonPrakriyaLines += line
            }
        }

        val sanitizedLines = regularNonPrakriyaLines
            .map { stripComment(it).trim() }
            .filter { it.isNotEmpty() }

        val sentences = if (sanitizedLines.isEmpty()) {
            emptyList()
        } else {
            parseSentences(sanitizedLines.joinToString(" "))
        }

        return prakriyaDefinitions + adhikaraDefinitions + rangeDefinitions + sentences
    }

    private fun isRangeDefinitionLine(line: String): Boolean = extractRangeDefinition(line) != null

    private fun extractRangeDefinition(line: String): SanskritValue.Range? {
        val ukti = parser.parseOrNull(line.trimEnd('।', '॥', ' ')) ?: return null
        val (rangePadas, reportingPadas) = (ukti.body as? Quotation)?.let { quotation ->
            quotation.quoted.vakya.padas to quotation.reporting.invocations().flatMap { it.vakya.padas }
        } ?: run {
            val padas = ukti.grammaticalVakyas().flatMap { it.padas }
            val iti = padas.indexOfFirst { (it as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE }
            if (iti < 0) return null
            padas.take(iti) to padas.drop(iti + 1)
        }
        val marker = reportingPadas
            .filterIsInstance<SubantaPada>()
            .singleOrNull() ?: return null
        if ((marker.pratipadika as? MulaPratipadika)?.text != "सीमा" ||
            SupAffix.fromUpadesha(marker.sup.text)?.vibhakti != Vibhakti.PRATHAMA
        ) return null
        val evaluator = dev.panini.sankhya.SankhyaEvaluator()
        fun value(pada: dev.panini.vyakaranam.ast.SankhyaPada): SanskritValue.Sankhya = SanskritValue.Sankhya(
            pada.value ?: evaluator.evaluateStems(pada.stems).value,
            pada.stems.joinToString(" "),
        )
        val explicitRange = rangePadas.filterIsInstance<ParyantaRangePada>().singleOrNull()
        val bounds = if (explicitRange != null) {
            listOf(value(explicitRange.lowerLimit), value(explicitRange.upperLimit))
        } else {
            rangePadas.mapNotNull { pada ->
                when (pada) {
                    is SankhyaPada -> SanskritValue.Sankhya(
                        pada.value ?: evaluator.evaluateStems(pada.stems).value,
                        pada.stems.joinToString(" "),
                    ) to pada.sup.text
                    is SubantaPada -> {
                        val normalized = NumeralAstNormalizer.normalize(pada)
                        val number = (normalized.pratipadika as? SankhyaPratipadika)?.semanticValue
                            ?: return@mapNotNull null
                        number to normalized.sup.text
                    }
                    else -> null
                }
            }.let { numeric ->
                fun bound(vibhakti: Vibhakti): SanskritValue.Sankhya? = numeric.firstOrNull { (_, sup) ->
                    SupAffix.candidates(sup).any { it.vibhakti == vibhakti }
                }?.first
                listOfNotNull(bound(Vibhakti.PANCHAMI), bound(Vibhakti.SAPTAMI))
            }
        }
        if (bounds.size != 2) return null
        val minimum = bounds[0]
        val maximum = bounds[1]
        return runCatching { SanskritValue.Range(minimum, maximum) }.getOrNull()
    }

    private fun prakriyaDefinition(
        header: String,
        body: List<PvmScriptStatement.Sentence>,
        blockText: List<String>,
    ): PvmScriptStatement.PrakriyaDefinition {
        require(body.all { it.program != null }) {
            val invalid = body.first { it.program == null }.text
            "A reusable प्रक्रिया body contains invalid Sanskrit: '$invalid'."
        }
        val parsed = PrakriyaDefinitionMarkerParser.qualifiers(header)
        val declarationSource = parsed?.declarationSource ?: header
        val methodHeader = TaddhitaStructEngine.detectMethodHeader(declarationSource)
        val cleanName = methodHeader?.second ?: declarationSource
        val qualifiers = parsed?.qualifiers.orEmpty()
        val isInternalProcedure =
            PrakriyaDefinitionQualifier.PRAKRIYA in qualifiers &&
                PrakriyaDefinitionQualifier.ANTARANGA in qualifiers
        val precedence = when {
            PrakriyaDefinitionQualifier.APAVADA in qualifiers -> PrakriyaPrecedence.APAVADA
            PrakriyaDefinitionQualifier.NITYA in qualifiers -> PrakriyaPrecedence.NITYA
            PrakriyaDefinitionQualifier.ANTARANGA in qualifiers -> PrakriyaPrecedence.ANTARANGA
            else -> PrakriyaPrecedence.DEFAULT
        }
        return PvmScriptStatement.PrakriyaDefinition(
            prakriya = Prakriya(
                sourceText = blockText.joinToString("\n"),
                name = cleanName,
                domain = methodHeader?.first,
                body = body.mapNotNull(PvmScriptStatement.Sentence::program),
                modifiers = PrakriyaModifiers(
                    visibility = if (isInternalProcedure) {
                        PrakriyaVisibility.INTERNAL
                    } else {
                        PrakriyaVisibility.PUBLIC
                    },
                    precedence = precedence,
                ),
            ),
            body = body,
        )
    }

    internal fun isAdhikaraLine(line: String): Boolean {
        return AdhikaraHeaderParser.domain(line) != null
    }

    internal fun extractAdhikaraDomain(line: String): String? = AdhikaraHeaderParser.domain(line)

    internal fun extractPrakriyaHeaderName(line: String): String? {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || isAdhikaraLine(trimmed) || isRangeDefinitionLine(trimmed)) return null

        // Preserve the complete grammatical declaration so its AST qualifiers
        // remain available when the Prakriya node is constructed.
        PrakriyaDefinitionMarkerParser.headerPrefix(trimmed)?.let { return trimmed }

        val ukti = parser.parseOrNull(trimmed.trimEnd('।', '॥', ' ')) ?: return null
        if (ukti.grammaticalVakyas().flatMap { it.padas }
                .any { (it as? dev.panini.vyakaranam.ast.AvyayaPada)?.function ==
                    dev.panini.vyakaranam.ast.AvyayaFunction.QUOTATIVE }
        ) return null
        if (PrakriyaHeaderIdentityParser.parse(trimmed) == null) return null
        if (ukti.grammaticalVakyas().flatMap { it.padas }
                .any { it is dev.panini.vyakaranam.ast.TingantaPada }
        ) {
            return null
        }
        val hasAccusative = ukti.grammaticalVakyas().flatMap { it.padas }
            .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>()
            .any {
                dev.panini.core.SupAffix.fromUpadesha(it.sup.text)?.vibhakti ==
                    dev.panini.core.Vibhakti.DVITIYA
            }
        if (hasAccusative) return null
        return trimmed.trimEnd('।', '॥', ' ').trim()
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

    private fun parseSentences(joinedText: String): List<PvmScriptStatement.Sentence> {
        if (joinedText.isBlank()) return emptyList()
        val sentenceRegex = Regex("""[^।॥]+[।॥]*""")
        return sentenceRegex.findAll(joinedText)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() }
            .map { text ->
                val trimmed = text.trim()
                val ukti = parser.parseOrNull(trimmed)?.let(VyakaranamExecutionAdapter::normalizeFrequencyAst)
                val isNishedha = ukti?.grammaticalVakyas()?.any { vakya ->
                    vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.AvyayaPada>()
                        .any { it.function == dev.panini.vyakaranam.ast.AvyayaFunction.NISHEDHA }
                } == true
                val sentence = PvmScriptStatement.Sentence(text = text, ukti = ukti, isNishedha = isNishedha)
                sentence.copy(semantics = PvmSentenceClassifier.classify(sentence))
            }
            .toList()
    }

}
