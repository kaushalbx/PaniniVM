package dev.panini.execution.binding

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.KriyaInvocationId
import dev.panini.execution.SanskritValue
import dev.panini.execution.SvamRupamEngine
import dev.panini.shiksha.Samjna
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.core.SupAffix
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.analysis.KarakaInference
import dev.panini.shiksha.Karmatva

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
        collectionMemberFrame: Boolean = false,
        resolvedKaraka: Karaka? = null,
    ): ExecutionExpression {
        val normalized = NumeralAstNormalizer.normalize(pada)
        val text = normalized.pratipadika.referenceKey()
        val semanticOperand = dev.panini.execution.SemanticOperandAnalysis.nominal(pada)
        val literalValue = (semanticOperand as? dev.panini.execution.SemanticOperand.Literal)?.value
        (literalValue as? SanskritValue.Satya)?.let { value ->
            return ExecutionExpression.Pada(text, value.samjnas, value = value)
        }
        val nominal = normalized.pratipadika as? MulaPratipadika
        if (collectionMemberFrame) {
            dev.panini.execution.CollectionMemberMorphology.ordinalExpression(pada)?.let { return it }
        }
        val selection = if (nominal?.vikaras?.isEmpty() == true) when {
            nominal.lexicalIdentity == MulaPratipadikaIdentity.SANKHYA &&
                normalized.sup.affixCandidates == listOf(SupAffix.SAS) -> dev.panini.execution.CollectionMemberSelection.NUMBERS
            nominal.lexicalIdentity == MulaPratipadikaIdentity.ANTIMA &&
                normalized.sup.affixCandidates == listOf(SupAffix.AM) -> dev.panini.execution.CollectionMemberSelection.FINAL
            else -> null
        } else null
        if (collectionMemberFrame && selection != null) {
            return ExecutionExpression.Pada(text, setOf(Samjna.SHABDA), memberSelection = selection)
        }
        val numeralValue = (normalized.pratipadika as? SankhyaPratipadika)?.semanticValue
        (literalValue as? SanskritValue.Sankhya)?.let { value ->
            return ExecutionExpression.sankhya(value.value, value.word)
        }
        val isPhalaReference = PhalaReference.isReference(normalized)
        if (!isPhalaReference) ctx.environment.values[text]?.let { value ->
            val candidates = normalized.sup.affixCandidates
            require(candidates.isNotEmpty()) { "Unknown sup ending: '${normalized.sup.text}'." }
            val prayoga = ctx.frame.prayoga.takeUnless { it == Prayoga.ANIRDHARITA } ?: Prayoga.KARTARI
            val compatible = candidates.filter {
                KarakaInference.infer(it.vibhakti, prayoga, ctx.dhatu.karmatva != Karmatva.AKARMAKA) == resolvedKaraka
            }
            val sup = if (resolvedKaraka != null) {
                requireNotNull(compatible.singleOrNull()) {
                    "Sup ending '${normalized.sup.text}' does not determine one slot for $resolvedKaraka."
                }
            } else candidates.first()
            return ExecutionExpression.TypedOperand(value, sup, referenceName = text)
        }
        if (!isPhalaReference && text in ctx.environment.constraints) {
            return ExecutionExpression.Reference(text)
        }
        var resolvedId: String? = null

        if (isPhalaReference) {
            resolvedId = overridePhalaId ?: (
                if (ctx.clauseIndex > 0) KriyaInvocationId.of(ctx.clauseIndex)
                else if (ctx.conversation?.previousTypedResults?.containsKey(PhalaReference.RUNTIME_KEY) == true)
                    PhalaReference.RUNTIME_KEY
                else ctx.memory.latestKriya()?.frame?.id?.value
                    ?: ctx.conversation?.resultHistory?.lastOrNull()?.id
                    ?: ctx.conversation?.previousResults?.keys?.lastOrNull()
            )
        } else if (ctx.conversation?.previousTypedResults?.containsKey(text) == true ||
            ctx.conversation?.previousResults?.containsKey(text) == true ||
            ctx.localVariables.contains(text)
        ) {
            resolvedId = text
        }

        if (resolvedId != null || isPhalaReference) {
            return ExecutionExpression.Reference(resolvedId ?: PhalaReference.RUNTIME_KEY)
        }

        dev.panini.execution.PuranaPratyayaResolver.semanticValue(pada)?.let { value ->
            return ExecutionExpression.Pada(text, value.samjnas, value = value)
        }
        numeralValue?.let { return ExecutionExpression.sankhya(it.value, it.word) }

        val samjnas = buildSet {
            add(Samjna.SHABDA)
            if (isPhalaReference) add(Samjna.REFERENCE)
            when (normalized.pratipadika) {
                is KridantaPratipadika -> add(Samjna.KRIDANTA)
                is SamasaPratipadika -> add(Samjna.SAMASA)
                else -> Unit
            }
        }
        val svamRupamValue = (semanticOperand as? dev.panini.execution.SemanticOperand.Reference)?.fallback
            ?: SvamRupamEngine.evaluate(normalized.pratipadika)
        return ExecutionExpression.Pada(text, samjnas, value = svamRupamValue, memberSelection = selection)
    }
}
