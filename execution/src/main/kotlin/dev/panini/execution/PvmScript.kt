package dev.panini.execution

import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.PrakriyaModifiers
import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaBoundaryPada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
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
     *         सङ्ख्या + अम् सङ्ख्या + अम् च गण् + णिच् + लोट् + सिप् ॥
     *
     * [nameSegmented] is the segmented prātipadika form (e.g. "गुण् + ल्युट् + सुँ").
     * [body] contains the vākya sentences that form the prakriyā.
     */
    data class PrakriyaDefinition(
        val prakriya: Prakriya,
        val body: List<Sentence>,
        /** Parsed declaration header, before normalization into the operation name. */
        val headerSource: String? = null,
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
     *     गणित + सुँ इति अधि + कृ + घञ् + सुँ ।
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

    fun parse(source: String): List<PvmScriptStatement> = parseNative(source)

    /** Explicit migration-only parser for old physical-line block syntax. */
    fun parseLegacy(source: String): List<PvmScriptStatement> {
        val rawLines = source.lines()

        val prakriyaDefinitions = mutableListOf<PvmScriptStatement.PrakriyaDefinition>()
        val nonPrakriyaLines = mutableListOf<String>()

        var inBlock = false
        var currentName = ""
        var currentBodyLines = mutableListOf<String>()
        var currentBlockText = mutableListOf<String>()

        for (line in rawLines) {
            val stripped = PvmSourceScanner.stripComment(line).trim()

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
                        .map { PvmSourceScanner.stripComment(it).trim() }
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
                .map { PvmSourceScanner.stripComment(it).trim() }
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
            val stripped = PvmSourceScanner.stripComment(line).trim()
            val range = extractRangeDefinition(stripped)
            val adhikaraHeader = AdhikaraHeaderParser.parse(stripped)
            if (range != null) {
                rangeDefinitions += PvmScriptStatement.RangeDefinition(range, line)
            } else if (adhikaraHeader != null) {
                adhikaraDefinitions += PvmScriptStatement.AdhikaraDefinition(
                    scope = Scope(
                        sourceText = line,
                        domain = adhikaraHeader.domainSource,
                        domainIdentity = adhikaraHeader.domainIdentity,
                    ),
                )
            } else {
                regularNonPrakriyaLines += line
            }
        }

        val sanitizedLines = regularNonPrakriyaLines
            .map { PvmSourceScanner.stripComment(it).trim() }
            .filter { it.isNotEmpty() }

        val sentences = if (sanitizedLines.isEmpty()) {
            emptyList()
        } else {
            parseSentences(sanitizedLines.joinToString(" "))
        }

        return prakriyaDefinitions + adhikaraDefinitions + rangeDefinitions + sentences
    }

    /** Native document adapter. No declaration or body is serialized and reparsed. */
    fun fromDocument(document: dev.panini.vyakaranam.ast.ProgramDocument): List<PvmScriptStatement> =
        document.items.mapIndexed { index, node ->
            val originalText = document.itemSpans.getOrNull(index)?.let { span ->
                document.sourceText.substring(span.start, span.endExclusive)
            } ?: node.sourceText
            when (node) {
                is Prakriya -> {
                    val body = node.body.mapIndexed { bodyIndex, program ->
                        val text = document.prakriyaBodySpans[index]?.getOrNull(bodyIndex)?.let { span ->
                            document.sourceText.substring(span.start, span.endExclusive)
                        } ?: program.sourceText
                        nativeSentence(dev.panini.vyakaranam.ast.Ukti(text, body = program))
                    }
                    val headerText = document.prakriyaHeaderSpans[index]?.let { span ->
                        document.sourceText.substring(span.start, span.endExclusive)
                    }
                    val nameText = document.prakriyaNameSpans[index]?.let { span ->
                        document.sourceText.substring(span.start, span.endExclusive)
                    } ?: node.name
                    PvmScriptStatement.PrakriyaDefinition(
                        node.copy(sourceText = originalText, name = nameText,
                            body = body.map { requireNotNull(it.program) }), body,
                        headerSource = headerText,
                    )
                }
                is Scope -> PvmScriptStatement.AdhikaraDefinition(node.copy(sourceText = originalText,
                    domain = document.scopeDomainSpans[index]?.let { span ->
                        document.sourceText.substring(span.start, span.endExclusive)
                    } ?: node.domain))
                is dev.panini.vyakaranam.ast.RangeDeclaration -> PvmScriptStatement.RangeDefinition(
                    evaluateRange(node.boundary), originalText,
                )
                is dev.panini.vyakaranam.ast.Ukti -> nativeSentence(node.copy(sourceText = originalText))
                else -> error("Unsupported native document item: ${node::class.simpleName}")
            }
        }

    fun parseNative(source: String): List<PvmScriptStatement> {
        return fromDocument(parser.parseDocument(source))
    }

    private fun nativeSentence(original: dev.panini.vyakaranam.ast.Ukti): PvmScriptStatement.Sentence {
        val ukti = VyakaranamExecutionAdapter.normalizeFrequencyAst(original)
        val isNishedha = ukti.grammaticalVakyas().any { vakya ->
            vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.AvyayaPada>()
                .any { it.function == dev.panini.vyakaranam.ast.AvyayaFunction.NISHEDHA }
        }
        val sentence = PvmScriptStatement.Sentence(original.sourceText, ukti, isNishedha)
        return sentence.copy(semantics = PvmSentenceClassifier.classify(sentence))
    }

    private fun evaluateRange(explicitRange: ParyantaRangePada): SanskritValue.Range {
        val generator = SankhyaGenerator()
        fun value(pada: SankhyaBoundaryPada): SanskritValue.Sankhya {
            val numericValue = requireNotNull(NaturalSemanticNormalizer.boundaryValue(pada)) {
                "A सीमा bound must be a numeric cardinal or ordinal."
            }
            return SanskritValue.Sankhya(numericValue, generator.cardinal(numericValue).final.surface.removeSuffix("न्"))
        }
        return SanskritValue.Range(value(explicitRange.lowerLimit), value(explicitRange.upperLimit))
    }

    private fun isRangeDefinitionLine(line: String): Boolean = extractRangeDefinition(line) != null

    private fun extractRangeDefinition(line: String): SanskritValue.Range? {
        val ukti = parser.parseOrNull(line.trimEnd('।', '॥', ' ')) ?: return null
        val declaration = ItiDeclarationAnalyzer.analyze(ukti) ?: return null
        val rangePadas = declaration.declaredPadas
        val marker = declaration.nominativeMarker ?: return null
        if ((marker.pratipadika as? MulaPratipadika)?.lexicalIdentity != MulaPratipadikaIdentity.SIMA ||
            SupAffix.fromUpadesha(marker.sup.text)?.vibhakti != Vibhakti.PRATHAMA
        ) return null
        val explicitRange = rangePadas.filterIsInstance<ParyantaRangePada>().singleOrNull() ?: return null
        return runCatching { evaluateRange(explicitRange) }.getOrNull()
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
        val parsedMethodHeader = parsed?.declarationPadas
            ?.let(TaddhitaStructEngine::detectMethodHeader)
        val legacyMethodHeader = if (parsedMethodHeader == null) {
            TaddhitaStructEngine.detectMethodHeader(declarationSource)
        } else null
        val methodDomain = parsedMethodHeader?.first ?: legacyMethodHeader?.first
        val cleanName = parsedMethodHeader?.second?.sourceText
            ?.let(PrakriyaInvocationMatcher::normalizeIdentity)
            ?: legacyMethodHeader?.second
            ?: declarationSource
        val headerIdentity = parsedMethodHeader?.let { (domain, method) ->
            PrakriyaHeaderIdentity(
                operationStem = method.pratipadika.prakriyaIdentity(),
                domainStem = domain,
            )
        } ?: parsed?.declarationPadas?.let(PrakriyaHeaderIdentityParser::parse)
            ?: requireNotNull(PrakriyaHeaderIdentityParser.parse(cleanName)) {
            "A reusable प्रक्रिया header must retain its parsed morphological identity."
        }
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
            headerSource = header,
            prakriya = Prakriya(
                sourceText = blockText.joinToString("\n"),
                name = cleanName,
                domain = methodDomain,
                body = body.mapNotNull(PvmScriptStatement.Sentence::program),
                nameIdentity = headerIdentity.operationStem,
                domainIdentity = methodDomain ?: headerIdentity.domainStem,
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
                    dev.panini.vyakaranam.ast.AvyayaFunction.QUOTATIVE ||
                    (it as? dev.panini.vyakaranam.ast.AvyayaPada)?.function ==
                    dev.panini.vyakaranam.ast.AvyayaFunction.NAMING }
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

    private val parser = dev.panini.vyakaranam.parser.PaniniParser()

    private fun parseSentences(joinedText: String): List<PvmScriptStatement.Sentence> {
        if (joinedText.isBlank()) return emptyList()
        return PvmSourceScanner.sentences(joinedText)
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
