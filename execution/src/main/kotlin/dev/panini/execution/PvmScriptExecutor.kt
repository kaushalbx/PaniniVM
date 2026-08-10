package dev.panini.execution

import dev.panini.execution.binding.FrequencyExtractor
import dev.panini.execution.binding.baseText
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.WhileLoop
import java.io.File

/** Executes PVM scripts and projects behind the stable [PaniniVM] facade. */
internal class PvmScriptExecutor(private val vm: PaniniVM) {
    fun evalScript(
        scriptContent: String,
        sourceFile: String? = null,
        sessionKey: String? = null,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        samjnaRegistry: SamjnaKriyaRegistry? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        val effectiveSessionKey = sessionKey ?: "script-${System.identityHashCode(scriptContent)}"
        val parsed = PvmScript.parse(scriptContent)

        val registry = samjnaRegistry ?: SamjnaKriyaRegistry()
        val topDomainDefn = parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().firstOrNull()
        val topDomainStem = topDomainDefn?.let { deriveSamjnaStem(it.scope.domain) }

        parsed.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().forEach { defn ->
            registerSamjna(registry, defn, sourceFile, topDomainStem)
        }
        registerInheritances(registry, parsed)

        val activeRange = parsed.filterIsInstance<PvmScriptStatement.RangeDefinition>()
            .lastOrNull()?.range
        val rangeEnvironment = activeRange?.let {
            ValueEnvironment(mapOf(ACTIVE_RANGE_NAME to it))
        } ?: ValueEnvironment()
        val effectiveScope = scope.copy(
            samjnaRegistry = registry,
            environment = scope.environment.mergedWith(rangeEnvironment),
        )
        val structStore = mutableMapOf<String, TaddhitaStruct>()

        parsed.filterIsInstance<PvmScriptStatement.Sentence>().forEach { statement ->
            val constructedStruct = TaddhitaStructEngine.detectStructConstruction(statement.text, statement.ukti)
            val nestedAttributeAccess = TaddhitaStructEngine.detectNestedAttributeAccess(statement.text, statement.ukti)
            val pipeline = statement.program as? dev.panini.vyakaranam.ast.Pipeline
            val whileLoop = statement.program as? WhileLoop

            when {
                constructedStruct != null -> structStore[constructedStruct.nameStem] = constructedStruct
                nestedAttributeAccess != null -> resolveNestedAttribute(nestedAttributeAccess, structStore).let {
                    results += it
                    onResult?.invoke(it)
                }
                whileLoop != null -> executeWhileLoop(
                    whileLoop,
                    effectiveSessionKey,
                    effectiveScope,
                    speaker,
                    listener,
                    registry,
                    sourceFile,
                    structStore,
                    onResult,
                ).also(results::addAll)
                pipeline != null -> PurvaparaPipelineEngine.executePipeline(
                    pipeline, vm, effectiveSessionKey, effectiveScope, speaker, listener, registry,
                    callerSourceFile = sourceFile,
                ).also { produced ->
                    results += produced
                    produced.forEach { onResult?.invoke(it) }
                }
                else -> {
                    val invocation = registry.detectInvocation(
                        statement.text,
                        callerSourceFile = sourceFile,
                        preParsedUkti = statement.ukti,
                    )
                    if (invocation != null) {
                        val invocationResults = executeSamjnaInvocation(
                            invocation,
                            effectiveSessionKey,
                            effectiveScope,
                            speaker,
                            listener,
                            registry,
                            callerSourceFile = sourceFile,
                            onResult = onResult,
                        )
                        results += if (invocationResults.any { it is ExecutionResult.Success }) {
                            invocationResults.filterIsInstance<ExecutionResult.Success>()
                        } else {
                            invocationResults
                        }
                    } else {
                        val result = vm.eval(
                            statement.text,
                            effectiveSessionKey,
                            effectiveScope,
                            speaker,
                            listener,
                            isExecutingScript = true,
                        )
                        results += result
                        onResult?.invoke(result)
                    }
                }
            }
        }
        return results
    }

    private fun executeWhileLoop(
        loop: WhileLoop,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        sourceFile: String?,
        structStore: MutableMap<String, TaddhitaStruct>,
        onResult: ((ExecutionResult) -> Unit)?,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        val isBounded = loop.maximumIterationStems.isNotEmpty()
        val maximumIterations = if (!isBounded) {
            MAX_CONDITION_ITERATIONS
        } else {
            val value = dev.panini.sankhya.SankhyaEvaluator()
                .evaluateStems(loop.maximumIterationStems).value
            if (value !in 1L..MAX_CONDITION_ITERATIONS.toLong()) {
                return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "A condition-controlled loop bound must be between 1 and $MAX_CONDITION_ITERATIONS.",
                    ),
                )
            }
            value.toInt()
        }
        val usesLatestResult = loop.condition.vakya.padas.any { pada ->
            pada is dev.panini.vyakaranam.ast.SubantaPada && pada.pratipadika.baseText() == "फल"
        }
        val isNegated = loop.condition.vakya.padas.any {
            it is dev.panini.vyakaranam.ast.AvyayaPada && it.form == "न"
        }
        var latestConditionValue = false
        var iterationCount = 0

        fun complete(outcome: ExecutionResult.LoopOutcome): List<ExecutionResult> {
            val outcomeValue = SanskritValue.Shabda(outcome.sanskritName)
            val attemptWord = dev.panini.sankhya.SankhyaGenerator()
                .cardinal(iterationCount.toLong()).final.surface
            structStore[LOOP_RESULT_NAME] = TaddhitaStruct(
                nameStem = LOOP_RESULT_NAME,
                attributes = mapOf(
                    "अवस्था" to outcome.sanskritName,
                    "प्रयत्नसङ्ख्या" to attemptWord,
                ),
                typedAttributes = mapOf(
                    "अवस्था" to outcomeValue,
                    "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(iterationCount.toLong(), attemptWord),
                ),
            )
            val completion = ExecutionResult.Success(
                value = outcome.sanskritName,
                operation = "pvm.while",
                typedValue = outcomeValue,
                loopOutcome = outcome,
                iterationCount = iterationCount,
            )
            results += completion
            onResult?.invoke(completion)

            val target = loop.resultTarget as? dev.panini.vyakaranam.ast.Invocation ?: return results
            val targetScope = scope.copy(
                environment = scope.environment.mergedWith(
                    ValueEnvironment(
                        mapOf(
                            "फल" to outcomeValue,
                            "परिणाम" to outcomeValue,
                            "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(iterationCount.toLong(), iterationCount.toString()),
                        ),
                    ),
                ),
            )
            val targetText = renderInvocation(target, pipedKarman = outcome.sanskritName)
            val invocation = registry.detectInvocation(targetText, callerSourceFile = sourceFile)
            val targetResults = if (invocation != null) {
                executeSamjnaInvocation(
                    invocation, sessionKey, targetScope, speaker, listener, registry,
                    callerSourceFile = sourceFile, onResult = onResult,
                )
            } else {
                listOf(vm.eval(targetText, sessionKey, targetScope, speaker, listener, isExecutingScript = true)).also {
                    it.forEach { result -> onResult?.invoke(result) }
                }
            }
            results += targetResults
            return results
        }

        repeat(maximumIterations) {
            val conditionHolds = if (usesLatestResult) {
                if (isNegated) !latestConditionValue else latestConditionValue
            } else {
                val conditionResult = vm.eval(
                    renderInvocation(loop.condition),
                    sessionKey,
                    scope,
                    speaker,
                    listener,
                    isExecutingScript = true,
                )
                (conditionResult as? ExecutionResult.Success)?.typedValue
                    ?.let { it as? SanskritValue.Satya }?.boolean == true
            }
            if (!conditionHolds) return complete(ExecutionResult.LoopOutcome.VIJAYA)

            val bodyInvocation = loop.body as? dev.panini.vyakaranam.ast.Invocation
                ?: return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "The तावत् body must be one executable invocation.",
                    ),
                )
            val bodyText = renderInvocation(bodyInvocation)
            val invocation = registry.detectInvocation(bodyText, callerSourceFile = sourceFile)
            val iterationResults = if (invocation != null) {
                executeSamjnaInvocation(
                    invocation,
                    sessionKey,
                    scope,
                    speaker,
                    listener,
                    registry,
                    callerSourceFile = sourceFile,
                    onResult = onResult,
                )
            } else {
                listOf(vm.eval(bodyText, sessionKey, scope, speaker, listener, isExecutingScript = true)).also {
                    it.forEach { result -> onResult?.invoke(result) }
                }
            }
            results += iterationResults
            iterationCount++
            if (usesLatestResult) {
                val reportedCondition = iterationResults.asSequence()
                    .filterIsInstance<ExecutionResult.Success>()
                    .mapNotNull { it.conditionValue }
                    .lastOrNull()
                    ?: return results + ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "A फल-controlled loop body must produce a truth value.",
                    )
                latestConditionValue = reportedCondition
            }
            if (iterationResults.any {
                    it is ExecutionResult.Success && it.controlSignal == ExecutionControlSignal.BREAK_LOOP
                }
            ) {
                return complete(ExecutionResult.LoopOutcome.VIJAYA)
            }
            if (usesLatestResult && (if (isNegated) latestConditionValue else !latestConditionValue)) {
                return complete(ExecutionResult.LoopOutcome.VIJAYA)
            }
        }
        if (!isBounded) {
            results += ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Condition-controlled loop exceeded its safety limit of $MAX_CONDITION_ITERATIONS iterations.",
            )
        } else {
            val exhaustedInvocation = loop.exhausted as? dev.panini.vyakaranam.ast.Invocation
            if (exhaustedInvocation != null) {
                val exhaustedText = renderInvocation(exhaustedInvocation)
                val invocation = registry.detectInvocation(exhaustedText, callerSourceFile = sourceFile)
                val exhaustedResults = if (invocation != null) {
                    executeSamjnaInvocation(
                        invocation, sessionKey, scope, speaker, listener, registry,
                        callerSourceFile = sourceFile, onResult = onResult,
                    )
                } else {
                    listOf(vm.eval(exhaustedText, sessionKey, scope, speaker, listener, isExecutingScript = true)).also {
                        it.forEach { result -> onResult?.invoke(result) }
                    }
                }
                results += exhaustedResults
            }
            return complete(ExecutionResult.LoopOutcome.SAMAPTI)
        }
        return results
    }

    private fun renderInvocation(invocation: dev.panini.vyakaranam.ast.Invocation): String =
        renderInvocation(invocation, pipedKarman = null)

    private fun renderInvocation(
        invocation: dev.panini.vyakaranam.ast.Invocation,
        pipedKarman: String?,
    ): String = buildString {
        if (pipedKarman != null) append("$pipedKarman + अम् ")
        append(invocation.vakya.padas.joinToString(" ") { pada ->
            pada.sourceText.replace("+", " + ").replace(Regex("\\s+"), " ").trim()
        })
        append(" ।")
    }

    private companion object {
        const val MAX_CONDITION_ITERATIONS = 10_000
        const val LOOP_RESULT_NAME = "परिणाम"
    }

    fun evalProject(
        entryFile: File,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> {
        require(entryFile.exists()) { "PaniniVM entry-point file not found: ${entryFile.absolutePath}" }

        val projectDir = entryFile.parentFile ?: entryFile.absoluteFile.parentFile
            ?: error("Cannot determine project directory for ${entryFile.path}")
        val libraryFiles = projectDir.walkTopDown()
            .filter { it.isFile && it.extension == "pvm" && it.canonicalPath != entryFile.canonicalPath }
            .sortedBy { it.name }
            .toList()

        val registry = SamjnaKriyaRegistry()
        for (libraryFile in libraryFiles) {
            val parsed = PvmScript.parse(libraryFile.readText())
            val fileDomainDefn = parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().firstOrNull()
            val fileDomainStem = fileDomainDefn?.let { deriveSamjnaStem(it.scope.domain) }
            registerInheritances(registry, parsed)
            parsed.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().forEach { definition ->
                registerSamjna(
                    registry,
                    definition,
                    libraryFile.name,
                    fileDomainStem,
                    includeExecutionModifiers = false,
                )
            }
        }

        val effectiveSessionKey = sessionKey
            ?: "project-${entryFile.nameWithoutExtension}-${System.currentTimeMillis()}"
        return evalScript(
            entryFile.readText(),
            sourceFile = entryFile.name,
            sessionKey = effectiveSessionKey,
            scope = scope,
            speaker = speaker,
            listener = listener,
            samjnaRegistry = registry,
            onResult = onResult,
        )
    }

    fun evalFile(
        file: File,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> {
        require(file.exists()) { "PaniniVM script file not found: ${file.absolutePath}" }
        val projectDir = file.parentFile ?: file.absoluteFile.parentFile
        val hasSiblingPvm = projectDir?.walkTopDown()?.any {
            it.isFile && it.extension == "pvm" && it.canonicalPath != file.canonicalPath
        } == true
        return if (hasSiblingPvm) {
            evalProject(file, sessionKey, scope, speaker, listener, onResult)
        } else {
            evalScript(
                file.readText(), sessionKey = sessionKey, scope = scope, speaker = speaker,
                listener = listener, onResult = onResult,
            )
        }
    }

    fun executeSamjnaInvocation(
        invocation: SamjnaInvocation,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        callerSourceFile: String? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        val argTerms = SubantaKarakaParser.extractKarmaTerms(invocation.karmaText, invocation.ukti)

        if (invocation.kriya.isMemoized) {
            registry.getCachedResult(invocation.kriya.nameStem, invocation.karmaText)?.let {
                return listOf(it)
            }
        }

        invocation.kriya.nishedhaGuards.forEach { guard ->
            var guardText = guard.text
            argTerms.forEachIndexed { index, argument ->
                guardText = PuranaPratyayaResolver.replacePatterns(guardText, index, argument)
            }
            val isProhibited = DynamicNishedhaEvaluator.evaluateProhibition(guardText)
            val requiredType = SamjnaSignatureCompiler.inferGuardType(guardText)
            val isTypeViolated = requiredType != null &&
                argTerms.any { SamjnaValueClassifier.classifyTerm(it) != requiredType }
            if (isProhibited || isTypeViolated) {
                return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "निषेध-प्रतिषेधः: Prohibition triggered by '${guard.text.trim()}'",
                    ),
                )
            }
        }

        val repetitionCount = (invocation.ukti?.body as? Repeat)?.count
            ?: invocation.ukti?.grammaticalVakyas()?.firstOrNull()?.padas
                ?.let(FrequencyExtractor::extractAbhyasaCount)
            ?: 1
        repeat(repetitionCount) {
            val childScope = scope.copy(environment = ValueEnvironment(scope.environment.values))
            invocation.kriya.vidhiSentences.forEach { bodySentence ->
                var sentenceText = bodySentence.text
                argTerms.forEachIndexed { index, argument ->
                    sentenceText = PuranaPratyayaResolver.replacePatterns(sentenceText, index, argument)
                }
                sentenceText = SamavayaParameterResolver.replace(sentenceText, invocation.karmaText)

                val kriyaSourceFile = invocation.kriya.sourceFile ?: callerSourceFile
                val bodyInvocation = registry.detectInvocation(sentenceText, callerSourceFile = kriyaSourceFile)
                results += if (bodyInvocation != null) {
                    executeSamjnaInvocation(
                        bodyInvocation,
                        sessionKey,
                        childScope,
                        speaker,
                        listener,
                        registry,
                        callerSourceFile = kriyaSourceFile,
                        onResult = onResult,
                    )
                } else {
                    listOf(vm.eval(sentenceText, sessionKey, childScope, speaker, listener)).also {
                        it.forEach { result -> onResult?.invoke(result) }
                    }
                }
            }
            if (results.any { it is ExecutionResult.Success && it.controlSignal == ExecutionControlSignal.BREAK_LOOP }) {
                return results
            }
        }

        if (invocation.kriya.isMemoized) {
            (results.lastOrNull() as? ExecutionResult.Success)?.let {
                registry.cacheResult(invocation.kriya.nameStem, invocation.karmaText, it)
            }
        }
        return results
    }

    private fun registerSamjna(
        registry: SamjnaKriyaRegistry,
        definition: PvmScriptStatement.SamjnaDefinition,
        sourceFile: String?,
        fallbackDomainStem: String?,
        includeExecutionModifiers: Boolean = true,
    ) {
        val procedure = definition.procedure
        registry.register(
            SamjnaKriya(
                nameSegmented = procedure.name,
                nameStem = deriveSamjnaStem(procedure.name),
                body = definition.body,
                sourceFile = sourceFile,
                domainStem = procedure.domain ?: deriveDomainStem(procedure.name) ?: fallbackDomainStem,
                isApavada = procedure.modifiers.isApavada,
                isAntaranga = includeExecutionModifiers && procedure.modifiers.isAntaranga,
                isNitya = includeExecutionModifiers && procedure.modifiers.isNitya,
                isInternal = procedure.modifiers.isInternal,
            ),
        )
    }

    private fun registerInheritances(
        registry: SamjnaKriyaRegistry,
        statements: List<PvmScriptStatement>,
    ) {
        statements.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().forEach { adhikara ->
            TaddhitaInheritanceEngine.detectInheritanceAdhikara(adhikara.scope.domain)?.let {
                registry.registerInheritance(it)
            }
        }
    }

    private fun resolveNestedAttribute(
        chain: List<String>,
        structStore: Map<String, TaddhitaStruct>,
    ): ExecutionResult {
        var currentObject: TaddhitaStruct? = structStore[chain[0]]
        var resolvedValue: SanskritValue? = null
        var failedStep: String? = null
        for (index in 1 until chain.size) {
            val key = chain[index]
            if (currentObject == null) {
                failedStep = chain[index - 1]
                break
            }
            val typedAttribute = currentObject.typedAttributes[key]
            val attribute = currentObject.attributes[key]
            if (typedAttribute != null || attribute != null) {
                if (index == chain.lastIndex) {
                    resolvedValue = typedAttribute ?: SanskritValue.of(requireNotNull(attribute))
                } else {
                    currentObject = attribute?.let(structStore::get)
                }
            } else if (index == chain.lastIndex) {
                resolvedValue = SanskritValue.Lopa
            } else {
                failedStep = key
                break
            }
        }
        return if (resolvedValue != null) {
            ExecutionResult.Success(
                operation = "taddhita.nested_query",
                value = resolvedValue.toDisplayText(),
                typedValue = resolvedValue,
            )
        } else {
            ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "षष्ठी-असंगतिः: Attribute '$failedStep' not found in nested genitive chain $chain",
            )
        }
    }

    private fun deriveSamjnaStem(nameSegmented: String): String =
        requireNotNull(SamjnaHeaderIdentityParser.parse(nameSegmented)) {
            "Unable to parse saṃjñā header identity: $nameSegmented"
        }.operationStem

    private fun deriveDomainStem(nameSegmented: String): String? =
        SamjnaHeaderIdentityParser.parse(nameSegmented)?.domainStem
}
