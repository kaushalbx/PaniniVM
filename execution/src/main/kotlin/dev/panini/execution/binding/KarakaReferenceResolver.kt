package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KarakaRelation
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.execution.ExecutionExpression
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

internal data class KarakaReferenceResolution(
    val expressions: Map<SubantaPada, ExecutionExpression>,
    val consumedGenitives: Set<SubantaPada>,
    val consumedQualifiers: Set<Pada>,
)

/** Morphological history relation, independent of interpreter memory. */
data class KarakaHistoryReference(
    val referent: SubantaPada,
    val genitive: SubantaPada,
    val karaka: Karaka,
    val dhatuUpadesha: String?,
    val qualifier: Pada?,
    val ordinalFromOldest: Long?,
    val previous: Boolean,
    val orderingValid: Boolean,
)

/** Resolves phrases such as योजनस्य कर्म into participants of a remembered kriyā. */
object KarakaReferenceResolver {
    fun references(padas: List<Pada>, subantas: List<SubantaPada> = padas.filterIsInstance<SubantaPada>()): List<KarakaHistoryReference> =
        subantas.mapIndexedNotNull { index, referent ->
            val karaka = Karaka.fromPratipadika(referent.pratipadika.baseText()) ?: return@mapIndexedNotNull null
            val genitive = subantas.take(index).lastOrNull {
                it.hasVibhakti(Vibhakti.SASTHI) && it.pratipadika is KridantaPratipadika
            } ?: return@mapIndexedNotNull null
            val order = MemoryOrderQualifierResolver.before(referent, padas)
            KarakaHistoryReference(referent, genitive, karaka,
                DhatuCache.resolve((genitive.pratipadika as KridantaPratipadika).dhatu)?.upadesha,
                order.pada.takeIf { order.isExplicit }, order.ordinalNumber, order.previous, order.agreesWith(referent))
        }

    /** History modifiers are not positional or named procedure placeholders. */
    fun protectedPadas(padas: List<Pada>): Set<Pada> {
        val protected = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<Pada, Boolean>())
        references(padas).forEach { reference ->
            protected.add(reference.referent)
            protected.add(reference.genitive)
            reference.qualifier?.let(protected::add)
        }
        return protected
    }

    internal fun resolve(
        padas: List<Pada>,
        subantas: List<SubantaPada>,
        ctx: BindingContext,
    ): KarakaReferenceResolution {
        // Equal morphological words can denote different discourse occurrences.
        val expressions = java.util.IdentityHashMap<SubantaPada, ExecutionExpression>()
        val consumedGenitives = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<SubantaPada, Boolean>())
        val consumedQualifiers = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<Pada, Boolean>())
        references(padas, subantas).forEach { reference ->
            val referencePada = reference.referent
            if (!reference.orderingValid) throw MissingActionResultException(
                "The ordering qualifier must resolve and agree with its kāraka reference in case and number.",
            )
            val upadesha = reference.dhatuUpadesha ?: throw MissingActionResultException(
                "The named action in this kāraka reference has no resolved dhātu identity.")
            val order = MemoryOrderQualifier(reference.qualifier, reference.ordinalFromOldest, reference.previous)
            val remembered = order.select(ctx.memory, upadesha) ?: throw MissingActionResultException(
                "No remembered action is available for this kāraka reference.",
            )
            val participants = remembered.frame.relations.filter {
                (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == reference.karaka
            }
            if (participants.isEmpty()) throw MissingActionResultException(
                "The remembered action has no participant in the requested kāraka relation.",
            )
            val members = participants.map(::participantExpression)
            expressions[referencePada] = if (members.size == 1) members.single()
            else ExecutionExpression.Coordination(members)
            consumedGenitives += reference.genitive
            reference.qualifier?.let(consumedQualifiers::add)
        }
        return KarakaReferenceResolution(expressions, consumedGenitives, consumedQualifiers)
    }

    private fun participantExpression(relation: KarakaRelation): ExecutionExpression {
        val pada = relation.participant.pada
        val pratipadika = pada.pratipadika
        if (pratipadika is SankhyaPratipadika) {
            val value = pratipadika.semanticValue.value
            val word = sharedSankhyaGenerator.cardinal(value).final.surface
            return ExecutionExpression.sankhya(value, word)
        }
        return ExecutionExpression.Pada(pratipadika.referenceKey())
    }
}
