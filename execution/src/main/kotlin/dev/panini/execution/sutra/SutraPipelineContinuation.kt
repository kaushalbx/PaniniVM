package dev.panini.execution.sutra

import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.sutra.runtime.SutraProgram

data class SutraPipelineContinuation(
    val input: SanskritUktiInput,
    val conversation: SambhashanaContext,
    val program: SutraProgram<ProgramAvastha>,
    val state: ProgramAvastha,
)
