package dev.panini.execution

/** Lexical predicate identity is grammatical metadata, not a mutable runtime value. */
enum class CopularPredicate {
    EQUAL, LESS_THAN, GREATER_THAN;

    companion object {
        fun from(expression: ExecutionExpression?): CopularPredicate? =
            when ((expression as? ExecutionExpression.Pada)?.prakriti) {
                "सम" -> EQUAL
                "न्यून" -> LESS_THAN
                "अधिक" -> GREATER_THAN
                else -> null
            }
    }
}
