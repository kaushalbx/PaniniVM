package dev.panini.compiler

import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.NamedSamjnaArgumentResolver
import dev.panini.execution.SamjnaKriya
import dev.panini.execution.SamjnaKriyaRegistry
import dev.panini.execution.SamjnaArgumentResolution
import dev.panini.execution.SamjnaSignatureDeclarationParser
import dev.panini.execution.SamjnaValueClassifier
import dev.panini.execution.SamjnaParameter
import dev.panini.execution.SamjnaValueType
import dev.panini.execution.TaddhitaInheritanceEngine
import dev.panini.execution.TaddhitaStructEngine
import dev.panini.execution.DynamicNishedhaEvaluator
import dev.panini.execution.PuranaPratyayaResolver
import dev.panini.execution.SamjnaSignatureCompiler
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.planning.ResolvedLeafPlanner
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
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada

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

    internal fun lowerModule(descriptor: PaniniModuleDescriptor, className: String): CompilerProgram {
        val analyzed = PaniniModuleAnalyzer.analyze(descriptor)
        val registry = SamjnaKriyaRegistry()
        analyzed.inheritance.forEach { (child, parent) ->
            registry.registerInheritance(dev.panini.execution.InheritanceRelation(child, parent))
        }
        analyzed.procedures.forEach { procedure ->
            registry.register(
                SamjnaKriya(
                    nameSegmented = procedure.definition.nameSegmented,
                    nameStem = procedure.symbol,
                    body = procedure.definition.body,
                    sourceFile = procedure.source.name,
                    domainStem = procedure.domain,
                    isInternal = procedure.visibility == PaniniSymbolVisibility.INTERNAL,
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
                    SamjnaKriya(
                        nameSegmented = procedure.symbol,
                        nameStem = procedure.symbol,
                        body = emptyList(),
                        sourceFile = "${dependency.moduleName}.pvmmeta",
                        domainStem = procedure.domain,
                        signatureOverride = dev.panini.execution.SamjnaSignature(
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
                    put(CompilerSymbols.localStem(procedure.symbol), target)
                }
            }
            analyzed.procedures.forEach { procedure ->
                put(procedure.symbol, ProcedureTarget.Local(procedure.methodName))
                put(procedure.localSymbol, ProcedureTarget.Local(procedure.methodName))
            }
        }
        val lowering = Lowering(registry, methodsByStem)
        val entryPoint = analyzed.statements.filterKeys(PaniniModuleSource::isEntryPoint).values.flatMap { statements ->
            statements.filterIsInstance<PvmScriptStatement.Sentence>().flatMap { sentence ->
                lowering.lowerTopLevel(sentence)
            }
        }
        val procedures = analyzed.procedures.map { procedure ->
            val definition = procedure.definition
            val signature = procedure.signature
            val instructions = definition.body.filterNot { sentence ->
                sentence.isNishedha || SamjnaSignatureDeclarationParser.isDeclaration(sentence)
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
                    procedure.parameters.map(SamjnaParameter::nameStem),
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
        private val registry: SamjnaKriyaRegistry,
        private val methodsByStem: Map<String, ProcedureTarget>,
    ) {
        private var nextLabel = 0

        fun lowerTopLevel(sentence: PvmScriptStatement.Sentence): List<CompilerInstruction> {
            TaddhitaStructEngine.detectStructConstruction(sentence.text, sentence.ukti)?.let { struct ->
                return buildList {
                    struct.attributes.values.forEach { add(CompilerInstruction.Constant(sourceValue(it))) }
                    add(CompilerInstruction.BuildRecord(struct.nameStem, struct.attributes.keys.toList()))
                    add(CompilerInstruction.Duplicate)
                    add(CompilerInstruction.Store(struct.nameStem))
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

        private fun sourceValue(source: String): dev.panini.execution.SanskritValue =
            runCatching {
                val parts = source.split('+').map(String::trim).filter(String::isNotEmpty)
                val value = dev.panini.sankhya.SankhyaEvaluator().evaluateStems(parts).value
                dev.panini.execution.SanskritValue.Sankhya(value, source)
            }.getOrElse { dev.panini.execution.SanskritValue.of(source) }
        fun lower(
            node: ProgramNode,
            exactSource: String? = null,
            allowDirectStore: Boolean = false,
        ): List<CompilerInstruction> = when (node) {
            is Invocation -> lowerInvocation(node, exactSource, allowDirectStore = allowDirectStore)
            is Sequence -> lowerSequence(node, exactSource)
            is Pipeline -> lowerPipeline(node)
            is Quotation -> lowerPlannedSource(exactSource ?: render(node))
            is Conditional -> lowerConditionalIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.CONDITIONAL, render(node), "Cannot lower conditional to compiler IR.",
            )
            is Repeat -> lowerRepeatIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.REPETITION, render(node), "Cannot lower repetition body to compiler IR.",
            )
            is WhileLoop -> lowerWhileIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.LOOP, render(node), "Cannot lower condition-controlled loop to compiler IR.",
            )
            is Procedure -> node.body.flatMap { lower(it) }
            is Scope -> node.body.flatMap { lower(it) }
        }

        private fun lowerSequence(node: Sequence, exactSource: String?): List<CompilerInstruction> {
            if (node.connectors.any { it != "ततः" } || node.statements.any { it !is Invocation }) {
                return lowerPlannedSource(exactSource ?: render(node))
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
                    pada.pratipadika.sourceText.substringBefore('+').trim() == "फल"
            }
            val source = normalized(
                if (piped && !alreadyReferencesResult) "फल + अम् $rendered" else rendered,
            )
            val collectionParameter = node.vakya.padas.filterIsInstance<SubantaPada>().any { pada ->
                (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.SAMAVAYA
            }
            val dhatu = node.vakya.padas.filterIsInstance<TingantaPada>().singleOrNull()?.dhatu?.mulaDhatu
            if (collectionParameter && dhatu == "युज्") {
                return listOf(
                    CompilerInstruction.Load("समवाय"),
                    CompilerInstruction.Collection(CollectionOperator.SUM),
                    CompilerInstruction.Store("LastResult"),
                )
            }
            lowerImplicitParameterOperation(node, dhatu)?.let { return it }
            return lowerSource(source, allowDirectStore)
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.INVOCATION, source, "Cannot resolve invocation as a compiler leaf.",
                )
        }

        private fun lowerImplicitParameterOperation(
            node: Invocation,
            dhatu: String?,
        ): List<CompilerInstruction>? {
            val operator = when (dhatu) {
                "युज्", "युज" -> ArithmeticOperator.ADD
                "गण्", "गण" -> ArithmeticOperator.MULTIPLY
                "भाज्", "भाज" -> ArithmeticOperator.DIVIDE
                else -> return null
            }
            val operands = node.vakya.padas.mapNotNull { pada ->
                PuranaPratyayaResolver.ordinalValue(pada)?.let { ordinal ->
                    val name = when (ordinal) { 1L -> "प्रथम"; 2L -> "द्वितीय"; 3L -> "तृतीय"; else -> return@let null }
                    return@mapNotNull CompilerInstruction.Load(name)
                }
                val subanta = pada as? SubantaPada ?: return@mapNotNull null
                if (subanta.pratipadika.sourceText.substringBefore('+').trim() == "फल") {
                    CompilerInstruction.LoadLastResult
                } else null
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

        private fun lowerSource(source: String, allowStore: Boolean = false): List<CompilerInstruction>? =
            lowerProcedureCall(source)
                ?: ResolvedLeafPlanner.plan(source, allowStore = allowStore)?.let(::lowerDirect)
                ?: ResolvedLeafPlanner.plansAny(source)?.flatMap(::lowerDirect)

        private fun lowerProcedureCall(source: String): List<CompilerInstruction>? {
            val invocation = registry.detectInvocation(source) ?: return null
            val target = invocation.kriya.nameStem.let(methodsByStem::get) ?: return null
            val signature = invocation.kriya.signature
            val parameterNames = signature.parameters.map { it.nameStem }
            val parameterKinds = signature.parameters.map { it.type.toCompilerValueKind() }
            val arguments = resolveArguments(invocation)
            return buildList {
                if (signature.parameters.singleOrNull()?.type == SamjnaValueType.SUCHI) {
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
            argument: String,
            value: dev.panini.execution.SanskritValue?,
        ): CompilerInstruction {
            val name = argument.substringBefore('+').trim()
            return when {
                name == "फल" -> CompilerInstruction.LoadLastResult
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
                    ?: registry.all().singleOrNull { localSamjnaStem(it.nameStem) == stage.operationStem }
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
                        val argument = node.arguments.getOrNull(parameterIndex)
                            ?: throw CompilerUnsupportedException(
                                CompilerUnsupportedKind.PIPELINE,
                                node.sourceText,
                                "Pipeline stage '${stage.operationStem}' lacks argument ${parameterIndex + 1}.",
                            )
                        add(CompilerInstruction.ResolveArgument(argument.substringBefore('+').trim(), null))
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
            val condition = ResolvedLeafPlanner.planAny(render(node.condition))
                ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
                ?: return null
            val consequent = lowerPrimitiveBranchIr(node.consequent) ?: return null
            val alternate = node.alternate?.let(::lowerPrimitiveBranchIr) ?: emptyList()
            if (node.alternate != null && alternate.isEmpty()) return null
            return CompilerIrLowering.lowerConditional(
                condition = CompilerIrLowering.lowerCondition(condition),
                consequent = consequent,
                alternate = alternate,
                labelPrefix = "conditional_${nextLabel++}",
            )
        }

        private fun lowerPrimitiveBranchIr(node: ProgramNode): List<CompilerInstruction>? = when (node) {
            is Invocation -> {
                val source = normalized(render(node))
                lowerSource(source)
            }
            is Conditional -> lowerConditionalIr(node)
            is Sequence -> buildList {
                for (statement in node.statements) {
                    addAll(lowerPrimitiveBranchIr(statement) ?: return null)
                }
            }
            is Procedure -> buildList {
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
                    pada.pratipadika.sourceText.substringBefore('+').trim() == "फल"
            }
            val isNegated = node.condition.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.AvyayaPada && pada.form == "न"
            }
            val condition = if (usesLatestResult) null else {
                ResolvedLeafPlanner.planAny(render(node.condition))
                    ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
                    ?: return null
            }
            val body = lowerPrimitiveBranchIr(node.body) ?: return null
            val exhausted = node.exhausted?.let(::lowerPrimitiveBranchIr) ?: emptyList()
            if (node.exhausted != null && exhausted.isEmpty()) return null
            val resultTarget = node.resultTarget?.let { target ->
                val rendered = render(target)
                val plan = ResolvedLeafPlanner.planAny("चक्रफल + अम् $rendered")
                    ?: ResolvedLeafPlanner.planAny(rendered)
                    ?: return null
                CompilerIrLowering.lowerLoopTarget(plan)
            } ?: emptyList()
            val maximumIterations = node.maximumIterationStems.takeIf(List<String>::isNotEmpty)?.let {
                dev.panini.sankhya.SankhyaEvaluator().evaluateStems(it).value
            }
            return CompilerIrLowering.lowerWhileInstructions(
                condition = condition?.let(CompilerIrLowering::lowerCondition),
                body = body,
                maximumIterations = maximumIterations,
                exhausted = exhausted,
                resultTarget = resultTarget,
                usesReportedCondition = usesLatestResult,
                negatedReportedCondition = isNegated,
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

        private fun lowerPlannedSource(source: String): List<CompilerInstruction> {
            val plans = ResolvedLeafPlanner.plansAny(source)
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PIPELINE, source, "Cannot resolve pipeline stage as compiler leaves.",
                )
            return plans.flatMap(::lowerDirect)
        }

        private fun resolveArguments(invocation: dev.panini.execution.SamjnaInvocation): List<String> {
            val signature = invocation.kriya.signature
            val resolution = NamedSamjnaArgumentResolver.resolve(invocation.karmaText, signature)
            val arguments = when (resolution) {
                is SamjnaArgumentResolution.Success -> resolution.terms
                is SamjnaArgumentResolution.Failure -> throw IllegalArgumentException(resolution.message)
            }
            val acceptsCollection = signature.parameters.singleOrNull()?.type == SamjnaValueType.SUCHI
            require(signature.parameters.size == arguments.size || signature.parameters.isEmpty() || acceptsCollection) {
                "संज्ञा-मानसङ्ख्या: '${invocation.kriya.nameStem}' expects ${signature.parameters.size} arguments, but received ${arguments.size}."
            }
            signature.parameters.zip(arguments).takeUnless { acceptsCollection }.orEmpty()
                .forEachIndexed { index, (parameter, argument) ->
                val actual = invocation.argumentValues.getOrNull(index)?.let(SamjnaValueClassifier::classifyValue)
                    ?: SamjnaValueClassifier.classifyTerm(argument)
                require(argument.substringBefore('+').trim() == "फल" || actual == parameter.type) {
                    "संज्ञा-मानप्रकारः: '${parameter.nameStem}' requires ${parameter.type}."
                }
            }
            invocation.kriya.nishedhaGuards.forEach { guard ->
                var guardText = guard.text
                arguments.forEachIndexed { index, argument ->
                    guardText = PuranaPratyayaResolver.replacePatterns(guardText, index, argument)
                }
                val prohibited = DynamicNishedhaEvaluator.evaluateProhibition(guardText)
                val requiredType = SamjnaSignatureCompiler.inferGuardType(guardText)
                val typeViolated = requiredType != null &&
                    arguments.any { SamjnaValueClassifier.classifyTerm(it) != requiredType }
                require(!prohibited && !typeViolated) {
                    "निषेध-प्रतिषेधः: Prohibition triggered by '${guard.text.trim()}'"
                }
            }
            return arguments
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

    private fun localSamjnaStem(stem: String): String =
        CompilerSymbols.localStem(stem)

}
