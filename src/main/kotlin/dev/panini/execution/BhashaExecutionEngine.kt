package dev.panini.execution

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
