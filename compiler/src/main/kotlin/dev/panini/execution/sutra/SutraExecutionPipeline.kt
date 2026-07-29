package dev.panini.execution.sutra

import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.DevanagariDigits
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionScope
import dev.panini.execution.Phala
import dev.panini.execution.Prativacana
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SambhashanaTurn
import dev.panini.execution.SanskritPrativacanaRenderer
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.SmrtaPhala
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.sankhya.SankhyaCountingFormRenderer
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.SutraTraceEntry

/** End-to-end compatibility entry point for the migrating runtime-sūtra path. */
object SutraExecutionPipeline {
    fun execute(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Phala {
        initialize()
        return when (val binding = VyakaranamExecutionAdapter.bind(input, conversation)) {
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
    }

    fun execute(
        ukti: ExecutableUkti,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): Phala {
        initialize()
        if (ukti.speaker != conversation.speaker || ukti.listener != conversation.listener) {
            return Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "Utterance participants do not match the trusted conversation context.",
                ),
                emptyList(),
            )
        }

        val blueprintGrantha = ExecutableUktiSutraCompiler.compileBlueprintGrantha(ukti)
        val execution = ProgramBlueprintGranthaEngine.execute(
            blueprintGrantha,
            ProgramBlueprintContext(
                speaker = ukti.speaker,
                listener = ukti.listener,
                text = ukti.text,
                prayojana = ukti.prayojana,
                polarity = ukti.polarity,
                lakara = ukti.lakara,
            ),
            scope,
            ProgramAvastha(environment(conversation, scope)),
        )
        val result = when (execution) {
            is ProgramGranthaExecution.Completed -> execution.result
            is ProgramGranthaExecution.InvalidBlueprint -> return Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    execution.diagnostics.joinToString(separator = "\n") { it.message },
                ),
                emptyList(),
            )
            is ProgramGranthaExecution.InvalidRuntime -> return Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    execution.diagnostics.joinToString(separator = "\n") { it.message },
                ),
                emptyList(),
            )
            is ProgramGranthaExecution.InvalidSource -> return Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    execution.diagnostics.joinToString(separator = "\n") { it.message },
                ),
                emptyList(),
            )
        }
        val state = result.state
        val machineTrace = result.trace.map(::render)
        if (result is SutraMachineResult.Failure) {
            return Phala.Asiddha(
                ExecutionResult.Failure(ExecutionError.ACTION_FAILED, result.message),
                state.executionTrace + machineTrace,
            )
        }

        return when (val last = state.lastPhala) {
            is Phala.Siddha -> {
                val values = ValueEnvironment(state.invocationValues)
                Phala.Siddha(
                    values = values.displayValues(),
                    samjnas = values.samjnas(),
                    trace = state.executionTrace + machineTrace,
                    typedValues = state.invocationValues,
                    localBindings = state.localBindings,
                )
            }
            null -> Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "The sūtra program completed without producing a result.",
                ),
                machineTrace,
            )
            else -> last
        }
    }

    fun executeTurn(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): SambhashanaTurn {
        val response = SanskritPrativacanaRenderer.render(execute(input, conversation, scope))
        val success = response.phala as? Phala.Siddha
            ?: return SambhashanaTurn(response, conversation)
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
                previousResults = conversation.previousResults +
                    success.values +
                    success.localBindings.mapValues { it.value.toDisplayText() },
                previousResultSamjnas = conversation.previousResultSamjnas +
                    success.samjnas +
                    success.localBindings.mapValues { it.value.samjnas },
                previousTypedResults = conversation.previousTypedResults +
                    success.typedValues +
                    success.localBindings,
                resultHistory = conversation.resultHistory + remembered,
                turnNumber = nextTurn,
            ),
        )
    }

    private fun environment(
        conversation: SambhashanaContext,
        scope: ExecutionScope,
    ): ValueEnvironment {
        val historicalValues = mutableMapOf<String, SanskritValue>()
        conversation.resultHistory.forEach { result ->
            val valObj = result.typedValue ?: SanskritValue.of(result.value, result.samjnas)
            historicalValues[result.id] = valObj
            historicalValues[result.invocationId] = valObj
        }
        conversation.resultHistory.lastOrNull()?.let { last ->
            val lastObj = last.typedValue ?: SanskritValue.of(last.value, last.samjnas)
            historicalValues["फल"] = lastObj
            historicalValues["पूर्वफल"] = lastObj
        }
        if (conversation.resultHistory.size >= 2) {
            val prevPrev = conversation.resultHistory[conversation.resultHistory.size - 2]
            val prevPrevObj = prevPrev.typedValue ?: SanskritValue.of(prevPrev.value, prevPrev.samjnas)
            historicalValues["पूर्वपूर्वफल"] = prevPrevObj
        } else if (conversation.resultHistory.size == 1) {
            conversation.previousTypedResults["द्वि"]?.let {
                historicalValues["पूर्वपूर्वफल"] = it
            }
        }
        val conversationEnvironment = ValueEnvironment.from(
            displayValues = conversation.mentionedEntities + conversation.previousResults,
            samjnas = conversation.mentionedEntitySamjnas + conversation.previousResultSamjnas,
            typedValues = historicalValues + conversation.previousTypedResults,
        )
        return conversationEnvironment.mergedWith(scope.environment)
    }

    private fun render(entry: SutraTraceEntry): String = when (entry) {
        is SutraTraceEntry.Applied ->
            "${entry.sutraId}: applied (${entry.reasons.joinToString()})."
        is SutraTraceEntry.Skipped ->
            "${entry.sutraId}: skipped (${entry.reasons.joinToString()})."
        is SutraTraceEntry.Blocked ->
            "${entry.sutraId}: blocked by ${entry.blocker} (${entry.reasons.joinToString()})."
        is SutraTraceEntry.Invalid ->
            "${entry.sutraId}: invalid (${entry.message})."
    }

    private fun initialize() {
        SankhyaCountingFormRenderer.init()
        DhatuPathaRegistration.ensureRegistered()
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
