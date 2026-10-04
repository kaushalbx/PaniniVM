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
        val persistSession: Boolean,
        val structStore: Map<String, TaddhitaStruct>,
        val resolveValue: (String) -> SanskritValue? = { null },
        val onResult: ((ExecutionResult) -> Unit)?,
    )

    fun execute(request: Request): List<ExecutionResult> {
        val node = request.conditional
        if (PvmSentenceClassifier.containsAttributeCondition(node)) {
            val result = executeStructured(node, request)
            request.onResult?.invoke(result)
            return listOf(result)
        }
        val truthTest = (node.condition as? dev.panini.vyakaranam.ast.Invocation)
            ?.let(NaturalSemanticNormalizer::normalize)
            as? NaturalSemanticNormalizer.Operation.TruthTest
        val conditionResults = truthTest?.let { test ->
            val state = request.scope.environment.values[test.stateName]
                ?: request.resolveValue(test.stateName)
            val truth = (state as? SanskritValue.Satya)?.boolean
            if (truth == null) {
                null
            } else {
                val value = if (test.negated) !truth else truth
                listOf(
                    ExecutionResult.Success(
                        value = if (value) "सत्यम्" else "असत्यम्",
                        operation = "pvm.truth-test",
                        trace = listOf("Resolved the nominative copular subject '${test.stateName}' as a truth state."),
                        typedValue = SanskritValue.Satya(value),
                        conditionValue = value,
                    ),
                )
            }
        } ?: request.executeNode(node.condition, true)
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
            persistSession = request.persistSession,
        )
    }
}
