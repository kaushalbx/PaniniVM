package dev.panini.execution

import java.io.File

/** Process-wide compatibility facade backed by a lazily initialized [PaniniVM]. */
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
