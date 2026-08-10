package dev.panini.execution.sutra

import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionScope
import dev.panini.execution.ExecutionMetadata
import dev.panini.execution.KriyaInvocationId
import dev.panini.execution.Phala
import dev.panini.execution.Prativacana
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SambhashanaTurn
import dev.panini.execution.SanskritPrativacanaRenderer
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.SmrtaPhala
import dev.panini.execution.SmrtaPhalaId
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.memory.KriyaMemory
import dev.panini.sankhya.SankhyaCountingFormRenderer
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.SutraTraceEntry

/** End-to-end compatibility entry point for the migrating runtime-sūtra path. */
object SutraExecutionPipeline {
    fun execute(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
        memory: KriyaMemory = KriyaMemory(),
    ): Phala {
        initialize()
        return when (val binding = VyakaranamExecutionAdapter.bind(input, conversation, memory)) {
            is ExecutionBindingResult.Bound -> {
                val phala = execute(binding.ukti, conversation, scope, memory)
                    .prependTrace(binding.trace)
                if (phala is Phala.Siddha) {
                    val metadata = buildMap {
                        val turnPrefix = SmrtaPhalaId.turnPrefix(conversation.turnNumber + 1)
                        binding.ukti.invocations.forEachIndexed { idx, inv ->
                            put(ExecutionMetadata.dhatu("$turnPrefix/${KriyaInvocationId.of(idx + 1)}"), inv.dhatu.upadesha)
                        }
                    }
                    phala.copy(metadata = metadata)
                } else {
                    phala
                }
            }
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
        memory: KriyaMemory = KriyaMemory(),
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
            ProgramAvastha(environment(conversation, scope, memory)),
        )
        val (result, program) = when (execution) {
            is ProgramGranthaExecution.Completed -> execution.result to execution.program
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
                    outputKind = last.outputKind,
                    controlSignal = last.controlSignal,
                )
            }
            null -> Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "The sūtra program completed without producing a result.",
                ),
                machineTrace,
            )
            is Phala.AnumatiApekshita -> last.copy(
                pipelineContinuation = SutraPipelineContinuation(
                    input = SanskritUktiInput(text = ukti.text, speaker = ukti.speaker, listener = ukti.listener),
                    conversation = conversation,
                    program = program,
                    state = state
                )
            )
            is Phala.SvikaraApekshita -> last.copy(
                pipelineContinuation = SutraPipelineContinuation(
                    input = SanskritUktiInput(text = ukti.text, speaker = ukti.speaker, listener = ukti.listener),
                    conversation = conversation,
                    program = program,
                    state = state
                )
            )
            else -> last
        }
    }

    fun executeTurn(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        scope: ExecutionScope,
        memory: KriyaMemory = KriyaMemory(),
    ): SambhashanaTurn {
        val response = SanskritPrativacanaRenderer.render(execute(input, conversation, scope, memory))
        val success = response.phala as? Phala.Siddha
            ?: return SambhashanaTurn(response, conversation)
        val nextTurn = conversation.turnNumber + 1
        val remembered = success.values.map { (invocationId, value) ->
            SmrtaPhala(
                id = SmrtaPhalaId.of(nextTurn, invocationId),
                turnNumber = nextTurn,
                invocationId = invocationId,
                value = value,
                samjnas = success.samjnas[invocationId].orEmpty(),
                typedValue = success.typedValues[invocationId],
            )
        }
        val historyMetadata = remembered.mapNotNull { r ->
            val dhatu = success.metadata[ExecutionMetadata.dhatu(r.invocationId)]
                ?: success.metadata[ExecutionMetadata.DEFAULT_DHATU]
            if (dhatu != null) ExecutionMetadata.dhatu(r.id) to dhatu else null
        }.toMap()
        val historyTypedResults = remembered.mapNotNull { r ->
            r.typedValue?.let { r.id to it }
        }.toMap()
        val historyDisplayResults = remembered.map { r ->
            r.id to r.value
        }.toMap()
        return SambhashanaTurn(
            response,
            conversation.copy(
                previousResults = conversation.previousResults +
                    success.values +
                    historyDisplayResults +
                    success.localBindings.mapValues { it.value.toDisplayText() },
                previousResultSamjnas = conversation.previousResultSamjnas +
                    success.samjnas +
                    success.localBindings.mapValues { it.value.samjnas },
                previousTypedResults = conversation.previousTypedResults +
                    success.typedValues +
                    historyTypedResults +
                    success.localBindings,
                resultHistory = conversation.resultHistory + remembered,
                turnNumber = nextTurn,
                metadata = conversation.metadata + success.metadata + historyMetadata,
            ),
        )
    }

    private fun environment(
        conversation: SambhashanaContext,
        scope: ExecutionScope,
        memory: KriyaMemory = KriyaMemory(),
    ): ValueEnvironment {
        val historicalValues = mutableMapOf<String, SanskritValue>()
        conversation.resultHistory.forEach { result ->
            val valObj = result.typedValue ?: SanskritValue.of(result.value, result.samjnas)
            historicalValues[result.id] = valObj
            historicalValues[result.invocationId] = valObj
        }
        val conversationEnvironment = ValueEnvironment.from(
            displayValues = conversation.mentionedEntities + conversation.previousResults,
            samjnas = conversation.mentionedEntitySamjnas + conversation.previousResultSamjnas,
            typedValues = conversation.previousTypedResults,
        ).mergedWith(ValueEnvironment(historicalValues))
        val rememberedValues = memory.entries.mapNotNull { entry ->
            entry.phala?.let { entry.frame.id.value to it }
        }.toMap()
        return conversationEnvironment
            .mergedWith(ValueEnvironment(rememberedValues))
            .mergedWith(scope.environment)
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
    fun resume(
        continuation: SutraPipelineContinuation,
        scope: ExecutionScope,
    ): Phala {
        val state = continuation.state
        val lastPhala = state.lastPhala
        val paused = when (lastPhala) {
            is Phala.AnumatiApekshita -> lastPhala.continuation
            is Phala.SvikaraApekshita -> lastPhala.continuation
            else -> return Phala.Asiddha(
                ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "No paused continuation to resume."),
                emptyList()
            )
        }

        val resumedPhala = dev.panini.execution.ExecutionRuntime.resume(paused, scope)

        val invocationId = when (lastPhala) {
            is Phala.AnumatiApekshita -> lastPhala.invocationId
            is Phala.SvikaraApekshita -> lastPhala.invocationId
            else -> throw IllegalStateException()
        }

        val nextState = when (resumedPhala) {
            is Phala.Siddha -> {
                val produced = resumedPhala.typedValues + resumedPhala.localBindings
                state.copy(
                    environment = state.environment.mergedWith(ValueEnvironment(produced)),
                    completedSutras = state.completedSutras + dev.panini.sutra.runtime.SutraId(invocationId),
                    invocationValues = state.invocationValues + resumedPhala.typedValues,
                    localBindings = state.localBindings + resumedPhala.localBindings,
                    executionTrace = state.executionTrace + resumedPhala.trace,
                    lastPhala = resumedPhala,
                    halted = false
                )
            }
            is Phala.Avagata -> {
                state.copy(
                    completedSutras = state.completedSutras + dev.panini.sutra.runtime.SutraId(invocationId),
                    executionTrace = state.executionTrace + resumedPhala.trace,
                    lastPhala = resumedPhala,
                    halted = false
                )
            }
            is Phala.AnumatiApekshita -> {
                return resumedPhala.copy(
                    pipelineContinuation = SutraPipelineContinuation(
                        input = continuation.input,
                        conversation = continuation.conversation,
                        program = continuation.program,
                        state = state.copy(lastPhala = resumedPhala, halted = true)
                    )
                ).prependTrace(state.executionTrace)
            }
            is Phala.SvikaraApekshita -> {
                return resumedPhala.copy(
                    pipelineContinuation = SutraPipelineContinuation(
                        input = continuation.input,
                        conversation = continuation.conversation,
                        program = continuation.program,
                        state = state.copy(lastPhala = resumedPhala, halted = true)
                    )
                ).prependTrace(state.executionTrace)
            }
            else -> {
                return resumedPhala.prependTrace(state.executionTrace)
            }
        }

        val result = dev.panini.sutra.runtime.SutraMachine(ProgramSutraEffectInterpreter(scope)).process(
            continuation.program,
            nextState
        )

        val finalState = result.state
        val machineTrace = result.trace.map(::render)
        if (result is dev.panini.sutra.runtime.SutraMachineResult.Failure) {
            return Phala.Asiddha(
                ExecutionResult.Failure(ExecutionError.ACTION_FAILED, result.message),
                finalState.executionTrace + machineTrace,
            )
        }

        return when (val last = finalState.lastPhala) {
            is Phala.Siddha -> {
                val values = ValueEnvironment(finalState.invocationValues)
                Phala.Siddha(
                    values = values.displayValues(),
                    samjnas = values.samjnas(),
                    trace = finalState.executionTrace + machineTrace,
                    typedValues = finalState.invocationValues,
                    localBindings = finalState.localBindings,
                    outputKind = last.outputKind,
                    controlSignal = last.controlSignal,
                )
            }
            null -> Phala.Asiddha(
                ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "The sūtra program completed without producing a result.",
                ),
                machineTrace,
            )
            is Phala.AnumatiApekshita -> last.copy(
                pipelineContinuation = SutraPipelineContinuation(
                    input = continuation.input,
                    conversation = continuation.conversation,
                    program = continuation.program,
                    state = finalState
                )
            )
            is Phala.SvikaraApekshita -> last.copy(
                pipelineContinuation = continuation.copy(state = finalState)
            )
            else -> last
        }
    }

    fun resumeTurn(
        continuation: SutraPipelineContinuation,
        scope: ExecutionScope,
    ): SambhashanaTurn {
        val response = SanskritPrativacanaRenderer.render(resume(continuation, scope))
        val success = response.phala as? Phala.Siddha
            ?: return SambhashanaTurn(response, continuation.conversation)
        val nextTurn = continuation.conversation.turnNumber + 1
        val remembered = success.values.map { (invocationId, value) ->
            SmrtaPhala(
                id = SmrtaPhalaId.of(nextTurn, invocationId),
                turnNumber = nextTurn,
                invocationId = invocationId,
                value = value,
                samjnas = success.samjnas[invocationId].orEmpty(),
                typedValue = success.typedValues[invocationId],
            )
        }
        return SambhashanaTurn(
            response,
            continuation.conversation.copy(
                previousResults = continuation.conversation.previousResults +
                    success.values +
                    success.localBindings.mapValues { it.value.toDisplayText() },
                previousResultSamjnas = continuation.conversation.previousResultSamjnas +
                    success.samjnas +
                    success.localBindings.mapValues { it.value.samjnas },
                previousTypedResults = continuation.conversation.previousTypedResults +
                    success.typedValues +
                    success.localBindings,
                resultHistory = continuation.conversation.resultHistory + remembered,
                turnNumber = nextTurn,
                metadata = continuation.conversation.metadata + success.metadata,
            ),
        )
    }
}
