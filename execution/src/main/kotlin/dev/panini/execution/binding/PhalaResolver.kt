package dev.panini.execution.binding

import dev.panini.core.Vibhakti
import dev.panini.execution.ExecutionMetadata
import dev.panini.execution.KriyaInvocationId

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.Pada

/**
 * Typed result of a [PhalaResolver.resolve] call.
 *
 * @property phalaMap      Maps each "फल" [SubantaPada] to the invocation-id whose
 *                         result it references (e.g. "योग-2" or a
 *                         historical result id).
 * @property resolvedGenitives The genitive-case modifier pādas that were consumed
 *                         during resolution and must be skipped in the main binding loop.
 */
internal data class PhalaResolution(
    val phalaMap: Map<SubantaPada, String>,
    val resolvedGenitives: Set<SubantaPada>,
    val resolvedQualifiers: Set<Pada>,
)

internal class MissingActionResultException(message: String) : IllegalArgumentException(message)

/**
 * Resolves "फल" (result-reference) pādas to concrete invocation ids.
 *
 * Builds chronological candidates from earlier discourse followed by preceding
 * clauses in the current utterance. Kriyā memory is authoritative for earlier
 * discourse; conversation history supplies compatibility when memory is absent.
 * Ordering is applied once to the combined candidates, not once per scope.
 * Ordering is expressed by an independent qualifier of फल, such as पूर्वम् or प्रथमम्.
 *
 * Remembered kriyās are matched by their canonical Dhātupāṭha upadeśa, never by result aliases.
 */
internal object PhalaResolver {

    internal fun resolve(
        phalaPadas: List<SubantaPada>,
        padas: List<Pada>,
        subantas: List<SubantaPada>,
        ctx: BindingContext,
    ): PhalaResolution {
        val resolvedGenitives = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<SubantaPada, Boolean>())
        val resolvedQualifiers = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<Pada, Boolean>())
        // Equal-looking फल padas are distinct occurrences in the source sentence.
        val phalaMap = java.util.IdentityHashMap<SubantaPada, String>()

        phalaPadas.forEach { phalaPada ->
            val namedReference = NamedActionResultReferenceResolver.resolve(padas)
                .singleOrNull { it.result === phalaPada }
            if (namedReference?.orderingAgrees == false) throw MissingActionResultException(
                "The ordering qualifier of फल is ambiguous or does not agree.",
            )
            val explicitOrder = if (namedReference != null)
                MemoryOrderQualifierResolver.from(namedReference.orderingQualifier)
                else MemoryOrderQualifierResolver.before(phalaPada, padas)
            if (!explicitOrder.agreesWith(phalaPada)) throw MissingActionResultException(
                "The ordering qualifier and फल must agree in case and number.",
            )
            val idx = subantas.indexOfFirst { it === phalaPada }
            val genitiveModifier = namedReference?.modifier ?: subantas.take(idx)
                .lastOrNull { it.hasVibhakti(Vibhakti.SASTHI) && it !in resolvedGenitives }
                ?: return@forEach

            val base = genitiveModifier.pratipadika.baseText()
            val order = explicitOrder
            val root = DhatuCache.getActionRoot(base)
            val referencedDhatu = (genitiveModifier.pratipadika as? KridantaPratipadika)
                ?.dhatu?.let(DhatuCache::resolve)?.upadesha

            // ---- 1. Resolve against earlier clauses in this utterance ----------------
            val matchingIndices = (0 until ctx.clauseIndex).filter { i ->
                val prevDhatu = ctx.previousDhatus.getOrNull(i) ?: return@filter false
                if (referencedDhatu != null) return@filter prevDhatu.upadesha == referencedDhatu
                val prevRoot = DhatuCache.getDhatuRoot(prevDhatu.upadesha)
                val prevActionRoots = prevDhatu.operations.mapTo(mutableSetOf()) {
                    DhatuCache.getActionRoot(it.name)
                }
                root == prevRoot || root in prevActionRoots
            }
            // ---- 3. Compatibility fallback to conversation result history -----------
            val historicalResults = ctx.conversation?.resultHistory?.filter { result ->
                val dhatuUpadesha = ctx.conversation.metadata[ExecutionMetadata.dhatu(result.id)]
                    ?: ctx.conversation.metadata[ExecutionMetadata.dhatu(result.invocationId)]
                val prevDhatu = dhatuUpadesha?.let { DhatuCache.upadeshaDhatuCache[it] }
                    ?: return@filter false
                if (referencedDhatu != null) return@filter prevDhatu.upadesha == referencedDhatu
                val prevRoot = DhatuCache.getDhatuRoot(prevDhatu.upadesha)
                val prevActionRoots = prevDhatu.operations.mapTo(mutableSetOf()) {
                    DhatuCache.getActionRoot(it.name)
                }
                root == prevRoot || root in prevActionRoots
            } ?: emptyList()

            val rememberedIds = if (referencedDhatu == null) emptyList() else ctx.memory.entries
                .filter { it.phala != null && it.frame.kriya?.dhatu?.upadesha == referencedDhatu }
                .map { it.frame.id.value }
            // Memory and compatibility history describe the same earlier discourse;
            // use one representation, then append preceding local clauses once.
            val earlierIds = rememberedIds.ifEmpty { historicalResults.map { it.id } }
            val selectedId = order.select(earlierIds + matchingIndices.map { KriyaInvocationId.of(it + 1) })

            if (selectedId != null) {
                phalaMap[phalaPada] = selectedId
                resolvedGenitives.add(genitiveModifier)
                if (explicitOrder.isExplicit && explicitOrder.pada != null) {
                    resolvedQualifiers.add(explicitOrder.pada)
                }
            } else if (genitiveModifier.pratipadika is KridantaPratipadika) {
                throw MissingActionResultException(
                    "No completed result matching '${genitiveModifier.sourceText}' and its ordering qualifier is available.",
                )
            }
        }

        return PhalaResolution(phalaMap, resolvedGenitives, resolvedQualifiers)
    }
}
