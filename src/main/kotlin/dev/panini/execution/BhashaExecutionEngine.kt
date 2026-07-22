package dev.panini.execution

/** End-to-end entry point from structured utterance to execution result. */
object BhashaExecutionEngine {
    fun execute(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Phala = when (val binding = VyakaranamExecutionAdapter.bind(input, conversation)) {
        is ExecutionBindingResult.Bound -> execute(binding.ukti, conversation, scope)
            .prependTrace(binding.trace)
        is ExecutionBindingResult.NeedsInput -> Phala.Asiddha(
            ExecutionResult.NeedsInput(emptySet(), binding.message),
            emptyList(),
        )
        is ExecutionBindingResult.Invalid -> Phala.Asiddha(
            ExecutionResult.Failure(ExecutionError.INVALID_VALUE, binding.message),
            emptyList(),
        )
    }

    fun execute(ukti: Ukti, conversation: SambhashanaContext, scope: ExecutionScope): Phala {
        if (ukti.speaker != conversation.speaker || ukti.listener != conversation.listener) {
            return Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "Utterance participants do not match the trusted conversation context.",
                ),
                emptyList(),
            )
        }
        val program = BhashaProgram(ukti, ukti.dependencies)
        val historicalValues = conversation.resultHistory.associate { result ->
            result.id to (result.typedValue ?: SanskritValue.of(result.value, result.samjnas))
        }
        val variableSamjnas = conversation.mentionedEntitySamjnas + conversation.previousResultSamjnas + scope.variableSamjnas
        val environment = ValueEnvironment.from(
            displayValues = conversation.mentionedEntities + conversation.previousResults + scope.variables,
            samjnas = variableSamjnas,
            typedValues = historicalValues + conversation.previousTypedResults + scope.typedVariables,
        )
        return when (val planning = ExecutionPlanner.plan(program, environment)) {
            is PlanningResult.Planned -> ExecutionRuntime.execute(
                planning,
                scope,
                environment,
            )
            is PlanningResult.Failed -> Phala.Asiddha(planning.result, emptyList())
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
                typedValue = success.typedValues[invocationId],
            )
        }
        return SambhashanaTurn(
            response,
            conversation.copy(
                previousResults = conversation.previousResults + success.values,
                previousResultSamjnas = conversation.previousResultSamjnas + success.samjnas,
                previousTypedResults = conversation.previousTypedResults + success.typedValues,
                resultHistory = conversation.resultHistory + remembered,
                turnNumber = nextTurn,
            ),
        )
    }

    private fun Phala.prependTrace(prefix: List<String>): Phala = when (this) {
        is Phala.Siddha -> copy(trace = prefix + trace)
        is Phala.Asiddha -> copy(trace = prefix + trace)
        is Phala.Avagata -> copy(trace = prefix + trace)
        is Phala.AnumatiApekshita -> copy(
            continuation = continuation.copy(trace = prefix + continuation.trace),
        )
        is Phala.SvikaraApekshita -> copy(
            continuation = continuation.copy(trace = prefix + continuation.trace),
        )
        is Phala.Nirasta -> this
    }
}
