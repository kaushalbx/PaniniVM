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

    fun derive(initial: DerivationState, bootstrap: List<DerivationSutra> = emptyList()): DerivationResult =
        execute(initial, emptySet(), bootstrap).single()

    /** Branches only phases explicitly selected by the workflow. */
    fun deriveAll(
        initial: DerivationState,
        branchingStages: Set<SutraStage>,
        bootstrap: List<DerivationSutra> = emptyList(),
    ): List<DerivationResult> = execute(initial, branchingStages, bootstrap)

    private fun execute(
        initial: DerivationState,
        branchingStages: Set<SutraStage>,
        bootstrap: List<DerivationSutra>,
    ): List<DerivationResult> {
        val bootstrapped = bootstrap.fold(Accumulated(initial)) { accumulated, sutra ->
            if (!sutra.matches(accumulated.state)) return@fold accumulated
            val change = sutra.apply(accumulated.state)
            require(change.applied && change.state != accumulated.state) {
                "Bootstrap sūtra ${sutra.sutra} did not perform an operation."
            }
            accumulated.append(sutra, change)
        }
        var branches = listOf(bootstrapped)
        phases.forEach { phase ->
            branches = branches.flatMap { accumulated ->
                if (!isStageEnabled(phase.stage, initial, accumulated.state)) return@flatMap listOf(accumulated)
                val prepared = prepareStage(phase.stage, accumulated.state)
                val results = if (phase.stage in branchingStages) {
                    phase.engine.deriveAll(prepared)
                } else {
                    listOf(phase.engine.derive(prepared, configForStage(phase.stage).copy(validateFinalItProcessing = false)))
                }
                results.map { accumulated.append(it) }
            }
        }

        return branches
            .map { accumulated ->
                val final = finalizeState(accumulated.state).let { state ->
                    if (state.stage == DerivationStage.FINAL) state.requireCompleteItProcessing() else state
                }
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

        fun append(sutra: DerivationSutra, change: DerivationChange): Accumulated {
            val application = DerivationApplication(
                sutra = sutra.sutra,
                role = sutra.role,
                action = sutra.action,
                scope = sutra.scope,
                trace = sutra.renderTrace(),
                before = state,
                after = change.state,
                explanation = change.explanation,
            )
            return copy(
                state = change.state,
                applications = applications + application,
                events = events + DerivationEvent.RuleApplied(
                    sutra.sutra,
                    state,
                    change.state,
                    change.explanation,
                ),
            )
        }
    }
}
