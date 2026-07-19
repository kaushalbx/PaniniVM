package dev.panini.execution

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
