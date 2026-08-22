package dev.panini.execution

import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.memory.FileKriyaMemoryStore
import dev.panini.execution.persistence.FileStateStore
import dev.panini.execution.persistence.StateStore
import java.io.File

/**
 * Optional host safeguards for PVM execution.
 *
 * A null loop budget gives `yāvat ... tāvat` its language-level, condition-controlled
 * semantics: it continues until its condition becomes false or the program breaks.
 * Embedders that execute untrusted programs may install a finite budget without making
 * that budget part of the PVM language definition.
 */
data class ExecutionLimits(
    val maxConditionIterations: Long? = null,
) {
    init {
        require(maxConditionIterations == null || maxConditionIterations > 0L) {
            "The condition-loop execution budget must be positive when specified."
        }
    }
}

/**
 * Top-level execution facade and API for PaniniVM.
 * Provides simplified evaluation, session persistence, external capability registration,
 * and capability-based security.
 */
class PaniniVM(
    storageDir: File = File(System.getProperty("java.io.tmpdir"), "paninivm_sessions_" + java.util.UUID.randomUUID()),
    val executionLimits: ExecutionLimits = ExecutionLimits(),
    val defaultScope: ExecutionScope = ExecutionScope(
        capabilities = setOf(
            ExecutionEffect.PURE,
            ExecutionEffect.READ_MEMORY,
            ExecutionEffect.WRITE_MEMORY,
            ExecutionEffect.READ_RESOURCE,
            ExecutionEffect.WRITE_RESOURCE,
        ),
        linguisticServices = dev.panini.derivation.LinguisticActionsInitializer.services(),
        sankhyaRenderer = dev.panini.sankhya.SankhyaCountingFormRenderer(),
    ),
) {
    val store: StateStore = FileStateStore(storageDir)
    private val kriyaMemoryStore = FileKriyaMemoryStore(storageDir)
    private val externalDispatcher = ExternalCapabilityDispatcher()

    init {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
    }

    private val sessionRuntime by lazy {
        SessionRuntime(store, kriyaMemoryStore, externalDispatcher)
    }

    /** Kriyā-centred memory accumulated automatically for this VM session. */
    fun kriyaMemory(sessionKey: String) = sessionRuntime.kriyaMemory(sessionKey)

    fun eval(
        utterance: String,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
        isExecutingScript: Boolean = false,
    ): ExecutionResult {
        if (!isExecutingScript && PvmScript.classify(utterance) == PvmSourceKind.SCRIPT) {
            val scriptResults = evalScript(utterance, sessionKey, scope, speaker, listener)
            return scriptResults.lastOrNull()
                ?: ExecutionResult.Success(operation = "panini.evalScript", value = "संसिद्धम्")
        }
        return sessionRuntime.eval(utterance, sessionKey, scope, speaker, listener)
    }

    fun resume(
        continuation: Any,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
    ): ExecutionResult = sessionRuntime.resume(continuation, sessionKey, scope)

    private val scriptExecutor by lazy { PvmScriptExecutor(this) }

    fun evalScript(
        scriptContent: String,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
        samjnaRegistry: SamjnaKriyaRegistry? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> = scriptExecutor.evalScript(
        scriptContent, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener,
        samjnaRegistry = samjnaRegistry, onResult = onResult,
    )

    fun evalScriptWithFileContext(
        scriptContent: String,
        sourceFile: String? = null,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
        samjnaRegistry: SamjnaKriyaRegistry? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> = scriptExecutor.evalScript(
        scriptContent, sourceFile, sessionKey, scope, speaker, listener, samjnaRegistry, onResult,
    )

    fun evalProject(
        entryFile: File,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> = scriptExecutor.evalProject(entryFile, sessionKey, scope, speaker, listener, onResult)

    internal fun executeSamjnaInvocation(
        invocation: SamjnaInvocation,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        callerSourceFile: String? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> = scriptExecutor.executeSamjnaInvocation(
        invocation, sessionKey, scope, speaker, listener, registry, callerSourceFile, onResult,
    )

    private val granthaExecutor by lazy { GranthaExecutor(store, externalDispatcher) }

    /**
     * Evaluates canonical, evaluator-free sūtra-grantha source through the
     * public VM facade.
     */
    fun evalGrantha(
        source: String,
        sourceName: String = "grantha",
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): ExecutionResult = granthaExecutor.eval(source, sourceName, scope, speaker, listener)

    fun evalGranthaFile(
        file: File,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): ExecutionResult = granthaExecutor.evalFile(file, scope, speaker, listener)

    fun evalFile(
        file: File,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> = scriptExecutor.evalFile(file, sessionKey, scope, speaker, listener, onResult)

    fun loadSession(sessionKey: String): SambhashanaContext? = sessionRuntime.load(sessionKey)

    fun saveSession(sessionKey: String) = sessionRuntime.save(sessionKey)

    fun listSessions(): List<String> = sessionRuntime.listKeys()

    fun checkpointSession(sessionKey: String): SessionCheckpoint = sessionRuntime.checkpoint(sessionKey)

    fun restoreSession(sessionKey: String, checkpoint: SessionCheckpoint) =
        sessionRuntime.restore(sessionKey, checkpoint)

    fun registerExternalCapability(effect: ExecutionEffect, handler: ExternalCapabilityDispatcher.CapabilityHandler) {
        externalDispatcher.register(effect, handler)
    }

}
