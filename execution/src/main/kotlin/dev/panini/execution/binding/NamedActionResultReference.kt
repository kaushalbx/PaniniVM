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
    val orderingQualifier: Pada? = null,
    val orderingQualifiers: List<Pada> = listOfNotNull(orderingQualifier),
) {
    val actionIdentity: dev.panini.core.ActionIdentity
        get() = (modifier.pratipadika as KridantaPratipadika).resolveActionHistoryIdentity()

    fun select(memory: dev.panini.execution.memory.KriyaMemory): dev.panini.execution.memory.RememberedKriya? {
        if (!orderingAgrees) return null
        val order = MemoryOrderQualifier(orderingQualifier, ordinalFromOldest, previous)
        val identity = actionIdentity
        return if (identity.upasargas.isNotEmpty() || identity.sanadi.isNotEmpty()) order.select(memory, identity)
            else order.select(memory, dhatuUpadesha)
    }
}

object NamedActionResultReferenceResolver {
    /** Consume genitive and ordering modifiers without consuming the referenced value. */
    fun operandPadas(padas: List<Pada>): List<Pada> {
        val references = resolve(padas)
        if (references.isEmpty()) return padas
        val modifiers = references.map { it.modifier }
        val qualifiers = references.filter { it.hasOrderingQualifier }.flatMap { it.orderingQualifiers }
        return members(padas).filter { pada -> modifiers.none { it === pada } && qualifiers.none { it === pada } }
    }

    private fun members(padas: List<Pada>): List<Pada> =
        padas.flatMap { if (it is SamuccitaSubanta) it.members else listOf(it) }

    fun resolve(sourcePadas: List<Pada>): List<NamedActionResultReference> {
        val padas = members(sourcePadas)
        val nominals = padas.filterIsInstance<SubantaPada>()
        return nominals.withIndex().mapNotNull { (index, result) ->
            if (!PhalaReference.isReference(result)) return@mapNotNull null
            val singleResult = nominals.count { PhalaReference.isReference(it) } == 1
            val actionModifiers = nominals.filter {
                it.hasVibhakti(Vibhakti.SASTHI) && it.pratipadika is KridantaPratipadika
            }
            require(!singleResult || actionModifiers.size <= 1) {
                "Ambiguous action-result reference: multiple genitive actions modify one फल."
            }
            val modifier = (if (singleResult) actionModifiers.singleOrNull()
                else null) ?: nominals.take(index).lastOrNull { it.hasVibhakti(Vibhakti.SASTHI) }
                ?: return@mapNotNull null
            val derivation = modifier.pratipadika as? KridantaPratipadika ?: return@mapNotNull null
            val dhatu = derivation.resolveActionHistoryDhatu()
            val remaining = padas.filter { it !== result && it !== modifier && it !is TingantaPada }
            val candidates = remaining.map(MemoryOrderQualifierResolver::from)
            val order = MemoryOrderQualifierResolver.forHead(result, modifier, padas, singleResult)
            val qualifiers = if (order.isExplicit && order.pada == null && singleResult && candidates.isNotEmpty() && candidates.all { it.isExplicit })
                candidates.mapNotNull { it.pada } else listOfNotNull(order.pada.takeIf { order.isExplicit })
            NamedActionResultReference(result, modifier, dhatu.upadesha, order.ordinalNumber, order.previous,
                order.agreesWith(result), order.isExplicit, order.pada.takeIf { order.isExplicit }, qualifiers)
        }
    }
}
