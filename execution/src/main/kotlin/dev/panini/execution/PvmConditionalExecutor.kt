package dev.panini.execution

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Ukti

/** Evaluates one conditional and selects its AST branch. */
internal class PvmConditionalExecutor(
    private val vm: PaniniVM,
    private val structuredValueExecutor: StructuredValueExecutor,
) {
    data class Request(
        val conditional: Conditional,
        val executeNode: (ProgramNode, conditionEvaluation: Boolean) -> List<ExecutionResult>,
        val sessionKey: String,
        val scope: ExecutionScope,
        val speaker: String,
        val listener: String,
        val structStore: Map<String, TaddhitaStruct>,
        val onResult: ((ExecutionResult) -> Unit)?,
    )

    fun execute(request: Request): List<ExecutionResult> {
        val node = request.conditional
        if (PvmSentenceClassifier.containsAttributeCondition(node)) {
            val result = executeStructured(node, request)
            request.onResult?.invoke(result)
            return listOf(result)
        }
        val conditionResults = request.executeNode(node.condition, true)
        val success = conditionResults.filterIsInstance<ExecutionResult.Success>().lastOrNull()
        val condition = success?.conditionValue ?: (success?.typedValue as? SanskritValue.Satya)?.boolean
        if (condition == null) {
            return conditionResults + ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "A conditional expression must produce a truth value.",
            )
        }
        val branch = if (condition) node.consequent else node.alternate
        val branchResults = branch?.let { request.executeNode(it, false) }.orEmpty()
        // Preserve the grammatical condition's satya-phala for an enclosing
        // फल-controlled loop even when the selected branch prints feedback.
        return conditionResults + branchResults
    }

    private fun executeStructured(
        conditional: Conditional,
        request: Request,
    ): ExecutionResult = structuredValueExecutor.executeConditional(
        conditional,
        request.structStore,
    ) { resolved, operands ->
        vm.evalParsed(
            Ukti(sourceText = resolved.sourceText, body = resolved),
            request.sessionKey,
            request.scope.copy(environment = request.scope.environment.mergedWith(operands)),
            request.speaker,
            request.listener,
        )
    }
}
