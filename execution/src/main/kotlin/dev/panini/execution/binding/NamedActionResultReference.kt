package dev.panini.execution.binding

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.*

/** Parsed grammatical reference to a particular dhātu's completed result. */
data class NamedActionResultReference(
    val result: SubantaPada,
    val modifier: SubantaPada,
    val dhatuUpadesha: String,
    val ordinalFromOldest: Long?,
    val previous: Boolean,
    val orderingAgrees: Boolean = true,
    val hasOrderingQualifier: Boolean = previous || ordinalFromOldest != null,
)

object NamedActionResultReferenceResolver {
    /** Consume genitive and ordering modifiers without consuming the referenced value. */
    fun operandPadas(padas: List<Pada>): List<Pada> {
        val references = resolve(padas)
        val modifiers = references.map { it.modifier }
        val qualifierIndices = references.filter { it.hasOrderingQualifier }
            .map { reference -> padas.indexOfFirst { it === reference.result } - 1 }.toSet()
        return padas.filterIndexed { index, pada -> modifiers.none { it === pada } && index !in qualifierIndices }
    }

    fun resolve(padas: List<Pada>): List<NamedActionResultReference> {
        val nominals = padas.filterIsInstance<SubantaPada>()
        return nominals.withIndex().mapNotNull { (index, result) ->
            if (!PhalaReference.isReference(result)) return@mapNotNull null
            val modifier = nominals.take(index).lastOrNull { it.hasVibhakti(Vibhakti.SASTHI) }
                ?: return@mapNotNull null
            val derivation = modifier.pratipadika as? KridantaPratipadika ?: return@mapNotNull null
            val dhatu = DhatuCache.resolve(derivation.dhatu) ?: return@mapNotNull null
            val order = MemoryOrderQualifierResolver.before(result, padas)
            NamedActionResultReference(result, modifier, dhatu.upadesha, order.ordinalNumber, order.previous,
                order.agreesWith(result), order.isExplicit)
        }
    }
}
