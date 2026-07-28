package dev.panini.execution.sutra

import dev.panini.execution.ExecutionScope
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraGranthaCompiler
import dev.panini.sutra.runtime.SutraGranthaDiagnostic
import dev.panini.sutra.runtime.SutraGranthaLowering
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult

sealed interface ProgramGranthaExecution {
    data class Completed(
        val result: SutraMachineResult<ProgramAvastha>,
    ) : ProgramGranthaExecution

    data class InvalidBlueprint(
        val diagnostics: List<ProgramBlueprintDiagnostic>,
    ) : ProgramGranthaExecution

    data class InvalidRuntime(
        val diagnostics: List<SutraGranthaDiagnostic>,
    ) : ProgramGranthaExecution
}

/** Processes evaluator-free program packages through every runtime boundary. */
object ProgramBlueprintGranthaEngine {
    fun execute(
        grantha: SutraBlueprintGrantha,
        context: ProgramBlueprintContext,
        scope: ExecutionScope,
        initialState: ProgramAvastha,
    ): ProgramGranthaExecution {
        val compiled = when (val compilation = ProgramBlueprintGranthaCompiler.compile(grantha, context)) {
            is ProgramGranthaCompilation.Success -> compilation.grantha
            is ProgramGranthaCompilation.Invalid -> {
                return ProgramGranthaExecution.InvalidBlueprint(compilation.diagnostics)
            }
        }
        val program = when (val lowering = SutraGranthaCompiler.lower(compiled)) {
            is SutraGranthaLowering.Success -> lowering.program
            is SutraGranthaLowering.Invalid -> {
                return ProgramGranthaExecution.InvalidRuntime(lowering.diagnostics)
            }
        }
        return ProgramGranthaExecution.Completed(
            SutraMachine(ProgramSutraEffectInterpreter(scope)).process(
                program,
                initialState,
            ),
        )
    }
}
