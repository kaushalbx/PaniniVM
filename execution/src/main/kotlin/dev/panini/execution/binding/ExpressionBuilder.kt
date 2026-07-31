package dev.panini.execution.binding

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SambhashanaContext
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.shiksha.Samjna
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * Converts a [SubantaPada] to an [ExecutionExpression], resolving references to prior
 * results (फल, ordinal forms) and attaching saṃjñā tags.
 */
internal object ExpressionBuilder {
    private val sankhyaGenerator = SankhyaGenerator()
    private val sankhyaEvaluator = SankhyaEvaluator()

    /**
     * Reverse-index of ordinal Sanskrit surface forms to 0-based history position (covers 1st–50th).
     * Used to resolve expressions like "प्रथमफल", "द्वितीयफल", etc.
     */
    internal val ordinalSurfaceToIndex: Map<String, Int> by lazy {
        (1..50).flatMap { i ->
            buildList {
                add(sankhyaGenerator.ordinal(i.toLong()).final.surface)
                addAll(sankhyaGenerator.ordinalVariants(i.toLong()).map { it.final.surface })
            }.map { surface -> surface to (i - 1) }
        }.toMap()
    }

    /**
     * Builds an [ExecutionExpression] for [pada], resolving:
     * - Named/typed prior results and local variables → [ExecutionExpression.Reference]
     * - "फल" and ordinal-prefixed फल forms → [ExecutionExpression.Reference]
     * - Numeric pratipadikas → [ExecutionExpression.sankhya]
     * - Everything else → [ExecutionExpression.Pada] with appropriate saṃjñā tags
     *
     * @param overridePhalaId When non-null, used instead of the implicit clause-index reference
     *                        for bare "फल" lookup.
     */
    internal fun build(
        pada: SubantaPada,
        conversation: SambhashanaContext?,
        clauseIndex: Int,
        overridePhalaId: String? = null,
        localVariables: Set<String> = emptySet(),
        localVariableInvocationIds: Map<String, String> = emptyMap(),
    ): ExecutionExpression {
        val text = pada.pratipadika.baseText()
        var resolvedId: String? = null
        var isOrdinalReference = false

        if (conversation?.previousTypedResults?.containsKey(text) == true ||
            conversation?.previousResults?.containsKey(text) == true ||
            localVariables.contains(text)
        ) {
            resolvedId = text
        } else if (text == "फल") {
            resolvedId = overridePhalaId ?: (if (clauseIndex > 0) "योग-$clauseIndex" else
                conversation?.resultHistory?.lastOrNull()?.id ?: conversation?.previousResults?.keys?.lastOrNull())
        } else if (text.endsWith("फल")) {
            val prefix = text.removeSuffix("फल")
            val idx = ordinalSurfaceToIndex[prefix]
            if (idx != null) {
                resolvedId = conversation?.resultHistory?.getOrNull(idx)?.id
                isOrdinalReference = true
            }
        }

        if (resolvedId != null) {
            return ExecutionExpression.Reference(resolvedId)
        }

        val sankhyaValue = when (val prat = pada.pratipadika) {
            is SankhyaPratipadika -> prat.value
            is MulaPratipadika -> sankhyaGenerator.annotatedPratipadikaValue(prat.text)
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
