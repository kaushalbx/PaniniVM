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
        val structuredCondition = (node.condition as? dev.panini.vyakaranam.ast.Invocation)?.takeIf {
            TaddhitaStructEngine.detectAttributeReference(it.vakya) != null
        }?.let { condition ->
            val resolved = structuredValueExecutor.resolveInvocation(condition, request.structStore)
                ?: return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                    "A structured attribute used by the condition could not be resolved."))
            val test = NaturalSemanticNormalizer.normalize(resolved.invocation) as? NaturalSemanticNormalizer.Operation.TruthTest
            if (test != null) {
                val value = resolved.environment.values[test.stateName] as? SanskritValue.Satya
                    ?: return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                        "A field truth predicate requires सत्य/असत्य."))
                val truth = if (test.negated) !value.boolean else value.boolean
                listOf(ExecutionResult.Success(if (truth) "सत्यम्" else "असत्यम्", "pvm.truth-test",
                    typedValue = SanskritValue.Satya(truth), conditionValue = truth))
            } else
            listOf(vm.evalParsed(Ukti(sourceText = condition.sourceText, body = resolved.invocation),
                request.sessionKey, request.scope.copy(environment =
                    request.scope.environment.mergedWith(resolved.environment)),
                request.speaker, request.listener, evaluateCondition = true,
                persistSession = request.persistSession))
        }
        val truthTest = (node.condition as? dev.panini.vyakaranam.ast.Invocation)
            ?.let(NaturalSemanticNormalizer::normalize)
            as? NaturalSemanticNormalizer.Operation.TruthTest
        val conditionResults = structuredCondition ?: truthTest?.let { test ->
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
        // Do not select a branch using an earlier successful truth result after
        // a later action has failed, suspended, or requested a control effect.
        if (conditionResults.any { it !is ExecutionResult.Success ||
                it.controlSignal != null }) return conditionResults
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

}
