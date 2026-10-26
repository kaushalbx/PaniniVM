package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SequenceConnector
import dev.panini.vyakaranam.ast.SequenceResultFlow
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
        val independentConnectors = setOf(
            SequenceConnector.SAMUCCAYA,
            SequenceConnector.VAKYANTARA,
            SequenceConnector.TADANIM,
        )
        if (node.independentActions ||
            (node.statements.size > 1 && node.connectorKinds.all { it in independentConnectors })) return buildList {
            for (statement in node.statements) {
                val produced = executeNode(statement)
                addAll(produced)
                if (produced.any { it !is ExecutionResult.Success || it.controlSignal != null }) break
            }
        }
        val hasNamedStage = try { node.statements.withIndex().any { (index, stage) ->
            stage is Invocation && registry.detectInvocation(
                Ukti(sourceText = stage.sourceText, body = stage), callerSourceFile = sourceFile,
                injectedKarman = if (index > 0 && node.boundaries.getOrNull(index - 1)?.resultFlow ==
                    SequenceResultFlow.PREVIOUS_RESULT)
                    InjectedKarmanBinding(PIPE_OPERAND, null) else null) != null
        } } catch (failure: IllegalArgumentException) {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                failure.message ?: "Invalid procedure invocation."))
        }
        val startsWithImplicitValue = (node.statements.firstOrNull() as? Invocation)?.implicitValue != null
        // Structured stages (for example a conditional after ततः) must be
        // executed from their existing AST. Sending the whole Sequence back
        // through evalParsed re-enters this executor with the same node.
        val hasStructuredStage = node.statements.any { it !is Invocation }
        val hasAnaphor = node.statements.filterIsInstance<Invocation>().any { invocation ->
            invocation.vakya.padas.any { pada -> when (pada) {
                is dev.panini.vyakaranam.ast.SubantaPada -> AnaphoricOperandProjection.agreement(pada) != null
                is dev.panini.vyakaranam.ast.SamuccitaSubanta -> pada.members.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().any { AnaphoricOperandProjection.agreement(it) != null }
                else -> false
            } }
        }
        val hasReturnStage = node.statements.filterIsInstance<Invocation>().any { invocation ->
            if (scope.procedureBindings?.hasProcedureOwner == true && ProcedureResultStatement.analyze(invocation,
                    roleShadowed = scope.procedureBindings.isDeclaredInProcedure("फल"))
                    is ProcedureResultStatement.Analysis.Resolved) return@any true
            val head = invocation.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.TingantaPada>().singleOrNull()
            val root = head?.let(dev.panini.execution.binding.DhatuCache::resolve)
            val features = head?.let { GrammaticalFeatures(it.upasargas.toSet(), it.dhatu.sanadiPratyayas.toSet(),
                lakara = it.lakara) }
            root?.operations?.any { it.controlSignal == ExecutionControlSignal.RETURN_PROCEDURE &&
                features != null && it.trigger.matches(features) } == true
        }
        // क्त्वा/ल्यप् lowering has already established explicit prior completion.
        // Execute those AST stages directly even when they need no result injection.
        val hasPriorAction = node.connectorKinds.any { it == SequenceConnector.PURVAKALA }
        if (node.statements.size < 2 ||
            node.connectorKinds.any { it !in setOf(SequenceConnector.ANANTARYA, SequenceConnector.PURVAKALA) } ||
            (!hasPriorAction && !hasNamedStage && !startsWithImplicitValue && !hasAnaphor && !hasReturnStage && !hasStructuredStage)
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
                    it.controlSignal != null }) break
            val invocation = stage as? Invocation
            stageResults = if (invocation != null && pipedValue != null &&
                node.boundaries.getOrNull(index)?.resultFlow == SequenceResultFlow.PREVIOUS_RESULT) {
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
