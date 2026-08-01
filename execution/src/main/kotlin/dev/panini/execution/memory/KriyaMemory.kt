package dev.panini.execution.memory

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaId
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KarakaRelation
import dev.panini.core.Karaka
import dev.panini.execution.SanskritValue
import dev.panini.shiksha.Samjna

/** One completed kriyā and the value it produced. */
data class RememberedKriya(
    val turn: Int,
    val frame: KriyaFrame,
    val phala: SanskritValue?,
)

/** Chronological, kriyā-centred discourse memory for one VM session. */
data class KriyaMemory(
    val entries: List<RememberedKriya> = emptyList(),
) {
    fun remember(values: List<RememberedKriya>): KriyaMemory = copy(entries = entries + values)

    fun frame(id: KriyaId): KriyaFrame? = entries.lastOrNull { it.frame.id == id }?.frame

    fun latest(count: Int = 1): List<RememberedKriya> {
        require(count >= 0) { "A memory query count cannot be negative." }
        return entries.takeLast(count)
    }

    fun latestKriyas(dhatuUpadesha: String, count: Int = 1): List<RememberedKriya> {
        require(count >= 0) { "A memory query count cannot be negative." }
        return entries.asReversed()
            .filter { it.frame.kriya?.dhatu?.upadesha == dhatuUpadesha }
            .take(count)
            .reversed()
    }

    fun latestKarakaRelations(karaka: Karaka, count: Int = 1): List<KarakaRelation> {
        require(count >= 0) { "A memory query count cannot be negative." }
        return entries.asReversed().flatMap { entry -> entry.frame.relations.asReversed() }
            .filter { (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == karaka }
            .take(count)
            .reversed()
    }

    fun latestPhalas(
        count: Int = 1,
        requiredSamjnas: Set<Samjna> = emptySet(),
    ): List<SanskritValue> {
        require(count >= 0) { "A memory query count cannot be negative." }
        return entries.asReversed().mapNotNull(RememberedKriya::phala)
            .filter { it.samjnas.containsAll(requiredSamjnas) }
            .take(count)
            .reversed()
    }
}
