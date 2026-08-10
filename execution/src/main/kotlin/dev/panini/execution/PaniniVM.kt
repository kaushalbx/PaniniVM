package dev.panini.execution

import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.memory.FileKriyaMemoryStore
import dev.panini.execution.persistence.FileStateStore
import dev.panini.execution.persistence.StateStore
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
    ): List<ExecutionResult> = scriptExecutor.evalScript(
        scriptContent, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener,
        samjnaRegistry = samjnaRegistry,
    )

    fun evalScriptWithFileContext(
        scriptContent: String,
        sourceFile: String? = null,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
        samjnaRegistry: SamjnaKriyaRegistry? = null,
    ): List<ExecutionResult> = scriptExecutor.evalScript(
        scriptContent, sourceFile, sessionKey, scope, speaker, listener, samjnaRegistry,
    )

    fun evalProject(
        entryFile: File,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> = scriptExecutor.evalProject(entryFile, sessionKey, scope, speaker, listener)

    internal fun executeSamjnaInvocation(
        invocation: SamjnaInvocation,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        callerSourceFile: String? = null,
    ): List<ExecutionResult> = scriptExecutor.executeSamjnaInvocation(
        invocation, sessionKey, scope, speaker, listener, registry, callerSourceFile,
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
    ): List<ExecutionResult> {
        require(file.exists()) { "PaniniVM script file not found: ${file.absolutePath}" }

        val projectDir = file.parentFile ?: file.absoluteFile.parentFile
        val hasSiblingPvm = projectDir?.walkTopDown()?.any { f -> f.isFile && f.extension == "pvm" && f.canonicalPath != file.canonicalPath } == true
        return if (hasSiblingPvm) {
            evalProject(file, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
        } else {
            val scriptContent = file.readText()
            evalScript(scriptContent, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
        }
    }

    fun loadSession(sessionKey: String): SambhashanaContext? = sessionRuntime.load(sessionKey)

    fun saveSession(sessionKey: String) = sessionRuntime.save(sessionKey)

    fun listSessions(): List<String> = sessionRuntime.listKeys()

    fun registerExternalCapability(effect: ExecutionEffect, handler: ExternalCapabilityDispatcher.CapabilityHandler) {
        externalDispatcher.register(effect, handler)
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

    fun evalProject(
        entryFile: File,
        sessionKey: String? = null,
        scope: ExecutionScope = instance.defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> = instance.evalProject(entryFile, sessionKey, scope, speaker, listener)

    fun resume(
        continuation: Any,
        sessionKey: String? = null,
        scope: ExecutionScope = instance.defaultScope,
    ): ExecutionResult = instance.resume(continuation, sessionKey, scope)
}

object PuranaPratyayaResolver {

    private val parser = dev.panini.vyakaranam.parser.PaniniParser()
    private val sankhyaEvaluator = dev.panini.sankhya.SankhyaEvaluator()
    private val sankhyaGenerator = dev.panini.sankhya.SankhyaGenerator()

    /** Replaces parsed pūraṇa parameter padas having the requested ordinal value. */
    fun replacePatterns(text: String, index: Int, rawArgVal: String): String {
        val cleanArg = if (isAccusative(rawArgVal)) rawArgVal else "$rawArgVal + अम्"
        val ukti = parser.parseOrNull(text.trim()) ?: return text
        val ordinalValue = index + 1L
        val ordinalSurface = sankhyaGenerator.ordinal(ordinalValue).final.surface
        val ordinalSources = ukti.grammaticalVakyas().asSequence()
            .flatMap { it.padas.asSequence() }
            .map { it.sourceText }
            .filter { isOrdinal(it, ordinalValue, ordinalSurface) }
            .distinct()
            .toList()
        return ordinalSources.fold(text) { result, source ->
            result.replace(sourcePattern(source), cleanArg)
        }
    }

    private fun isAccusative(source: String): Boolean {
        val padas = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' '))
            ?.grammaticalVakyas()
            ?.flatMap { it.padas }
            ?: return false
        val argument = padas.singleOrNull() as? dev.panini.vyakaranam.ast.SubantaPada ?: return false
        return dev.panini.core.SupAffix.fromUpadesha(argument.sup.text)?.vibhakti ==
            dev.panini.core.Vibhakti.DVITIYA
    }

    private fun isOrdinal(padaSource: String, value: Long, surface: String): Boolean {
        val morphemes = padaSource.split('+').map(String::trim).filter(String::isNotEmpty)
        if (morphemes.size < 2) return false
        val stems = morphemes.dropLast(1)
        val expression = runCatching { sankhyaEvaluator.evaluateStems(stems) }.getOrNull()
        return (expression as? dev.panini.sankhya.SankhyaExpression.Purana)?.value == value ||
            stems.joinToString("") == surface
    }

    private fun sourcePattern(source: String): Regex = Regex(
        source.split('+').joinToString("\\s*\\+\\s*") { Regex.escape(it) },
    )
}
