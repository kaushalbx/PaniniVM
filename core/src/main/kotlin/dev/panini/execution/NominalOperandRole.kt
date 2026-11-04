package dev.panini.execution

/** An execution operand supplied by an established nominal relation, not a kāraka. */
enum class NominalOperandRole { COMPARISON_STANDARD }

/** Each named relational slot supplies exactly one semantic value. */
data class NominalOperandRequirement(
    val role: NominalOperandRole,
    val valueType: OperationValueType? = null,
)
