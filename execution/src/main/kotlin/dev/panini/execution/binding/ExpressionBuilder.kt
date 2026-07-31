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
 * results (फल, ordinal forms) and attaching saṃjñā tags.
 *
 * Clause-level context (conversation, clauseIndex, local variable state) is supplied
 * via [BindingContext] instead of individual parameters.
 */
internal object ExpressionBuilder {
    /**
     * Reverse-index of ordinal Sanskrit surface forms to 0-based history position (covers 1st–50th).
     * Used to resolve expressions like "प्रथमफल", "द्वितीयफल", etc.
     */
    private val ordinalSurfaceToIndex: Map<String, Int> by lazy {
        (1..50).flatMap { i ->
            buildList {
                add(sharedSankhyaGenerator.ordinal(i.toLong()).final.surface)
                addAll(sharedSankhyaGenerator.ordinalVariants(i.toLong()).map { it.final.surface })
            }.map { surface -> surface to (i - 1) }
        }.toMap()
    }

    /**
     * Builds an [ExecutionExpression] for [pada] within [ctx], resolving:
     * - Named/typed prior results and local variables → [ExecutionExpression.Reference]
     * - "फल" and ordinal-prefixed फल forms → [ExecutionExpression.Reference]
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
        val text = pada.pratipadika.baseText()
        var resolvedId: String? = null
        var isOrdinalReference = false

        if (ctx.conversation?.previousTypedResults?.containsKey(text) == true ||
            ctx.conversation?.previousResults?.containsKey(text) == true ||
            ctx.localVariables.contains(text)
        ) {
            resolvedId = text
        } else if (text == "फल") {
            resolvedId = overridePhalaId ?: (
                if (ctx.clauseIndex > 0) "योग-${ctx.clauseIndex}"
                else ctx.conversation?.resultHistory?.lastOrNull()?.id
                    ?: ctx.conversation?.previousResults?.keys?.lastOrNull()
            )
        } else if (text.endsWith("फल")) {
            val prefix = text.removeSuffix("फल")
            val idx = ordinalSurfaceToIndex[prefix]
            if (idx != null) {
                resolvedId = ctx.conversation?.resultHistory?.getOrNull(idx)?.id
                isOrdinalReference = true
            }
        }

        if (resolvedId != null) {
            return ExecutionExpression.Reference(resolvedId)
        }

        val sankhyaValue = when (val prat = pada.pratipadika) {
            is SankhyaPratipadika -> prat.value
            is MulaPratipadika -> sharedSankhyaGenerator.annotatedPratipadikaValue(prat.text)
            else -> null
        }
        val samjnas = buildSet {
            add(Samjna.SHABDA)
            if (sankhyaValue != null) add(Samjna.SANKHYA)
            if (text == "फल" || isOrdinalReference) add(Samjna.REFERENCE)
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
