package dev.panini.execution

import dev.panini.execution.binding.baseText
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.Ukti
import java.io.File

/** Executes PVM scripts and projects behind the stable [PaniniVM] facade. */
internal class PvmScriptExecutor(private val vm: PaniniVM) {
    private val prakriyaExecutor = PrakriyaExecutor()
    private val structuredValueExecutor = StructuredValueExecutor()
    private val projectLoader = PvmProjectLoader()
    private val sequenceExecutor = PvmSequenceExecutor()
    private val loopExecutor = PvmLoopExecutor()
    fun evalScript(
        scriptContent: String,
        sourceFile: String? = null,
        sessionKey: String? = null,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        prakriyaRegistry: PrakriyaRegistry? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        val effectiveSessionKey = sessionKey ?: "script-${System.identityHashCode(scriptContent)}"
        val parsed = PvmScript.parse(scriptContent)

        val registry = prakriyaRegistry ?: PrakriyaRegistry()
        projectLoader.registerDeclarations(registry, parsed, sourceFile)

        val activeRange = parsed.filterIsInstance<PvmScriptStatement.RangeDefinition>()
            .lastOrNull()?.range
        val rangeEnvironment = activeRange?.let {
            ValueEnvironment(mapOf(ACTIVE_RANGE_NAME to it))
        } ?: ValueEnvironment()
        val effectiveScope = scope.copy(
            prakriyaRegistry = registry,
            environment = scope.environment.mergedWith(rangeEnvironment),
        )
        val structStore = mutableMapOf<String, TaddhitaStruct>()
        val structSchemas = mutableMapOf<String, TaddhitaStructSchema>()
        val context = ExecutionContext(
            effectiveSessionKey, effectiveScope, speaker, listener, registry, sourceFile,
            structStore, structSchemas, onResult,
        )

        parsed.filterIsInstance<PvmScriptStatement.Sentence>().forEach { statement ->
            val constructedStruct = TaddhitaStructEngine.detectStructConstruction(statement.text, statement.ukti)
            val declaredSchema = TaddhitaStructEngine.detectResultSchema(statement.text, statement.ukti)
            val attributeAccess = statement.ukti?.grammaticalVakyas()?.singleOrNull()
                ?.let(TaddhitaStructEngine::detectAttributeAccess)
            val attributePipeline = structuredValueExecutor.detectPipeline(statement.program)
            val program = statement.program
            val conditional = program as? dev.panini.vyakaranam.ast.Conditional

            when {
                declaredSchema != null -> {
                    structSchemas[declaredSchema.nameStem] = declaredSchema
                    registry.registerSchema(declaredSchema)
                }
                constructedStruct != null -> structStore[constructedStruct.nameStem] = constructedStruct
                attributePipeline != null -> executeStructuredPipeline(
                    attributePipeline,
                    context,
                ).also(results::addAll)
                attributeAccess != null -> structuredValueExecutor.resolve(attributeAccess, structStore).let {
                    results += it
                    onResult?.invoke(it)
                }
                conditional != null && structuredValueExecutor.containsAttributeCondition(conditional) -> {
                    val result = executeStructuredConditional(conditional, context)
                    results += result
                    onResult?.invoke(result)
                }
                program != null -> executeProgramNode(
                    program,
                    context.copy(sourceTextOverride = statement.text),
                ).also(results::addAll)
                else -> {
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
        return results
    }

    /** The single recursive execution boundary for every parsed executable node. */
    private fun executeProgramNode(
        node: ProgramNode,
        context: ExecutionContext,
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
        is WhileLoop -> executeWhileLoop(node, context)
        is Pipeline -> PurvaparaPipelineEngine.executePipeline(
            node, vm, context.sessionKey, context.scope, context.speaker, context.listener,
            context.registry, callerSourceFile = context.sourceFile,
        ).also { produced -> produced.forEach { context.onResult?.invoke(it) } }
        is Quotation -> executeEvaluatorNode(node, context)
        is Prakriya -> node.body.flatMap {
            executeProgramNode(it, context.copy(sourceTextOverride = null))
        }
        is Scope -> node.body.flatMap {
            executeProgramNode(it, context.copy(sourceTextOverride = null))
        }
    }

    private fun executeEvaluatorNode(
        node: ProgramNode,
        context: ExecutionContext,
    ): List<ExecutionResult> = listOf(
        vm.evalParsed(
            Ukti(
                sourceText = context.sourceTextOverride ?: node.sourceText,
                body = node,
            ),
            context.sessionKey,
            context.scope,
            context.speaker,
            context.listener,
            evaluateCondition = context.conditionEvaluation,
        ),
    ).also { produced -> produced.forEach { context.onResult?.invoke(it) } }

    private fun executeSequenceNode(
        node: Sequence,
        context: ExecutionContext,
    ): List<ExecutionResult> = sequenceExecutor.execute(
        node = node,
        scope = context.scope,
        registry = context.registry,
        sourceFile = context.sourceFile,
        evaluateWhole = { executeEvaluatorNode(node, context) },
        executeNode = { executeProgramNode(it, context.copy(sourceTextOverride = null)) },
        executePipedInvocation = { invocation, scope, injected ->
            executeInvocationNode(
                invocation,
                context.copy(
                    scope = scope,
                    sourceTextOverride = ProgramNodeRenderer.invocation(
                        invocation,
                        pipedKarman = injected.first,
                    ),
                    injectedKarman = injected,
                ),
            )
        },
    )

    private fun executeInvocationNode(
        node: Invocation,
        context: ExecutionContext,
    ): List<ExecutionResult> {
        val text = context.sourceTextOverride?.trim()?.let {
            it.trimEnd('।', '॥').trim() + " ।"
        } ?: ProgramNodeRenderer.invocation(node)
        val parsedUkti = Ukti(sourceText = text, body = node)
        val invocation = context.registry.detectInvocation(
            parsedUkti,
            callerSourceFile = context.sourceFile,
            injectedKarman = context.injectedKarman,
        )
        return if (invocation != null) {
            executePrakriyaInvocation(
                invocation,
                context,
            )
        } else {
            listOf(
                vm.evalParsed(
                    parsedUkti,
                    context.sessionKey,
                    context.scope,
                    context.speaker,
                    context.listener,
                    evaluateCondition = context.conditionEvaluation,
                    injectedBindings = context.injectedKarman?.let { (reference, _) ->
                        mapOf(dev.panini.core.Karaka.KARMAN to ExecutionExpression.Reference(reference))
                    }.orEmpty(),
                ),
            ).also { produced -> produced.forEach { context.onResult?.invoke(it) } }
        }
    }

    private fun executeConditionalNode(
        node: Conditional,
        context: ExecutionContext,
    ): List<ExecutionResult> {
        if (structuredValueExecutor.containsAttributeCondition(node)) {
            val result = executeStructuredConditional(node, context)
            context.onResult?.invoke(result)
            return listOf(result)
        }
        val conditionResults = executeProgramNode(
            node.condition,
            context.copy(onResult = null, sourceTextOverride = null, conditionEvaluation = true),
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
        val branchResults = branch?.let {
            executeProgramNode(it, context.copy(sourceTextOverride = null))
        }.orEmpty()
        // Preserve the grammatical condition's satya-phala for an enclosing
        // फल-controlled loop even when the selected branch prints feedback.
        return conditionResults + branchResults
    }

    private fun List<ExecutionResult>.hasBreakSignal(): Boolean = any {
        it is ExecutionResult.Success && it.controlSignal == ExecutionControlSignal.BREAK_LOOP
    }

    private data class ExecutionContext(
        val sessionKey: String,
        val scope: ExecutionScope,
        val speaker: String,
        val listener: String,
        val registry: PrakriyaRegistry,
        val sourceFile: String?,
        val structStore: MutableMap<String, TaddhitaStruct>,
        val structSchemas: Map<String, TaddhitaStructSchema>,
        val onResult: ((ExecutionResult) -> Unit)?,
        val sourceTextOverride: String? = null,
        val conditionEvaluation: Boolean = false,
        val injectedKarman: Pair<String, SanskritValue?>? = null,
    )

    private fun executeStructuredConditional(
        conditional: Conditional,
        context: ExecutionContext,
    ): ExecutionResult = structuredValueExecutor.executeConditional(
        conditional,
        context.structStore,
    ) { resolved, operands ->
        vm.evalParsed(
            Ukti(sourceText = resolved.sourceText, body = resolved),
            context.sessionKey,
            context.scope.copy(environment = context.scope.environment.mergedWith(operands)),
            context.speaker,
            context.listener,
        )
    }

    private fun executeWhileLoop(
        loop: WhileLoop,
        context: ExecutionContext,
    ): List<ExecutionResult> = loopExecutor.execute(
        PvmLoopExecutor.Request(
            loop = loop,
            scope = context.scope,
            structStore = context.structStore,
            structSchemas = context.structSchemas,
            hostBudget = vm.executionLimits.maxConditionIterations,
            executeNode = { node, scope, conditionEvaluation ->
                executeProgramNode(
                    node,
                    context.copy(
                        scope = scope,
                        sourceTextOverride = null,
                        conditionEvaluation = conditionEvaluation,
                    ),
                )
            },
            evaluateCondition = { invocation, scope ->
                vm.evalParsed(
                    Ukti(sourceText = invocation.sourceText, body = invocation),
                    context.sessionKey,
                    scope,
                    context.speaker,
                    context.listener,
                    evaluateCondition = true,
                )
            },
            resolveCondition = { structuredValueExecutor.resolveInvocation(it, context.structStore) },
            onResult = context.onResult,
        ),
    )

    private companion object {
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

        val registry = projectLoader.loadLibraryRegistry(entryFile)

        val effectiveSessionKey = sessionKey
            ?: "project-${entryFile.nameWithoutExtension}-${System.currentTimeMillis()}"
        return evalScript(
            entryFile.readText(),
            sourceFile = entryFile.name,
            sessionKey = effectiveSessionKey,
            scope = scope,
            speaker = speaker,
            listener = listener,
            prakriyaRegistry = registry,
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
        return if (projectLoader.hasSiblingSource(file)) {
            evalProject(file, sessionKey, scope, speaker, listener, onResult)
        } else {
            evalScript(
                file.readText(), sessionKey = sessionKey, scope = scope, speaker = speaker,
                listener = listener, onResult = onResult,
            )
        }
    }

    fun executePrakriyaInvocation(
        invocation: PrakriyaInvocation,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: PrakriyaRegistry,
        callerSourceFile: String? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
    ): List<ExecutionResult> = executePrakriyaInvocation(
        invocation,
        ExecutionContext(
            sessionKey = sessionKey,
            scope = scope,
            speaker = speaker,
            listener = listener,
            registry = registry,
            sourceFile = callerSourceFile,
            structStore = mutableMapOf(),
            structSchemas = emptyMap(),
            onResult = onResult,
        ),
    )

    private fun executePrakriyaInvocation(
        invocation: PrakriyaInvocation,
        context: ExecutionContext,
    ): List<ExecutionResult> = prakriyaExecutor.execute(
        PrakriyaExecutor.Request(
            invocation = invocation,
            scope = context.scope,
            registry = context.registry,
            callerSourceFile = context.sourceFile,
            executeBody = { program, scope, sourceFile, sourceText ->
                executeProgramNode(
                    program,
                    context.copy(
                        scope = scope,
                        sourceFile = sourceFile,
                        structStore = mutableMapOf(),
                        structSchemas = emptyMap(),
                        sourceTextOverride = sourceText,
                        injectedKarman = null,
                    ),
                )
            },
            evaluateFallback = { text, scope ->
                vm.eval(text, context.sessionKey, scope, context.speaker, context.listener).also {
                    context.onResult?.invoke(it)
                }
            },
        ),
    )

    private fun executeStructuredPipeline(
        pipeline: StructuredValueExecutor.AttributePipeline,
        context: ExecutionContext,
    ): List<ExecutionResult> = structuredValueExecutor.executePipeline(
        pipeline,
        context.scope,
        context.structStore,
        executeTarget = { target, targetScope, pipedValue ->
            val targetText = ProgramNodeRenderer.invocation(target)
            executeInvocationNode(
                target,
                context.copy(
                    scope = targetScope,
                    onResult = null,
                    sourceTextOverride = targetText,
                    injectedKarman = PIPE_OPERAND to pipedValue,
                ),
            )
        },
        onResult = context.onResult,
    )

}
