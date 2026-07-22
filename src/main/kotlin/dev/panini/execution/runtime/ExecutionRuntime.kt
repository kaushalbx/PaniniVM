package dev.panini.execution

object ExecutionRuntime {
    fun execute(
        planning: PlanningResult.Planned,
        scope: ExecutionScope,
        environment: ValueEnvironment = scope.environment,
    ): Phala {
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
                environment = environment,
                trace = emptyList(),
            ),
            scope,
        )
    }

    fun resume(continuation: ExecutionContinuation, scope: ExecutionScope): Phala {
        val values = scope.environment.mergedWith(continuation.environment).values.toMutableMap()
        val trace = continuation.trace.toMutableList()
        val plans = continuation.planning.plans
        for (index in continuation.nextPlanIndex until plans.size) {
            val plan = plans[index]
            val paused = ExecutionContinuation(
                continuation.planning,
                index,
                ValueEnvironment(values.toMap()),
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
            val refreshedContext = plan.resolved.context.copy(
                variables = values,
                stateStore = scope.stateStore,
                externalDispatcher = scope.externalDispatcher,
            )
            when (val result = plan.resolved.operation.action.execute(refreshedContext, plan.resolved.operation)) {
                is ExecutionResult.Success -> {
                    values[plan.invocationId] = result.typedValue ?: SanskritValue.of(
                        result.value,
                        plan.resolved.operation.resultSamjnas,
                    )
                    trace += plan.resolved.resolutionTrace + result.trace
                }
                else -> return Phala.Asiddha(result, trace + result.trace)
            }
        }
        val invocationIds = continuation.planning.plans.mapTo(mutableSetOf()) { it.invocationId }
        val results = ValueEnvironment(values.filterKeys { it in invocationIds })
        return Phala.Siddha(
            results.displayValues(),
            results.samjnas(),
            trace,
            results.values,
        )
    }
}
