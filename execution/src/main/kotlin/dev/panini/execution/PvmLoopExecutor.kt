package dev.panini.execution

import dev.panini.execution.binding.baseText
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.WhileLoop

/** Executes condition-controlled loops and materializes their structured outcome. */
internal class PvmLoopExecutor {
    data class Request(
        val loop: WhileLoop,
        val scope: ExecutionScope,
        val structStore: MutableMap<String, TaddhitaStruct>,
        val structSchemas: Map<String, TaddhitaStructSchema>,
        val hostBudget: Long?,
        val executeNode: (ProgramNode, ExecutionScope, Boolean) -> List<ExecutionResult>,
        val evaluateCondition: (dev.panini.vyakaranam.ast.Invocation, ExecutionScope) -> ExecutionResult,
        val resolveCondition: (dev.panini.vyakaranam.ast.Invocation) -> StructuredValueExecutor.ResolvedInvocation?,
        val onResult: ((ExecutionResult) -> Unit)?,
    )

    fun execute(request: Request): List<ExecutionResult> {
        val loop = request.loop
        val results = mutableListOf<ExecutionResult>()
        val grammaticalBound = if (loop.maximumIterationStems.isEmpty()) null else {
            val value = dev.panini.sankhya.SankhyaEvaluator().evaluateStems(loop.maximumIterationStems).value
            if (value < 1L) return listOf(
                ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "A condition-controlled loop bound must be positive."),
            )
            value
        }
        val usesLatestResult = loop.condition.vakya.padas.any {
            it is SubantaPada && it.pratipadika.baseText() in setOf("फल", "विजय")
        }
        val isNegated = loop.condition.vakya.padas.any {
            (it is AvyayaPada && it.form == "न") ||
                (it is SubantaPada && it.pratipadika.baseText() == "असत्य")
        }
        var latestConditionValue = false
        var iterationCount = 0L

        fun complete(outcome: ExecutionResult.LoopOutcome): List<ExecutionResult> {
            val outcomeValue = SanskritValue.Shabda(outcome.sanskritName)
            val attemptWord = dev.panini.sankhya.SankhyaGenerator().cardinal(iterationCount).final.surface
            val attributes = mapOf("अवस्था" to outcome.sanskritName, "प्रयत्नसङ्ख्या" to attemptWord)
            val schema = request.structSchemas[LOOP_RESULT_NAME]
            if (schema != null && schema.fields.toSet() != attributes.keys) {
                return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "The परिणाम schema requires ${schema.fields}, but the loop produced ${attributes.keys}.",
                )
            }
            val fields = mapOf(
                "अवस्था" to outcomeValue,
                "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(iterationCount, attemptWord),
            )
            request.structStore[LOOP_RESULT_NAME] = TaddhitaStruct(LOOP_RESULT_NAME, attributes, fields)
            val completion = ExecutionResult.Success(
                value = outcome.sanskritName,
                operation = "pvm.while",
                typedValue = SanskritValue.Rupa(LOOP_RESULT_NAME, fields),
                loopOutcome = outcome,
                iterationCount = iterationCount,
            )
            results += completion
            request.onResult?.invoke(completion)
            loop.resultTarget?.let { target ->
                val targetScope = request.scope.copy(
                    environment = request.scope.environment.mergedWith(
                        ValueEnvironment(
                            mapOf(
                                "फल" to outcomeValue,
                                "परिणाम" to outcomeValue,
                                "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(iterationCount, iterationCount.toString()),
                            ),
                        ),
                    ),
                )
                results += request.executeNode(target, targetScope, false)
            }
            return results
        }

        while (grammaticalBound == null || iterationCount < grammaticalBound) {
            if (request.hostBudget != null && iterationCount >= request.hostBudget) {
                return results + ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "Condition-controlled loop exhausted its host execution budget of ${request.hostBudget} iterations.",
                )
            }
            val conditionHolds = if (usesLatestResult) {
                if (isNegated) !latestConditionValue else latestConditionValue
            } else {
                val resolved = request.resolveCondition(loop.condition) ?: return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "A structured attribute used by the loop condition could not be resolved.",
                )
                val conditionResult = request.evaluateCondition(
                    resolved.invocation,
                    request.scope.copy(environment = request.scope.environment.mergedWith(resolved.environment)),
                )
                val success = conditionResult as? ExecutionResult.Success
                (success?.conditionValue ?: (success?.typedValue as? SanskritValue.Satya)?.boolean) == true
            }
            if (!conditionHolds) return complete(ExecutionResult.LoopOutcome.VIJAYA)

            val iterationResults = request.executeNode(loop.body, request.scope, usesLatestResult)
            results += iterationResults
            iterationCount++
            if (usesLatestResult) {
                latestConditionValue = iterationResults.asSequence()
                    .filterIsInstance<ExecutionResult.Success>().mapNotNull { it.conditionValue }.firstOrNull()
                    ?: return results + ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "A फल-controlled loop body must produce a truth value.",
                    )
            }
            if (iterationResults.any {
                    it is ExecutionResult.Success && it.controlSignal == ExecutionControlSignal.BREAK_LOOP
                } || usesLatestResult && (if (isNegated) latestConditionValue else !latestConditionValue)
            ) return complete(ExecutionResult.LoopOutcome.VIJAYA)
        }
        loop.exhausted?.let { results += request.executeNode(it, request.scope, false) }
        return complete(ExecutionResult.LoopOutcome.SAMAPTI)
    }

    private companion object {
        const val LOOP_RESULT_NAME = "परिणाम"
    }
}
