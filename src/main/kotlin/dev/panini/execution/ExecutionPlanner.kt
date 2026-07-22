package dev.panini.execution

object ExecutionPlanner {
    fun plan(
        program: BhashaProgram,
        variables: Map<String, String>,
        variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
        typedVariables: Map<String, SanskritValue> = emptyMap(),
    ): PlanningResult {
        val disposition = DispositionResolver.resolve(program.ukti)
        val plans = mutableListOf<ExecutionPlan>()
        val symbolicValues = variables.toMutableMap()
        val symbolicSamjnas = variableSamjnas.toMutableMap()
        val symbolicTypedValues = variables.mapValues { (name, value) ->
            SanskritValue.of(value, variableSamjnas[name].orEmpty())
        }.toMutableMap().apply { putAll(typedVariables) }
        val orderedInvocations = order(program) ?: return PlanningResult.Failed(
            ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "The bhāṣā program contains duplicate, unknown, or cyclic action dependencies.",
            )
        )
        orderedInvocations.forEach { invocation ->
            when (val resolution = OperationResolver.resolve(invocation, symbolicTypedValues)) {
                is OperationResolution.Resolved -> {
                    plans += ExecutionPlan(
                        invocation.id,
                        resolution.value,
                        disposition,
                        resolution.value.operation.effects,
                        program.ukti.speaker,
                        program.ukti.listener,
                    )
                    symbolicValues[invocation.id] = "<${invocation.id}>"
                    symbolicSamjnas[invocation.id] = resolution.value.operation.resultSamjnas
                    symbolicTypedValues[invocation.id] = SanskritValue.Shabda(
                        "<${invocation.id}>", resolution.value.operation.resultSamjnas,
                    )
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
