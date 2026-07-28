package dev.panini.sutra.runtime

fun interface SutraEffectInterpreter<S : SutraAvastha> {
    fun apply(effect: SutraEffect<S>, state: S): SutraEffectApplication<S>
}
sealed interface SutraEffectApplication<out S : SutraAvastha> {
    data class Applied<S : SutraAvastha>(
        val state: S,
        val explanation: String,
    ) : SutraEffectApplication<S>

    data class Failed(
        val message: String,
    ) : SutraEffectApplication<Nothing>
}

sealed interface SutraTraceEntry {
    val sutraId: SutraId

    data class Applied(
        override val sutraId: SutraId,
        val reasons: List<String>,
        val effects: List<String>,
    ) : SutraTraceEntry

    data class Skipped(
        override val sutraId: SutraId,
        val reasons: List<String>,
    ) : SutraTraceEntry

    data class Blocked(
        override val sutraId: SutraId,
        val blocker: SutraId,
        val reasons: List<String>,
    ) : SutraTraceEntry

    data class Invalid(
        override val sutraId: SutraId,
        val message: String,
    ) : SutraTraceEntry
}

sealed interface SutraMachineResult<out S : SutraAvastha> {
    val state: S
    val trace: List<SutraTraceEntry>

    data class Success<S : SutraAvastha>(
        override val state: S,
        override val trace: List<SutraTraceEntry>,
    ) : SutraMachineResult<S>

    data class Failure<S : SutraAvastha>(
        override val state: S,
        override val trace: List<SutraTraceEntry>,
        val failedSutra: SutraId,
        val message: String,
    ) : SutraMachineResult<S>
}

/**
 * First migration-stage machine. It processes an already ordered program once.
 * Domain-specific agenda scheduling and Aṣṭādhyāyī conflict resolution remain
 * outside this class until their legacy behaviour has parity coverage.
 */
class SutraMachine<S : SutraAvastha>(
    private val effectInterpreter: SutraEffectInterpreter<S>,
) {
    fun process(
        program: SutraProgram<S>,
        initialState: S,
        maximumSteps: Int = 10_000,
    ): SutraMachineResult<S> {
        require(maximumSteps > 0) { "The maximum sūtra step count must be positive." }
        var state = initialState
        val trace = mutableListOf<SutraTraceEntry>()

        program.sutras.forEachIndexed { index, sutra ->
            if (index >= maximumSteps) {
                return SutraMachineResult.Failure(
                    state,
                    trace,
                    sutra.id,
                    "Sūtra program exceeded the maximum step count of $maximumSteps.",
                )
            }
            when (val decision = sutra.evaluator.evaluate(sutra, state)) {
                is SutraNirnaya.Applicable -> {
                    val explanations = mutableListOf<String>()
                    decision.effects.forEach { effect ->
                        when (val application = effectInterpreter.apply(effect, state)) {
                            is SutraEffectApplication.Applied -> {
                                state = application.state
                                explanations += application.explanation
                            }
                            is SutraEffectApplication.Failed -> {
                                val entry = SutraTraceEntry.Invalid(sutra.id, application.message)
                                return SutraMachineResult.Failure(
                                    state,
                                    trace + entry,
                                    sutra.id,
                                    application.message,
                                )
                            }
                        }
                    }
                    trace += SutraTraceEntry.Applied(
                        sutra.id,
                        decision.reasons,
                        explanations,
                    )
                }
                is SutraNirnaya.NotApplicable ->
                    trace += SutraTraceEntry.Skipped(sutra.id, decision.reasons)
                is SutraNirnaya.Blocked ->
                    trace += SutraTraceEntry.Blocked(sutra.id, decision.blocker, decision.reasons)
                is SutraNirnaya.Invalid -> {
                    val entry = SutraTraceEntry.Invalid(sutra.id, decision.message)
                    return SutraMachineResult.Failure(
                        state,
                        trace + entry,
                        sutra.id,
                        decision.message,
                    )
                }
            }
        }
        return SutraMachineResult.Success(state, trace)
    }
}
