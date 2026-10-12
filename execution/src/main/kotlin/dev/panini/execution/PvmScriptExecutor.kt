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
import dev.panini.vyakaranam.ast.ForEach
import dev.panini.execution.binding.referenceKey
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
    /** Execute caller-owned control-flow AST without rendering or reparsing it. */
    fun evalProgram(program: ProgramNode, sessionKey: String?, scope: ExecutionScope,
        speaker: String, listener: String, persistSession: Boolean): List<ExecutionResult> {
        val registry = scope.prakriyaRegistry ?: PrakriyaRegistry()
        return executeProgramNode(program, ExecutionContext(
            sessionKey ?: "ast-${System.identityHashCode(program)}", scope.copy(prakriyaRegistry = registry),
            speaker, listener, registry, null, mutableMapOf(), mutableMapOf(), null, persistSession))
    }

    fun evalScript(
        scriptContent: String,
        sourceFile: String? = null,
        sessionKey: String? = null,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        prakriyaRegistry: PrakriyaRegistry? = null,
        onResult: ((ExecutionResult) -> Unit)? = null,
        persistSession: Boolean = sessionKey != null,
        parsedStatements: List<PvmScriptStatement>? = null,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        val effectiveSessionKey = sessionKey ?: "script-${System.identityHashCode(scriptContent)}"
        val parsed = parsedStatements ?: PvmScript.parse(scriptContent)
        if (sourceFile != null) vm.executionMetrics.recordParsedFile()
        vm.executionMetrics.recordParsedSentences(parsed.sentenceCount())

        val registry = prakriyaRegistry ?: PrakriyaRegistry()
        projectLoader.registerDeclarations(registry, parsed, sourceFile)

        val effectiveScope = scope.copy(
            prakriyaRegistry = registry,
        )
        val structStore = mutableMapOf<String, TaddhitaStruct>()
        val structSchemas = mutableMapOf<String, TaddhitaStructSchema>()
        var context = ExecutionContext(
            effectiveSessionKey, effectiveScope, speaker, listener, registry, sourceFile,
            structStore, structSchemas, onResult, persistSession,
        )

        for (item in parsed) {
            if (item is PvmScriptStatement.RangeDefinition) {
                context = context.copy(scope = context.scope.copy(environment =
                    context.scope.environment.with(ACTIVE_RANGE_NAME, item.range)))
                continue
            }
            val statement = item as? PvmScriptStatement.Sentence ?: continue
            val program = statement.program
            when (val semantics = statement.semantics) {
                is PvmSentenceSemantics.SchemaDeclaration -> {
                    registry.registerSchema(semantics.schema)
                    structSchemas[semantics.schema.nameStem] = semantics.schema
                }
                is PvmSentenceSemantics.StructFieldAssertion -> {
                    val assertion = semantics.assertion
                    val existing = structStore[assertion.ownerStem]
                    structStore[assertion.ownerStem] = TaddhitaStruct(
                        nameStem = assertion.ownerStem,
                        attributes = existing?.attributes.orEmpty() +
                            (assertion.fieldStem to assertion.valueStem),
                        typedAttributes = existing?.typedAttributes.orEmpty() +
                            (assertion.fieldStem to TaddhitaStructEngine.assertionValue(assertion)),
                    )
                }
                is PvmSentenceSemantics.AttributePipeline -> executeStructuredPipeline(
                    semantics,
                    context,
                ).also(results::addAll)
                is PvmSentenceSemantics.AttributeAccess -> structuredValueExecutor.resolve(semantics.access, structStore).let {
                    if (it is ExecutionResult.Success) it.typedValue?.let { value ->
                        vm.retainStructuredResult(effectiveSessionKey, value, speaker, listener, persistSession)
                    }
                    results += it
                    context.publish(it)
                }
                is PvmSentenceSemantics.StructuredConditional ->
                    executeProgramNode(semantics.conditional, context).also(results::addAll)
                PvmSentenceSemantics.Executable -> if (program != null) {
                    executeProgramNode(program, context).also(results::addAll)
                } else {
                    val result = vm.evalScriptUtterance(
                        statement.text,
                        effectiveSessionKey,
                        effectiveScope,
                        speaker,
                        listener,
                        persistSession,
                    )
                    results += result
                    context.publish(result)
                }
            }
            val last = results.lastOrNull()
            if (last != null && last !is ExecutionResult.Success) break
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
        is Invocation -> {
            TaddhitaStructEngine.detectAttributeAccess(node.vakya)?.let { access ->
                val result = structuredValueExecutor.resolve(access, context.structStore)
                if (result is ExecutionResult.Success) result.typedValue?.let { value ->
                    vm.retainStructuredResult(context.sessionKey, value, context.speaker,
                        context.listener, context.persistSession)
                }
                context.publish(result)
                return listOf(result)
            }
            node.implicitValuePada?.let { nominal ->
                val bound = try { LocalAnaphorBinding.resolve(node, context.scope) }
                catch (failure: IllegalArgumentException) {
                    return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                        failure.message ?: "Unresolved nominal pronoun.")).also(context::publish)
                }
                val value = NominalResultSemantics.operand(bound.invocation.implicitValuePada ?: nominal).resolve { name ->
                    bound.scope.environment.values[name] ?: vm.runtimeValue(context.sessionKey, name)
                }
                vm.retainStructuredResult(context.sessionKey, value, context.speaker, context.listener, context.persistSession)
                return listOf(ExecutionResult.Success(value.toDisplayText(), "pvm.nominal-result",
                    typedValue = value, conditionValue = (value as? SanskritValue.Satya)?.boolean)).also(context::publish)
            }
            val expanded = try {
                ListDeclarationLowering.expand(node) ?: OrdinalObjectLowering.expand(node) ?: PriorActionLowering.expand(node)
            } catch (error: IllegalArgumentException) {
                // Binding owns the public typed diagnostic for invalid morphology.
                return executeEvaluatorNode(node, context)
            }
            expanded?.let { executeSequenceNode(it, context) } ?: executeInvocationNode(node, context)
        }
        is Sequence -> executeSequenceNode(node, context)
        is Conditional -> executeConditionalNode(node, context)
        is Repeat -> buildList {
            repeat(node.count) {
                val produced = executeProgramNode(node.body, context.copy(onResult = { context.publish(it.consumeLoopBreak()) }))
                addAll(produced.map(ExecutionResult::consumeLoopBreak))
                if (produced.hasControlSignal() || produced.any { it !is ExecutionResult.Success }) return@buildList
            }
        }
        is WhileLoop -> executeWhileLoop(node, context)
        is ForEach -> executeForEach(node, context)
        is Pipeline -> PurvaparaPipelineEngine.executePipeline(
            node, vm, context.sessionKey, context.scope, context.speaker, context.listener,
            context.registry, callerSourceFile = context.sourceFile,
            persistSession = context.persistSession,
        ).also(context::publish)
        is Quotation -> {
            val expanded = try { ListDeclarationLowering.expand(node) }
                catch (_: IllegalArgumentException) { return executeEvaluatorNode(node, context) }
            expanded?.let { executeSequenceNode(it, context) } ?: executeEvaluatorNode(node, context)
        }
        is Prakriya -> executeBody(node.body, context)
        is Scope -> executeBody(node.body, context)
        }
    }

    private fun executeBody(body: List<ProgramNode>, context: ExecutionContext): List<ExecutionResult> = buildList {
        for (statement in body) {
            val produced = executeProgramNode(statement, context)
            addAll(produced)
            if (produced.any { it !is ExecutionResult.Success || it.controlSignal != null }) break
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
            persistSession = context.persistSession,
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
    ): List<ExecutionResult> {
        val operations = context.registry.all().mapTo(mutableSetOf()) { it.nameStem }
        val bound = try {
            LocalAnaphorBinding.resolve(node, context.scope, operations)
        } catch (failure: IllegalArgumentException) {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                failure.message ?: "Unresolved pronoun.")).also(context::publish)
        }
        val resolved = structuredValueExecutor.resolveInvocation(bound.invocation, context.structStore,
            operations)
            ?: return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "A structured field operand could not be resolved.")).also(context::publish)
        val operandContext = context.copy(scope = bound.scope.copy(environment =
            bound.scope.environment.mergedWith(resolved.environment)))
        return invocationExecutor.execute(
        PvmInvocationExecutor.Request(
            node = resolved.invocation,
            sessionKey = context.sessionKey,
            scope = operandContext.scope,
            speaker = context.speaker,
            listener = context.listener,
            registry = context.registry,
            sourceFile = context.sourceFile,
            conditionEvaluation = context.conditionEvaluation,
            persistSession = context.persistSession,
            injectedKarman = context.injectedKarman,
            onResult = context.onResult,
            executePrakriya = { executePrakriyaInvocation(it, operandContext) },
        ),
    )
    }

    private fun executeConditionalNode(
        node: Conditional,
        context: ExecutionContext,
    ): List<ExecutionResult> {
        val condition = node.condition as? Invocation
        val bound = try {
            condition?.let { LocalAnaphorBinding.resolve(it, context.scope,
                context.registry.all().mapTo(mutableSetOf()) { operation -> operation.nameStem }) }
        } catch (failure: IllegalArgumentException) {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                failure.message ?: "Unresolved pronoun.")).also(context::publish)
        }
        val conditionContext = bound?.let { context.copy(scope = it.scope) } ?: context
        return conditionalExecutor.execute(
        PvmConditionalExecutor.Request(
            conditional = bound?.let { node.copy(condition = it.invocation) } ?: node,
            executeNode = { child, conditionEvaluation ->
                executeProgramNode(
                    child,
                    if (conditionEvaluation) {
                        conditionContext.copy(onResult = null, conditionEvaluation = true)
                    } else context,
                )
            },
            sessionKey = context.sessionKey,
            scope = conditionContext.scope,
            speaker = context.speaker,
            listener = context.listener,
            persistSession = context.persistSession,
            structStore = context.structStore,
            resolveValue = { vm.runtimeValue(context.sessionKey, it) },
            onResult = context.onResult,
        ),
    )
    }

    private fun List<ExecutionResult>.hasControlSignal(): Boolean = any {
        it is ExecutionResult.Success && it.controlSignal != null
    }

    private fun executeForEach(loop: ForEach, context: ExecutionContext): List<ExecutionResult> {
        val whole = Invocation(dev.panini.vyakaranam.ast.NamaVakya(loop.collection.sourceText, listOf(loop.collection)))
        val bound = try { LocalAnaphorBinding.resolve(whole, context.scope) }
        catch (failure: IllegalArgumentException) {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                failure.message ?: "Unresolved collection pronoun.")).also(context::publish)
        }
        val source = (bound.invocation.vakya.padas.single() as dev.panini.vyakaranam.ast.SubantaPada)
            .pratipadika.referenceKey()
        val value = bound.scope.environment.values[source] ?: vm.runtimeValue(context.sessionKey, source)
        if (value !is SanskritValue.Suchi) return listOf(ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "Distributive iteration requires a list: '$source'.")).also(context::publish)
        val items = value.items.toList()
        val name = loop.member.pratipadika.referenceKey()
        return buildList {
            for ((index, item) in items.withIndex()) {
                val budget = vm.executionLimits.maxConditionIterations
                if (budget != null && index.toLong() >= budget) {
                    val failure = ExecutionResult.Failure(ExecutionError.ACTION_FAILED,
                        "Collection iteration exhausted its host execution budget.")
                    add(failure)
                    context.publish(failure)
                    break
                }
                val memberBindings = ProcedureLocalBindings.member(context.scope.procedureBindings, name, item, loop.member)
                val scope = context.scope.copy(
                    environment = context.scope.environment.mergedWith(ValueEnvironment.live(memberBindings.values)),
                    procedureBindings = memberBindings,
                )
                val produced = vm.withRestoredBinding(context.sessionKey, name, context.persistSession) {
                    executeProgramNode(loop.body, context.copy(scope = scope,
                        onResult = { context.publish(it.consumeLoopBreak()) }))
                }
                addAll(produced.map(ExecutionResult::consumeLoopBreak))
                if (produced.hasControlSignal() || produced.any { it !is ExecutionResult.Success }) break
            }
        }
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
        val persistSession: Boolean,
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
                        onResult = if (node === loop.body) {
                            { result -> context.publish(result.consumeLoopBreak()) }
                        } else context.onResult,
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
                    persistSession = context.persistSession,
                )
            },
            resolveCondition = {
                val bound = LocalAnaphorBinding.resolve(it, context.scope,
                    context.registry.all().mapTo(mutableSetOf()) { operation -> operation.nameStem })
                structuredValueExecutor.resolveInvocation(bound.invocation, context.structStore)?.let { resolved ->
                    resolved.copy(environment = bound.scope.environment.mergedWith(resolved.environment))
                }
            },
            resolveValue = { vm.runtimeValue(context.sessionKey, it) },
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
        persistSession: Boolean = sessionKey != null,
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
            persistSession = persistSession,
        )
    }

    fun evalFile(
        file: File,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        onResult: ((ExecutionResult) -> Unit)? = null,
        persistSession: Boolean = sessionKey != null,
    ): List<ExecutionResult> {
        require(file.exists()) { "PaniniVM script file not found: ${file.absolutePath}" }
        return if (projectLoader.hasSiblingSource(file)) {
            evalProject(file, sessionKey, scope, speaker, listener, onResult, persistSession)
        } else {
            evalScript(
                file.readText(), sourceFile = file.name, sessionKey = sessionKey, scope = scope, speaker = speaker,
                listener = listener, onResult = onResult, persistSession = persistSession,
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
        persistSession: Boolean = true,
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
            persistSession = persistSession,
        ),
    )

    private fun executePrakriyaInvocation(
        invocation: PrakriyaInvocation,
        context: ExecutionContext,
    ): List<ExecutionResult> {
        vm.executionMetrics.recordPrakriyaCall()
        val localStructStore = context.structStore.toMutableMap()
        return prakriyaExecutor.execute(
        PrakriyaExecutor.Request(
            invocation = invocation,
            scope = context.scope,
            registry = context.registry,
            callerSourceFile = context.sourceFile,
            resolveValue = { vm.runtimeValue(context.sessionKey, it) },
            executeBody = { program, scope, sourceFile ->
                executeProgramNode(
                    program,
                    context.copy(
                        scope = scope,
                        sourceFile = sourceFile,
                        structStore = localStructStore,
                        structSchemas = context.structSchemas,
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
