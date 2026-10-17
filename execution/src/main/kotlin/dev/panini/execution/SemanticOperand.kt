package dev.panini.execution

import dev.panini.execution.binding.referenceKey
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * Static meaning of one source operand before a backend performs runtime lookup.
 *
 * The source pada is retained as grammatical provenance. A [Reference] remains
 * symbolic: its fallback is evidence licensed by the same morphology, not a
 * fabricated value used to force operation selection.
 */
sealed interface SemanticOperand {
    val source: Pada?
    val typeConstraint: PrakriyaValueType?

    data class Literal(
        val value: SanskritValue,
        override val source: Pada,
        override val typeConstraint: PrakriyaValueType? = null,
    ) : SemanticOperand

    data class Resolved(
        val value: SanskritValue,
        override val source: Pada? = null,
        override val typeConstraint: PrakriyaValueType? = null,
    ) : SemanticOperand

    data class Reference(
        val name: String,
        val fallback: SanskritValue? = null,
        override val source: Pada? = null,
        override val typeConstraint: PrakriyaValueType? = null,
    ) : SemanticOperand {
        init { require(name.isNotBlank()) { "A semantic operand reference requires a name." } }
    }

    fun resolve(
        lookup: (String) -> SanskritValue?,
        unresolved: (Reference) -> SanskritValue,
    ): SanskritValue = when (val result = resolveResult(lookup)) {
        is SemanticOperandResolution.Value -> result.value
        is SemanticOperandResolution.Unresolved -> unresolved(result.reference)
        is SemanticOperandResolution.Invalid -> error(result.message)
        is SemanticOperandResolution.Ambiguous -> error(result.message)
        is SemanticOperandResolution.Unsupported -> error(result.message)
    }

    fun resolveResult(lookup: (String) -> SanskritValue?): SemanticOperandResolution = when (this) {
        is Literal -> SemanticOperandResolution.Value(value, this)
        is Resolved -> SemanticOperandResolution.Value(value, this)
        is Reference -> (lookup(name) ?: fallback)?.let {
            SemanticOperandResolution.Value(it, this)
        } ?: SemanticOperandResolution.Unresolved(this)
    }
}

/** Explicit semantic outcome; callers no longer need to encode failure classes as null. */
sealed interface SemanticOperandResolution {
    data class Value(val value: SanskritValue, val operand: SemanticOperand) : SemanticOperandResolution
    data class Unresolved(val reference: SemanticOperand.Reference) : SemanticOperandResolution
    data class Invalid(val message: String, val source: Pada? = null) : SemanticOperandResolution
    data class Ambiguous(val message: String, val candidates: List<SemanticOperand>) : SemanticOperandResolution
    data class Unsupported(val message: String, val source: Pada? = null) : SemanticOperandResolution
}

/** Single owner for intrinsic-literal versus contextual-reference precedence. */
object SemanticOperandAnalysis {
    fun nominal(
        pada: Pada,
        typeConstraint: PrakriyaValueType? = null,
    ): SemanticOperand {
        NominalValueAnalysis.literalValue(pada)?.let {
            return SemanticOperand.Literal(it, pada, typeConstraint)
        }
        val nominal = pada as? SubantaPada
            ?: return SemanticOperand.Resolved(
                requireNotNull(NominalValueAnalysis.fallbackValue(pada)) {
                    "A non-nominal semantic operand requires intrinsic value evidence: ${pada.sourceText}"
                },
                pada,
                typeConstraint,
            )
        return SemanticOperand.Reference(
            name = nominal.pratipadika.referenceKey(),
            fallback = NominalValueAnalysis.fallbackValue(pada),
            source = pada,
            typeConstraint = typeConstraint,
        )
    }
}

