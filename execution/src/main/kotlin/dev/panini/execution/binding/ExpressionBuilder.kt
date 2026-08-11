package dev.panini.execution.binding

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.KriyaInvocationId
import dev.panini.execution.SvamRupamEngine
import dev.panini.shiksha.Samjna
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.core.SupAffix

/**
 * Converts a [SubantaPada] to an [ExecutionExpression], resolving references to prior
 * results (फल) and attaching saṃjñā tags.
 *
 * Clause-level context (conversation, clauseIndex, local variable state) is supplied
 * via [BindingContext] instead of individual parameters.
 */
internal object ExpressionBuilder {
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
        val normalized = NumeralAstNormalizer.normalize(pada)
        val baseText = normalized.pratipadika.baseText()
        val text = normalized.pratipadika.referenceKey()
        val isPhalaReference = PhalaReference.isReference(normalized)
        ctx.environment.values[text]?.let { value ->
            val sup = SupAffix.fromUpadesha(normalized.sup.text) ?: SupAffix.AM
            return ExecutionExpression.TypedOperand(value, sup)
        }
        var resolvedId: String? = null

        if (ctx.conversation?.previousTypedResults?.containsKey(text) == true ||
            ctx.conversation?.previousResults?.containsKey(text) == true ||
            ctx.localVariables.contains(text)
        ) {
            resolvedId = text
        } else if (isPhalaReference) {
            resolvedId = overridePhalaId ?: (
                if (ctx.clauseIndex > 0) KriyaInvocationId.of(ctx.clauseIndex)
                else ctx.memory.latestKriya()?.frame?.id?.value
                    ?: ctx.conversation?.resultHistory?.lastOrNull()?.id
                    ?: ctx.conversation?.previousResults?.keys?.lastOrNull()
            )
        }

        if (resolvedId != null) {
            return ExecutionExpression.Reference(resolvedId)
        }

        val sankhyaValue = (normalized.pratipadika as? SankhyaPratipadika)?.semanticValue
        val samjnas = buildSet {
            add(Samjna.SHABDA)
            if (sankhyaValue != null) add(Samjna.SANKHYA)
            if (isPhalaReference) add(Samjna.REFERENCE)
            when (normalized.pratipadika) {
                is KridantaPratipadika -> add(Samjna.KRIDANTA)
                is SamasaPratipadika -> add(Samjna.SAMASA)
                else -> Unit
            }
        }
        return if (sankhyaValue != null) {
            ExecutionExpression.sankhya(sankhyaValue.value, sankhyaValue.word)
        } else {
            val svamRupamValue = SvamRupamEngine.evaluateTerm(baseText)
            ExecutionExpression.Pada(text, samjnas, value = svamRupamValue)
        }
    }
}
