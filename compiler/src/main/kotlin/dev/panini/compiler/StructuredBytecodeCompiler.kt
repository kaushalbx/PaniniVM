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
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.bindingName
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
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

/** Lowers grammatical control flow to JVM branches while leaves use the normal action runtime. */
internal object StructuredBytecodeCompiler {
    fun supports(statements: List<PvmScriptStatement>): Boolean =
        statements.any { it is PvmScriptStatement.SamjnaDefinition } ||
            statements.filterIsInstance<PvmScriptStatement.Sentence>().any { sentence ->
                sentence.program?.containsControlFlow() == true
            }

    fun compile(scriptContent: String, className: String): ByteArray {
        val statements = PvmScript.parse(scriptContent)
        val definitions = statements.filterIsInstance<PvmScriptStatement.SamjnaDefinition>()
        val registry = SamjnaKriyaRegistry()
        definitions.forEach { definition ->
            registry.register(
                SamjnaKriya(
                    nameSegmented = definition.nameSegmented,
                    nameStem = samjnaStem(definition.nameSegmented),
                    body = definition.body,
                ),
            )
        }
        val methods = definitions.mapIndexed { index, definition ->
            definition to "samjna_$index"
        }.toMap()
        val methodsByStem = methods.entries.associate { (definition, method) ->
            samjnaStem(definition.nameSegmented) to method
        }
        val lowering = Lowering(className, registry, methodsByStem)

        val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        cw.visit(V1_8, ACC_PUBLIC or ACC_SUPER, className, null, "java/lang/Object", null)
        emitConstructor(cw)

        val executable = statements.filterIsInstance<PvmScriptStatement.Sentence>()
        emitExecute(cw, lowering, executable, withLimit = false)
        emitExecute(cw, lowering, executable, withLimit = true)

        definitions.forEach { definition ->
            val method = requireNotNull(methods[definition])
            val mv = cw.visitMethod(
                ACC_PRIVATE or ACC_STATIC,
                method,
                "(Ldev/panini/compiler/CompiledProgramRuntime;)V",
                null,
                null,
            )
            mv.visitCode()
            definition.body.filterNot { sentence ->
                sentence.isNishedha || SamjnaSignatureDeclarationParser.isDeclaration(sentence)
            }.forEach { sentence ->
                sentence.program?.let {
                    lowering.emit(mv, it, sentence.text)
                    lowering.emitReturnIfBreak(mv)
                }
            }
            lowering.emitReturn(mv)
            mv.visitMaxs(0, 0)
            mv.visitEnd()
        }

        val main = cw.visitMethod(ACC_PUBLIC or ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        main.visitCode()
        main.visitMethodInsn(INVOKESTATIC, className, "execute", "()Ljava/util/Map;", false)
        main.visitInsn(POP)
        main.visitInsn(RETURN)
        main.visitMaxs(0, 0)
        main.visitEnd()
        cw.visitEnd()
        return cw.toByteArray()
    }

    private class Lowering(
        private val className: String,
        private val registry: SamjnaKriyaRegistry,
        private val methodsByStem: Map<String, String>,
    ) {
        private var nextLocal = 1
        private var nextLabel = 0

        fun emit(
            mv: MethodVisitor,
            node: ProgramNode,
            exactSource: String? = null,
            allowDirectStore: Boolean = false,
        ) {
            when (node) {
                is Invocation -> emitInvocation(mv, node, exactSource, allowDirectStore = allowDirectStore)
                is Sequence -> emitSequence(mv, node, exactSource)
                is Pipeline, is Quotation -> emitPlannedSource(mv, exactSource ?: render(node))
                is Conditional -> emitConditional(mv, node)
                is Repeat -> emitRepeat(mv, node)
                is WhileLoop -> emitWhile(mv, node)
                is Procedure -> node.body.forEach { emit(mv, it) }
                is Scope -> node.body.forEach { emit(mv, it) }
            }
        }

        fun emitDirectPlan(mv: MethodVisitor, plan: ExecutionPlan) = emitDirect(mv, plan)

        private fun emitIr(mv: MethodVisitor, instructions: List<CompilerInstruction>) {
            CompilerIrJvmEmitter(className, mv) { width ->
                val local = nextLocal
                nextLocal += width
                local
            }.emit(instructions)
        }

        fun emitDirectPlans(mv: MethodVisitor, plans: List<ExecutionPlan>) =
            plans.forEach { emitDirect(mv, it) }

        private fun emitSequence(mv: MethodVisitor, node: Sequence, exactSource: String?) {
            if (node.connectors.any { it != "ततः" } || node.statements.any { it !is Invocation }) {
                emitPlannedSource(mv, exactSource ?: render(node))
                return
            }
            node.statements.forEachIndexed { index, statement ->
                if (index == 0) {
                    emit(mv, statement)
                } else if (statement is Invocation) {
                    emitInvocation(mv, statement, exactSource = null, piped = true)
                } else {
                    emit(mv, statement)
                }
            }
        }

        fun emitReturnIfBreak(mv: MethodVisitor) {
            emitIr(mv, listOf(CompilerInstruction.ReturnIfBreak))
        }

        fun emitReturn(mv: MethodVisitor) = emitIr(mv, listOf(CompilerInstruction.Return))

        private fun emitInvocation(
            mv: MethodVisitor,
            node: Invocation,
            exactSource: String?,
            piped: Boolean = false,
            allowDirectStore: Boolean = false,
        ) {
            val rendered = exactSource ?: render(node)
            val repetition = Regex("^([^\\s+]+)\\s*\\+\\s*[^\\s]*कृत्व[^\\s]*\\s+(.+)$")
                .find(rendered.trim())
            if (repetition != null) {
                val count = dev.panini.sankhya.SankhyaEvaluator()
                    .evaluateStems(listOf(repetition.groupValues[1])).value.toInt()
                val repeatedSource = normalized(repetition.groupValues[2])
                val repeatedInstruction = lowerProcedureCall(repeatedSource)
                    ?: DirectLeafPlanner.planAny(repeatedSource)?.let(CompilerIrLowering::lowerLeaf)
                val instruction = requireNotNull(repeatedInstruction) {
                    "The JVM compiler cannot lower repeated invocation to IR: $repeatedSource"
                }
                emitIr(
                    mv,
                    CompilerIrLowering.lowerRepeat(
                        count = count,
                        body = listOf(instruction),
                        namePrefix = "repeat_${nextLabel++}",
                    ),
                )
                return
            }
            val alreadyReferencesResult = node.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.SubantaPada &&
                    pada.pratipadika.sourceText.substringBefore('+').trim() == "फल"
            }
            val source = normalized(
                if (piped && !alreadyReferencesResult) "फल + अम् $rendered" else rendered,
            )
            val procedureCall = lowerProcedureCall(source)
            if (procedureCall != null) {
                emitIr(mv, listOf(procedureCall))
            } else {
                val directPlan = DirectLeafPlanner.plan(source, allowStore = allowDirectStore)
                if (directPlan != null) {
                    emitDirect(mv, directPlan)
                } else {
                    val generalPlan = DirectLeafPlanner.planAny(source)
                        ?: error("The JVM compiler cannot preplan invocation: $source")
                    emitDirect(mv, generalPlan)
                }
            }
        }

        private fun lowerProcedureCall(source: String): CompilerInstruction.ProcedureCall? {
            val invocation = registry.detectInvocation(source) ?: return null
            val method = invocation.kriya.nameStem.let(methodsByStem::get) ?: return null
            val signature = invocation.kriya.signature
            return CompilerInstruction.ProcedureCall(
                methodName = method,
                parameterNames = signature.parameters.map { it.nameStem },
                arguments = resolveArguments(invocation),
                argumentValues = List(signature.parameters.size) { index ->
                    invocation.argumentValues.getOrNull(index)
                },
            )
        }

        private fun emitDirect(
            mv: MethodVisitor,
            plan: ExecutionPlan,
            asBoolean: Boolean = false,
            asLoopTarget: Boolean = false,
        ) {
            val instruction = CompilerIrLowering.lowerLeaf(
                plan,
                resultMode = when {
                    asBoolean -> CallResultMode.BOOLEAN
                    asLoopTarget -> CallResultMode.LOOP_TARGET
                    else -> CallResultMode.VALUE
                },
            )
            emitIr(mv, listOf(instruction))
        }

        private fun emitConditional(mv: MethodVisitor, node: Conditional) {
            val instructions = requireNotNull(lowerConditionalIr(node)) {
                "The JVM compiler cannot lower conditional to IR: ${render(node)}"
            }
            emitIr(mv, instructions)
        }

        /**
         * Produces complete IR for conditionals whose leaves are primitive plans.
         * Named calls continue through the existing emitter until Call IR carries
         * procedure invocation and argument-frame semantics.
         */
        private fun lowerConditionalIr(node: Conditional): List<CompilerInstruction>? {
            val condition = DirectLeafPlanner.planAny(render(node.condition))
                ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
                ?: return null
            val consequent = lowerPrimitiveBranchIr(node.consequent) ?: return null
            val alternate = node.alternate?.let(::lowerPrimitiveBranchIr) ?: emptyList()
            if (node.alternate != null && alternate.isEmpty()) return null
            return CompilerIrLowering.lowerConditional(
                condition = CompilerIrLowering.lowerLeaf(
                    condition,
                    CallResultMode.BOOLEAN,
                ) as CompilerInstruction.Call,
                consequent = consequent,
                alternate = alternate,
                labelPrefix = "conditional_${nextLabel++}",
            )
        }

        private fun lowerPrimitiveBranchIr(node: ProgramNode): List<CompilerInstruction>? = when (node) {
            is Invocation -> {
                val source = normalized(render(node))
                val instruction = lowerProcedureCall(source)
                    ?: DirectLeafPlanner.planAny(source)?.let(CompilerIrLowering::lowerLeaf)
                instruction?.let(::listOf)
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

        private fun emitWhile(mv: MethodVisitor, node: WhileLoop) {
            val instructions = requireNotNull(lowerWhileIr(node)) {
                "The JVM compiler cannot lower while loop to IR: ${render(node)}"
            }
            emitIr(mv, instructions)
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
                DirectLeafPlanner.planAny(render(node.condition))
                    ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
                    ?: return null
            }
            val body = lowerPrimitiveBranchIr(node.body) ?: return null
            val exhausted = node.exhausted?.let(::lowerPrimitiveBranchIr) ?: emptyList()
            if (node.exhausted != null && exhausted.isEmpty()) return null
            val resultTarget = node.resultTarget?.let { target ->
                val rendered = render(target)
                val plan = DirectLeafPlanner.planAny(rendered)
                    ?: DirectLeafPlanner.planAny("चक्रफल + अम् $rendered")
                    ?: return null
                listOf(CompilerIrLowering.lowerLeaf(plan, CallResultMode.LOOP_TARGET))
            } ?: emptyList()
            val maximumIterations = node.maximumIterationStems.takeIf(List<String>::isNotEmpty)?.let {
                dev.panini.sankhya.SankhyaEvaluator().evaluateStems(it).value
            }
            return CompilerIrLowering.lowerWhile(
                condition = condition?.let {
                    CompilerIrLowering.lowerLeaf(it, CallResultMode.BOOLEAN) as CompilerInstruction.Call
                },
                body = body,
                maximumIterations = maximumIterations,
                exhausted = exhausted,
                resultTarget = resultTarget,
                usesReportedCondition = usesLatestResult,
                negatedReportedCondition = isNegated,
                namePrefix = "while_${nextLabel++}",
            )
        }

        private fun emitRepeat(mv: MethodVisitor, node: Repeat) {
            val instructions = requireNotNull(lowerRepeatIr(node)) {
                "The JVM compiler cannot lower repetition to IR: ${render(node)}"
            }
            emitIr(mv, instructions)
        }

        private fun lowerRepeatIr(node: Repeat): List<CompilerInstruction>? {
            val body = node.body
            val bodyInstructions = if (body is Invocation) {
                val renderedBody = render(body)
                val bodySource = normalized(renderedBody.split(Regex("\\s+")).drop(3).joinToString(" "))
                val instruction = lowerProcedureCall(bodySource)
                    ?: DirectLeafPlanner.planAny(bodySource)?.let(CompilerIrLowering::lowerLeaf)
                instruction?.let(::listOf)
            } else {
                lowerPrimitiveBranchIr(body)
            } ?: return null
            return CompilerIrLowering.lowerRepeat(
                count = node.count,
                body = bodyInstructions,
                namePrefix = "repeat_${nextLabel++}",
            )
        }

        private fun emitPlannedSource(mv: MethodVisitor, source: String) {
            val plans = DirectLeafPlanner.plansAny(source)
                ?: error("The JVM compiler cannot preplan source without the interpreter bridge: $source")
            plans.forEach { emitDirect(mv, it) }
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

    private fun ProgramNode.containsControlFlow(): Boolean = when (this) {
        is Conditional, is Repeat, is WhileLoop -> true
        is Sequence -> statements.any { it.containsControlFlow() }
        is Quotation -> reporting.containsControlFlow()
        is Procedure -> body.any { it.containsControlFlow() }
        is Scope -> body.any { it.containsControlFlow() }
        is Invocation, is Pipeline -> false
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
        return if (parts.lastOrNull() in setOf("सुँ", "औ", "जस्", "अम्", "औट्", "शस्", "टा")) {
            parts.dropLast(1).joinToString(" + ")
        } else {
            name.trim()
        }
    }

    private fun emitConstructor(cw: ClassWriter) {
        val mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(RETURN)
        mv.visitMaxs(0, 0)
        mv.visitEnd()
    }

    private fun emitExecute(
        cw: ClassWriter,
        lowering: Lowering,
        statements: List<PvmScriptStatement.Sentence>,
        withLimit: Boolean,
    ) {
        val descriptor = if (withLimit) "(J)Ljava/util/Map;" else "()Ljava/util/Map;"
        val mv = cw.visitMethod(ACC_PUBLIC or ACC_STATIC, "execute", descriptor, null, null)
        mv.visitCode()
        mv.visitTypeInsn(NEW, "dev/panini/compiler/CompiledProgramRuntime")
        mv.visitInsn(DUP)
        if (withLimit) mv.visitVarInsn(LLOAD, 0)
        mv.visitMethodInsn(
            INVOKESPECIAL,
            "dev/panini/compiler/CompiledProgramRuntime",
            "<init>",
            if (withLimit) "(J)V" else "()V",
            false,
        )
        mv.visitVarInsn(ASTORE, 0)
        val directSlice = planDirectStraightLine(statements)
        statements.forEachIndexed { index, sentence ->
            val directPlan = directSlice?.get(index)
            if (directPlan != null) {
                lowering.emitDirectPlan(mv, directPlan)
            } else sentence.program?.let {
                lowering.emit(
                    mv,
                    it,
                    sentence.text,
                    allowDirectStore = index == statements.lastIndex,
                )
            }
        }
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "dev/panini/compiler/CompiledProgramRuntime",
            "snapshot",
            "()Ljava/util/Map;",
            false,
        )
        mv.visitInsn(ARETURN)
        mv.visitMaxs(0, 0)
        mv.visitEnd()
    }

    /**
     * Validates an entire straight-line slice before allowing a nonterminal direct store.
     * This keeps compiled state authoritative: a slice either stays wholly direct or retains
     * the existing bridge, except for the independently safe terminal-store optimization.
     */
    private fun planDirectStraightLine(
        statements: List<PvmScriptStatement.Sentence>,
    ): List<ExecutionPlan>? {
        if (statements.isEmpty() || statements.any { it.program !is Invocation }) return null
        var environment = ValueEnvironment()
        return buildList {
            for (sentence in statements) {
                val plan = DirectLeafPlanner.plan(
                    sentence.text,
                    environment = environment,
                    allowStore = true,
                ) ?: DirectLeafPlanner.planAny(
                    sentence.text,
                    environment = environment,
                )?.takeIf {
                    it.resolved.operation.name in setOf("सूचीसंयोजनम्", "सूचीशोधनम्", "सूचीसङ्क्षेपः")
                } ?: return null
                environment = advancePlanningEnvironment(plan, environment) ?: return null
                add(plan)
            }
        }
    }

    private fun advancePlanningEnvironment(
        plan: ExecutionPlan,
        environment: ValueEnvironment,
    ): ValueEnvironment? {
        val result = runCatching {
            PaniniRuntime.execute(
                plan.resolved.invocation.dhatu.upadesha,
                plan.resolved.operation.name,
                plan.resolved.operation.trigger.requiredSanadi.sorted().joinToString(","),
                plan.resolved.context.bindings,
                environment.values,
            )
        }.getOrNull() ?: return null
        val destination = plan.resolved.operation.resultBindingKaraka
            ?.let(plan.resolved.context.bindings::get)
            ?.bindingName()
        return ValueEnvironment(
            environment.values + ("LastResult" to result) +
                (destination?.let { mapOf(it to result) } ?: emptyMap()),
        )
    }


}
