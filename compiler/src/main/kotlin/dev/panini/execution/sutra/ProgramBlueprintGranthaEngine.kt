package dev.panini.execution.sutra

import dev.panini.execution.ExecutionScope
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextDecoding
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextDiagnostic
import dev.panini.sutra.runtime.SutraGranthaCompiler
import dev.panini.sutra.runtime.SutraGranthaDiagnostic
import dev.panini.sutra.runtime.SutraGranthaLowering
import dev.panini.sutra.runtime.SutraGranthaRegistry
import dev.panini.sutra.runtime.SutraGrantha
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

    data class InvalidSource(
        val diagnostics: List<SutraBlueprintGranthaTextDiagnostic>,
    ) : ProgramGranthaExecution
}

sealed interface ProgramGranthaValidation {
    data class Valid(
        val grantha: SutraGrantha<ProgramAvastha>,
    ) : ProgramGranthaValidation

    data class InvalidBlueprint(
        val diagnostics: List<ProgramBlueprintDiagnostic>,
    ) : ProgramGranthaValidation

    data class InvalidRuntime(
        val diagnostics: List<SutraGranthaDiagnostic>,
    ) : ProgramGranthaValidation

    data class InvalidSource(
        val diagnostics: List<SutraBlueprintGranthaTextDiagnostic>,
    ) : ProgramGranthaValidation
}

/** Processes evaluator-free program packages through every runtime boundary. */
object ProgramBlueprintGranthaEngine {
    fun validate(
        source: String,
        context: ProgramBlueprintContext,
    ): ProgramGranthaValidation {
        val grantha = when (val decoding = SutraBlueprintGranthaTextCodec.decode(source)) {
            is SutraBlueprintGranthaTextDecoding.Success -> decoding.grantha
            is SutraBlueprintGranthaTextDecoding.Invalid -> {
                return ProgramGranthaValidation.InvalidSource(decoding.diagnostics)
            }
        }
        return validate(grantha, context)
    }

    fun validate(
        grantha: SutraBlueprintGrantha,
        context: ProgramBlueprintContext,
    ): ProgramGranthaValidation {
        val compiled = when (val compilation = ProgramBlueprintGranthaCompiler.compile(grantha, context)) {
            is ProgramGranthaCompilation.Success -> compilation.grantha
            is ProgramGranthaCompilation.Invalid -> {
                return ProgramGranthaValidation.InvalidBlueprint(compilation.diagnostics)
            }
        }
        return when (val lowering = SutraGranthaCompiler.lower(compiled)) {
            is SutraGranthaLowering.Success -> ProgramGranthaValidation.Valid(compiled)
            is SutraGranthaLowering.Invalid ->
                ProgramGranthaValidation.InvalidRuntime(lowering.diagnostics)
        }
    }

    fun execute(
        source: String,
        context: ProgramBlueprintContext,
        scope: ExecutionScope,
        initialState: ProgramAvastha,
    ): ProgramGranthaExecution {
        return when (val validation = validate(source, context)) {
            is ProgramGranthaValidation.Valid ->
                execute(validation.grantha, scope, initialState)
            is ProgramGranthaValidation.InvalidSource ->
                ProgramGranthaExecution.InvalidSource(validation.diagnostics)
            is ProgramGranthaValidation.InvalidBlueprint ->
                ProgramGranthaExecution.InvalidBlueprint(validation.diagnostics)
            is ProgramGranthaValidation.InvalidRuntime ->
                ProgramGranthaExecution.InvalidRuntime(validation.diagnostics)
        }
    }

    fun execute(
        grantha: SutraBlueprintGrantha,
        context: ProgramBlueprintContext,
        scope: ExecutionScope,
        initialState: ProgramAvastha,
    ): ProgramGranthaExecution {
        return when (val validation = validate(grantha, context)) {
            is ProgramGranthaValidation.Valid ->
                execute(validation.grantha, scope, initialState)
            is ProgramGranthaValidation.InvalidSource ->
                ProgramGranthaExecution.InvalidSource(validation.diagnostics)
            is ProgramGranthaValidation.InvalidBlueprint ->
                ProgramGranthaExecution.InvalidBlueprint(validation.diagnostics)
            is ProgramGranthaValidation.InvalidRuntime ->
                ProgramGranthaExecution.InvalidRuntime(validation.diagnostics)
        }
    }

    private fun execute(
        compiled: SutraGrantha<ProgramAvastha>,
        scope: ExecutionScope,
        initialState: ProgramAvastha,
    ): ProgramGranthaExecution {
        val program = when (val lowering = SutraGranthaCompiler.lower(compiled)) {
            is SutraGranthaLowering.Success -> lowering.program
            is SutraGranthaLowering.Invalid ->
                return ProgramGranthaExecution.InvalidRuntime(lowering.diagnostics)
        }
        val loadedGranthas = scope.sutraRegistry?.granthas.orEmpty()
            .filterNot { it.id == compiled.id }
        val executionScope = scope.copy(
            sutraRegistry = SutraGranthaRegistry(loadedGranthas + compiled),
            currentGrantha = compiled.id,
        )
        return ProgramGranthaExecution.Completed(
            SutraMachine(ProgramSutraEffectInterpreter(executionScope)).process(
                program,
                initialState,
            ),
        )
    }
}
