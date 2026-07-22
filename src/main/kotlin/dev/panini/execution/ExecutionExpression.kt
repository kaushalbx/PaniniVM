package dev.panini.execution

/**
 * An executable argument. Leaves remain Sanskrit strings; composition is
 * represented structurally so an action need not parse punctuation.
 */
sealed interface ExecutionExpression {
    data class Pada(
        val prakriti: String,
        val samjnas: Set<ExecutionSamjna> = emptySet(),
        val value: SanskritValue? = null,
    ) : ExecutionExpression {
        init {
            require(prakriti.isNotBlank()) { "An execution prakriti cannot be blank." }
        }
    }

    companion object {
        fun sankhya(value: Long, prakriti: String): Pada = Pada(
            prakriti = prakriti,
            samjnas = setOf(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA),
            value = SanskritValue.Sankhya(value, prakriti),
        )
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
