package dev.panini.execution

import dev.panini.execution.binding.FrequencyExtractor
import dev.panini.execution.binding.baseText
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.Procedure
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Sequence
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
        val structSchemas = mutableMapOf<String, TaddhitaStructSchema>()
        val context = ProgramExecutionContext(
            effectiveSessionKey, effectiveScope, speaker, listener, registry, sourceFile,
            structStore, structSchemas, onResult,
        )

        parsed.filterIsInstance<PvmScriptStatement.Sentence>().forEach { statement ->
            val constructedStruct = TaddhitaStructEngine.detectStructConstruction(statement.text, statement.ukti)
            val declaredSchema = TaddhitaStructEngine.detectResultSchema(statement.text, statement.ukti)
            val attributeAccess = statement.ukti?.grammaticalVakyas()?.singleOrNull()
                ?.let(TaddhitaStructEngine::detectAttributeAccess)
            val attributePipeline = detectAttributePipeline(statement.program)
            val program = statement.program
            val conditional = program as? dev.panini.vyakaranam.ast.Conditional

            when {
                declaredSchema != null -> {
                    structSchemas[declaredSchema.nameStem] = declaredSchema
                    registry.registerSchema(declaredSchema)
                }
                constructedStruct != null -> structStore[constructedStruct.nameStem] = constructedStruct
                attributePipeline != null -> executeAttributePipeline(
                    attributePipeline, structStore, effectiveSessionKey, effectiveScope,
                    speaker, listener, registry, sourceFile, onResult,
                ).also(results::addAll)
                attributeAccess != null -> resolveNestedAttribute(attributeAccess, structStore).let {
                    results += it
                    onResult?.invoke(it)
                }
                conditional != null && containsAttributeCondition(conditional) -> {
                    val result = executeAttributeConditional(
                        conditional, structStore, effectiveSessionKey, effectiveScope, speaker, listener,
                    )
                    results += result
                    onResult?.invoke(result)
                }
                program != null -> executeProgramNode(
                    program,
                    context.copy(sourceTextOverride = statement.text),
                ).also(results::addAll)
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

    /** The single recursive execution boundary for every parsed executable node. */
    private fun executeProgramNode(
        node: ProgramNode,
        context: ProgramExecutionContext,
    ): List<ExecutionResult> = when (node) {
        is Invocation -> executeInvocationNode(node, context)
        is Sequence -> executeSequenceNode(node, context)
        is Conditional -> executeConditionalNode(node, context)
        is Repeat -> buildList {
            repeat(node.count) {
                val produced = executeProgramNode(node.body, context.copy(sourceTextOverride = null))
                addAll(produced)
                if (produced.hasBreakSignal()) return@buildList
            }
        }
        is WhileLoop -> executeWhileLoop(
            node, context.sessionKey, context.scope, context.speaker, context.listener,
            context.registry, context.sourceFile, context.structStore, context.structSchemas,
            context.onResult,
        )
        is Pipeline -> PurvaparaPipelineEngine.executePipeline(
            node, vm, context.sessionKey, context.scope, context.speaker, context.listener,
            context.registry, callerSourceFile = context.sourceFile,
        ).also { produced -> produced.forEach { context.onResult?.invoke(it) } }
        is Quotation -> executeEvaluatorNode(node, context)
        is Procedure -> node.body.flatMap {
            executeProgramNode(it, context.copy(sourceTextOverride = null))
        }
        is Scope -> node.body.flatMap {
            executeProgramNode(it, context.copy(sourceTextOverride = null))
        }
    }

    private fun executeEvaluatorNode(
        node: ProgramNode,
        context: ProgramExecutionContext,
    ): List<ExecutionResult> = listOf(
        vm.eval(
            context.sourceTextOverride?.trim()?.let {
                it.trimEnd('।', '॥').trim() + " ।"
            } ?: (renderProgramSegmented(node) + " ।"),
            context.sessionKey,
            context.scope,
            context.speaker,
            context.listener,
            isExecutingScript = true,
        ),
    ).also { produced -> produced.forEach { context.onResult?.invoke(it) } }

    private fun executeSequenceNode(
        node: Sequence,
        context: ProgramExecutionContext,
    ): List<ExecutionResult> {
        val hasNamedStage = node.statements.drop(1).any { stage ->
            stage is Invocation && context.registry.detectInvocation(
                renderInvocation(stage, pipedKarman = PIPE_OPERAND),
                callerSourceFile = context.sourceFile,
            ) != null
        }
        if (node.statements.size < 2 || node.connectors.any { it != "ततः" } || !hasNamedStage) {
            return executeEvaluatorNode(node, context)
        }
        val results = mutableListOf<ExecutionResult>()
        var stageResults = executeProgramNode(
            node.statements.first(),
            context.copy(sourceTextOverride = null),
        )
        results += stageResults
        var pipedValue = stageResults.filterIsInstance<ExecutionResult.Success>()
            .lastOrNull()?.typedValue
        for (stage in node.statements.drop(1)) {
            if (stageResults.any { it is ExecutionResult.Failure }) break
            val invocation = stage as? Invocation
            stageResults = if (invocation != null && pipedValue != null) {
                val operand = PIPE_OPERAND
                val stageScope = context.scope.copy(
                    environment = context.scope.environment.mergedWith(
                        ValueEnvironment(mapOf(operand to pipedValue)),
                    ),
                )
                executeInvocationNode(
                    invocation,
                    context.copy(
                        scope = stageScope,
                        sourceTextOverride = renderInvocation(invocation, pipedKarman = operand),
                    ),
                )
            } else {
                executeProgramNode(stage, context.copy(sourceTextOverride = null))
            }
            results += stageResults
            pipedValue = stageResults.filterIsInstance<ExecutionResult.Success>()
                .lastOrNull()?.typedValue ?: pipedValue
        }
        return results
    }

    private fun executeInvocationNode(
        node: Invocation,
        context: ProgramExecutionContext,
    ): List<ExecutionResult> {
        val text = context.sourceTextOverride?.trim()?.let {
            it.trimEnd('।', '॥').trim() + " ।"
        } ?: renderInvocation(node)
        val invocation = context.registry.detectInvocation(text, callerSourceFile = context.sourceFile)
        return if (invocation != null) {
            executeSamjnaInvocation(
                invocation, context.sessionKey, context.scope, context.speaker, context.listener,
                context.registry, callerSourceFile = context.sourceFile, onResult = context.onResult,
            )
        } else {
            listOf(
                vm.eval(
                    text, context.sessionKey, context.scope, context.speaker, context.listener,
                    isExecutingScript = true,
                ),
            ).also { produced -> produced.forEach { context.onResult?.invoke(it) } }
        }
    }

    private fun executeConditionalNode(
        node: Conditional,
        context: ProgramExecutionContext,
    ): List<ExecutionResult> {
        if (containsAttributeCondition(node)) {
            val result = executeAttributeConditional(
                node, context.structStore, context.sessionKey, context.scope,
                context.speaker, context.listener,
            )
            context.onResult?.invoke(result)
            return listOf(result)
        }
        val conditionResults = executeProgramNode(
            node.condition,
            context.copy(onResult = null, sourceTextOverride = null),
        )
        val success = conditionResults.filterIsInstance<ExecutionResult.Success>().lastOrNull()
        val condition = success?.conditionValue ?: (success?.typedValue as? SanskritValue.Satya)?.boolean
        if (condition == null) {
            return conditionResults + ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "A conditional expression must produce a truth value.",
            )
        }
        val branch = if (condition) node.consequent else node.alternate
        return branch?.let {
            executeProgramNode(it, context.copy(sourceTextOverride = null))
        }.orEmpty()
    }

    private fun List<ExecutionResult>.hasBreakSignal(): Boolean = any {
        it is ExecutionResult.Success && it.controlSignal == ExecutionControlSignal.BREAK_LOOP
    }

    private data class ProgramExecutionContext(
        val sessionKey: String,
        val scope: ExecutionScope,
        val speaker: String,
        val listener: String,
        val registry: SamjnaKriyaRegistry,
        val sourceFile: String?,
        val structStore: MutableMap<String, TaddhitaStruct>,
        val structSchemas: Map<String, TaddhitaStructSchema>,
        val onResult: ((ExecutionResult) -> Unit)?,
        val sourceTextOverride: String? = null,
    )

    private fun containsAttributeCondition(
        conditional: dev.panini.vyakaranam.ast.Conditional,
    ): Boolean = ((conditional.condition as? dev.panini.vyakaranam.ast.Invocation)?.vakya
        ?.let(TaddhitaStructEngine::detectAttributeReference) != null) ||
        (conditional.alternate as? dev.panini.vyakaranam.ast.Conditional)
            ?.let(::containsAttributeCondition) == true

    private fun executeAttributeConditional(
        conditional: dev.panini.vyakaranam.ast.Conditional,
        structStore: Map<String, TaddhitaStruct>,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
    ): ExecutionResult {
        val operandValues = linkedMapOf<String, SanskritValue>()
        val resolved = resolveAttributeConditions(conditional, structStore, operandValues)
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "A structured attribute used by the condition could not be resolved.",
            )
        return vm.eval(
            renderConditionalSegmented(resolved) + " ।",
            sessionKey,
            scope.copy(environment = scope.environment.mergedWith(ValueEnvironment(operandValues))),
            speaker,
            listener,
            isExecutingScript = true,
        )
    }

    private fun resolveAttributeConditions(
        conditional: dev.panini.vyakaranam.ast.Conditional,
        structStore: Map<String, TaddhitaStruct>,
        operandValues: MutableMap<String, SanskritValue>,
    ): dev.panini.vyakaranam.ast.Conditional? {
        val conditionInvocation = conditional.condition as? dev.panini.vyakaranam.ast.Invocation ?: return null
        val condition = replaceAttributeReference(conditionInvocation, structStore, operandValues) ?: return null
        val alternate = conditional.alternate?.let { node ->
            if (node is dev.panini.vyakaranam.ast.Conditional) {
                resolveAttributeConditions(node, structStore, operandValues) ?: return null
            } else {
                node
            }
        }
        return conditional.copy(condition = condition, alternate = alternate)
    }

    private fun replaceAttributeReference(
        invocation: dev.panini.vyakaranam.ast.Invocation,
        structStore: Map<String, TaddhitaStruct>,
        operandValues: MutableMap<String, SanskritValue> = linkedMapOf(),
    ): dev.panini.vyakaranam.ast.Invocation? {
        var current = invocation
        while (true) {
            val reference = TaddhitaStructEngine.detectAttributeReference(current.vakya)
                ?: return current
            val resolved = resolveNestedAttribute(reference.access, structStore) as? ExecutionResult.Success
                ?: return null
            val operandName = typedOperandName(operandValues.size)
            operandValues[operandName] = requireNotNull(resolved.typedValue)
            val sup = reference.access.resultAffix.upadesha
            val replacement = dev.panini.vyakaranam.ast.SubantaPada(
                sourceText = "$operandName+$sup",
                pratipadika = dev.panini.vyakaranam.ast.MulaPratipadika(operandName, operandName),
                sup = dev.panini.vyakaranam.ast.SupPratyaya(sup, sup),
            )
            val padas = current.vakya.padas.toMutableList().apply {
                subList(reference.padaRange.first, reference.padaRange.last + 1).clear()
                add(reference.padaRange.first, replacement)
            }
            val vakya = current.vakya as? dev.panini.vyakaranam.ast.AkhyataVakya ?: return null
            current = current.copy(vakya = vakya.copy(padas = padas))
        }
    }

    private fun typedOperandName(index: Int): String =
        "विशेषणफल" + dev.panini.sankhya.SankhyaGenerator().cardinal(index.toLong() + 1L).final.surface

    private fun renderConditionalSegmented(
        conditional: dev.panini.vyakaranam.ast.Conditional,
        includePipelineTarget: Boolean = true,
    ): String = buildString {
        val hasSharedTarget = includePipelineTarget && conditional.surfacePipelineTarget != null
        val stripLoweredTargets = hasSharedTarget || !includePipelineTarget
        append("यदि ")
        append(renderProgramSegmented(conditional.condition))
        append(" तर्हि ")
        append(renderConditionalBranch(conditional.consequent, stripLoweredTargets))
        conditional.alternate?.let {
            append(" अन्यथा ")
            append(renderConditionalBranch(it, stripLoweredTargets))
        }
        if (hasSharedTarget) {
            append(" ततः ")
            append(renderProgramSegmented(requireNotNull(conditional.surfacePipelineTarget)))
        }
    }

    private fun renderConditionalBranch(
        node: dev.panini.vyakaranam.ast.ProgramNode,
        stripPipelineTarget: Boolean,
    ): String = when {
        !stripPipelineTarget -> renderProgramSegmented(node)
        node is dev.panini.vyakaranam.ast.Conditional -> renderConditionalSegmented(node, false)
        node is dev.panini.vyakaranam.ast.Sequence && node.connectors.lastOrNull() == "ततः" ->
            renderProgramSegmented(node.statements.first())
        else -> renderProgramSegmented(node)
    }

    private fun renderProgramSegmented(node: dev.panini.vyakaranam.ast.ProgramNode): String = when (node) {
        is dev.panini.vyakaranam.ast.Invocation -> node.implicitValue
            ?: node.vakya.padas.joinToString(" ") { it.sourceText }
        is dev.panini.vyakaranam.ast.Sequence -> node.statements.mapIndexed { index, statement ->
            val connector = if (index == 0) "" else "${node.connectors.getOrNull(index - 1) ?: "।"} "
            connector + renderProgramSegmented(statement)
        }.joinToString(" ")
        is dev.panini.vyakaranam.ast.Conditional -> renderConditionalSegmented(node)
        else -> node.sourceText
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
        structSchemas: Map<String, TaddhitaStructSchema>,
        onResult: ((ExecutionResult) -> Unit)?,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        val grammaticalBound = if (loop.maximumIterationStems.isEmpty()) {
            null
        } else {
            val value = dev.panini.sankhya.SankhyaEvaluator()
                .evaluateStems(loop.maximumIterationStems).value
            if (value < 1L) {
                return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "A condition-controlled loop bound must be positive.",
                    ),
                )
            }
            value
        }
        val hostBudget = vm.executionLimits.maxConditionIterations
        val usesLatestResult = loop.condition.vakya.padas.any { pada ->
            pada is dev.panini.vyakaranam.ast.SubantaPada && pada.pratipadika.baseText() == "फल"
        }
        val isNegated = loop.condition.vakya.padas.any {
            it is dev.panini.vyakaranam.ast.AvyayaPada && it.form == "न"
        }
        var latestConditionValue = false
        var iterationCount = 0L

        fun complete(outcome: ExecutionResult.LoopOutcome): List<ExecutionResult> {
            val outcomeValue = SanskritValue.Shabda(outcome.sanskritName)
            val attemptWord = dev.panini.sankhya.SankhyaGenerator()
                .cardinal(iterationCount).final.surface
            val attributes = mapOf(
                "अवस्था" to outcome.sanskritName,
                "प्रयत्नसङ्ख्या" to attemptWord,
            )
            val schema = structSchemas[LOOP_RESULT_NAME]
            if (schema != null && schema.fields.toSet() != attributes.keys) {
                return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "The परिणाम schema requires ${schema.fields}, but the loop produced ${attributes.keys}.",
                )
            }
            structStore[LOOP_RESULT_NAME] = TaddhitaStruct(
                nameStem = LOOP_RESULT_NAME,
                attributes = attributes,
                typedAttributes = mapOf(
                    "अवस्था" to outcomeValue,
                    "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(iterationCount, attemptWord),
                ),
            )
            val structuredOutcome = SanskritValue.Rupa(
                schema = LOOP_RESULT_NAME,
                fields = mapOf(
                    "अवस्था" to outcomeValue,
                    "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(iterationCount, attemptWord),
                ),
            )
            val completion = ExecutionResult.Success(
                value = outcome.sanskritName,
                operation = "pvm.while",
                typedValue = structuredOutcome,
                loopOutcome = outcome,
                iterationCount = iterationCount,
            )
            results += completion
            onResult?.invoke(completion)

            val target = loop.resultTarget ?: return results
            val targetScope = scope.copy(
                environment = scope.environment.mergedWith(
                    ValueEnvironment(
                        mapOf(
                            "फल" to outcomeValue,
                            "परिणाम" to outcomeValue,
                            "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(iterationCount, iterationCount.toString()),
                        ),
                    ),
                ),
            )
            val targetResults = executeProgramNode(
                target,
                ProgramExecutionContext(
                    sessionKey, targetScope, speaker, listener, registry, sourceFile,
                    structStore, structSchemas, onResult,
                ),
            )
            results += targetResults
            return results
        }

        while (grammaticalBound == null || iterationCount < grammaticalBound) {
            if (hostBudget != null && iterationCount >= hostBudget) {
                return results + ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "Condition-controlled loop exhausted its host execution budget of $hostBudget iterations.",
                )
            }
            val conditionHolds = if (usesLatestResult) {
                if (isNegated) !latestConditionValue else latestConditionValue
            } else {
                val operandValues = linkedMapOf<String, SanskritValue>()
                val resolvedCondition = replaceAttributeReference(loop.condition, structStore, operandValues)
                    ?: return results + ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "A structured attribute used by the loop condition could not be resolved.",
                    )
                val conditionResult = vm.eval(
                    renderInvocation(resolvedCondition),
                    sessionKey,
                    scope.copy(environment = scope.environment.mergedWith(ValueEnvironment(operandValues))),
                    speaker,
                    listener,
                    isExecutingScript = true,
                )
                val success = conditionResult as? ExecutionResult.Success
                (success?.conditionValue ?: (success?.typedValue as? SanskritValue.Satya)?.boolean) == true
            }
            if (!conditionHolds) return complete(ExecutionResult.LoopOutcome.VIJAYA)

            val iterationResults = executeProgramNode(
                loop.body,
                ProgramExecutionContext(
                    sessionKey, scope, speaker, listener, registry, sourceFile,
                    structStore, structSchemas, onResult,
                ),
            )
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
        val exhausted = loop.exhausted
        if (exhausted != null) {
            val exhaustedResults = executeProgramNode(
                exhausted,
                ProgramExecutionContext(
                    sessionKey, scope, speaker, listener, registry, sourceFile,
                    structStore, structSchemas, onResult,
                ),
            )
            results += exhaustedResults
        }
        return complete(ExecutionResult.LoopOutcome.SAMAPTI)
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
        const val LOOP_RESULT_NAME = "परिणाम"
        const val PIPE_OPERAND = "विशेषणफल"
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
        val signature = invocation.kriya.signature
        val argumentResolution = NamedSamjnaArgumentResolver.resolve(invocation.karmaText, signature)
        if (argumentResolution is SamjnaArgumentResolution.Failure) {
            return listOf(
                ExecutionResult.Failure(ExecutionError.INVALID_VALUE, argumentResolution.message),
            )
        }
        val argTerms = (argumentResolution as SamjnaArgumentResolution.Success).terms

        if (signature.parameters.isNotEmpty() && signature.parameters.size != argTerms.size) {
            return listOf(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-मानसङ्ख्या: '${invocation.kriya.nameStem}' expects ${signature.parameters.size} arguments, but received ${argTerms.size}.",
                ),
            )
        }
        signature.parameters.zip(argTerms).withIndex().firstOrNull { (index, pair) ->
            val (parameter, argument) = pair
            val actual = invocation.argumentValues.getOrNull(index)?.let(SamjnaValueClassifier::classifyValue)
                ?: scope.environment.values[argument.substringBefore('+').trim()]
                    ?.let(SamjnaValueClassifier::classifyValue)
                ?: SamjnaValueClassifier.classifyTerm(argument)
            actual != parameter.type
        }?.let { (_, pair) ->
            val parameter = pair.first
            return listOf(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-मानप्रकारः: '${parameter.nameStem}' requires ${parameter.type}.",
                ),
            )
        }

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
                signature.parameters.zip(argTerms).forEach { (parameter, argument) ->
                    sentenceText = NamedSamjnaParameterResolver.replace(
                        sentenceText,
                        parameter.nameStem,
                        argument,
                    )
                }
                sentenceText = SamavayaParameterResolver.replace(sentenceText, invocation.karmaText)

                val kriyaSourceFile = invocation.kriya.sourceFile ?: callerSourceFile
                val parsedProgram = PvmScript.parse(sentenceText)
                    .filterIsInstance<PvmScriptStatement.Sentence>()
                    .singleOrNull()?.program
                results += if (parsedProgram != null) {
                    executeProgramNode(
                        parsedProgram,
                        ProgramExecutionContext(
                            sessionKey, childScope, speaker, listener, registry, kriyaSourceFile,
                            mutableMapOf(), emptyMap(), onResult, sentenceText,
                        ),
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
        signature.resultType?.let { expected ->
            val finalResult = results.lastOrNull() as? ExecutionResult.Success
                ?: return results
            val typedValue = finalResult.typedValue ?: SanskritValue.of(finalResult.value)
            val actual = SamjnaValueClassifier.classifyValue(typedValue)
            if (actual != expected) {
                return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-परिणामप्रकारः: '${invocation.kriya.nameStem}' declared $expected but returned $actual.",
                )
            }
        }
        signature.resultSchema?.let { expectedSchema ->
            val declaredSchema = registry.resolveSchema(expectedSchema)
                ?: return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-परिणामरूपम्: No schema named '$expectedSchema' is declared.",
                )
            val finalResult = results.lastOrNull() as? ExecutionResult.Success ?: return results
            val structured = finalResult.typedValue as? SanskritValue.Rupa
                ?: return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-परिणामरूपम्: '${invocation.kriya.nameStem}' must return '$expectedSchema'.",
                )
            if (structured.schema != expectedSchema || structured.fields.keys != declaredSchema.fields.toSet()) {
                return results + ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-परिणामरूपम्: '$expectedSchema' requires ${declaredSchema.fields}, but returned ${structured.fields.keys}.",
                )
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
        access: TaddhitaAttributeAccess,
        structStore: Map<String, TaddhitaStruct>,
        inflectResult: Boolean = false,
    ): ExecutionResult {
        val chain = access.chain
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
            if (inflectResult) {
                resolvedValue = inflectAttributeValue(resolvedValue, access.resultAffix)
            }
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

    private data class AttributePipeline(
        val access: TaddhitaAttributeAccess,
        val targets: List<dev.panini.vyakaranam.ast.Invocation>,
    )

    private fun detectAttributePipeline(program: dev.panini.vyakaranam.ast.ProgramNode?): AttributePipeline? {
        val sequence = program as? dev.panini.vyakaranam.ast.Sequence ?: return null
        if (sequence.statements.size < 2 || sequence.connectors.any { it != "ततः" }) return null
        val source = sequence.statements.first() as? dev.panini.vyakaranam.ast.Invocation ?: return null
        val targets = sequence.statements.drop(1).map {
            it as? dev.panini.vyakaranam.ast.Invocation ?: return null
        }
        val access = TaddhitaStructEngine.detectAttributeAccess(source.vakya) ?: return null
        return AttributePipeline(access, targets)
    }

    private fun executeAttributePipeline(
        pipeline: AttributePipeline,
        structStore: Map<String, TaddhitaStruct>,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        sourceFile: String?,
        onResult: ((ExecutionResult) -> Unit)?,
    ): List<ExecutionResult> {
        val source = resolveNestedAttribute(pipeline.access, structStore, inflectResult = true)
        if (source !is ExecutionResult.Success) return listOf(source)
        var pipedValue = source.typedValue ?: return listOf(source)
        val results = mutableListOf<ExecutionResult>(source)
        for (target in pipeline.targets) {
            val targetScope = scope.copy(
                environment = scope.environment.mergedWith(
                    ValueEnvironment(mapOf(PIPE_OPERAND to pipedValue)),
                ),
            )
            val targetText = renderInvocation(target, pipedKarman = PIPE_OPERAND)
            val invocation = registry.detectInvocation(targetText, callerSourceFile = sourceFile)
            val executedResults = if (invocation != null) {
                executeSamjnaInvocation(
                    invocation, sessionKey, targetScope, speaker, listener, registry,
                    callerSourceFile = sourceFile, onResult = null,
                )
            } else {
                listOf(vm.eval(targetText, sessionKey, targetScope, speaker, listener, isExecutingScript = true))
            }
            val targetResults = executedResults.map { result ->
                if (result is ExecutionResult.Success && result.outputKind == OutputKind.CONSOLE) {
                    result.copy(typedValue = pipedValue)
                } else {
                    result
                }
            }
            results += targetResults
            targetResults.forEach { onResult?.invoke(it) }
            if (targetResults.any { it !is ExecutionResult.Success }) break
            pipedValue = targetResults.filterIsInstance<ExecutionResult.Success>()
                .lastOrNull()?.typedValue ?: pipedValue
        }
        return results
    }

    private fun inflectAttributeValue(
        value: SanskritValue,
        affix: dev.panini.core.SupAffix,
    ): SanskritValue {
        return when (value) {
            is SanskritValue.Sankhya -> inflectNumeral(value, affix)
            is SanskritValue.Shabda -> value.copy(text = deriveSubantaSurface(value.text, affix))
            is SanskritValue.Satya -> value.copy(
                surface = deriveSubantaSurface(if (value.boolean) "सत्य" else "असत्य", affix),
            )
            else -> value
        }
    }

    private fun inflectNumeral(
        number: SanskritValue.Sankhya,
        affix: dev.panini.core.SupAffix,
    ): SanskritValue.Sankhya {
        val surface = dev.panini.sankhya.SankhyaGenerator().decline(
            number.value, affix.vibhakti, affix.vacana,
        )
        return number.copy(word = surface)
    }

    private fun deriveSubantaSurface(
        stem: String,
        affix: dev.panini.core.SupAffix,
    ): String = runCatching {
        dev.panini.derivation.SubantaEngine().derive(
            dev.panini.derivation.SubantaDerivationRequest(stem, affix.vibhakti, affix.vacana),
        ).final.surface
    }.getOrDefault(stem)

    private fun deriveSamjnaStem(nameSegmented: String): String =
        requireNotNull(SamjnaHeaderIdentityParser.parse(nameSegmented)) {
            "Unable to parse saṃjñā header identity: $nameSegmented"
        }.operationStem

    private fun deriveDomainStem(nameSegmented: String): String? =
        SamjnaHeaderIdentityParser.parse(nameSegmented)?.domainStem
}
