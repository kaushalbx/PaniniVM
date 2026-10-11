package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SequenceConnector
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
        val hasNamedStage = node.statements.drop(1).withIndex().any { (index, stage) ->
            stage is Invocation && registry.detectInvocation(
                Ukti(sourceText = stage.sourceText, body = stage),
                callerSourceFile = sourceFile,
                injectedKarman = if (node.connectorKinds.getOrNull(index) == SequenceConnector.ANANTARYA)
                    InjectedKarmanBinding(PIPE_OPERAND, null) else null,
            ) != null
        }
        val startsWithImplicitValue = (node.statements.firstOrNull() as? Invocation)?.implicitValue != null
        val hasAnaphor = node.statements.filterIsInstance<Invocation>().any { invocation ->
            invocation.vakya.padas.any { pada -> when (pada) {
                is dev.panini.vyakaranam.ast.SubantaPada -> AnaphoricOperandProjection.agreement(pada) != null
                is dev.panini.vyakaranam.ast.SamuccitaSubanta -> pada.members.any {
                    AnaphoricOperandProjection.agreement(it) != null
                }
                else -> false
            } }
        }
        if (node.statements.size < 2 ||
            node.connectorKinds.any { it !in setOf(SequenceConnector.ANANTARYA, SequenceConnector.PURVAKALA) } ||
            (!hasNamedStage && !startsWithImplicitValue && !hasAnaphor)
        ) return evaluateWhole()

        val results = mutableListOf<ExecutionResult>()
        var stageResults = executeNode(node.statements.first())
        results += stageResults
        fun resultValue(produced: List<ExecutionResult>): SanskritValue? =
            produced.filterIsInstance<ExecutionResult.Success>().lastOrNull()?.let {
                it.typedValue ?: SanskritValue.of(it.value)
            }
        var pipedValue = resultValue(stageResults)
        for ((index, stage) in node.statements.drop(1).withIndex()) {
            if (stageResults.any { it !is ExecutionResult.Success ||
                    it.controlSignal == ExecutionControlSignal.BREAK_LOOP }) break
            val invocation = stage as? Invocation
            stageResults = if (invocation != null && pipedValue != null &&
                node.connectorKinds.getOrNull(index) == SequenceConnector.ANANTARYA) {
                val stageScope = scope.copy(
                    environment = scope.environment.mergedWith(
                        ValueEnvironment(mapOf(PIPE_OPERAND to requireNotNull(pipedValue))),
                    ),
                )
                executePipedInvocation(invocation, stageScope, InjectedKarmanBinding(PIPE_OPERAND, pipedValue))
            } else executeNode(stage)
            results += stageResults
            pipedValue = resultValue(stageResults)
        }
        return results
    }
}
