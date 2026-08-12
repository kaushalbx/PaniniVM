package dev.panini.execution

import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.persistence.StateStore
import dev.panini.execution.sutra.ProgramAvastha
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintGranthaEngine
import dev.panini.execution.sutra.ProgramGranthaExecution
import dev.panini.execution.sutra.SutraPipelineContinuation
import dev.panini.sutra.runtime.SutraMachineResult
import java.io.File

/** Executes canonical sūtra-grantha sources behind the [PaniniVM] facade. */
internal class GranthaExecutor(
    private val stateStore: StateStore,
    private val externalDispatcher: ExternalCapabilityDispatcher,
) {
    fun eval(
        source: String,
        sourceName: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
    ): ExecutionResult {
        val effectiveScope = scope.copy(
            stateStore = scope.stateStore ?: stateStore,
            externalDispatcher = scope.externalDispatcher ?: externalDispatcher,
        )
        return when (
            val execution = ProgramBlueprintGranthaEngine.execute(
                source,
                ProgramBlueprintContext(speaker = speaker, listener = listener, text = sourceName),
                effectiveScope,
                ProgramAvastha(ValueEnvironment()),
            )
        ) {
            is ProgramGranthaExecution.InvalidSource -> ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                execution.diagnostics.joinToString("\n") { diagnostic ->
                    "${diagnostic.code}${diagnostic.position?.let { " at $it" }.orEmpty()}: ${diagnostic.message}"
                },
            )
            is ProgramGranthaExecution.InvalidBlueprint -> invalidDiagnostics(
                execution.diagnostics.map { it.code.toString() to it.message },
            )
            is ProgramGranthaExecution.InvalidRuntime -> invalidDiagnostics(
                execution.diagnostics.map { it.code.toString() to it.message },
            )
            is ProgramGranthaExecution.Completed -> completed(execution, sourceName, speaker, listener)
        }
    }

    fun evalFile(file: File, scope: ExecutionScope, speaker: String, listener: String): ExecutionResult {
        require(file.exists()) { "Sūtra grantha source file not found: ${file.path}" }
        return eval(file.readText(), file.name, scope, speaker, listener)
    }

    private fun completed(
        execution: ProgramGranthaExecution.Completed,
        sourceName: String,
        speaker: String,
        listener: String,
    ): ExecutionResult = when (val result = execution.result) {
        is SutraMachineResult.Failure -> ExecutionResult.Failure(
            ExecutionError.ACTION_FAILED,
            "Sūtra ${result.failedSutra}: ${result.message}",
            result.trace.map { it.toString() },
        )
        is SutraMachineResult.Success -> {
            val phala = result.state.lastPhala ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Grantha '$sourceName' completed without producing a result.",
            )
            val continuation = SutraPipelineContinuation(
                input = SanskritUktiInput(text = sourceName, speaker = speaker, listener = listener),
                conversation = SambhashanaContext(speaker = speaker, listener = listener),
                program = execution.program,
                state = result.state,
            )
            when (phala) {
                is Phala.AnumatiApekshita -> phala.copy(pipelineContinuation = continuation)
                is Phala.SvikaraApekshita -> phala.copy(pipelineContinuation = continuation)
                else -> phala
            }.toExecutionResult("panini.grantha")
        }
    }

    private fun invalidDiagnostics(diagnostics: List<Pair<String, String>>): ExecutionResult.Failure =
        ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            diagnostics.joinToString("\n") { (code, message) -> "$code: $message" },
        )
}
