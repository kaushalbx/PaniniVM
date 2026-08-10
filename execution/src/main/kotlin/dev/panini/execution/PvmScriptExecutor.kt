package dev.panini.execution

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

        val effectiveScope = scope.copy(samjnaRegistry = registry)
        val structStore = mutableMapOf<String, TaddhitaStruct>()

        parsed.filterIsInstance<PvmScriptStatement.Sentence>().forEach { statement ->
            val constructedStruct = TaddhitaStructEngine.detectStructConstruction(statement.text, statement.ukti)
            val nestedAttributeAccess = TaddhitaStructEngine.detectNestedAttributeAccess(statement.text, statement.ukti)
            val pipeline = statement.program as? dev.panini.vyakaranam.ast.Pipeline

            when {
                constructedStruct != null -> structStore[constructedStruct.nameStem] = constructedStruct
                nestedAttributeAccess != null -> results += resolveNestedAttribute(nestedAttributeAccess, structStore)
                pipeline != null -> results += PurvaparaPipelineEngine.executePipeline(
                    pipeline,
                    vm,
                    effectiveSessionKey,
                    effectiveScope,
                    speaker,
                    listener,
                    registry,
                    callerSourceFile = sourceFile,
                )
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
                        )
                        results += if (invocationResults.any { it is ExecutionResult.Success }) {
                            invocationResults.filterIsInstance<ExecutionResult.Success>()
                        } else {
                            invocationResults
                        }
                    } else {
                        results += vm.eval(
                            statement.text,
                            effectiveSessionKey,
                            effectiveScope,
                            speaker,
                            listener,
                            isExecutingScript = true,
                        )
                    }
                }
            }
        }
        return results
    }

    fun evalProject(
        entryFile: File,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
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
        )
    }

    fun evalFile(
        file: File,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
    ): List<ExecutionResult> {
        require(file.exists()) { "PaniniVM script file not found: ${file.absolutePath}" }
        val projectDir = file.parentFile ?: file.absoluteFile.parentFile
        val hasSiblingPvm = projectDir?.walkTopDown()?.any {
            it.isFile && it.extension == "pvm" && it.canonicalPath != file.canonicalPath
        } == true
        return if (hasSiblingPvm) {
            evalProject(file, sessionKey, scope, speaker, listener)
        } else {
            evalScript(file.readText(), sessionKey = sessionKey, scope = scope, speaker = speaker, listener = listener)
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
                )
            } else {
                listOf(vm.eval(sentenceText, sessionKey, childScope, speaker, listener))
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
            val attribute = currentObject.attributes[key]
            if (attribute != null) {
                if (index == chain.lastIndex) resolvedValue = SanskritValue.of(attribute)
                else currentObject = structStore[attribute]
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
