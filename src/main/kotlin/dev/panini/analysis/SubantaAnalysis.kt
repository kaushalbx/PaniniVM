package dev.panini.analysis

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.execution.ExecutionSamjna
import dev.panini.parser.ast.ParsedNominalBase

data class SubantaAnalysis(
    val base: ParsedNominalBase,
    val sup: SupAnalysis?,
    val samjnas: Set<ExecutionSamjna> = emptySet(),
) {
    val prakriti: String
        get() = base.segmentedText

    val pratyaya: String?
        get() = sup?.pratyaya

    val vibhaktiCandidates: Set<Vibhakti>
        get() = sup
            ?.candidates
            ?.mapTo(mutableSetOf()) { it.vibhakti }
            .orEmpty()

    val vacanaCandidates: Set<Vacana>
        get() = sup
            ?.candidates
            ?.mapTo(mutableSetOf()) { it.vacana }
            .orEmpty()

    val resolvedVibhakti: Vibhakti?
        get() = sup
            ?.resolvedPosition
            ?.vibhakti

    val resolvedVacana: Vacana?
        get() = sup
            ?.resolvedPosition
            ?.vacana
}
