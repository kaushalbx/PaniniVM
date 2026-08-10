package dev.panini.execution

import dev.panini.execution.binding.PhalaReference

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
                lastOutputKind = OutputKind.INTERNAL,
            ),
            scope,
        )
    }

    fun resume(continuation: ExecutionContinuation, scope: ExecutionScope): Phala {
        val values = scope.environment.mergedWith(continuation.environment).values.toMutableMap()
        val localBindings = mutableMapOf<String, SanskritValue>()
        val trace = continuation.trace.toMutableList()
        var lastOutputKind = continuation.lastOutputKind
        var lastControlSignal: ExecutionControlSignal? = null
        val plans = continuation.planning.plans
        for (index in continuation.nextPlanIndex until plans.size) {
            val plan = plans[index]
            val paused = ExecutionContinuation(
                continuation.planning,
                index,
                ValueEnvironment(values.toMap()),
                trace.toList(),
                lastOutputKind,
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
                variables = values.filterKeys { it != plan.invocationId },
                stateStore = scope.stateStore,
                externalDispatcher = scope.externalDispatcher,
                sutraRegistry = scope.sutraRegistry,
                currentGrantha = scope.currentGrantha,
                operationCatalog = scope.operationCatalog,
                linguisticServices = scope.linguisticServices,
                sankhyaRenderer = scope.sankhyaRenderer,
            )
            when (val result = plan.resolved.operation.action.execute(refreshedContext, plan.resolved.operation)) {
                is ExecutionResult.Success -> {
                    val typedResult = result.typedValue ?: SanskritValue.of(
                        result.value,
                        plan.resolved.operation.resultSamjnas,
                    )
                    val karmanExpr = plan.resolved.context.bindings[dev.panini.core.Karaka.KARMAN]
                    val sankhyaVals = karmanExpr?.let { plan.resolved.context.resolveSankhyaValues(it) }
                    val secondVal = if (sankhyaVals != null && sankhyaVals.size >= 2 && karmanExpr is dev.panini.execution.ExecutionExpression.Coordination) sankhyaVals[1] else null

                    val oldPhala = values[PhalaReference.KEY]
                    if (secondVal != null) {
                        val surf = scope.sankhyaRenderer.render(secondVal) ?: secondVal.toString()
                        values["पूर्वफल"] = dev.panini.execution.SanskritValue.Sankhya(secondVal, surf)
                    } else if (oldPhala != null) {
                        values["पूर्वफल"] = oldPhala
                    }
                    values[PhalaReference.KEY] = typedResult
                    values[plan.invocationId] = typedResult
                    val bindingKaraka = plan.resolved.operation.resultBindingKaraka
                    val bindingName = bindingKaraka
                        ?.let(plan.resolved.context.bindings::get)
                        ?.bindingName()
                    if (bindingName != null) {
                        values[bindingName] = typedResult
                        localBindings[bindingName] = typedResult
                        trace += "Bound result to local name '$bindingName'."
                    }
                    trace += plan.resolved.resolutionTrace + result.trace
                    lastOutputKind = result.outputKind
                    lastControlSignal = result.controlSignal
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
            localBindings,
            outputKind = lastOutputKind,
            controlSignal = lastControlSignal,
        )
    }
}
