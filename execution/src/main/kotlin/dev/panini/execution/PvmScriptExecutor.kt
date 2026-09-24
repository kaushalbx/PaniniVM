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
    private val projectLoader = PvmProjectLoader(vm.executionMetrics)
    private val sequenceExecutor = PvmSequenceExecutor()
    private val loopExecutor = PvmLoopExecutor()
    private val invocationExecutor = PvmInvocationExecutor(vm)
    private val conditionalExecutor = PvmConditionalExecutor(vm, structuredValueExecutor)
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
        if (sourceFile != null) vm.executionMetrics.recordParsedFile()
        vm.executionMetrics.recordParsedSentences(parsed.sentenceCount())

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
            val program = statement.program
            when (val semantics = statement.semantics) {
                is PvmSentenceSemantics.SchemaDeclaration -> {
                    structSchemas[semantics.schema.nameStem] = semantics.schema
                    registry.registerSchema(semantics.schema)
                }
                is PvmSentenceSemantics.StructConstruction ->
                    structStore[semantics.struct.nameStem] = semantics.struct
                is PvmSentenceSemantics.AttributePipeline -> executeStructuredPipeline(
                    semantics,
                    context,
                ).also(results::addAll)
                is PvmSentenceSemantics.AttributeAccess -> structuredValueExecutor.resolve(semantics.access, structStore).let {
                    results += it
                    context.publish(it)
                }
                is PvmSentenceSemantics.StructuredConditional ->
                    executeProgramNode(semantics.conditional, context).also(results::addAll)
                PvmSentenceSemantics.Executable -> if (program != null) {
                    executeProgramNode(program, context).also(results::addAll)
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
                    context.publish(result)
                }
            }
        }
        return results
    }

    /** The single recursive execution boundary for every parsed executable node. */
    private fun executeProgramNode(
        node: ProgramNode,
        context: ExecutionContext,
    ): List<ExecutionResult> {
        vm.executionMetrics.recordAstNode()
        return when (node) {
        is Invocation -> executeInvocationNode(node, context)
        is Sequence -> executeSequenceNode(node, context)
        is Conditional -> executeConditionalNode(node, context)
        is Repeat -> buildList {
            repeat(node.count) {
                val produced = executeProgramNode(node.body, context)
                addAll(produced)
                if (produced.hasBreakSignal()) return@buildList
            }
        }
        is WhileLoop -> executeWhileLoop(node, context)
        is Pipeline -> PurvaparaPipelineEngine.executePipeline(
            node, vm, context.sessionKey, context.scope, context.speaker, context.listener,
            context.registry, callerSourceFile = context.sourceFile,
        ).also(context::publish)
        is Quotation -> executeEvaluatorNode(node, context)
        is Prakriya -> node.body.flatMap {
            executeProgramNode(it, context)
        }
        is Scope -> node.body.flatMap {
            executeProgramNode(it, context)
        }
        }
    }

    private fun executeEvaluatorNode(
        node: ProgramNode,
        context: ExecutionContext,
    ): List<ExecutionResult> = listOf(
        vm.evalParsed(
            Ukti(
                sourceText = node.sourceText,
                body = node,
            ),
            context.sessionKey,
            context.scope,
            context.speaker,
            context.listener,
            evaluateCondition = context.conditionEvaluation,
        ),
    ).also(context::publish)

    private fun executeSequenceNode(
        node: Sequence,
        context: ExecutionContext,
    ): List<ExecutionResult> = sequenceExecutor.execute(
        node = node,
        scope = context.scope,
        registry = context.registry,
        sourceFile = context.sourceFile,
        evaluateWhole = { executeEvaluatorNode(node, context) },
        executeNode = { executeProgramNode(it, context) },
        executePipedInvocation = { invocation, scope, injected ->
            executeInvocationNode(
                invocation,
                context.copy(
                    scope = scope,
                    injectedKarman = injected,
                ),
            )
        },
    )

    private fun executeInvocationNode(
        node: Invocation,
        context: ExecutionContext,
    ): List<ExecutionResult> = invocationExecutor.execute(
        PvmInvocationExecutor.Request(
            node = node,
            sessionKey = context.sessionKey,
            scope = context.scope,
            speaker = context.speaker,
            listener = context.listener,
            registry = context.registry,
            sourceFile = context.sourceFile,
            conditionEvaluation = context.conditionEvaluation,
            injectedKarman = context.injectedKarman,
            onResult = context.onResult,
            executePrakriya = { executePrakriyaInvocation(it, context) },
        ),
    )

    private fun executeConditionalNode(
        node: Conditional,
        context: ExecutionContext,
    ): List<ExecutionResult> = conditionalExecutor.execute(
        PvmConditionalExecutor.Request(
            conditional = node,
            executeNode = { child, conditionEvaluation ->
                executeProgramNode(
                    child,
                    if (conditionEvaluation) {
                        context.copy(onResult = null, conditionEvaluation = true)
                    } else context,
                )
            },
            sessionKey = context.sessionKey,
            scope = context.scope,
            speaker = context.speaker,
            listener = context.listener,
            structStore = context.structStore,
            onResult = context.onResult,
        ),
    )

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
        val conditionEvaluation: Boolean = false,
        val injectedKarman: InjectedKarmanBinding? = null,
    ) {
        fun publish(result: ExecutionResult) {
            onResult?.invoke(result)
        }

        fun publish(results: Iterable<ExecutionResult>) {
            results.forEach(::publish)
        }
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
                file.readText(), sourceFile = file.name, sessionKey = sessionKey, scope = scope, speaker = speaker,
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
    ): List<ExecutionResult> {
        vm.executionMetrics.recordPrakriyaCall()
        return prakriyaExecutor.execute(
        PrakriyaExecutor.Request(
            invocation = invocation,
            scope = context.scope,
            registry = context.registry,
            callerSourceFile = context.sourceFile,
            executeBody = { program, scope, sourceFile ->
                executeProgramNode(
                    program,
                    context.copy(
                        scope = scope,
                        sourceFile = sourceFile,
                        structStore = mutableMapOf(),
                        structSchemas = emptyMap(),
                        injectedKarman = null,
                    ),
                )
            },
        ),
        )
    }

    private fun executeStructuredPipeline(
        pipeline: PvmSentenceSemantics.AttributePipeline,
        context: ExecutionContext,
    ): List<ExecutionResult> = structuredValueExecutor.executePipeline(
        pipeline,
        context.scope,
        context.structStore,
        executeTarget = { target, targetScope, pipedValue ->
            executeInvocationNode(
                target,
                context.copy(
                    scope = targetScope,
                    onResult = null,
                    injectedKarman = InjectedKarmanBinding(PIPE_OPERAND, pipedValue),
                ),
            )
        },
        onResult = context.onResult,
    )

    private fun List<PvmScriptStatement>.sentenceCount(): Int = sumOf { statement ->
        when (statement) {
            is PvmScriptStatement.Sentence -> 1
            is PvmScriptStatement.PrakriyaDefinition -> statement.body.size
            is PvmScriptStatement.AdhikaraDefinition, is PvmScriptStatement.RangeDefinition -> 0
        }
    }

}
