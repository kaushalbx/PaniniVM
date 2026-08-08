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

/** Resolves phrases such as योजनस्य कर्म into participants of a remembered kriyā. */
internal object KarakaReferenceResolver {
    fun resolve(
        padas: List<Pada>,
        subantas: List<SubantaPada>,
        ctx: BindingContext,
    ): KarakaReferenceResolution {
        val expressions = mutableMapOf<SubantaPada, ExecutionExpression>()
        val consumedGenitives = mutableSetOf<SubantaPada>()
        val consumedQualifiers = mutableSetOf<Pada>()
        subantas.forEachIndexed { index, referencePada ->
            val karaka = Karaka.fromPratipadika(referencePada.pratipadika.baseText()) ?: return@forEachIndexed
            val order = MemoryOrderQualifierResolver.before(referencePada, padas)
            val genitive = subantas.take(index).lastOrNull {
                it.hasVibhakti(Vibhakti.SASTHI) && it.pratipadika is KridantaPratipadika
            } ?: return@forEachIndexed
            val upadesha = (genitive.pratipadika as KridantaPratipadika).dhatu.mulaDhatu
                .let(DhatuCache::get)?.upadesha ?: return@forEachIndexed
            val remembered = order.select(ctx.memory, upadesha) ?: return@forEachIndexed
            val participants = remembered.frame.relations.filter {
                (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == karaka
            }
            if (participants.isEmpty()) return@forEachIndexed
            val members = participants.map(::participantExpression)
            expressions[referencePada] = if (members.size == 1) members.single()
            else ExecutionExpression.Coordination(members)
            consumedGenitives += genitive
            if (order.isExplicit && order.pada != null) consumedQualifiers += order.pada
        }
        return KarakaReferenceResolution(expressions, consumedGenitives, consumedQualifiers)
    }

    private fun participantExpression(relation: KarakaRelation): ExecutionExpression {
        val pada = relation.participant.pada
        val pratipadika = pada.pratipadika
        if (pratipadika is SankhyaPratipadika) {
            val stems = pada.sourceText.split('+').dropLast(1)
            val value = pratipadika.value ?: NumeralPadaBinder.evaluateStems(stems).value
            val word = sharedSankhyaGenerator.cardinal(value).final.surface
            return ExecutionExpression.sankhya(value, word)
        }
        return ExecutionExpression.Pada(pratipadika.referenceKey())
    }
}
