package dev.panini.execution

import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.SankhyaBoundaryPada
import dev.panini.vyakaranam.ast.depthFirst

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
     *     गुण् + ल्युट् + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        statements.size == 1 && statements.single() is PvmScriptStatement.Sentence -> {
            val sentence = statements.single() as PvmScriptStatement.Sentence
            val requiresOrchestration = sentence.program?.depthFirst()?.any {
                it is dev.panini.vyakaranam.ast.ForEach ||
                    it is dev.panini.vyakaranam.ast.CollectionRelativeAction ||
                    it is dev.panini.vyakaranam.ast.QuantifiedCollectionPredicate ||
                    it is dev.panini.vyakaranam.ast.Repeat ||
                    it is dev.panini.vyakaranam.ast.Recurrence ||
                    (it is dev.panini.vyakaranam.ast.Invocation && it.implicitValuePada != null)
            } == true
            if (requiresOrchestration) PvmSourceKind.SCRIPT else PvmSourceKind.UTTERANCE
        }
        else -> PvmSourceKind.SCRIPT
    }

    private fun hasExplicitDefinitionMarker(source: String): Boolean =
        PrakriyaDefinitionMarkerParser.hasExplicitMarker(source)

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

    fun parse(source: String): List<PvmScriptStatement> {
        return fromDocument(parser.parseDocument(source))
    }

    private fun nativeSentence(original: dev.panini.vyakaranam.ast.Ukti): PvmScriptStatement.Sentence {
        val ukti = VyakaranamExecutionAdapter.normalizeFrequencyAst(original)
        val isNishedha = isProhibitionSentence(ukti)
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

    private val parser = dev.panini.vyakaranam.parser.PaniniParser()

    private fun isProhibitionSentence(ukti: dev.panini.vyakaranam.ast.Ukti): Boolean {
        // Negation inside a condition or loop belongs to that clause, not to
        // the whole procedure as a declaration-level prohibition.
        val invocation = ukti.body as? dev.panini.vyakaranam.ast.Invocation ?: return false
        return invocation.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.AvyayaPada>()
            .any { it.function == dev.panini.vyakaranam.ast.AvyayaFunction.NISHEDHA }
    }

}
