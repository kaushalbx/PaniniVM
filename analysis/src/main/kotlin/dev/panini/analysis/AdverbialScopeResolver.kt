package dev.panini.analysis

/** Targets identify occurrences, so repeated identical words remain distinct. */
sealed interface AdverbialTarget {
    data class Event(val kriyaId: KriyaId) : AdverbialTarget
    data class Proposition(val kriyaId: KriyaId) : AdverbialTarget
    data class Constituent(val kriyaId: KriyaId, val sourceIndex: Int) : AdverbialTarget
}

sealed interface AdverbialResolution {
    data class Resolved(val qualification: KriyaQualification, val target: AdverbialTarget) : AdverbialResolution
    data class Unresolved(val qualification: KriyaQualification, val reason: String) : AdverbialResolution
}

/**
 * Validates an explicitly established attachment; word order does not supply it.
 * Clause correlations and distribution need their own relational analysis.
 */
object AdverbialScopeResolver {
    fun resolve(
        qualification: KriyaQualification,
        analyses: List<PadaAnalysis>,
        target: AdverbialTarget?,
        nominalReadingExcluded: Boolean = false,
    ): AdverbialResolution {
        fun unresolved(reason: String) = AdverbialResolution.Unresolved(qualification, reason)
        val index = qualification.sourceIndex
            ?: return unresolved("The source occurrence is unknown.")
        val source = analyses.getOrNull(index)
        if (source != qualification.source || source?.pada !== qualification.source.pada) {
            return unresolved("The source occurrence does not match this clause.")
        }
        val semantics = qualification.semantics
            ?: return unresolved("The adverbial meaning is unknown.")
        if (qualification.competingNominalReading && !nominalReadingExcluded) {
            return unresolved("A nominal reading remains possible.")
        }
        if (target == null) return unresolved("No attachment has been established.")
        val (frame, scope) = when (target) {
            is AdverbialTarget.Event -> target.kriyaId to AdverbialScope.EVENT
            is AdverbialTarget.Proposition -> target.kriyaId to AdverbialScope.PROPOSITION
            is AdverbialTarget.Constituent -> {
                if (target.sourceIndex == index || analyses.getOrNull(target.sourceIndex) == null) {
                    return unresolved("The constituent target must be another occurrence in this clause.")
                }
                target.kriyaId to AdverbialScope.CONSTITUENT
            }
        }
        if (frame != qualification.kriyaId) return unresolved("The target belongs to another clause.")
        if (scope !in semantics.scopes) return unresolved("The lexical reading does not license this scope.")
        return AdverbialResolution.Resolved(qualification, target)
    }
}
