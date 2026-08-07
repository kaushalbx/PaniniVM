package dev.panini.execution.binding

import dev.panini.execution.ExecutionExpression
import dev.panini.shiksha.Samjna
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * Converts a [SubantaPada] to an [ExecutionExpression], resolving references to prior
 * results (फल) and attaching saṃjñā tags.
 *
 * Clause-level context (conversation, clauseIndex, local variable state) is supplied
 * via [BindingContext] instead of individual parameters.
 */
internal object ExpressionBuilder {
    /** Prathama is lexical; later ordinals are represented by segmented purāṇa padas. */
    internal fun ordinalNumber(surface: String): Int? = 1.takeIf { surface == "प्रथम" }

    /**
     * Builds an [ExecutionExpression] for [pada] within [ctx], resolving:
     * - Named/typed prior results and local variables → [ExecutionExpression.Reference]
     * - "फल" resolved by [PhalaResolver] → [ExecutionExpression.Reference]
     * - Numeric pratipadikas → [ExecutionExpression.sankhya]
     * - Everything else → [ExecutionExpression.Pada] with appropriate saṃjñā tags
     *
     * @param overridePhalaId When non-null, used instead of the implicit clause-index
     *                        reference for bare "फल" lookup. Supplied by [PhalaResolver].
     */
    internal fun build(
        pada: SubantaPada,
        ctx: BindingContext,
        overridePhalaId: String? = null,
    ): ExecutionExpression {
        val baseText = pada.pratipadika.baseText()
        val text = pada.pratipadika.referenceKey()
        var resolvedId: String? = null

        if (ctx.conversation?.previousTypedResults?.containsKey(text) == true ||
            ctx.conversation?.previousResults?.containsKey(text) == true ||
            ctx.localVariables.contains(text)
        ) {
            resolvedId = text
        } else if (baseText == "फल") {
            resolvedId = overridePhalaId ?: (
                if (ctx.clauseIndex > 0) "योग-${ctx.clauseIndex}"
                else ctx.memory.latestKriya()?.frame?.id?.value
                    ?: ctx.conversation?.resultHistory?.lastOrNull()?.id
                    ?: ctx.conversation?.previousResults?.keys?.lastOrNull()
            )
        }

        if (resolvedId != null) {
            return ExecutionExpression.Reference(resolvedId)
        }

        val sankhyaValue = when (val prat = pada.pratipadika) {
            is SankhyaPratipadika -> prat.value
            is MulaPratipadika -> sharedSankhyaGenerator.annotatedPratipadikaValue(prat.text)
                ?: runCatching { dev.panini.sankhya.SankhyaEvaluator().evaluateStems(listOf(prat.text)).value }.getOrNull()
            else -> null
        }
        val samjnas = buildSet {
            add(Samjna.SHABDA)
            if (sankhyaValue != null) add(Samjna.SANKHYA)
            if (baseText == "फल") add(Samjna.REFERENCE)
            when (pada.pratipadika) {
                is KridantaPratipadika -> add(Samjna.KRIDANTA)
                is SamasaPratipadika -> add(Samjna.SAMASA)
                else -> Unit
            }
        }
        return if (sankhyaValue != null) {
            ExecutionExpression.Companion.sankhya(sankhyaValue, text)
        } else {
            ExecutionExpression.Pada(text, samjnas)
        }
    }
}
