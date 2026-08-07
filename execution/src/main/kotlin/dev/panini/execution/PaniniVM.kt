package dev.panini.execution

import dev.panini.analysis.KriyaId
import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.memory.KriyaMemory
import dev.panini.execution.memory.RememberedKriya
import dev.panini.execution.memory.FileKriyaMemoryStore
import dev.panini.execution.memory.withMemoryId
import dev.panini.execution.persistence.FileStateStore
import dev.panini.execution.persistence.StateStore
import dev.panini.execution.sutra.ProgramAvastha
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintGranthaEngine
import dev.panini.execution.sutra.ProgramGranthaExecution
import dev.panini.execution.sutra.SutraExecutionPipeline
import dev.panini.sutra.runtime.SutraMachineResult
import java.io.File
import java.util.concurrent.ConcurrentHashMap

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

    private val sessions = ConcurrentHashMap<String, SambhashanaContext>()
    private val kriyaMemories = ConcurrentHashMap<String, KriyaMemory>()

    /** Kriyā-centred memory accumulated automatically for this VM session. */
    fun kriyaMemory(sessionKey: String): KriyaMemory = kriyaMemories.computeIfAbsent(sessionKey) {
        kriyaMemoryStore.load(sessionKey) { source ->
            VyakaranamExecutionAdapter.analyzeForMemory(source)?.frames.orEmpty()
        } ?: KriyaMemory()
    }

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
        val analysis = VyakaranamExecutionAdapter.analyzeForMemory(utterance)
        val effectiveScope = scope.copy(
            stateStore = scope.stateStore ?: store,
            externalDispatcher = scope.externalDispatcher ?: externalDispatcher,
        )
        val memory = sessionKey?.let(::kriyaMemory) ?: KriyaMemory()
        val turn = executeTurn(input, activeContext, effectiveScope, memory)
        val phala = turn.response.phala

        val result = phala.toExecutionResult("panini.eval")

        if (phala is Phala.Siddha && sessionKey != null) {
            sessions[sessionKey] = turn.context
            store.save(sessionKey, turn.context)
            if (analysis != null) rememberKriyas(sessionKey, turn.context.turnNumber, analysis.frames, phala)
        }

        return result
    }

    private fun rememberKriyas(
        sessionKey: String,
        turn: Int,
        frames: List<dev.panini.analysis.KriyaFrame>,
        phala: Phala.Siddha,
    ) {
        if (frames.isEmpty()) return
        val invocationValues = phala.typedValues.entries
            .filter { (id, _) -> id.startsWith("योग-") }
            .sortedBy { (id, _) -> id.substringAfter("योग-").toIntOrNull() ?: Int.MAX_VALUE }
        val remembered = invocationValues.mapIndexed { index, (_, value) ->
            val source = frames[index % frames.size]
            RememberedKriya(
                turn = turn,
                frame = source.withMemoryId(KriyaId("turn-$turn-kriya-${index + 1}")),
                phala = value,
            )
        }
        if (remembered.isNotEmpty()) {
            val memory = kriyaMemory(sessionKey).remember(remembered)
            kriyaMemories[sessionKey] = memory
            kriyaMemoryStore.save(sessionKey, memory)
        }
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

        val result = phala.toExecutionResult("panini.resume")

        if (phala is Phala.Siddha && sessionKey != null) {
            sessions[sessionKey] = turn.context
            store.save(sessionKey, turn.context)
            VyakaranamExecutionAdapter.analyzeForMemory(cont.input.text)?.let { analysis ->
                rememberKriyas(sessionKey, turn.context.turnNumber, analysis.frames, phala)
            }
        }

        return result
    }

    private fun executeTurn(
        input: SanskritUktiInput,
        context: SambhashanaContext,
        scope: ExecutionScope,
        memory: KriyaMemory,
    ): SambhashanaTurn = SutraExecutionPipeline.executeTurn(input, context, scope, memory)

    fun evalScript(
        scriptContent: String,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
        samjnaRegistry: SamjnaKriyaRegistry? = null,
    ): List<ExecutionResult> = evalScriptWithFileContext(
        scriptContent = scriptContent,
        sourceFile = null,
        sessionKey = sessionKey,
        scope = scope,
        speaker = speaker,
        listener = listener,
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
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        val effectiveSessionKey = sessionKey ?: "script-${System.identityHashCode(scriptContent)}"
        val parsed = PvmScript.parse(scriptContent)

        val registry = samjnaRegistry ?: SamjnaKriyaRegistry()
        val isEntryPoint = samjnaRegistry != null
        val topDomainDefn = parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().firstOrNull()
        val topDomainStem = topDomainDefn?.let { deriveSamjnaStem(it.domainSegmented) }

        parsed.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().forEach { defn ->
            val stem = deriveSamjnaStem(defn.nameSegmented)
            val domain = deriveDomainStem(defn.nameSegmented) ?: topDomainStem
            registry.register(
                SamjnaKriya(
                    nameSegmented = defn.nameSegmented,
                    nameStem = stem,
                    body = defn.body,
                    sourceFile = sourceFile,
                    domainStem = domain,
                    isApavada = isEntryPoint,
                    isInternal = defn.isInternal,
                ),
            )
        }

        parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().forEach { adhikara ->
            val inheritance = TaddhitaInheritanceEngine.detectInheritanceAdhikara(adhikara.domainSegmented)
            if (inheritance != null) {
                registry.registerInheritance(inheritance)
            }
        }

        val effectiveScope = scope.copy(samjnaRegistry = registry)
        val structStore = mutableMapOf<String, TaddhitaStruct>()

        parsed.filterIsInstance<PvmScriptStatement.Sentence>().forEach { statement ->
            val constructedStruct = TaddhitaStructEngine.detectStructConstruction(statement.text, statement.ukti)
            val attributeAccess = TaddhitaStructEngine.detectAttributeAccess(statement.text, statement.ukti)

            if (constructedStruct != null) {
                structStore[constructedStruct.nameStem] = constructedStruct
            } else if (attributeAccess != null) {
                val (structName, keyTerm) = attributeAccess
                val structObj = structStore[structName]
                val attrVal = structObj?.attributes?.get(keyTerm)
                if (attrVal != null) {
                    results += ExecutionResult.Success(operation = "taddhita.query", value = attrVal)
                } else {
                    results += ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "तद्धित-असंगतिः: Attribute '$keyTerm' not found in struct '$structName'",
                    )
                }
            } else {
                val invocation = registry.detectInvocation(statement.text, callerSourceFile = sourceFile, preParsedUkti = statement.ukti)
                if (invocation != null) {
                    val invResults = executeSamjnaInvocation(
                        invocation, effectiveSessionKey, effectiveScope, speaker, listener, registry, callerSourceFile = sourceFile,
                    )
                    val cleanResults = if (invResults.any { it is ExecutionResult.Success }) {
                        invResults.filter { it is ExecutionResult.Success }
                    } else invResults
                    results += cleanResults
                } else {
                    results += eval(statement.text, effectiveSessionKey, effectiveScope, speaker, listener)
                }
            }
        }
        return results
    }

    fun evalProject(
        entryFile: File,
        sessionKey: String? = null,
        scope: ExecutionScope = defaultScope,
        speaker: String = "प्रयोक्ता",
        listener: String = "यन्त्रम्",
    ): List<ExecutionResult> {
        require(entryFile.exists()) { "PaniniVM entry-point file not found: ${entryFile.absolutePath}" }

        val projectDir = entryFile.parentFile ?: entryFile.absoluteFile.parentFile
            ?: error("Cannot determine project directory for ${entryFile.path}")

        val libraryFiles = projectDir.walkTopDown()
            .filter { f -> f.isFile && f.extension == "pvm" && f.canonicalPath != entryFile.canonicalPath }
            .sortedBy { it.name }
            .toList()

        val registry = SamjnaKriyaRegistry()
        for (libFile in libraryFiles) {
            val parsed = PvmScript.parse(libFile.readText())
            val fileDomainDefn = parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().firstOrNull()
            val fileDomainStem = fileDomainDefn?.let { deriveSamjnaStem(it.domainSegmented) }

            parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().forEach { adhikara ->
                val inheritance = TaddhitaInheritanceEngine.detectInheritanceAdhikara(adhikara.domainSegmented)
                if (inheritance != null) {
                    registry.registerInheritance(inheritance)
                }
            }

            parsed.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().forEach { defn ->
                val stem = deriveSamjnaStem(defn.nameSegmented)
                val domain = deriveDomainStem(defn.nameSegmented) ?: fileDomainStem
                registry.register(
                    SamjnaKriya(
                        nameSegmented = defn.nameSegmented,
                        nameStem = stem,
                        body = defn.body,
                        sourceFile = libFile.name,
                        domainStem = domain,
                        isApavada = false,
                        isInternal = defn.isInternal,
                    ),
                )
            }
        }

        val effectiveSessionKey = sessionKey ?: "project-${entryFile.nameWithoutExtension}-${System.currentTimeMillis()}"
        return evalScriptWithFileContext(
            entryFile.readText(),
            sourceFile = entryFile.name,
            sessionKey = effectiveSessionKey,
            scope = scope,
            speaker = speaker,
            listener = listener,
            samjnaRegistry = registry,
        )
    }

    private fun executeSamjnaInvocation(
        invocation: SamjnaInvocation,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        callerSourceFile: String? = null,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()

        // Extract caller's argument base terms dynamically via SubantaKarakaParser (karma: + अम्)
        val argTerms = SubantaKarakaParser.extractKarmaTerms(invocation.karmaText, invocation.ukti)
        val paramNames = PuranaPratyayaResolver.getOrdinalsUpTo(maxOf(6, argTerms.size))

        // Step 0: Check Memoization Cache for क्त-प्रत्यय Constant Saṃjñās
        if (invocation.kriya.isMemoized) {
            val cached = registry.getCachedResult(invocation.kriya.nameStem, invocation.karmaText)
            if (cached != null) {
                return listOf(cached)
            }
        }

        // Step 1: Evaluate Niṣedha Sūtra & Tva-Pratyaya Type Guard Preconditions
        invocation.kriya.nishedhaGuards.forEach { guard ->
            var guardText = guard.text
            paramNames.forEachIndexed { index, param ->
                if (index < argTerms.size && guardText.contains(param)) {
                    guardText = guardText.replace(param, argTerms[index])
                }
            }

            val isProhibited = DynamicNishedhaEvaluator.evaluateProhibition(guardText, argTerms, guard.ukti)

            // Check Tva-pratyaya Type Guard (e.g. "न प्रथम + अम् सङ्ख्या + त्व + अम्")
            val isTypeGuard = guardText.contains("सङ्ख्या + त्व") || guardText.contains("सूची + त्व")
            val isTypeViolated = isTypeGuard && argTerms.any { term ->
                val numVal = term.toLongOrNull() ?: runCatching { dev.panini.sankhya.SankhyaEvaluator().evaluateStems(listOf(term)).value }.getOrNull() ?: -1L
                numVal <= 0L && !term.any { it.isDigit() }
            }

            if (isProhibited || isTypeViolated) {
                return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "निषेध-प्रतिषेधः: Prohibition triggered by '${guard.text.trim()}'",
                    ),
                )
            }
        }

        // Step 2: Create isolated Child Scope (वातावरण-सीमा) for local saṃjñā execution
        val childEnvironment = ValueEnvironment(scope.environment.values)
        var childScope = scope.copy(environment = childEnvironment)

        // Step 3: Execute Vidhi Sūtra (Mandate) sentences inside childScope
        invocation.kriya.vidhiSentences.forEach { bodySentence ->
            var sentenceText = bodySentence.text

            // Substitute explicit parameter names (प्रथम, द्वितीय, तृतीय...)
            paramNames.forEachIndexed { index, param ->
                if (index < argTerms.size && sentenceText.contains(param)) {
                    sentenceText = sentenceText.replace(param, argTerms[index])
                }
            }

            // Substitute समवाय (Collection / List batch fold parameter)
            if (sentenceText.contains("समवाय") && invocation.karmaText.isNotBlank()) {
                if (sentenceText.contains("समवाय + अम्")) {
                    sentenceText = sentenceText.replace("समवाय + अम्", invocation.karmaText)
                } else if (sentenceText.contains("समवाय")) {
                    sentenceText = sentenceText.replace("समवाय", invocation.karmaText)
                }
            }

            // Sibling body invocations inside a saṃjñā pass the saṃjñā's own sourceFile
            val kriyaSourceFile = invocation.kriya.sourceFile ?: callerSourceFile
            val bodyInvocation = registry.detectInvocation(sentenceText, callerSourceFile = kriyaSourceFile)
            if (bodyInvocation != null) {
                results += executeSamjnaInvocation(
                    bodyInvocation, sessionKey, childScope, speaker, listener, registry, callerSourceFile = kriyaSourceFile,
                )
            } else {
                val stepResults = eval(sentenceText, sessionKey, childScope, speaker, listener)
                results += stepResults
            }
        }

        if (invocation.kriya.isMemoized && results.isNotEmpty()) {
            val lastRes = results.last()
            if (lastRes is ExecutionResult.Success) {
                registry.cacheResult(invocation.kriya.nameStem, invocation.karmaText, lastRes)
            }
        }

        return results
    }

    private fun deriveSamjnaStem(nameSegmented: String): String {
        var clean = nameSegmented
        if (clean.contains("+ ङस्")) {
            clean = clean.substringAfter("+ ङस्").trim()
        }
        return SamjnaKriyaRegistry.stripSupSuffix(clean)
    }

    private fun deriveDomainStem(nameSegmented: String): String? {
        if (!nameSegmented.contains("+ ङस्")) return null
        val beforeNgas = nameSegmented.substringBefore("+ ङस्").trim()
        val matupClean = beforeNgas.substringBefore("+ वत्").substringBefore("+ मत्").trim()
        return SamjnaKriyaRegistry.stripSupSuffix(matupClean).ifEmpty { null }
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
                    null -> ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "Grantha '$sourceName' completed without producing a result.",
                    )
                    else -> {
                        val continuation = dev.panini.execution.sutra.SutraPipelineContinuation(
                            input = SanskritUktiInput(text = sourceName, speaker = speaker, listener = listener),
                            conversation = SambhashanaContext(speaker = speaker, listener = listener),
                            program = execution.program,
                            state = result.state,
                        )
                        val resumable = when (phala) {
                            is Phala.AnumatiApekshita -> phala.copy(pipelineContinuation = continuation)
                            is Phala.SvikaraApekshita -> phala.copy(pipelineContinuation = continuation)
                            else -> phala
                        }
                        resumable.toExecutionResult("panini.grantha")
                    }
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

        val projectDir = file.parentFile ?: file.absoluteFile.parentFile
        val hasSiblingPvm = projectDir?.walkTopDown()?.any { f -> f.isFile && f.extension == "pvm" && f.canonicalPath != file.canonicalPath } == true
        return if (hasSiblingPvm) {
            evalProject(file, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
        } else {
            val scriptContent = file.readText()
            evalScript(scriptContent, sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
        }
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

    private fun Phala.toExecutionResult(operation: String): ExecutionResult = when (this) {
        is Phala.Siddha -> ExecutionResult.Success(
            value = values.values.lastOrNull() ?: "",
            operation = operation,
            trace = trace,
            typedValue = typedValues.values.lastOrNull(),
        )
        is Phala.Asiddha -> result
        is Phala.AnumatiApekshita -> pipelineContinuation?.let { resumable ->
            ExecutionResult.NeedsApproval(
                invocationId = invocationId,
                requiredEffects = effects,
                continuation = resumable,
                trace = continuation.trace,
            )
        } ?: ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Approval continuation is unavailable.")
        is Phala.SvikaraApekshita -> pipelineContinuation?.let { resumable ->
            ExecutionResult.NeedsAcceptance(
                invocationId = invocationId,
                speaker = speaker,
                listener = listener,
                continuation = resumable,
                trace = continuation.trace,
            )
        } ?: ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Acceptance continuation is unavailable.")
        is Phala.Nirasta -> ExecutionResult.Failure(ExecutionError.ACTION_FAILED, reason)
        else -> ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Execution resulted in $this")
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
    private val BASE_ORDINALS = listOf(
        "प्रथम", "द्वितीय", "तृतीय", "चतुर्थ", "पञ्चम",
        "षष्ठ", "सप्तम", "अष्टम", "नवम", "दशम",
        "एकादशम", "द्वादशम", "त्रयोदशम", "चतुर्दशम", "पञ्चदशम",
    )

    fun getOrdinal(index: Int): String {
        return BASE_ORDINALS.getOrElse(index) { "संख्या-${index + 1}" }
    }

    fun getOrdinalsUpTo(count: Int): List<String> {
        return List(count) { getOrdinal(it) }
    }
}
