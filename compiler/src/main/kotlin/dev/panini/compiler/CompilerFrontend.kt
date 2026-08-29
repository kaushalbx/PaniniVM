package dev.panini.compiler

import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.NamedSamjnaArgumentResolver
import dev.panini.execution.SamjnaKriya
import dev.panini.execution.SamjnaKriyaRegistry
import dev.panini.execution.SamjnaArgumentResolution
import dev.panini.execution.SamjnaSignatureDeclarationParser
import dev.panini.execution.SamjnaValueClassifier
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

/** Lowers grammatical control flow to JVM branches while leaves use the normal action runtime. */
internal object CompilerFrontend {
    fun compile(scriptContent: String, className: String): ByteArray =
        CompilerProgramJvmEmitter.emit(lower(scriptContent, className))

    /** Frontend boundary: parses and lowers a complete source unit without emitting JVM bytecode. */
    internal fun lower(scriptContent: String, className: String): CompilerProgram {
        val statements = PvmScript.parse(scriptContent)
        val definitions = statements.filterIsInstance<PvmScriptStatement.SamjnaDefinition>()
        val registry = SamjnaKriyaRegistry()
        definitions.forEach { definition ->
            registry.register(
                SamjnaKriya(
                    nameSegmented = definition.nameSegmented,
                    nameStem = samjnaStem(definition.nameSegmented),
                    body = definition.body,
                    domainStem = definition.domainStem,
                ),
            )
        }
        val methods = definitions.mapIndexed { index, definition ->
            definition to "samjna_$index"
        }.toMap()
        val methodsByStem = buildMap {
            methods.forEach { (definition, method) ->
                val stem = samjnaStem(definition.nameSegmented)
                put(stem, method)
                put(localSamjnaStem(stem), method)
            }
        }
        val lowering = Lowering(registry, methodsByStem)
        val executable = statements.filterIsInstance<PvmScriptStatement.Sentence>()
        val entryPoint = executable.flatMap { sentence ->
            sentence.program?.let {
                lowering.lower(it, sentence.text, allowDirectStore = true)
            }.orEmpty()
        }
        val procedures = definitions.map { definition ->
            val signature = requireNotNull(registry.resolve(samjnaStem(definition.nameSegmented))).signature
            val instructions = definition.body.filterNot { sentence ->
                sentence.isNishedha || SamjnaSignatureDeclarationParser.isDeclaration(sentence)
            }.flatMap { sentence ->
                sentence.program?.let {
                    lowering.lower(it, sentence.text) + CompilerInstruction.ReturnIfBreak
                }.orEmpty()
            } + CompilerInstruction.Return
            CompilerProcedure(
                methodName = requireNotNull(methods[definition]),
                instructions = instructions,
                parameterNames = signature.parameters.map { it.nameStem },
                parameterKinds = signature.parameters.map { it.type.toCompilerValueKind() },
                returnKind = signature.resultType?.toCompilerValueKind()
                    ?: signature.resultSchema?.let { CompilerValueKind.RECORD },
            )
        }
        return CompilerProgram(className, entryPoint, procedures).also(CompilerProgramVerifier::verify)
    }

    private class Lowering(
        private val registry: SamjnaKriyaRegistry,
        private val methodsByStem: Map<String, String>,
    ) {
        private var nextLabel = 0
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
            return lowerSource(source, allowDirectStore)
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.INVOCATION, source, "Cannot resolve invocation as a compiler leaf.",
                )
        }

        private fun lowerSource(source: String, allowStore: Boolean = false): List<CompilerInstruction>? =
            lowerProcedureCall(source)
                ?: ResolvedLeafPlanner.plan(source, allowStore = allowStore)?.let(::lowerDirect)
                ?: ResolvedLeafPlanner.plansAny(source)?.flatMap(::lowerDirect)

        private fun lowerProcedureCall(source: String): List<CompilerInstruction>? {
            val invocation = registry.detectInvocation(source) ?: return null
            val method = invocation.kriya.nameStem.let(methodsByStem::get) ?: return null
            val signature = invocation.kriya.signature
            val parameterNames = signature.parameters.map { it.nameStem }
            val parameterKinds = signature.parameters.map { it.type.toCompilerValueKind() }
            val arguments = resolveArguments(invocation)
            return buildList {
                arguments.take(parameterNames.size).forEachIndexed { index, argument ->
                    val name = argument.substringBefore('+').trim()
                    val value = invocation.argumentValues.getOrNull(index)
                    add(
                        when {
                            name == "फल" -> CompilerInstruction.LoadLastResult
                            value != null -> CompilerInstruction.Constant(value)
                            else -> CompilerInstruction.ResolveArgument(name, null)
                        },
                    )
                }
                add(CompilerInstruction.EnterFrame(parameterNames, parameterKinds))
                add(CompilerInstruction.InvokeProcedure(method, parameterNames.size))
                add(CompilerInstruction.ExitFrame)
            }
        }

        private fun lowerPipeline(node: Pipeline): List<CompilerInstruction> = buildList {
            node.stages.forEachIndexed { stageIndex, stage ->
                val method = methodsByStem[stage.operationStem]
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
                add(CompilerInstruction.InvokeProcedure(method, parameters.size))
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
            require(signature.parameters.size == arguments.size || signature.parameters.isEmpty()) {
                "संज्ञा-मानसङ्ख्या: '${invocation.kriya.nameStem}' expects ${signature.parameters.size} arguments, but received ${arguments.size}."
            }
            signature.parameters.zip(arguments).forEachIndexed { index, (parameter, argument) ->
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

    private fun samjnaStem(name: String): String {
        val parts = name.split('+').map(String::trim).filter(String::isNotEmpty)
        return if (parts.lastOrNull() in setOf(
                "सुँ", "औ", "जस्", "अम्", "औट्", "शस्", "टा", "भ्याम्", "भिस्",
                "ङे", "भ्यस्", "ङसि", "ङसिँ", "ङस्", "ओस्", "आम्", "ङि", "सुप्",
            )) {
            parts.dropLast(1).joinToString(" + ")
        } else {
            name.trim()
        }
    }

    private fun localSamjnaStem(stem: String): String =
        stem.substringAfter("ङस्", stem).trim().trimStart('+').trim()

}
