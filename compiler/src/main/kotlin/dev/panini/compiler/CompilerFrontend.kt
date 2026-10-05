package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.PrakriyaInvocationArgumentResolver
import dev.panini.execution.Prakriya
import dev.panini.execution.PrakriyaRegistry
import dev.panini.execution.PrakriyaArgumentResolution
import dev.panini.execution.ResolvedPrakriyaArgument
import dev.panini.execution.PrakriyaSignatureDeclarationParser
import dev.panini.execution.PrakriyaValueClassifier
import dev.panini.execution.PrakriyaParameter
import dev.panini.execution.PrakriyaValueType
import dev.panini.execution.TaddhitaInheritanceEngine
import dev.panini.execution.TaddhitaStructEngine
import dev.panini.execution.SanskritValue
import dev.panini.execution.NishedhaGuardEvaluator
import dev.panini.execution.NaturalSemanticNormalizer
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.InjectedKarmanBinding
import dev.panini.execution.planning.ResolvedLeafPlanner
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.Prakriya as PrakriyaNode
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SequenceConnector
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti

/** Lowers grammatical control flow to JVM branches while leaves use the normal action runtime. */
internal object CompilerFrontend {
    private sealed interface ProcedureTarget {
        data class Local(val methodName: String) : ProcedureTarget
        data class Dependency(val className: String, val methodName: String) : ProcedureTarget
    }
    internal data class SourceUnit(val name: String, val content: String, val isEntryPoint: Boolean = true)

    fun compile(scriptContent: String, className: String): ByteArray =
        CompilerProgramJvmEmitter.emit(lower(scriptContent, className))

    /** Frontend boundary: parses and lowers a complete source unit without emitting JVM bytecode. */
    internal fun lower(scriptContent: String, className: String): CompilerProgram {
        return lowerModule(listOf(SourceUnit("<memory>", scriptContent)), className)
    }

    /** Analyzes every source file in a module before any executable body is lowered. */
    internal fun lowerModule(sourceUnits: List<SourceUnit>, className: String): CompilerProgram {
        val descriptor = PaniniModuleDescriptor(
            className,
            sourceUnits.map { PaniniModuleSource(it.name, it.content, it.isEntryPoint) },
        )
        return lowerModule(descriptor, className)
    }

    internal fun lowerModule(descriptor: PaniniModuleDescriptor, className: String,
        parseSource: (String) -> List<PvmScriptStatement> = PvmScript::parse): CompilerProgram {
        val analyzed = PaniniModuleAnalyzer.analyze(descriptor, parseSource)
        val registry = PrakriyaRegistry()
        analyzed.inheritance.forEach { (child, parent) ->
            registry.registerInheritance(dev.panini.execution.InheritanceRelation(child, parent))
        }
        analyzed.procedures.forEach { procedure ->
            registry.register(
                Prakriya(
                    nameSegmented = procedure.definition.nameSegmented,
                    nameStem = procedure.symbol,
                    body = procedure.definition.body,
                    sourceFile = procedure.source.name,
                    domainStem = procedure.domain,
                    visibility = if (procedure.visibility == PaniniSymbolVisibility.INTERNAL) {
                        PrakriyaVisibility.INTERNAL
                    } else {
                        PrakriyaVisibility.PUBLIC
                    },
                    signatureOverride = procedure.signature,
                ),
            )
        }
        descriptor.dependencies.forEach { dependency ->
            dependency.inheritance.forEach { (child, parent) ->
                registry.registerInheritance(dev.panini.execution.InheritanceRelation(child, parent))
            }
            dependency.procedures.forEach { procedure ->
                registry.register(
                    Prakriya(
                        nameSegmented = procedure.symbol,
                        nameStem = procedure.symbol,
                        body = emptyList(),
                        sourceFile = "${dependency.moduleName}.pvmmeta",
                        domainStem = procedure.domain,
                        signatureOverride = dev.panini.execution.PrakriyaSignature(
                            parameters = procedure.parameters,
                            resultType = procedure.resultType,
                            resultSchema = procedure.resultSchema,
                        ),
                    ),
                )
            }
        }
        val methods = analyzed.procedures.associate { it.definition to it.methodName }
        val methodsByStem = buildMap {
            descriptor.dependencies.forEach { dependency ->
                dependency.procedures.forEach { procedure ->
                    val target = ProcedureTarget.Dependency(dependency.className, procedure.methodName)
                    put(procedure.symbol, target)
                }
            }
            analyzed.procedures.forEach { procedure ->
                put(procedure.symbol, ProcedureTarget.Local(procedure.methodName))
                put(procedure.localSymbol, ProcedureTarget.Local(procedure.methodName))
            }
        }
        val lowering = Lowering(registry, methodsByStem)
        val entryPoint = analyzed.statements.filterKeys(PaniniModuleSource::isEntryPoint).values.flatMap { statements ->
            statements.flatMap { statement ->
                when (statement) {
                    is PvmScriptStatement.RangeDefinition -> listOf(CompilerInstruction.Constant(statement.range),
                        CompilerInstruction.Store(dev.panini.execution.ACTIVE_RANGE_NAME))
                    is PvmScriptStatement.Sentence -> lowering.lowerTopLevel(statement)
                    else -> emptyList()
                }
            }
        }
        val procedures = analyzed.procedures.map { procedure ->
            val definition = procedure.definition
            val signature = procedure.signature
            val instructions = definition.body.filterNot { sentence ->
                sentence.isNishedha || PrakriyaSignatureDeclarationParser.isDeclaration(sentence)
            }.flatMap { sentence ->
                sentence.program?.let {
                    lowering.lower(it, sentence.text) + CompilerInstruction.ReturnIfBreak
                }.orEmpty()
            } + CompilerInstruction.Return
            CompilerProcedure(
                methodName = procedure.methodName,
                instructions = instructions,
                parameterNames = signature.parameters.map { it.nameStem },
                parameterKinds = signature.parameters.map { it.type.toCompilerValueKind() },
                returnKind = signature.resultType?.toCompilerValueKind()
                    ?: signature.resultSchema?.let { CompilerValueKind.RECORD },
            )
        }
        val dependencyProcedures = descriptor.dependencies.flatMap { dependency ->
            dependency.procedures.map { procedure ->
                CompilerDependencyProcedure(
                    dependency.className,
                    procedure.methodName,
                    procedure.parameters.map(PrakriyaParameter::nameStem),
                    procedure.parameters.map { it.type.toCompilerValueKind() },
                    procedure.resultType?.toCompilerValueKind()
                        ?: procedure.resultSchema?.let { CompilerValueKind.RECORD },
                )
            }
        }
        return CompilerProgram(className, entryPoint, procedures, dependencyProcedures)
            .also(CompilerProgramVerifier::verify)
    }

    private class Lowering(
        private val registry: PrakriyaRegistry,
        private val methodsByStem: Map<String, ProcedureTarget>,
    ) {
        private var nextLabel = 0
        private val assertedStructFields =
            mutableMapOf<String, LinkedHashMap<String, dev.panini.execution.TaddhitaFieldAssertion>>()

        fun lowerTopLevel(sentence: PvmScriptStatement.Sentence): List<CompilerInstruction> {
            TaddhitaStructEngine.detectFieldAssertion(sentence.text, sentence.ukti)?.let { assertion ->
                val fields = assertedStructFields.getOrPut(assertion.ownerStem, ::linkedMapOf)
                fields[assertion.fieldStem] = assertion
                return buildList {
                    fields.values.forEach {
                        add(CompilerInstruction.Constant(TaddhitaStructEngine.assertionValue(it)))
                    }
                    add(CompilerInstruction.BuildRecord(assertion.ownerStem, fields.keys.toList()))
                    add(CompilerInstruction.Duplicate)
                    add(CompilerInstruction.Store(assertion.ownerStem))
                    add(CompilerInstruction.Store("LastResult"))
                }
            }
            sentence.ukti?.grammaticalVakyas()?.singleOrNull()
                ?.let(TaddhitaStructEngine::detectAttributeAccess)?.let { access ->
                    if (access.chain.size == 2) {
                        return listOf(
                            CompilerInstruction.Load(access.chain.first()),
                            CompilerInstruction.LoadFieldOrLopa(access.chain.last()),
                            CompilerInstruction.Store("LastResult"),
                        )
                    }
                }
            return sentence.program?.let {
                lower(it, sentence.text, allowDirectStore = true)
            }.orEmpty()
        }

        fun lower(
            node: ProgramNode,
            exactSource: String? = null,
            allowDirectStore: Boolean = false,
        ): List<CompilerInstruction> = when (node) {
            is Invocation -> lowerInvocation(node, exactSource, allowDirectStore = allowDirectStore)
            is Sequence -> lowerSequence(node, exactSource)
            is Pipeline -> lowerPipeline(node)
            is Quotation -> lowerPlannedNode(node)
            is Conditional -> lowerConditionalIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.CONDITIONAL, render(node), "Cannot lower conditional to compiler IR.",
            )
            is Repeat -> lowerRepeatIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.REPETITION, render(node), "Cannot lower repetition body to compiler IR.",
            )
            is WhileLoop -> lowerWhileIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.LOOP, render(node), "Cannot lower condition-controlled loop to compiler IR.",
            )
            is PrakriyaNode -> node.body.flatMap { lower(it) }
            is Scope -> node.body.flatMap { lower(it) }
        }

        private fun lowerSequence(node: Sequence, exactSource: String?): List<CompilerInstruction> {
            if (node.connectorKinds.any { it != SequenceConnector.ANANTARYA } ||
                node.statements.any { it !is Invocation }
            ) {
                return lowerPlannedNode(node)
            }
            return node.statements.flatMapIndexed { index, statement ->
                if (index == 0) {
                    lower(statement)
                } else if (statement is Invocation) {
                    lowerInvocation(statement, exactSource = null, piped = true)
                } else {
                    lower(statement)
                }
            }
        }

        private fun lowerInvocation(
            node: Invocation,
            exactSource: String?,
            piped: Boolean = false,
            allowDirectStore: Boolean = false,
        ): List<CompilerInstruction> {
            val rendered = exactSource ?: render(node)
            val alreadyReferencesResult = node.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.SubantaPada &&
                    NaturalSemanticNormalizer.isPriorResult(pada)
            }
            val source = normalized(rendered)
            val naturalSemantic = NaturalSemanticNormalizer.normalize(node)
            lowerRangeChoice(node)?.let { return it }
            if (naturalSemantic == NaturalSemanticNormalizer.Operation.CollectionParameterSum) {
                return listOf(
                    CompilerInstruction.Load("समवाय"),
                    CompilerInstruction.Collection(CollectionOperator.SUM),
                    CompilerInstruction.Store("LastResult"),
                )
            }
            (naturalSemantic as? NaturalSemanticNormalizer.Operation.ProcedureParameterArithmetic)
                ?.let(::lowerImplicitParameterOperation)
                ?.let { return it }
            lowerProcedureCall(node, piped)?.let { return it }
            val consumesPriorResult = piped && !alreadyReferencesResult ||
                naturalSemantic ==
                NaturalSemanticNormalizer.Operation.DisplayPriorResult
            val injectedBindings = if (consumesPriorResult) {
                mapOf(Karaka.KARMAN to ExecutionExpression.Reference("LastResult"))
            } else {
                emptyMap()
            }
            return (ResolvedLeafPlanner.plan(
                node,
                allowStore = allowDirectStore,
                injectedBindings = injectedBindings,
            ) ?: ResolvedLeafPlanner.planAny(
                node,
                injectedBindings = injectedBindings,
            ))?.let(::lowerDirect)
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.INVOCATION, source, "Cannot resolve invocation as a compiler leaf.",
                )
        }

        private fun lowerRangeChoice(node: Invocation): List<CompilerInstruction>? {
            val semantic = NaturalSemanticNormalizer.normalize(node)
                as? NaturalSemanticNormalizer.Operation.RangeChoice
                ?: return null
            val evaluator = dev.panini.sankhya.SankhyaEvaluator()
            val astBounds = node.vakya.padas.flatMap { pada ->
                when (pada) {
                    is ParyantaRangePada -> listOf(pada.lowerLimit, pada.upperLimit).map { bound ->
                        requireNotNull(NaturalSemanticNormalizer.boundaryValue(bound))
                    }
                    is SankhyaPada -> listOf(pada.value ?: evaluator.evaluateStems(pada.stems).value)
                    is SubantaPada -> (pada.pratipadika as? MulaPratipadika)?.text?.let { stem ->
                        runCatching { evaluator.evaluateStems(listOf(stem)).value }.getOrNull()
                    }?.let(::listOf).orEmpty()
                    else -> emptyList()
                }
            }
            val exclusionName = semantic.exclusionName
            return buildList {
                if (astBounds.size < 2) add(CompilerInstruction.Load(dev.panini.execution.ACTIVE_RANGE_NAME))
                exclusionName?.let { add(CompilerInstruction.Load(it)) }
                if (astBounds.size >= 2) add(CompilerInstruction.RandomRange(astBounds[0], astBounds[1], exclusionName != null))
                else add(CompilerInstruction.RandomActiveRange(exclusionName != null))
                add(CompilerInstruction.Store("LastResult"))
            }
        }

        private fun lowerImplicitParameterOperation(
            semantic: NaturalSemanticNormalizer.Operation.ProcedureParameterArithmetic,
        ): List<CompilerInstruction>? {
            val operator = when (semantic.kind) {
                NaturalSemanticNormalizer.ArithmeticKind.ADD -> ArithmeticOperator.ADD
                NaturalSemanticNormalizer.ArithmeticKind.MULTIPLY -> ArithmeticOperator.MULTIPLY
                NaturalSemanticNormalizer.ArithmeticKind.DIVIDE -> ArithmeticOperator.DIVIDE
            }
            val operands = semantic.operands.mapNotNull { operand ->
                when (operand) {
                    NaturalSemanticNormalizer.ProcedureOperand.PriorResult ->
                        CompilerInstruction.LoadLastResult
                    is NaturalSemanticNormalizer.ProcedureOperand.Parameter -> {
                        val name = when (operand.position) {
                            1L -> "प्रथम"
                            2L -> "द्वितीय"
                            3L -> "तृतीय"
                            else -> return@mapNotNull null
                        }
                        CompilerInstruction.Load(name)
                    }
                }
            }
            if (operands.size < 2 || operands.none { it is CompilerInstruction.Load }) return null
            return buildList {
                add(operands.first())
                operands.drop(1).forEach { operand ->
                    add(operand)
                    add(CompilerInstruction.Arithmetic(operator))
                }
                add(CompilerInstruction.Store("LastResult"))
            }
        }

        /** Lowers an already parsed call without rendering and reparsing its Sanskrit. */
        private fun lowerProcedureCall(node: Invocation, piped: Boolean): List<CompilerInstruction>? {
            val ukti = Ukti(node.sourceText, body = node)
            val injected = if (piped) InjectedKarmanBinding("LastResult", null) else null
            val invocation = registry.detectInvocation(ukti, injectedKarman = injected) ?: return null
            return lowerProcedureInvocation(invocation)
        }

        private fun lowerProcedureInvocation(
            invocation: dev.panini.execution.PrakriyaInvocation,
        ): List<CompilerInstruction>? {
            val target = invocation.kriya.nameStem.let(methodsByStem::get) ?: return null
            val signature = invocation.kriya.signature
            val parameterNames = signature.parameters.map { it.nameStem }
            val parameterKinds = signature.parameters.map { it.type.toCompilerValueKind() }
            val arguments = resolveArguments(invocation)
            return buildList {
                if (signature.parameters.singleOrNull()?.type == PrakriyaValueType.SUCHI) {
                    arguments.forEachIndexed { index, argument ->
                        add(lowerCallArgument(argument, invocation.argumentValues.getOrNull(index)))
                    }
                    add(CompilerInstruction.BuildList(arguments.size))
                } else {
                    arguments.take(parameterNames.size).forEachIndexed { index, argument ->
                        add(lowerCallArgument(argument, invocation.argumentValues.getOrNull(index)))
                    }
                }
                add(CompilerInstruction.EnterFrame(parameterNames, parameterKinds))
                add(when (target) {
                    is ProcedureTarget.Local -> CompilerInstruction.InvokeProcedure(
                        target.methodName,
                        parameterNames.size,
                        signature.resultType?.toCompilerValueKind()
                            ?: signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                    is ProcedureTarget.Dependency -> CompilerInstruction.InvokeDependencyProcedure(
                        target.className,
                        target.methodName,
                        parameterNames.size,
                        signature.resultType?.toCompilerValueKind()
                            ?: signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                })
                add(CompilerInstruction.ExitFrame)
            }
        }

        private fun lowerCallArgument(
            argument: ResolvedPrakriyaArgument,
            fallbackValue: dev.panini.execution.SanskritValue?,
        ): CompilerInstruction {
            val value = argument.argument.value ?: fallbackValue
            val name = argument.referenceName
            return when {
                argument.isPriorResult -> CompilerInstruction.LoadLastResult
                value != null -> CompilerInstruction.Constant(value)
                else -> CompilerInstruction.ResolveArgument(name, null)
            }
        }

        private fun lowerPipeline(node: Pipeline): List<CompilerInstruction> = buildList {
            node.stages.forEachIndexed { stageIndex, stage ->
                val target = methodsByStem[stage.operationStem]
                    ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.PIPELINE,
                        node.sourceText,
                        "Unknown compiled pipeline stage '${stage.operationStem}'.",
                    )
                val kriya = registry.resolve(stage.operationStem)
                    ?: registry.all().singleOrNull { it.nameStem == stage.operationStem }
                    ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.PIPELINE,
                        node.sourceText,
                        "Missing signature for pipeline stage '${stage.operationStem}'.",
                    )
                val parameters = kriya.signature.parameters
                parameters.forEachIndexed { parameterIndex, _ ->
                    if (stageIndex > 0 && parameterIndex == 0) {
                        add(CompilerInstruction.LoadLastResult)
                    } else {
                        val argumentPada = node.argumentPadas.getOrNull(parameterIndex) as? SubantaPada
                            ?: throw CompilerUnsupportedException(
                                CompilerUnsupportedKind.PIPELINE,
                                node.sourceText,
                                "Pipeline stage '${stage.operationStem}' lacks parsed nominal argument ${parameterIndex + 1}.",
                            )
                        add(CompilerInstruction.ResolveArgument(argumentPada.pratipadika.semanticKey(), null))
                    }
                }
                val kinds = parameters.map { it.type.toCompilerValueKind() }
                add(CompilerInstruction.EnterFrame(parameters.map { it.nameStem }, kinds))
                add(when (target) {
                    is ProcedureTarget.Local -> CompilerInstruction.InvokeProcedure(
                        target.methodName,
                        parameters.size,
                        kriya.signature.resultType?.toCompilerValueKind()
                            ?: kriya.signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                    is ProcedureTarget.Dependency -> CompilerInstruction.InvokeDependencyProcedure(
                        target.className,
                        target.methodName,
                        parameters.size,
                        kriya.signature.resultType?.toCompilerValueKind()
                            ?: kriya.signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                })
                add(CompilerInstruction.ExitFrame)
            }
        }

        private fun lowerDirect(plan: ExecutionPlan): List<CompilerInstruction> =
            CompilerIrLowering.lowerLeafValues(plan)

        /**
         * Produces complete IR for conditionals whose leaves are primitive plans.
         * Named calls continue through the existing emitter until Call IR carries
         * procedure invocation and argument-frame semantics.
         */
        private fun lowerConditionalIr(node: Conditional): List<CompilerInstruction>? {
            val truthTest = (node.condition as? Invocation)?.let(NaturalSemanticNormalizer::normalize)
                as? NaturalSemanticNormalizer.Operation.TruthTest
            val condition = if (truthTest != null) {
                listOf(
                    CompilerInstruction.Load(truthTest.stateName),
                    CompilerInstruction.Constant(dev.panini.execution.SanskritValue.Satya(!truthTest.negated)),
                    CompilerInstruction.Compare(ComparisonOperator.EQUAL),
                )
            } else (node.condition as? Invocation)?.let(ResolvedLeafPlanner::planAny)
                ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
                ?.let(CompilerIrLowering::lowerCondition)
                ?: (lower(node.condition) + CompilerInstruction.Booleanize)
            val consequent = lowerPrimitiveBranchIr(node.consequent) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.CONDITIONAL,
                render(node.consequent),
                "Cannot lower the consequent branch to compiler IR (${node.consequent::class.simpleName}).",
            )
            val alternate = node.alternate?.let { alternateNode ->
                lowerPrimitiveBranchIr(alternateNode) ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.CONDITIONAL,
                    render(alternateNode),
                    "Cannot lower the alternate branch to compiler IR (${alternateNode::class.simpleName}).",
                )
            } ?: emptyList()
            return CompilerIrLowering.lowerConditional(
                condition = condition,
                consequent = consequent,
                alternate = alternate,
                labelPrefix = "conditional_${nextLabel++}",
            )
        }

        private fun lowerPrimitiveBranchIr(node: ProgramNode): List<CompilerInstruction>? = when (node) {
            is Invocation -> {
                if (node.vakya.padas.none { it is TingantaPada }) {
                    val pratipadika = (node.implicitValuePada as? SubantaPada)?.pratipadika
                        ?: node.vakya.padas.filterIsInstance<SubantaPada>().firstOrNull()?.pratipadika
                        ?: return null
                    val value = when (pratipadika) {
                        is SankhyaPratipadika -> pratipadika.semanticValue
                        is MulaPratipadika -> when (pratipadika.lexicalIdentity) {
                            MulaPratipadikaIdentity.SATYA ->
                                dev.panini.execution.SanskritValue.Satya(true, pratipadika.text)
                            MulaPratipadikaIdentity.ASATYA ->
                                dev.panini.execution.SanskritValue.Satya(false, pratipadika.text)
                            else -> dev.panini.execution.SanskritValue.Shabda(pratipadika.semanticKey())
                        }
                        else -> dev.panini.execution.SanskritValue.Shabda(pratipadika.semanticKey())
                    }
                    listOf(
                        CompilerInstruction.Constant(value),
                        CompilerInstruction.Store("LastResult"),
                    )
                } else {
                    runCatching { lowerInvocation(node, exactSource = null) }.getOrNull()
                        ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION,
                        normalized(render(node)),
                        "Cannot lower conditional invocation with padas " +
                            node.vakya.padas.joinToString { it::class.simpleName.orEmpty() },
                    )
                }
            }
            is Conditional -> lowerConditionalIr(node)
            is Sequence -> buildList {
                for (statement in node.statements) {
                    addAll(lowerPrimitiveBranchIr(statement) ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.CONDITIONAL,
                        render(statement),
                        "Cannot lower a sequence statement in a conditional branch (${statement::class.simpleName}).",
                    ))
                }
            }
            is PrakriyaNode -> buildList {
                for (statement in node.body) {
                    addAll(lowerPrimitiveBranchIr(statement) ?: return null)
                }
            }
            is Scope -> buildList {
                for (statement in node.body) {
                    addAll(lowerPrimitiveBranchIr(statement) ?: return null)
                }
            }
            is Repeat -> lowerRepeatIr(node)
            is WhileLoop -> lowerWhileIr(node)
            is Pipeline, is Quotation -> null
        }

        private fun lowerWhileIr(node: WhileLoop): List<CompilerInstruction>? {
            val usesLatestResult = node.condition.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.SubantaPada &&
                    NaturalSemanticNormalizer.isPriorResult(pada)
            }
            val normalizedTruth = NaturalSemanticNormalizer.normalize(node.condition)
                as? NaturalSemanticNormalizer.Operation.TruthTest
            val reportedOutcome = NaturalSemanticNormalizer.normalize(node.condition)
                as? NaturalSemanticNormalizer.Operation.ReportedOutcomeTest
            val isNegated = normalizedTruth?.negated ?: node.condition.vakya.padas.any { pada ->
                pada is AvyayaPada && pada.function == AvyayaFunction.NISHEDHA
            }
            val condition = if (usesLatestResult) null else {
                if (reportedOutcome != null) {
                    listOf(
                        CompilerInstruction.LoadLastResult,
                        CompilerInstruction.Constant(
                            dev.panini.execution.SanskritValue.Shabda(reportedOutcome.outcomeName),
                        ),
                        CompilerInstruction.Compare(ComparisonOperator.EQUAL),
                    )
                } else {
                    ResolvedLeafPlanner.planAny(node.condition)
                        ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
                        ?.let(CompilerIrLowering::lowerCondition)
                        ?: normalizedTruth?.let { truth ->
                            listOf(
                                CompilerInstruction.Load(truth.stateName),
                                CompilerInstruction.Constant(dev.panini.execution.SanskritValue.Satya(!truth.negated)),
                                CompilerInstruction.Compare(ComparisonOperator.EQUAL),
                            )
                        }
                        ?: runCatching { lower(node.condition) }
                            .getOrNull()
                            ?.plus(
                                listOf(
                                    CompilerInstruction.LoadLastResult,
                                    CompilerInstruction.Booleanize,
                                ),
                            )
                        ?: return null
                }
            }
            val body = lower(node.body)
            val exhausted = node.exhausted?.let { runCatching { lower(it) }.getOrNull() } ?: emptyList()
            if (node.exhausted != null && exhausted.isEmpty()) return null
            val resultTarget = node.resultTarget?.let { target ->
                val plan = (target as? Invocation)?.let { invocation ->
                    ResolvedLeafPlanner.planAny(
                        invocation,
                        injectedBindings = mapOf(
                            Karaka.KARMAN to ExecutionExpression.Reference("चक्रफल"),
                        ),
                    ) ?: ResolvedLeafPlanner.planAny(invocation)
                } ?: ResolvedLeafPlanner.plansAny(target)?.singleOrNull()
                    ?: return null
                CompilerIrLowering.lowerLoopTarget(plan)
            } ?: emptyList()
            val maximumIterations = node.maximumIterationStems.takeIf(List<String>::isNotEmpty)?.let {
                dev.panini.sankhya.SankhyaEvaluator().evaluateStems(it).value
            }
            return CompilerIrLowering.lowerWhileInstructions(
                condition = condition,
                body = body,
                maximumIterations = maximumIterations,
                exhausted = exhausted,
                resultTarget = resultTarget,
                usesReportedCondition = usesLatestResult,
                negatedReportedCondition = reportedOutcome?.negated ?: isNegated,
                namePrefix = "while_${nextLabel++}",
            )
        }

        private fun lowerRepeatIr(node: Repeat): List<CompilerInstruction>? {
            val body = node.body
            val bodyInstructions = runCatching { lower(body) }.getOrNull() ?: return null
            return CompilerIrLowering.lowerRepeat(
                count = node.count,
                body = bodyInstructions,
                namePrefix = "repeat_${nextLabel++}",
            )
        }

        private fun lowerPlannedNode(node: ProgramNode): List<CompilerInstruction> {
            val plans = ResolvedLeafPlanner.plansAny(node)
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PIPELINE,
                    render(node),
                    "Cannot resolve parsed compound node as compiler leaves.",
                )
            return plans.flatMap(::lowerDirect)
        }

        private fun resolveArguments(
            invocation: dev.panini.execution.PrakriyaInvocation,
        ): List<ResolvedPrakriyaArgument> {
            val signature = invocation.kriya.signature
            val resolution = PrakriyaInvocationArgumentResolver.resolve(invocation)
            val resolvedArguments = when (resolution) {
                is PrakriyaArgumentResolution.Success -> resolution.arguments
                is PrakriyaArgumentResolution.Failure -> throw IllegalArgumentException(resolution.message)
            }
            val arguments = resolvedArguments.map { it.argument.term }
            val acceptsCollection = signature.parameters.singleOrNull()?.type == PrakriyaValueType.SUCHI
            require(signature.parameters.size == arguments.size || signature.parameters.isEmpty() || acceptsCollection) {
                dev.panini.execution.PrakriyaDiagnostics.arity(
                    invocation.kriya.nameStem,
                    signature.parameters.size,
                    arguments.size,
                )
            }
            signature.parameters.zip(arguments).takeUnless { acceptsCollection }.orEmpty()
                .forEachIndexed { index, (parameter, argument) ->
                val actual = invocation.argumentValues.getOrNull(index)?.let(PrakriyaValueClassifier::classifyValue)
                    ?: PrakriyaValueClassifier.classifyTerm(argument)
                require(resolvedArguments[index].isPriorResult || actual == parameter.type) {
                    dev.panini.execution.PrakriyaDiagnostics.parameterType(parameter)
                }
            }
            invocation.kriya.nishedhaGuards.forEach { guard ->
                val prohibited = NishedhaGuardEvaluator.isProhibitedResolved(
                    guard,
                    signature.parameters,
                    resolvedArguments,
                    invocation.argumentValues,
                )
                val requiredType = signature.argumentType
                val typeViolated = requiredType != null &&
                    arguments.any { PrakriyaValueClassifier.classifyTerm(it) != requiredType }
                require(!prohibited && !typeViolated) {
                    "निषेध-प्रतिषेधः: Prohibition triggered by '${guard.text.trim()}'"
                }
            }
            return resolvedArguments
        }
    }

    private fun render(node: ProgramNode): String = when (node) {
        is Invocation -> node.vakya.padas.joinToString(" ") { it.sourceText }
        is Sequence -> node.statements.mapIndexed { index, statement ->
            val connector = if (index == 0) "" else "${node.connectors.getOrNull(index - 1) ?: "।"} "
            connector + render(statement)
        }.joinToString(" ")
        else -> node.sourceText
    }

    private fun normalized(source: String): String =
        source.trim().trimEnd('।', '॥').trim() + " ।"

}
