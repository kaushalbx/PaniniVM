package dev.panini.execution

import dev.panini.actions.control.LoopAction
import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.persistence.FileStateStore
import dev.panini.execution.persistence.StateStore
import dev.panini.execution.runtime.ExecutionPipeline
import dev.panini.execution.sutra.SutraExecutionPipeline
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
    val executionArchitecture: PaniniExecutionArchitecture = PaniniExecutionArchitecture.LEGACY,
) {
    val store: StateStore = FileStateStore(storageDir)
    private val externalDispatcher = ExternalCapabilityDispatcher()

    init {
        dev.panini.derivation.LinguisticActionsInitializer.initialize()
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
    ): SambhashanaTurn = when (executionArchitecture) {
        PaniniExecutionArchitecture.LEGACY ->
            ExecutionPipeline.executeTurn(input, context, scope)
        PaniniExecutionArchitecture.SUTRA_MACHINE ->
            SutraExecutionPipeline.executeTurn(input, context, scope)
        PaniniExecutionArchitecture.COMPARE -> {
            require(scope.capabilities == setOf(ExecutionEffect.PURE)) {
                "COMPARE execution requires a PURE-only scope to avoid repeating external effects."
            }
            val legacy = ExecutionPipeline.executeTurn(input, context, scope)
            val migrated = SutraExecutionPipeline.executeTurn(input, context, scope)
            if (equivalent(legacy.response.phala, migrated.response.phala) &&
                legacy.context == migrated.context
            ) {
                migrated
            } else {
                val mismatch = Phala.Asiddha(
                    ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "Legacy and sūtra-machine execution produced different results.",
                    ),
                    listOf(
                        "Legacy: ${legacy.response.phala}",
                        "Sūtra machine: ${migrated.response.phala}",
                    ),
                )
                SambhashanaTurn(
                    SanskritPrativacanaRenderer.render(mismatch),
                    context,
                )
            }
        }
    }

    private fun equivalent(legacy: Phala, migrated: Phala): Boolean = when {
        legacy is Phala.Siddha && migrated is Phala.Siddha ->
            legacy.values == migrated.values &&
                legacy.samjnas == migrated.samjnas &&
                legacy.typedValues == migrated.typedValues &&
                legacy.localBindings == migrated.localBindings
        legacy is Phala.Asiddha && migrated is Phala.Asiddha ->
            legacy.result::class == migrated.result::class
        legacy is Phala.Avagata && migrated is Phala.Avagata ->
            legacy.disposition == migrated.disposition
        legacy is Phala.AnumatiApekshita && migrated is Phala.AnumatiApekshita ->
            legacy.invocationId == migrated.invocationId &&
                legacy.effects == migrated.effects
        legacy is Phala.SvikaraApekshita && migrated is Phala.SvikaraApekshita ->
            legacy.invocationId == migrated.invocationId &&
                legacy.speaker == migrated.speaker &&
                legacy.listener == migrated.listener
        legacy is Phala.Nirasta && migrated is Phala.Nirasta ->
            legacy.invocationId == migrated.invocationId &&
                legacy.reason == migrated.reason
        else -> false
    }

    fun evalScript(
        scriptContent: String,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        PvmScript.parse(scriptContent).forEach { statement ->
            when (statement) {
                is PvmScriptStatement.Sentence -> results += eval(
                    statement.text, sessionKey, scope, speaker, listener,
                )
                is PvmScriptStatement.While -> {
                    val invocation = eval(
                        statement.invocation.text, sessionKey, scope, speaker, listener,
                    )
                    results += invocation
                    if (invocation !is ExecutionResult.Success ||
                        invocation.trace.none { "मूलधातु वृत् with यङ्" in it }
                    ) {
                        return results
                    }
                    results += LoopAction.executeStructured(
                        condition = {
                            eval(statement.condition, sessionKey, scope, speaker, listener)
                        },
                        body = {
                            statement.body.map { clause ->
                                eval(clause.text, sessionKey, scope, speaker, listener)
                            }
                        },
                        maximumIterations = MAX_LOOP_ITERATIONS,
                    )
                }
            }
        }
        return results
    }

    fun evalFile(
        file: File,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> {
        require(file.exists()) { "PaniniVM script file not found: ${file.absolutePath}" }
        return evalScript(file.readText(), sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
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
        const val MAX_LOOP_ITERATIONS = 100_000
    }
}
