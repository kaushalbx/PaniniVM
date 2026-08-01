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
    fun remember(values: List<RememberedKriya>): KriyaMemory {
        val knownIds = entries.mapTo(mutableSetOf()) { it.frame.id }
        return copy(entries = entries + values.filter { knownIds.add(it.frame.id) })
    }

    fun frame(id: KriyaId): KriyaFrame? = entries.lastOrNull { it.frame.id == id }?.frame

    fun latest(count: Int = 1): List<RememberedKriya> {
        require(count >= 0) { "A memory query count cannot be negative." }
        return entries.takeLast(count)
    }

    /** Selects the 1-based prathama, dvitīya, ... kriyā in chronological order. */
    fun ordinalKriya(number: Int, dhatuUpadesha: String? = null): RememberedKriya? {
        require(number > 0) { "A memory ordinal must be positive." }
        return matchingKriyas(dhatuUpadesha).getOrNull(number - 1)
    }

    /** Selects the latest kriyā; offset 1 is upāntima, 2 is the one before it, and so on. */
    fun latestKriya(dhatuUpadesha: String? = null, offset: Int = 0): RememberedKriya? {
        require(offset >= 0) { "A latest-memory offset cannot be negative." }
        val matches = matchingKriyas(dhatuUpadesha)
        return matches.getOrNull(matches.lastIndex - offset)
    }

    fun latestKriyas(dhatuUpadesha: String, count: Int = 1): List<RememberedKriya> {
        require(count >= 0) { "A memory query count cannot be negative." }
        return matchingKriyas(dhatuUpadesha).takeLast(count)
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

    private fun matchingKriyas(dhatuUpadesha: String?): List<RememberedKriya> =
        if (dhatuUpadesha == null) entries
        else entries.filter { it.frame.kriya?.dhatu?.upadesha == dhatuUpadesha }
}

internal fun KriyaFrame.withMemoryId(id: KriyaId): KriyaFrame = copy(
    id = id,
    relations = relations.map { it.copy(kriyaId = id) },
    qualifications = qualifications.map { it.copy(kriyaId = id) },
)
