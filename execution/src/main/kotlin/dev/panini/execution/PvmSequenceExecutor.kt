package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.Ukti

/** Executes ततः sequences and carries typed results between their stages. */
internal class PvmSequenceExecutor {
    fun execute(
        node: Sequence,
        scope: ExecutionScope,
        registry: PrakriyaRegistry,
        sourceFile: String?,
        evaluateWhole: () -> List<ExecutionResult>,
        executeNode: (dev.panini.vyakaranam.ast.ProgramNode) -> List<ExecutionResult>,
        executePipedInvocation: (Invocation, ExecutionScope, InjectedKarmanBinding) -> List<ExecutionResult>,
    ): List<ExecutionResult> {
        val hasNamedStage = node.statements.drop(1).any { stage ->
            stage is Invocation && registry.detectInvocation(
                Ukti(sourceText = stage.sourceText, body = stage),
                callerSourceFile = sourceFile,
                injectedKarman = InjectedKarmanBinding(PIPE_OPERAND, null),
            ) != null
        }
        val startsWithImplicitValue = (node.statements.firstOrNull() as? Invocation)?.implicitValue != null
        if (node.statements.size < 2 || node.connectors.any { it != "ततः" } ||
            (!hasNamedStage && !startsWithImplicitValue)
        ) return evaluateWhole()

        val results = mutableListOf<ExecutionResult>()
        var stageResults = executeNode(node.statements.first())
        results += stageResults
        var pipedValue = stageResults.filterIsInstance<ExecutionResult.Success>().lastOrNull()?.typedValue
        for (stage in node.statements.drop(1)) {
            if (stageResults.any { it is ExecutionResult.Failure }) break
            val invocation = stage as? Invocation
            stageResults = if (invocation != null && pipedValue != null) {
                val stageScope = scope.copy(
                    environment = scope.environment.mergedWith(
                        ValueEnvironment(mapOf(PIPE_OPERAND to requireNotNull(pipedValue))),
                    ),
                )
                executePipedInvocation(invocation, stageScope, InjectedKarmanBinding(PIPE_OPERAND, pipedValue))
            } else executeNode(stage)
            results += stageResults
            pipedValue = stageResults.filterIsInstance<ExecutionResult.Success>()
                .lastOrNull()?.typedValue ?: pipedValue
        }
        return results
    }
}
