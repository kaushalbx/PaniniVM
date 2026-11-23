package dev.panini.execution

import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.WhileLoop

/** Executes condition-controlled loops and materializes their structured outcome. */
internal class PvmLoopExecutor {
    sealed interface ConditionResolution {
        data class Resolved(
            val value: StructuredValueExecutor.ResolvedInvocation,
        ) : ConditionResolution

        data class Invalid(
            val failure: ExecutionResult.Failure,
        ) : ConditionResolution
    }

    data class Request(
        val loop: WhileLoop,
        val scope: ExecutionScope,
        val structStore: MutableMap<String, TaddhitaStruct>,
        val structSchemas: Map<String, TaddhitaStructSchema>,
        val hostBudget: Long?,
        val executeNode: (ProgramNode, ExecutionScope, Boolean) -> List<ExecutionResult>,
        val evaluateCondition: (dev.panini.vyakaranam.ast.Invocation, ExecutionScope) -> ExecutionResult,
        val resolveCondition: (dev.panini.vyakaranam.ast.Invocation) -> ConditionResolution,
        val resolveValue: (String) -> SanskritValue? = { null },
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
        val normalizedCondition = NaturalSemanticNormalizer.normalize(loop.condition)
        val reportedOutcome = normalizedCondition as? NaturalSemanticNormalizer.Operation.ReportedOutcomeTest
        val hasNamedResult = dev.panini.execution.binding.NamedActionResultReferenceResolver
            .resolve(loop.condition.vakya.padas).isNotEmpty()
        val usesLatestResult = reportedOutcome != null || !hasNamedResult && loop.condition.vakya.padas.any {
            it is SubantaPada && NaturalSemanticNormalizer.isPriorResult(it)
        }
        val hasExplicitNegation = loop.condition.vakya.padas.any {
            it is AvyayaPada && it.function == AvyayaFunction.NISHEDHA
        }
        val isNegated = reportedOutcome?.negated ?: (hasExplicitNegation || loop.condition.vakya.padas.any {
                it is SubantaPada &&
                    nominalTruthValue(it.pratipadika)?.boolean == false
        })
        val truthStateName = (normalizedCondition as? NaturalSemanticNormalizer.Operation.TruthTest)?.stateName
        var latestConditionValue = false
        var iterationCount = 0L

        fun complete(outcome: ExecutionResult.LoopOutcome): List<ExecutionResult> {
            val outcomeValue = SanskritValue.Shabda(outcome.sanskritName)
            val attemptWord = dev.panini.sankhya.SankhyaGenerator().cardinal(iterationCount).final.surface
            val attributes = mapOf("अवस्था" to outcome.sanskritName, "प्रयत्न-सङ्ख्या" to attemptWord)
            val schema = request.structSchemas[LOOP_RESULT_NAME]
            if (schema != null && schema.fields.toSet() != attributes.keys) {
                return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "The परिणाम schema requires ${schema.fields}, but the loop produced ${attributes.keys}.",
                )
            }
            val fields = mapOf(
                "अवस्था" to outcomeValue,
                "प्रयत्न-सङ्ख्या" to SanskritValue.Sankhya(iterationCount, attemptWord),
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
                                "प्रयत्न-सङ्ख्या" to SanskritValue.Sankhya(iterationCount, iterationCount.toString()),
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
                // च conjoins clauses left to right, and a false clause ends the test: in स्थानम् एकाद् अधिकम् अस्ति च
                // पूर्वाङ्कः अङ्काद् अधिकः अस्ति the second clause is read only when a member before स्थान exists.
                var holds = true
                for (clause in listOf(loop.condition) + loop.conjuncts) {
                    when (val truth = clauseTruth(clause, request)) {
                        is ClauseTruth.Value -> if (!truth.holds) { holds = false; break }
                        is ClauseTruth.Stop -> return results + truth.result
                    }
                }
                holds
            }
            if (!conditionHolds) return complete(ExecutionResult.LoopOutcome.VIJAYA)

            val iterationResults = request.executeNode(loop.body, request.scope, usesLatestResult)
            results += iterationResults.map(ExecutionResult::consumeLoopBreak)
            if (iterationResults.any { it !is ExecutionResult.Success }) return results
            if (iterationResults.any { it is ExecutionResult.Success &&
                    it.controlSignal == ExecutionControlSignal.RETURN_PROCEDURE }) return results
            iterationCount++
            if (iterationResults.any { it is ExecutionResult.Success &&
                    it.controlSignal == ExecutionControlSignal.BREAK_LOOP }) {
                return complete(ExecutionResult.LoopOutcome.VIJAYA)
            }
            if (usesLatestResult) {
                latestConditionValue = iterationResults.asSequence()
                    .filterIsInstance<ExecutionResult.Success>().mapNotNull { it.conditionValue }.firstOrNull()
                    ?: return results + ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "A फल-controlled loop body must produce a truth value.",
                    )
            }
            if (usesLatestResult && (if (isNegated) latestConditionValue else !latestConditionValue))
                return complete(ExecutionResult.LoopOutcome.VIJAYA)
        }
        loop.exhausted?.let {
            val exhaustedResults = request.executeNode(it, request.scope, false)
            results += exhaustedResults
            if (exhaustedResults.any { result -> result !is ExecutionResult.Success ||
                    result.controlSignal != null }) return results
        }
        return complete(ExecutionResult.LoopOutcome.SAMAPTI)
    }

    private sealed interface ClauseTruth {
        data class Value(val holds: Boolean) : ClauseTruth
        /** Evaluation produced a failure or a procedure return that ends the loop. */
        data class Stop(val result: ExecutionResult) : ClauseTruth
    }

    /** The truth of one condition clause that is not tested against the latest result. */
    private fun clauseTruth(condition: dev.panini.vyakaranam.ast.Invocation, request: Request): ClauseTruth {
        val hasExplicitNegation = condition.vakya.padas.any { it is AvyayaPada && it.function == AvyayaFunction.NISHEDHA }
        val truthStateName = (NaturalSemanticNormalizer.normalize(condition) as? NaturalSemanticNormalizer.Operation.TruthTest)?.stateName
        val truthState = truthStateName
            ?.let { request.scope.environment.values[it] ?: request.resolveValue(it) }
            as? SanskritValue.Satya
        if (truthState != null) return ClauseTruth.Value(if (hasExplicitNegation) !truthState.boolean else truthState.boolean)
        val resolved = when (val resolution = request.resolveCondition(condition)) {
            is ConditionResolution.Resolved -> resolution.value
            is ConditionResolution.Invalid -> return ClauseTruth.Stop(resolution.failure)
        }
        val fieldTest = NaturalSemanticNormalizer.normalize(resolved.invocation)
            as? NaturalSemanticNormalizer.Operation.TruthTest
        if (fieldTest != null && resolved.environment.values.containsKey(fieldTest.stateName)) {
            val fieldTruth = resolved.environment.values[fieldTest.stateName] as? SanskritValue.Satya
                ?: return ClauseTruth.Stop(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                    "A field truth predicate requires सत्य/असत्य."))
            return ClauseTruth.Value(if (fieldTest.negated) !fieldTruth.boolean else fieldTruth.boolean)
        }
        // न scopes over the predicate's truth here. Passing it
        // as an independent utterance would instead classify
        // the predicate as a prohibition speech act.
        val predicate = if (!hasExplicitNegation) resolved.invocation else {
            val original = resolved.invocation.vakya
            val affirmative = original.padas.filterNot {
                it is AvyayaPada && it.function == AvyayaFunction.NISHEDHA
            }
            resolved.invocation.copy(vakya = when (original) {
                is dev.panini.vyakaranam.ast.AkhyataVakya -> original.copy(padas = affirmative)
                is dev.panini.vyakaranam.ast.NamaVakya -> original.copy(padas = affirmative)
            })
        }
        val conditionResult = request.evaluateCondition(
            predicate,
            request.scope.copy(environment = request.scope.environment.mergedWith(resolved.environment)),
        )
        val success = conditionResult as? ExecutionResult.Success ?: return ClauseTruth.Stop(conditionResult)
        if (success.controlSignal == ExecutionControlSignal.RETURN_PROCEDURE) return ClauseTruth.Stop(success)
        val truth = success.conditionValue ?: (success.typedValue as? SanskritValue.Satya)?.boolean
            ?: return ClauseTruth.Stop(ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "A loop condition must produce a truth value.",
            ))
        return ClauseTruth.Value(if (hasExplicitNegation) !truth else truth)
    }

    private companion object {
        const val LOOP_RESULT_NAME = "परिणाम"
    }

}
