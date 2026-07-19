package dev.panini.execution

/**
 * An executable argument. Leaves remain Sanskrit strings; composition is
 * represented structurally so an action need not parse punctuation.
 */
sealed interface ExecutionExpression {
    data class Literal(
        val value: String,
        val samjnas: Set<ExecutionSamjna> = emptySet(),
    ) : ExecutionExpression {
        init {
            require(value.isNotBlank()) { "An execution literal cannot be blank." }
        }
    }

    data class Coordination(val members: List<ExecutionExpression>) : ExecutionExpression {
        init {
            require(members.isNotEmpty()) { "A coordination requires at least one member." }
        }

        constructor(vararg members: ExecutionExpression) : this(members.toList())
    }

    data class Reference(val name: String) : ExecutionExpression {
        init {
            require(name.isNotBlank()) { "An execution reference requires a name." }
        }
    }
}

