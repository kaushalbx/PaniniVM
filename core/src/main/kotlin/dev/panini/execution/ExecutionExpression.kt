package dev.panini.execution

import dev.panini.shiksha.Samjna
import dev.panini.core.SupAffix

/**
 * An executable argument. Leaves remain Sanskrit strings; composition is
 * represented structurally so an action need not parse punctuation.
 */
sealed interface ExecutionExpression {
    data class Pada(
        val prakriti: String,
        val samjnas: Set<Samjna> = emptySet(),
        val value: SanskritValue? = null,
    ) : ExecutionExpression {
        init {
            require(prakriti.isNotBlank()) { "An execution prakriti cannot be blank." }
        }
    }

    companion object {
        fun sankhya(value: Long, prakriti: String): Pada = Pada(
            prakriti = prakriti,
            samjnas = setOf(Samjna.SANKHYA, Samjna.SHABDA),
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

    /** A value already resolved by the caller, retaining the source-written sup slot. */
    data class TypedOperand(
        val value: SanskritValue,
        val sup: SupAffix,
    ) : ExecutionExpression
}

/** A literal name suitable for a local result binding. */
fun ExecutionExpression.bindingName(): String? = when (this) {
    is ExecutionExpression.Pada -> prakriti.trim().takeIf { it.isNotEmpty() }
    is ExecutionExpression.Reference -> name
    is ExecutionExpression.TypedOperand -> null
    is ExecutionExpression.Coordination -> null
}
