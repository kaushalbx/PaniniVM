package dev.panini.execution

import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.persistence.FileStateStore
import dev.panini.execution.persistence.StateStore
import java.io.File

/**
 * Top-level execution facade and API for PaniniVM.
 * Provides simplified evaluation, session persistence, external capability registration,
 * and capability-based security.
 */
class PaniniVM(
    storageDir: File = File(System.getProperty("java.io.tmpdir"), "paninivm_sessions_" + System.currentTimeMillis()),
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
        val turn = ExecutionPipeline.executeTurn(
            input,
            activeContext,
            scope.copy(
                stateStore = scope.stateStore ?: store,
                externalDispatcher = scope.externalDispatcher ?: externalDispatcher,
            ),
        )
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

    fun evalScript(
        scriptContent: String,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> {
        val lines = scriptContent.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("//") }

        return lines.map { line ->
            eval(line, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
        }
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
}
