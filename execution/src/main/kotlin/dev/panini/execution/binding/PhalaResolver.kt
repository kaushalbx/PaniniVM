package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.Pada

/**
 * Typed result of a [PhalaResolver.resolve] call.
 *
 * @property phalaMap      Maps each "फल" [SubantaPada] to the invocation-id whose
 *                         result it references (e.g. "योग-2", "पूर्वफल", or a
 *                         historical result id).
 * @property resolvedGenitives The genitive-case modifier pādas that were consumed
 *                         during resolution and must be skipped in the main binding loop.
 */
internal data class PhalaResolution(
    val phalaMap: Map<SubantaPada, String>,
    val resolvedGenitives: Set<SubantaPada>,
    val resolvedQualifiers: Set<Pada>,
)

/**
 * Resolves "फल" (result-reference) pādas to concrete invocation ids.
 *
 * Handles three resolution scopes in priority order:
 * 1. **Within-utterance** — a genitive modifier names a prior clause's dhātu action.
 * 2. **Kriyā memory** — the named action matches a remembered frame by exact dhātu upadeśa.
 * 3. **Conversation compatibility** — older contexts still resolve through result history.
 * 4. **पूर्वफल shorthand** — "पूर्व" prefix selects the penultimate matching result.
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
        val resolvedGenitives = mutableSetOf<SubantaPada>()
        val resolvedQualifiers = mutableSetOf<Pada>()
        val phalaMap = mutableMapOf<SubantaPada, String>()

        phalaPadas.forEach { phalaPada ->
            val explicitOrder = MemoryOrderQualifierResolver.before(phalaPada, padas)
            val idx = subantas.indexOf(phalaPada)
            val genitiveModifier = subantas.take(idx)
                .lastOrNull { it.sup.text in setOf("ङस्", "आम्") && it !in resolvedGenitives }
                ?: return@forEach

            val modIdx = subantas.indexOf(genitiveModifier)
            val precedingSub = subantas.take(modIdx).lastOrNull()
            val base = genitiveModifier.pratipadika.baseText()
            val isPrevious = base.startsWith("पूर्व") ||
                precedingSub?.pratipadika?.baseText()?.startsWith("पूर्व") == true ||
                explicitOrder.previous
            val order = explicitOrder.copy(previous = isPrevious)

            if (precedingSub?.pratipadika?.baseText()?.startsWith("पूर्व") == true) {
                resolvedGenitives.add(precedingSub)
            }

            val cleanBase = if (base.startsWith("पूर्व")) base.removePrefix("पूर्व") else base
            val root = DhatuCache.getActionRoot(cleanBase)

            // ---- 1. Resolve against earlier clauses in this utterance ----------------
            val matchingIndices = (0 until ctx.clauseIndex).filter { i ->
                val prevDhatu = ctx.previousDhatus.getOrNull(i) ?: return@filter false
                val prevRoot = DhatuCache.getDhatuRoot(prevDhatu.upadesha)
                val prevActionRoots = prevDhatu.operations.mapTo(mutableSetOf()) {
                    DhatuCache.getActionRoot(it.name)
                }
                root == prevRoot || root in prevActionRoots
            }
            val withinUtteranceMatch = order.select(matchingIndices)

            if (withinUtteranceMatch != null) {
                phalaMap[phalaPada] = "योग-${withinUtteranceMatch + 1}"
                resolvedGenitives.add(genitiveModifier)
                if (explicitOrder.isExplicit && explicitOrder.pada != null) {
                    resolvedQualifiers.add(explicitOrder.pada)
                }
                return@forEach
            }

            // ---- 2. Resolve against kriyā-centred memory -----------------------------
            val referencedDhatu = (genitiveModifier.pratipadika as? KridantaPratipadika)
                ?.dhatu?.mulaDhatu?.let(DhatuCache::get)?.upadesha
            val rememberedKriya = referencedDhatu?.let { order.select(ctx.memory, it) }
            if (rememberedKriya != null) {
                phalaMap[phalaPada] = rememberedKriya.frame.id.value
                resolvedGenitives.add(genitiveModifier)
                if (explicitOrder.isExplicit && explicitOrder.pada != null) {
                    resolvedQualifiers.add(explicitOrder.pada)
                }
                return@forEach
            }

            // ---- 3. Compatibility fallback to conversation result history -----------
            val historicalResults = ctx.conversation?.resultHistory?.filter { result ->
                val dhatuUpadesha = ctx.conversation.metadata["dhatu:${result.invocationId}"]
                    ?: ctx.conversation.metadata["dhatu:${result.id}"]
                val prevDhatu = dhatuUpadesha?.let { DhatuCache.upadeshaDhatuCache[it] }
                    ?: return@filter false
                val prevRoot = DhatuCache.getDhatuRoot(prevDhatu.upadesha)
                val prevActionRoots = prevDhatu.operations.mapTo(mutableSetOf()) {
                    DhatuCache.getActionRoot(it.name)
                }
                root == prevRoot || root in prevActionRoots
            } ?: emptyList()

            val historicalResult = order.select(historicalResults)

            if (historicalResult != null) {
                phalaMap[phalaPada] = historicalResult.id
                resolvedGenitives.add(genitiveModifier)
                if (explicitOrder.isExplicit && explicitOrder.pada != null) {
                    resolvedQualifiers.add(explicitOrder.pada)
                }
            }
        }

        return PhalaResolution(phalaMap, resolvedGenitives, resolvedQualifiers)
    }
}
