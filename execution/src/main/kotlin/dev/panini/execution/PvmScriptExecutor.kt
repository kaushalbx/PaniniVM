package dev.panini.execution

import dev.panini.execution.binding.baseText
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Recurrence
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.ForEach
import dev.panini.vyakaranam.ast.CollectionQuantifier
import dev.panini.vyakaranam.ast.CollectionRelativeAction
import dev.panini.vyakaranam.ast.CollectionRelativeCardinality
import dev.panini.vyakaranam.ast.CollectionRelativeDirection
import dev.panini.vyakaranam.ast.QuantifiedCollectionPredicate
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
        val parsed = try {
            parsedStatements ?: PvmScript.parse(scriptContent)
        } catch (error: PvmSemanticException) {
            return listOf(ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                error.diagnostic.message,
                diagnostic = error.diagnostic,
            )).also { failures -> failures.forEach { onResult?.invoke(it) } }
        } catch (error: dev.panini.vyakaranam.parser.PaniniSemanticException) {
            val diagnostic = ExecutionDiagnostic.from(error.diagnostic)
            return listOf(ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                diagnostic.message,
                diagnostic = diagnostic,
            )).also { failures -> failures.forEach { onResult?.invoke(it) } }
        }
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
            try {
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
            } catch (error: PvmSemanticException) {
                val failure = ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    error.diagnostic.message,
                    diagnostic = error.diagnostic,
                )
                results += failure
                context.publish(failure)
            }
            val last = results.lastOrNull()
            if (last != null && (last !is ExecutionResult.Success ||
                    last.controlSignal == ExecutionControlSignal.HALT_SCRIPT)) break
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
            if (NaturalScriptStop.matches(node)) {
                NaturalScriptStop.contextDiagnostic(
                    node,
                    context.scope.procedureBindings?.hasProcedureOwner == true,
                )?.let { diagnostic ->
                    return listOf(ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        diagnostic.message,
                        diagnostic = ExecutionDiagnostic.from(diagnostic),
                    )).also(context::publish)
                }
                return listOf(ExecutionResult.Success(
                    value = "विरतम्",
                    operation = "pvm.natural-script-stop",
                    controlSignal = ExecutionControlSignal.HALT_SCRIPT,
                )).also(context::publish)
            }
            if (context.loopDepth > 0 && NaturalLoopStop.matches(node)) {
                return listOf(ExecutionResult.Success(
                    value = "स्थितम्",
                    operation = "pvm.natural-loop-stop",
                    controlSignal = ExecutionControlSignal.BREAK_LOOP,
                )).also(context::publish)
            }
            if (!context.conditionEvaluation && context.scope.procedureBindings?.hasProcedureOwner == true) {
                val bound = when (val analysis = LocalAnaphorBinding.analyze(node, context.scope)) {
                    is LocalAnaphorBinding.Analysis.Resolved -> analysis.value
                    is LocalAnaphorBinding.Analysis.Invalid -> return anaphorFailure(analysis.diagnostic, context)
                }
                val relation = when (val analysis = ProcedureResultStatement.analyze(
                    bound.invocation,
                    roleShadowed = bound.scope.procedureBindings?.isDeclaredInProcedure("फल") == true,
                )) {
                    ProcedureResultStatement.Analysis.NotResult -> null
                    is ProcedureResultStatement.Analysis.Resolved -> analysis.relation
                    is ProcedureResultStatement.Analysis.Invalid -> return listOf(ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        analysis.diagnostic.message,
                        diagnostic = ExecutionDiagnostic.from(analysis.diagnostic),
                    )).also(context::publish)
                }
                if (relation != null) {
                    val value = try { NominalResultSemantics.operand(relation.value).resolve { name ->
                        bound.scope.environment.values[name] ?: vm.runtimeValue(context.sessionKey, name)
                    } } catch (failure: IllegalArgumentException) {
                        return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                            failure.message ?: "Invalid procedure result value.")).also(context::publish)
                    }
                    vm.retainStructuredResult(context.sessionKey, value, context.speaker, context.listener, context.persistSession)
                    return listOf(ExecutionResult.Success(value.toDisplayText(), "pvm.procedure-result",
                        typedValue = value, controlSignal = ExecutionControlSignal.RETURN_PROCEDURE)).also(context::publish)
                }
            }
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
                val bound = when (val analysis = LocalAnaphorBinding.analyze(node, context.scope)) {
                    is LocalAnaphorBinding.Analysis.Resolved -> analysis.value
                    is LocalAnaphorBinding.Analysis.Invalid -> return anaphorFailure(analysis.diagnostic, context)
                }
                val value = NominalResultSemantics.operand(bound.invocation.implicitValuePada ?: nominal).resolve { name ->
                    bound.scope.environment.values[name] ?: vm.runtimeValue(context.sessionKey, name)
                }
                vm.retainStructuredResult(context.sessionKey, value, context.speaker, context.listener, context.persistSession)
                return listOf(ExecutionResult.Success(value.toDisplayText(), "pvm.nominal-result",
                    typedValue = value, conditionValue = (value as? SanskritValue.Satya)?.boolean)).also(context::publish)
            }
            val listExpansion = when (val analysis = ListDeclarationLowering.analyze(node)) {
                ListDeclarationLowering.Analysis.NotDeclaration -> null
                is ListDeclarationLowering.Analysis.Resolved -> analysis.sequence
                is ListDeclarationLowering.Analysis.Invalid -> return listOf(ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    analysis.diagnostic.message,
                    diagnostic = ExecutionDiagnostic.from(analysis.diagnostic),
                )).also(context::publish)
            }
            val ordinalExpansion = when (val analysis = OrdinalObjectLowering.analyze(node)) {
                OrdinalObjectLowering.Analysis.NotOrdinalObject -> null
                is OrdinalObjectLowering.Analysis.Resolved -> analysis.sequence
                is OrdinalObjectLowering.Analysis.Invalid -> return listOf(ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    analysis.diagnostic.message,
                    diagnostic = ExecutionDiagnostic.from(analysis.diagnostic),
                )).also(context::publish)
            }
            val priorExpansion = when (val analysis = PriorActionLowering.analyze(node)) {
                PriorActionLowering.Analysis.NotPriorAction -> null
                is PriorActionLowering.Analysis.Resolved -> analysis.sequence
                is PriorActionLowering.Analysis.Invalid -> return listOf(ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    analysis.diagnostic.message,
                    diagnostic = ExecutionDiagnostic.from(analysis.diagnostic),
                )).also(context::publish)
            }
            val expanded = listExpansion ?: ordinalExpansion ?: priorExpansion
            expanded?.let { executeSequenceNode(it, context) } ?: executeInvocationNode(node, context)
        }
        is Sequence -> executeSequenceNode(node, context)
        is Conditional -> executeConditionalNode(node, context)
        is Repeat -> buildList {
            repeat(node.count) {
                val produced = executeProgramNode(node.body, context.copy(
                    loopDepth = context.loopDepth + 1,
                    onResult = { context.publish(it.consumeLoopBreak()) },
                ))
                addAll(produced.map(ExecutionResult::consumeLoopBreak))
                if (produced.hasControlSignal() || produced.any { it !is ExecutionResult.Success }) return@buildList
            }
        }
        is Recurrence -> executeRecurrence(node, context)
        is WhileLoop -> executeWhileLoop(node, context)
        is ForEach -> executeForEach(node, context)
        is CollectionRelativeAction -> executeCollectionRelativeAction(node, context)
        is QuantifiedCollectionPredicate -> executeQuantifiedCollectionPredicate(node, context)
        is Pipeline -> PurvaparaPipelineEngine.executePipeline(
            node, vm, context.sessionKey, context.scope, context.speaker, context.listener,
            context.registry, callerSourceFile = context.sourceFile,
            persistSession = context.persistSession,
        ).also(context::publish)
        is Quotation -> {
            val expanded = when (val analysis = ListDeclarationLowering.analyze(node)) {
                ListDeclarationLowering.Analysis.NotDeclaration -> null
                is ListDeclarationLowering.Analysis.Resolved -> analysis.sequence
                is ListDeclarationLowering.Analysis.Invalid -> return listOf(ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    analysis.diagnostic.message,
                    diagnostic = ExecutionDiagnostic.from(analysis.diagnostic),
                )).also(context::publish)
            }
            expanded?.let { executeSequenceNode(it, context) } ?: executeEvaluatorNode(node, context)
        }
        is Prakriya -> executeBody(node.body, context)
        is Scope -> executeBody(node.body, context)
        }
    }

    private fun executeRecurrence(node: Recurrence, context: ExecutionContext): List<ExecutionResult> {
        val analysis = dev.panini.execution.binding.VyakaranamExecutionAdapter.analyzeForMemory(
            Ukti(node.body.sourceText, body = node.body),
        )
        val identity = analysis?.frames?.firstOrNull()?.kriya?.actionIdentity
        val hasAntecedent = identity != null && vm.kriyaMemory(context.sessionKey)
            .matchingActions(identity).isNotEmpty()
        if (!hasAntecedent) {
            return listOf(ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "पुनः requires a compatible earlier completed action.",
            )).also(context::publish)
        }
        return executeProgramNode(node.body, context)
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
        val bound = when (val analysis = LocalAnaphorBinding.analyze(node, context.scope, operations)) {
            is LocalAnaphorBinding.Analysis.Resolved -> analysis.value
            is LocalAnaphorBinding.Analysis.Invalid -> return anaphorFailure(analysis.diagnostic, context)
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
        val bound = condition?.let {
            when (val analysis = LocalAnaphorBinding.analyze(it, context.scope,
                context.registry.all().mapTo(mutableSetOf()) { operation -> operation.nameStem })) {
                is LocalAnaphorBinding.Analysis.Resolved -> analysis.value
                is LocalAnaphorBinding.Analysis.Invalid -> return anaphorFailure(analysis.diagnostic, context)
            }
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

    private fun anaphorFailure(
        diagnostic: LocalAnaphorBinding.Diagnostic,
        context: ExecutionContext,
    ): List<ExecutionResult> = listOf(ExecutionResult.Failure(
        ExecutionError.INVALID_VALUE,
        with(LocalAnaphorBinding) { diagnostic.render() },
        diagnostic = with(LocalAnaphorBinding) { diagnostic.executionDiagnostic() },
    )).also(context::publish)

    private fun executeForEach(loop: ForEach, context: ExecutionContext): List<ExecutionResult> {
        val whole = Invocation(dev.panini.vyakaranam.ast.NamaVakya(loop.collection.sourceText, listOf(loop.collection)))
        val bound = when (val analysis = LocalAnaphorBinding.analyze(whole, context.scope)) {
            is LocalAnaphorBinding.Analysis.Resolved -> analysis.value
            is LocalAnaphorBinding.Analysis.Invalid -> return anaphorFailure(analysis.diagnostic, context)
        }
        val source = (bound.invocation.vakya.padas.single() as dev.panini.vyakaranam.ast.SubantaPada)
            .pratipadika.referenceKey()
        val value = bound.scope.environment.values[source] ?: vm.runtimeValue(context.sessionKey, source)
        if (value !is SanskritValue.Suchi) return listOf(ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "Distributive iteration requires a list: '$source'.")).also(context::publish)
        val items = value.items.toList()
        val name = loop.member.pratipadika.referenceKey()
        val indexName = loop.index?.pratipadika?.referenceKey()
        val previousSelection = loop.relativeSelections.singleOrNull {
            it.direction == dev.panini.vyakaranam.ast.CollectionRelativeDirection.PREVIOUS
        }
        val nextSelection = loop.relativeSelections.singleOrNull {
            it.direction == dev.panini.vyakaranam.ast.CollectionRelativeDirection.NEXT
        }
        val previousName = previousSelection?.binding?.pratipadika?.referenceKey()
        val nextName = nextSelection?.binding?.pratipadika?.referenceKey()
        val previousIsPlural = previousSelection?.cardinality ==
            dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS
        val nextIsPlural = nextSelection?.cardinality ==
            dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS
        data class Iteration(
            val ordinal: Int,
            val member: SanskritValue,
            val index: SanskritValue,
            val previous: SanskritValue?,
            val next: SanskritValue?,
        )
        val iterations = items.mapIndexedNotNull { index, item ->
            if (previousName != null && !previousIsPlural && index == 0 ||
                nextName != null && !nextIsPlural && index == items.lastIndex) {
                return@mapIndexedNotNull null
            }
            val position = index + 1L
            Iteration(index, item, SanskritValue.Sankhya(position,
                renderSankhyaResult(position) ?: position.toString()),
                if (previousIsPlural) SanskritValue.Suchi(items.take(index), value.memberType) else items.getOrNull(index - 1),
                if (nextIsPlural) SanskritValue.Suchi(items.drop(index + 1), value.memberType) else items.getOrNull(index + 1))
        }
        return buildList {
            for (iteration in iterations) {
                val budget = vm.executionLimits.maxConditionIterations
                if (budget != null && iteration.ordinal.toLong() >= budget) {
                    val failure = ExecutionResult.Failure(ExecutionError.ACTION_FAILED,
                        "Collection iteration exhausted its host execution budget.")
                    add(failure)
                    context.publish(failure)
                    break
                }
                val memberBindings = ProcedureLocalBindings.member(context.scope.procedureBindings, name,
                    iteration.member, loop.member)
                val indexBindings = if (indexName == null) memberBindings else ProcedureLocalBindings.member(
                    memberBindings, indexName, iteration.index, loop.index)
                val previousBindings = if (previousName == null) indexBindings else ProcedureLocalBindings.member(
                    indexBindings, previousName, requireNotNull(iteration.previous), previousSelection.binding)
                val loopBindings = if (nextName == null) previousBindings else ProcedureLocalBindings.member(
                    previousBindings, nextName, requireNotNull(iteration.next), nextSelection.binding)
                val loopEnvironment = context.scope.environment
                    .mergedWith(ValueEnvironment.live(memberBindings.values))
                    .let { environment -> if (loopBindings === memberBindings) environment else
                        environment.mergedWith(ValueEnvironment.live(loopBindings.values)) }
                val scope = context.scope.copy(
                    environment = loopEnvironment,
                    procedureBindings = loopBindings,
                )
                val executeBody = {
                    executeProgramNode(loop.body, context.copy(scope = scope,
                        loopDepth = context.loopDepth + 1,
                        onResult = { context.publish(it.consumeLoopBreak()) }))
                }
                fun withRestored(names: List<String>, action: () -> List<ExecutionResult>): List<ExecutionResult> {
                    val current = names.firstOrNull() ?: return action()
                    return vm.withRestoredBinding(context.sessionKey, current, context.persistSession) {
                        withRestored(names.drop(1), action)
                    }
                }
                val produced = withRestored(listOfNotNull(name, indexName, previousName, nextName), executeBody)
                addAll(produced.map(ExecutionResult::consumeLoopBreak))
                if (produced.hasControlSignal() || produced.any { it !is ExecutionResult.Success }) break
            }
        }
    }

    /** Executes the grammatical relative operand directly; no source-visible binding is fabricated. */
    private fun executeCollectionRelativeAction(
        expression: CollectionRelativeAction,
        context: ExecutionContext,
    ): List<ExecutionResult> {
        val whole = Invocation(dev.panini.vyakaranam.ast.NamaVakya(
            expression.collection.sourceText,
            listOf(expression.collection),
        ))
        val bound = when (val analysis = LocalAnaphorBinding.analyze(whole, context.scope)) {
            is LocalAnaphorBinding.Analysis.Resolved -> analysis.value
            is LocalAnaphorBinding.Analysis.Invalid -> return anaphorFailure(analysis.diagnostic, context)
        }
        val source = (bound.invocation.vakya.padas.single() as dev.panini.vyakaranam.ast.SubantaPada)
            .pratipadika.referenceKey()
        val collection = bound.scope.environment.values[source] ?: vm.runtimeValue(context.sessionKey, source)
        val items = (collection as? SanskritValue.Suchi)?.items?.toList()
            ?: return listOf(ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "A collection-relative action requires a list: '$source'.",
            )).also(context::publish)

        val pairs = sequence {
            for (currentIndex in items.indices) {
                val relatives = when (expression.operand.direction) {
                    CollectionRelativeDirection.NEXT ->
                        if (expression.operand.cardinality == CollectionRelativeCardinality.ITEM)
                            listOfNotNull(items.getOrNull(currentIndex + 1))
                        else items.drop(currentIndex + 1)
                    CollectionRelativeDirection.PREVIOUS ->
                        if (expression.operand.cardinality == CollectionRelativeCardinality.ITEM)
                            listOfNotNull(items.getOrNull(currentIndex - 1))
                        else items.take(currentIndex)
                }
                relatives.forEach { yield(items[currentIndex] to it) }
            }
        }

        return buildList {
            for ((ordinal, pair) in pairs.withIndex()) {
                val budget = vm.executionLimits.maxConditionIterations
                if (budget != null && ordinal.toLong() >= budget) {
                    val failure = ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "Collection-relative action exhausted its host execution budget.",
                    )
                    add(failure); context.publish(failure); break
                }
                val operands = ExecutionExpression.Coordination(listOf(
                    ExecutionExpression.TypedOperand(pair.first, dev.panini.core.SupAffix.AM),
                    ExecutionExpression.TypedOperand(pair.second, dev.panini.core.SupAffix.AM),
                ))
                val actionResult = vm.evalParsed(
                    Ukti(expression.action.sourceText, body = expression.actionInvocation()),
                    context.sessionKey,
                    bound.scope,
                    context.speaker,
                    context.listener,
                    injectedBindings = mapOf(dev.panini.core.Karaka.KARMAN to operands),
                    persistSession = context.persistSession,
                ).let { result ->
                    if (result is ExecutionResult.Failure) result.copy(
                        message = "Collection-relative action '${expression.action.dhatu.mulaDhatu}' failed: ${result.message}",
                    ) else result
                }
                add(actionResult); context.publish(actionResult)
                if (actionResult !is ExecutionResult.Success) break

                val continuation = expression.continuation ?: continue
                val continued = executeProgramNode(
                    continuation,
                    context.copy(
                        scope = bound.scope.copy(environment = bound.scope.environment.mergedWith(
                            ValueEnvironment(mapOf(PIPE_OPERAND to
                                (actionResult.typedValue ?: SanskritValue.of(actionResult.value)))),
                        )),
                        loopDepth = context.loopDepth + 1,
                        injectedKarman = InjectedKarmanBinding(PIPE_OPERAND, actionResult.typedValue),
                        onResult = { context.publish(it.consumeLoopBreak()) },
                    ),
                )
                addAll(continued.map(ExecutionResult::consumeLoopBreak))
                if (continued.hasControlSignal() || continued.any { it !is ExecutionResult.Success }) break
            }
        }
    }

    private fun executeQuantifiedCollectionPredicate(
        expression: QuantifiedCollectionPredicate,
        context: ExecutionContext,
    ): List<ExecutionResult> {
        val whole = Invocation(dev.panini.vyakaranam.ast.NamaVakya(
            expression.collection.sourceText,
            listOf(expression.collection),
        ))
        val bound = when (val analysis = LocalAnaphorBinding.analyze(whole, context.scope)) {
            is LocalAnaphorBinding.Analysis.Resolved -> analysis.value
            is LocalAnaphorBinding.Analysis.Invalid -> return anaphorFailure(analysis.diagnostic, context)
        }
        val source = (bound.invocation.vakya.padas.single() as dev.panini.vyakaranam.ast.SubantaPada)
            .pratipadika.referenceKey()
        val collection = bound.scope.environment.values[source] ?: vm.runtimeValue(context.sessionKey, source)
        val items = when (collection) {
            is SanskritValue.Suchi -> collection.items.toList()
            is SanskritValue.Gana -> collection.elements.toList()
            else -> return listOf(ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "A quantified predicate requires a list or group: '$source'.",
            )).also(context::publish)
        }
        val memberName = expression.member.pratipadika.referenceKey()
        val trace = mutableListOf<String>()
        var inspected = 0
        var truth = expression.quantifier == CollectionQuantifier.ALL
        for ((index, item) in items.withIndex()) {
            val budget = vm.executionLimits.maxConditionIterations
            if (budget != null && index.toLong() >= budget) {
                return listOf(ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "Collection predicate exhausted its host execution budget.",
                    trace,
                )).also(context::publish)
            }
            val memberBindings = ProcedureLocalBindings.member(
                context.scope.procedureBindings,
                memberName,
                item,
                expression.member,
            )
            val predicateScope = context.scope.copy(
                environment = context.scope.environment.mergedWith(ValueEnvironment.live(memberBindings.values)),
                procedureBindings = memberBindings,
            )
            val predicateResult = vm.withRestoredBinding(context.sessionKey, memberName, context.persistSession) {
                vm.evalParsed(
                    Ukti(expression.predicate.sourceText, body = expression.predicate),
                    context.sessionKey,
                    predicateScope,
                    context.speaker,
                    context.listener,
                    evaluateCondition = true,
                    persistSession = context.persistSession,
                )
            }
            val matches = when (predicateResult) {
                is ExecutionResult.Success -> predicateResult.conditionValue
                    ?: (predicateResult.typedValue as? SanskritValue.Satya)?.boolean
                    ?: return listOf(ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "The quantified member predicate must produce a truth value.",
                        trace + predicateResult.trace,
                    )).also(context::publish)
                is ExecutionResult.Failure -> return listOf(predicateResult.copy(
                    message = "Collection predicate failed for member ${index + 1}: ${predicateResult.message}",
                    trace = trace + predicateResult.trace,
                )).also(context::publish)
                else -> return listOf(ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "Collection predicate could not resolve member ${index + 1} to truth.",
                    trace + predicateResult.trace,
                )).also(context::publish)
            }
            inspected++
            trace += "Member ${index + 1} ('${item.toDisplayText()}') satisfies predicate: $matches."
            if (expression.quantifier == CollectionQuantifier.ANY && matches) {
                truth = true
                break
            }
            if (expression.quantifier == CollectionQuantifier.ALL && !matches) {
                truth = false
                break
            }
        }
        val value = SanskritValue.Satya(truth)
        return listOf(ExecutionResult.Success(
            value.toDisplayText(),
            "pvm.collection-${expression.quantifier.name.lowercase()}",
            trace + "Inspected $inspected of ${items.size} collection members.",
            typedValue = value,
            conditionValue = truth,
        )).also(context::publish)
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
        val loopDepth: Int = 0,
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
                        loopDepth = if (node === loop.body) context.loopDepth + 1 else context.loopDepth,
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
                when (val analysis = LocalAnaphorBinding.analyze(it, context.scope,
                    context.registry.all().mapTo(mutableSetOf()) { operation -> operation.nameStem })) {
                    is LocalAnaphorBinding.Analysis.Invalid -> PvmLoopExecutor.ConditionResolution.Invalid(
                        ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                            with(LocalAnaphorBinding) { analysis.diagnostic.render() },
                            diagnostic = with(LocalAnaphorBinding) {
                                analysis.diagnostic.executionDiagnostic()
                            }),
                    )
                    is LocalAnaphorBinding.Analysis.Resolved -> {
                        val bound = analysis.value
                        structuredValueExecutor.resolveInvocation(bound.invocation, context.structStore)
                            ?.let { resolved -> PvmLoopExecutor.ConditionResolution.Resolved(resolved.copy(
                                environment = bound.scope.environment.mergedWith(resolved.environment),
                            )) }
                            ?: PvmLoopExecutor.ConditionResolution.Invalid(ExecutionResult.Failure(
                                ExecutionError.INVALID_VALUE,
                                "A structured attribute used by the loop condition could not be resolved.",
                            ))
                    }
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
