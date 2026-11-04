package dev.panini.analysis

/**
 * Established clause-local facts, indexed by the flattened nominal occurrence
 * list used by KriyaFrame.nominalRelations. These facts do not modify a lexicon
 * or claim to have been inferred automatically.
 */
data class VakyaSemanticContext(
    val participantRelations: Map<Int, Set<SemanticRelation>> = emptyMap(),
    /** Recipient occurrence → object occurrence; a present null value means explicitly implied. */
    val intendedObjects: Map<Int, Int?> = emptyMap(),
    val krtGovernors: Map<Int, KrtGovernorReading> = emptyMap(),
    val avyayaGovernors: Map<Int, dev.panini.vyakaranam.ast.AvyayaPada> = emptyMap(),
    val purposeGovernors: Map<Int, PurposeActionGovernor> = emptyMap(),
) {
    init {
        require((participantRelations.keys + intendedObjects.keys + krtGovernors.keys + avyayaGovernors.keys + purposeGovernors.keys).all { it >= 0 })
        require(krtGovernors.keys.intersect(avyayaGovernors.keys).isEmpty()) { "A selected dependent cannot have conflicting kṛt and avyaya governors." }
        require(purposeGovernors.keys.intersect(krtGovernors.keys + avyayaGovernors.keys).isEmpty()) {
            "A purpose-action dependent cannot also select another governor."
        }
        require(purposeGovernors.values.groupBy { it.id }.values.all { group -> group.all {
            it == group.first() && (it !is PurposeActionGovernor.Written ||
                it.pada === (group.first() as PurposeActionGovernor.Written).pada)
        } }) { "One purpose action identity cannot carry conflicting readings or written occurrences." }
        val writtenIds = java.util.IdentityHashMap<dev.panini.vyakaranam.ast.AvyayaPada, String>()
        purposeGovernors.values.filterIsInstance<PurposeActionGovernor.Written>().forEach {
            val previous = writtenIds.put(it.pada, it.id)
            require(previous == null || previous == it.id) { "One written purpose occurrence cannot have multiple action identities." }
        }
        require(intendedObjects.values.filterNotNull().all { it >= 0 })
        require(intendedObjects.none { (recipient, obj) -> recipient == obj })
        require(krtGovernors.none { (dependent, reading) -> dependent == reading.governorIndex })
        require(krtGovernors.values.groupBy { it.governorIndex }.values.all { it.distinct().size == 1 }) {
            "One kṛt occurrence cannot carry conflicting selected readings."
        }
        krtGovernors.keys.forEach { start ->
            val seen = mutableSetOf<Int>()
            var current: Int? = start
            while (current != null) {
                require(seen.add(current)) { "Kṛt governor links cannot form a dependency cycle." }
                current = krtGovernors[current]?.governorIndex
            }
        }
    }

    internal fun validate(nominalCount: Int, padas: List<dev.panini.vyakaranam.ast.Pada>? = null) {
        require((participantRelations.keys + intendedObjects.keys + intendedObjects.values.filterNotNull() +
            krtGovernors.keys + krtGovernors.values.map { it.governorIndex } + avyayaGovernors.keys + purposeGovernors.keys).all { it < nominalCount }) {
            "Semantic context refers to a nominal occurrence outside this clause."
        }
        if (padas != null) require(avyayaGovernors.values.all { governor -> padas.any { it === governor } }) {
            "An avyaya governor must reference an actual occurrence in this clause."
        }
        if (padas != null) require(purposeGovernors.values.filterIsInstance<PurposeActionGovernor.Written>().all {
            governor -> padas.any { it === governor.pada }
        }) { "A written purpose action must reference an actual occurrence in this clause." }
    }
}
