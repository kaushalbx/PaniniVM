package dev.panini.execution

import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.persistence.FileStateStore
import dev.panini.execution.persistence.StateStore
import dev.panini.execution.sutra.ProgramAvastha
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintGranthaEngine
import dev.panini.execution.sutra.ProgramGranthaExecution
import dev.panini.execution.sutra.SutraExecutionPipeline
import dev.panini.sutra.runtime.SutraMachineResult
import java.io.File

/**
 * Top-level execution facade and API for PaniniVM.
 * Provides simplified evaluation, session persistence, external capability registration,
 * and capability-based security.
 */
class PaniniVM(
    storageDir: File = File(System.getProperty("java.io.tmpdir"), "paninivm_sessions_" + java.util.UUID.randomUUID()),
    val defaultScope: ExecutionScope = ExecutionScope(
        capabilities = setOf(
            ExecutionEffect.PURE,
            ExecutionEffect.READ_MEMORY,
            ExecutionEffect.WRITE_MEMORY,
            ExecutionEffect.READ_RESOURCE,
            ExecutionEffect.WRITE_RESOURCE,
            ExecutionEffect.NETWORK,
            ExecutionEffect.EXECUTE_PROCESS,
            ExecutionEffect.SEND_MESSAGE,
        )
    ),
) {
    val store: StateStore = FileStateStore(storageDir)
    private val externalDispatcher = ExternalCapabilityDispatcher()

    init {
        dev.panini.derivation.LinguisticActionsInitializer.initialize()
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        dev.panini.sankhya.SankhyaCountingFormRenderer.init()
    }

    private val sessions = mutableMapOf<String, SambhashanaContext>()

    fun eval(
        utterance: String,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): ExecutionResult {
        val activeContext = if (sessionKey != null) {
            sessions.getOrPut(sessionKey) {
                store.load(sessionKey) ?: SambhashanaContext(speaker = speaker, listener = listener)
            }
        } else {
            SambhashanaContext(speaker = speaker, listener = listener)
        }

        val input = SanskritUktiInput(text = utterance, speaker = activeContext.speaker, listener = activeContext.listener)
        val effectiveScope = scope.copy(
            stateStore = scope.stateStore ?: store,
            externalDispatcher = scope.externalDispatcher ?: externalDispatcher,
        )
        val turn = executeTurn(input, activeContext, effectiveScope)
        val phala = turn.response.phala

        val result = when (phala) {
            is Phala.Siddha -> ExecutionResult.Success(
                value = phala.values.values.lastOrNull() ?: "",
                operation = "panini.eval",
                trace = phala.trace,
                typedValue = phala.typedValues.values.lastOrNull(),
            )
            is Phala.Asiddha -> phala.result
            is Phala.AnumatiApekshita -> ExecutionResult.NeedsApproval(
                invocationId = phala.invocationId,
                requiredEffects = phala.effects,
                continuation = requireNotNull(phala.pipelineContinuation),
                trace = phala.continuation.trace
            )
            is Phala.SvikaraApekshita -> ExecutionResult.NeedsAcceptance(
                invocationId = phala.invocationId,
                speaker = phala.speaker,
                listener = phala.listener,
                continuation = requireNotNull(phala.pipelineContinuation),
                trace = phala.continuation.trace
            )
            is Phala.Nirasta -> ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                phala.reason,
                emptyList()
            )
            else -> ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Execution resulted in $phala")
        }

        if (phala is Phala.Siddha && sessionKey != null) {
            sessions[sessionKey] = turn.context
            store.save(sessionKey, turn.context)
        }

        return result
    }

    fun resume(
        continuation: Any,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
    ): ExecutionResult {
        val cont = continuation as? dev.panini.execution.sutra.SutraPipelineContinuation
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Invalid continuation object provided.")

        val effectiveScope = scope.copy(
            stateStore = scope.stateStore ?: store,
            externalDispatcher = scope.externalDispatcher ?: externalDispatcher,
        )

        val turn = SutraExecutionPipeline.resumeTurn(cont, effectiveScope)
        val phala = turn.response.phala

        val result = when (phala) {
            is Phala.Siddha -> ExecutionResult.Success(
                value = phala.values.values.lastOrNull() ?: "",
                operation = "panini.resume",
                trace = phala.trace,
                typedValue = phala.typedValues.values.lastOrNull(),
            )
            is Phala.Asiddha -> phala.result
            is Phala.AnumatiApekshita -> ExecutionResult.NeedsApproval(
                invocationId = phala.invocationId,
                requiredEffects = phala.effects,
                continuation = requireNotNull(phala.pipelineContinuation),
                trace = phala.continuation.trace
            )
            is Phala.SvikaraApekshita -> ExecutionResult.NeedsAcceptance(
                invocationId = phala.invocationId,
                speaker = phala.speaker,
                listener = phala.listener,
                continuation = requireNotNull(phala.pipelineContinuation),
                trace = phala.continuation.trace
            )
            is Phala.Nirasta -> ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                phala.reason,
                emptyList()
            )
            else -> ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Execution resulted in $phala")
        }

        if (phala is Phala.Siddha && sessionKey != null) {
            sessions[sessionKey] = turn.context
            store.save(sessionKey, turn.context)
        }

        return result
    }

    private fun executeTurn(
        input: SanskritUktiInput,
        context: SambhashanaContext,
        scope: ExecutionScope,
    ): SambhashanaTurn = SutraExecutionPipeline.executeTurn(input, context, scope)

    fun evalScript(
        scriptContent: String,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        PvmScript.parse(scriptContent).forEach { statement ->
            results += eval(statement.text, sessionKey, scope, speaker, listener)
        }
        return results
    }

    /**
     * Evaluates canonical, evaluator-free sūtra-grantha source through the
     * public VM facade. This lets PaniniVM programs consume the same segmented
     * source representation that the sūtra machine processes.
     */
    fun evalGrantha(
        source: String,
        sourceName: String = "grantha",
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): ExecutionResult {
        val effectiveScope = scope.copy(
            stateStore = scope.stateStore ?: store,
            externalDispatcher = scope.externalDispatcher ?: externalDispatcher,
        )
        return when (
            val execution = ProgramBlueprintGranthaEngine.execute(
                source,
                ProgramBlueprintContext(
                    speaker = speaker,
                    listener = listener,
                    text = sourceName,
                ),
                effectiveScope,
                ProgramAvastha(ValueEnvironment()),
            )
        ) {
            is ProgramGranthaExecution.InvalidSource -> ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                execution.diagnostics.joinToString("\n") { diagnostic ->
                    "${diagnostic.code}${diagnostic.position?.let { " at $it" }.orEmpty()}: ${diagnostic.message}"
                },
            )
            is ProgramGranthaExecution.InvalidBlueprint -> ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                execution.diagnostics.joinToString("\n") { "${it.code}: ${it.message}" },
            )
            is ProgramGranthaExecution.InvalidRuntime -> ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                execution.diagnostics.joinToString("\n") { "${it.code}: ${it.message}" },
            )
            is ProgramGranthaExecution.Completed -> when (val result = execution.result) {
                is SutraMachineResult.Failure -> ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "Sūtra ${result.failedSutra}: ${result.message}",
                    result.trace.map { it.toString() },
                )
                is SutraMachineResult.Success -> when (val phala = result.state.lastPhala) {
                    is Phala.Siddha -> ExecutionResult.Success(
                        value = phala.values.values.lastOrNull() ?: "",
                        operation = "panini.grantha",
                        trace = phala.trace,
                        typedValue = phala.typedValues.values.lastOrNull(),
                    )
                    is Phala.Asiddha -> phala.result
                    is Phala.AnumatiApekshita -> ExecutionResult.NeedsApproval(
                        invocationId = phala.invocationId,
                        requiredEffects = phala.effects,
                        continuation = dev.panini.execution.sutra.SutraPipelineContinuation(
                            input = SanskritUktiInput(text = sourceName, speaker = speaker, listener = listener),
                            conversation = SambhashanaContext(speaker = speaker, listener = listener),
                            program = execution.program,
                            state = result.state
                        ),
                        trace = phala.continuation.trace
                    )
                    is Phala.SvikaraApekshita -> ExecutionResult.NeedsAcceptance(
                        invocationId = phala.invocationId,
                        speaker = phala.speaker,
                        listener = phala.listener,
                        continuation = dev.panini.execution.sutra.SutraPipelineContinuation(
                            input = SanskritUktiInput(text = sourceName, speaker = speaker, listener = listener),
                            conversation = SambhashanaContext(speaker = speaker, listener = listener),
                            program = execution.program,
                            state = result.state
                        ),
                        trace = phala.continuation.trace
                    )
                    null -> ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "Grantha '$sourceName' completed without producing a result.",
                    )
                    else -> ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "Grantha '$sourceName' resulted in $phala",
                    )
                }
            }
        }
    }

    fun evalGranthaFile(
        file: File,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): ExecutionResult {
        require(file.exists()) { "Sūtra grantha source file not found: ${file.path}" }
        return evalGrantha(file.readText(), file.name, scope, speaker, listener)
    }

    fun evalFile(
        file: File,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> {
        require(file.exists()) { "PaniniVM script file not found: ${file.absolutePath}" }
        val scriptContent = file.readText()
        runCatching {
            val txtFile = File(file.parentFile, file.nameWithoutExtension + ".txt")
            txtFile.writeText(PvmUktiSadhaka().sadhayaScript(scriptContent) + "\n")
        }
        return evalScript(scriptContent, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
    }

    fun loadSession(sessionKey: String): SambhashanaContext? {
        val loaded = store.load(sessionKey)
        if (loaded != null) {
            sessions[sessionKey] = loaded
        }
        return loaded
    }

    fun saveSession(sessionKey: String) {
        val context = sessions[sessionKey]
        if (context != null) {
            store.save(sessionKey, context)
        }
    }

    fun listSessions(): List<String> = store.listKeys()

    fun registerExternalCapability(effect: ExecutionEffect, handler: ExternalCapabilityDispatcher.CapabilityHandler) {
        externalDispatcher.register(effect, handler)
    }

    private companion object {
    }
}

object VM {
    private val instance by lazy { PaniniVM() }

    fun eval(
        utterance: String,
        sessionKey: String? = null,
        scope: ExecutionScope = instance.defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): ExecutionResult = instance.eval(utterance, sessionKey, scope, speaker, listener)

    fun evalFile(
        file: File,
        sessionKey: String? = null,
        scope: ExecutionScope = instance.defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> = instance.evalFile(file, sessionKey, scope, speaker, listener)

    fun evalScript(
        scriptContent: String,
        sessionKey: String? = null,
        scope: ExecutionScope = instance.defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> = instance.evalScript(scriptContent, sessionKey, scope, speaker, listener)

    fun resume(
        continuation: Any,
        sessionKey: String? = null,
        scope: ExecutionScope = instance.defaultScope,
    ): ExecutionResult = instance.resume(continuation, sessionKey, scope)
}
