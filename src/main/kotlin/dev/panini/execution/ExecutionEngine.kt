package dev.panini.execution

import dev.panini.dhatupatha.Dhatu

/** Compatibility facade for executing one already-structured dhātu invocation. */
object ExecutionEngine {
    fun execute(dhatu: Dhatu, context: ExecutionContext): ExecutionResult {
        val invocation = DhatuInvocation(
            id = "invocation",
            dhatu = dhatu,
            bindings = context.bindings,
            selectedOperation = context.selectedOperation,
            metadata = context.metadata,
        )
        return when (val resolution = OperationResolver.resolve(invocation, context.variables, context.variableSamjnas)) {
            is OperationResolution.Resolved -> resolution.value.operation.action.execute(
                resolution.value.context,
                resolution.value.operation,
            )
            is OperationResolution.MissingInput -> ExecutionResult.NeedsInput(resolution.karakas, resolution.message)
            is OperationResolution.Invalid -> ExecutionResult.Failure(resolution.error, resolution.message)
            is OperationResolution.Ambiguous -> ExecutionResult.Ambiguous(resolution.operations, resolution.message)
        }
    }
}

sealed interface PlanningResult {
    data class Planned(val program: BhashaProgram, val plans: List<ExecutionPlan>) : PlanningResult
    data class Failed(val result: ExecutionResult) : PlanningResult
}

object ExecutionPlanner {
    fun plan(
        program: BhashaProgram,
        variables: Map<String, String>,
        variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    ): PlanningResult {
        val disposition = DispositionResolver.resolve(program.nirdesha)
        val plans = mutableListOf<ExecutionPlan>()
        val symbolicValues = variables.toMutableMap()
        val symbolicSamjnas = variableSamjnas.toMutableMap()
        val orderedInvocations = order(program) ?: return PlanningResult.Failed(
            ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "The bhāṣā program contains duplicate, unknown, or cyclic action dependencies.",
            )
        )
        orderedInvocations.forEach { invocation ->
            when (val resolution = OperationResolver.resolve(invocation, symbolicValues, symbolicSamjnas)) {
                is OperationResolution.Resolved -> {
                    plans += ExecutionPlan(
                        invocation.id,
                        resolution.value,
                        disposition,
                        resolution.value.operation.effects,
                        program.nirdesha.speaker,
                        program.nirdesha.listener,
                    )
                    symbolicValues[invocation.id] = "<${invocation.id}>"
                    symbolicSamjnas[invocation.id] = resolution.value.operation.resultSamjnas
                }
                is OperationResolution.MissingInput -> return PlanningResult.Failed(
                    ExecutionResult.NeedsInput(resolution.karakas, resolution.message),
                )
                is OperationResolution.Invalid -> return PlanningResult.Failed(
                    ExecutionResult.Failure(resolution.error, resolution.message),
                )
                is OperationResolution.Ambiguous -> return PlanningResult.Failed(
                    ExecutionResult.Ambiguous(resolution.operations, resolution.message),
                )
            }
        }
        return PlanningResult.Planned(program, plans)
    }

    private fun order(program: BhashaProgram): List<DhatuInvocation>? {
        val byId = program.invocations.associateBy { it.id }
        if (byId.size != program.invocations.size) return null
        if (program.dependencies.any { it.before !in byId || it.after !in byId }) return null

        val incoming = byId.keys.associateWith { 0 }.toMutableMap()
        val outgoing = byId.keys.associateWith { mutableListOf<String>() }
        program.dependencies.forEach { dependency ->
            outgoing.getValue(dependency.before) += dependency.after
            incoming[dependency.after] = incoming.getValue(dependency.after) + 1
        }
        val sourceOrder = program.invocations.mapIndexed { index, invocation -> invocation.id to index }.toMap()
        val ready = incoming.filterValues { it == 0 }.keys.sortedBy { sourceOrder.getValue(it) }.toMutableList()
        val ordered = mutableListOf<DhatuInvocation>()
        while (ready.isNotEmpty()) {
            val id = ready.removeAt(0)
            ordered += byId.getValue(id)
            outgoing.getValue(id).forEach { dependent ->
                incoming[dependent] = incoming.getValue(dependent) - 1
                if (incoming.getValue(dependent) == 0) {
                    ready += dependent
                    ready.sortBy { sourceOrder.getValue(it) }
                }
            }
        }
        return ordered.takeIf { it.size == program.invocations.size }
    }
}

sealed interface AuthorityDecision {
    data object Authorized : AuthorityDecision
    data class NeedsApproval(val effects: Set<ExecutionEffect>) : AuthorityDecision
    data object NeedsAcceptance : AuthorityDecision
    data class Denied(val reason: String) : AuthorityDecision
}

object AuthorityPolicy {
    fun authorize(plan: ExecutionPlan, scope: ExecutionScope): AuthorityDecision {
        when (plan.disposition) {
            ExecutionDisposition.EXECUTE -> if (plan.speaker !in scope.authorizedSpeakers) {
                return AuthorityDecision.Denied(
                    "Speaker ${plan.speaker} is not authorized to command listener ${plan.listener}.",
                )
            }
            ExecutionDisposition.REQUEST_EXECUTION -> if (plan.invocationId !in scope.acceptedInvocations) {
                return AuthorityDecision.NeedsAcceptance
            }
            else -> Unit
        }
        val missing = plan.requiredEffects - scope.capabilities
        return if (missing.isEmpty()) AuthorityDecision.Authorized else AuthorityDecision.NeedsApproval(missing)
    }
}

object ExecutionRuntime {
    fun execute(planning: PlanningResult.Planned, scope: ExecutionScope): Phala {
        if (planning.plans.any { it.disposition !in setOf(ExecutionDisposition.EXECUTE, ExecutionDisposition.REQUEST_EXECUTION) }) {
            val disposition = requireNotNull(planning.plans.firstOrNull()?.disposition)
            return Phala.Avagata(
                disposition,
                planning.plans,
                listOf("Understood the utterance as $disposition; no task action was performed."),
            )
        }

        return resume(
            ExecutionContinuation(
                planning,
                nextPlanIndex = 0,
                values = scope.variables,
                valueSamjnas = scope.variableSamjnas,
                trace = emptyList(),
            ),
            scope,
        )
    }

    fun resume(continuation: ExecutionContinuation, scope: ExecutionScope): Phala {
        val values = (scope.variables + continuation.values).toMutableMap()
        val valueSamjnas = (scope.variableSamjnas + continuation.valueSamjnas).toMutableMap()
        val trace = continuation.trace.toMutableList()
        val plans = continuation.planning.plans
        for (index in continuation.nextPlanIndex until plans.size) {
            val plan = plans[index]
            val paused = ExecutionContinuation(
                continuation.planning,
                index,
                values.toMap(),
                valueSamjnas.toMap(),
                trace.toList(),
            )
            when (val authority = AuthorityPolicy.authorize(plan, scope)) {
                AuthorityDecision.Authorized -> Unit
                is AuthorityDecision.NeedsApproval -> return Phala.AnumatiApekshita(
                    plan.invocationId,
                    authority.effects,
                    paused,
                )
                AuthorityDecision.NeedsAcceptance -> return Phala.SvikaraApekshita(
                    plan.invocationId,
                    plan.speaker,
                    plan.listener,
                    paused,
                )
                is AuthorityDecision.Denied -> return Phala.Nirasta(plan.invocationId, authority.reason)
            }
            val refreshedContext = plan.resolved.invocation.executionContext(values, valueSamjnas)
            when (val result = plan.resolved.operation.action.execute(refreshedContext, plan.resolved.operation)) {
                is ExecutionResult.Success -> {
                    values[plan.invocationId] = result.value
                    valueSamjnas[plan.invocationId] = plan.resolved.operation.resultSamjnas
                    trace += plan.resolved.resolutionTrace + result.trace
                }
                else -> return Phala.Asiddha(result, trace + result.trace)
            }
        }
        val invocationIds = continuation.planning.plans.mapTo(mutableSetOf()) { it.invocationId }
        return Phala.Siddha(
            values.filterKeys { it in invocationIds },
            valueSamjnas.filterKeys { it in invocationIds },
            trace,
        )
    }
}

/** End-to-end entry point from structured utterance to execution result. */
object BhashaExecutionEngine {
    fun execute(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Phala = when (val analysis = ControlledSanskritAnalyzer.analyze(input, conversation)) {
        is VakyaAnalysisResult.Analyzed -> execute(analysis.analysis, conversation, scope)
        is VakyaAnalysisResult.NeedsClarification -> Phala.Asiddha(
            ExecutionResult.NeedsInput(emptySet(), analysis.question),
            emptyList(),
        )
        is VakyaAnalysisResult.Unsupported -> Phala.Asiddha(
            ExecutionResult.Failure(ExecutionError.INVALID_VALUE, analysis.message),
            emptyList(),
        )
    }

    fun execute(
        analysis: VakyaAnalysis,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Phala = when (val compilation = BhashaCompiler.compile(analysis)) {
        is UktiCompilation.Compiled -> execute(compilation.ukti, conversation, scope)
        is UktiCompilation.Invalid -> Phala.Asiddha(
            ExecutionResult.Failure(ExecutionError.INVALID_VALUE, compilation.message),
            emptyList(),
        )
    }

    fun execute(ukti: Ukti, conversation: SambhashanaContext, scope: ExecutionScope): Phala {
        val interpretation = BhashaInterpreter.interpret(ukti, conversation)
        val understood = interpretation as? UktiInterpretation.Understood ?: return when (interpretation) {
            is UktiInterpretation.NeedsClarification -> Phala.Asiddha(
                ExecutionResult.Failure(ExecutionError.MISSING_KARAKA, interpretation.question),
                emptyList(),
            )
            is UktiInterpretation.Contradictory -> Phala.Asiddha(
                ExecutionResult.Failure(ExecutionError.INVALID_VALUE, interpretation.reason),
                emptyList(),
            )
            is UktiInterpretation.Understood -> error("Unreachable")
        }
        val program = BhashaProgram(
            understood.nirdesha,
            understood.nirdesha.invocations,
            ukti.dependencies,
        )
        val historicalValues = conversation.resultHistory.associate { it.id to it.value }
        val historicalSamjnas = conversation.resultHistory.associate { it.id to it.samjnas }
        val variables = historicalValues + conversation.mentionedEntities + conversation.previousResults + scope.variables
        val variableSamjnas = conversation.mentionedEntitySamjnas +
            historicalSamjnas + conversation.previousResultSamjnas + scope.variableSamjnas
        return when (val planning = ExecutionPlanner.plan(program, variables, variableSamjnas)) {
            is PlanningResult.Planned -> ExecutionRuntime.execute(
                planning,
                scope.copy(variables = variables, variableSamjnas = variableSamjnas),
            )
            is PlanningResult.Failed -> Phala.Asiddha(planning.result, understood.trace)
        }
    }

    fun resume(continuation: ExecutionContinuation, scope: ExecutionScope): Phala =
        ExecutionRuntime.resume(continuation, scope)

    fun executeAndRespond(
        ukti: Ukti,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Prativacana = SanskritPrativacanaRenderer.render(execute(ukti, conversation, scope))

    fun executeAndRespond(
        analysis: VakyaAnalysis,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Prativacana = SanskritPrativacanaRenderer.render(execute(analysis, conversation, scope))

    fun executeAndRespond(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Prativacana = SanskritPrativacanaRenderer.render(execute(input, conversation, scope))

    fun resumeAndRespond(
        continuation: ExecutionContinuation,
        scope: ExecutionScope,
    ): Prativacana = SanskritPrativacanaRenderer.render(resume(continuation, scope))

    fun executeTurn(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): SambhashanaTurn {
        val response = executeAndRespond(input, conversation, scope)
        val success = response.phala as? Phala.Siddha ?: return SambhashanaTurn(response, conversation)
        val nextTurn = conversation.turnNumber + 1
        val remembered = success.values.map { (invocationId, value) ->
            SmrtaPhala(
                id = "उक्ति-${DevanagariDigits.render(nextTurn)}/$invocationId",
                turnNumber = nextTurn,
                invocationId = invocationId,
                value = value,
                samjnas = success.samjnas[invocationId].orEmpty(),
            )
        }
        return SambhashanaTurn(
            response,
            conversation.copy(
                previousResults = conversation.previousResults + success.values,
                previousResultSamjnas = conversation.previousResultSamjnas + success.samjnas,
                resultHistory = conversation.resultHistory + remembered,
                turnNumber = nextTurn,
            ),
        )
    }
}
