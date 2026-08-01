package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.SutraStage

/**
 * Runs metadata-selected sūtras through an ordered sequence of grammatical phases.
 * Workflows declare their stages and policies; rule membership remains owned by the
 * canonical Aṣṭādhyāyī registry.
 */
class DerivationPipeline(
    stages: List<SutraStage>,
    private val prepareStage: (SutraStage, DerivationState) -> DerivationState = { _, state -> state },
    private val configForStage: (SutraStage) -> DerivationConfig = { DerivationConfig() },
    private val isStageEnabled: (SutraStage, DerivationState, DerivationState) -> Boolean = { _, _, _ -> true },
    private val finalizeState: (DerivationState) -> DerivationState = { it },
    sutrasForStage: (SutraStage) -> List<DerivationSutra> = Ashtadhyayi::executableSutrasAt,
) {
    private val phases: List<Phase> = stages.map { stage ->
        require(stage != SutraStage.UNSPECIFIED) { "A derivation pipeline cannot route UNSPECIFIED sūtras." }
        val sutras = sutrasForStage(stage)
        require(sutras.isNotEmpty()) { "No executable sūtras registered for $stage." }
        require(sutras.all { it.stage == stage }) { "The $stage registry view contains incorrectly staged sūtras." }
        Phase(stage, DerivationEngine(sutras))
    }

    fun derive(initial: DerivationState): DerivationResult =
        execute(initial, emptySet()).single()

    /** Branches only phases explicitly selected by the workflow. */
    fun deriveAll(initial: DerivationState, branchingStages: Set<SutraStage>): List<DerivationResult> =
        execute(initial, branchingStages)

    private fun execute(initial: DerivationState, branchingStages: Set<SutraStage>): List<DerivationResult> {
        var branches = listOf(Accumulated(initial))
        phases.forEach { phase ->
            branches = branches.flatMap { accumulated ->
                if (!isStageEnabled(phase.stage, initial, accumulated.state)) return@flatMap listOf(accumulated)
                val prepared = prepareStage(phase.stage, accumulated.state)
                val results = if (phase.stage in branchingStages) {
                    phase.engine.deriveAll(prepared)
                } else {
                    listOf(phase.engine.derive(prepared, configForStage(phase.stage)))
                }
                results.map { accumulated.append(it) }
            }
        }

        return branches
            .map { accumulated ->
                val final = finalizeState(accumulated.state)
                DerivationResult(
                    initial = initial,
                    final = final,
                    applications = accumulated.applications,
                    events = accumulated.events + DerivationEvent.Completed(final, accumulated.applications.size),
                )
            }
            .distinctBy { result -> result.final to result.applications.map(DerivationApplication::sutra) }
    }

    private data class Phase(val stage: SutraStage, val engine: DerivationEngine)

    private data class Accumulated(
        val state: DerivationState,
        val applications: List<DerivationApplication> = emptyList(),
        val events: List<DerivationEvent> = emptyList(),
    ) {
        fun append(result: DerivationResult): Accumulated = copy(
            state = result.final,
            applications = applications + result.applications,
            events = events + result.events.filterNot { it is DerivationEvent.Completed },
        )
    }
}
