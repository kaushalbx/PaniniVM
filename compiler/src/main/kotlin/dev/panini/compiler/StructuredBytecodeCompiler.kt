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
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.SanskritValue
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
import org.objectweb.asm.Label
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
            mv.visitInsn(RETURN)
            mv.visitMaxs(0, 0)
            mv.visitEnd()
        }

        val executable = statements.filterIsInstance<PvmScriptStatement.Sentence>()
        emitExecute(cw, lowering, executable, withLimit = false)
        emitExecute(cw, lowering, executable, withLimit = true)

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

        fun emit(
            mv: MethodVisitor,
            node: ProgramNode,
            exactSource: String? = null,
            allowDirectStore: Boolean = false,
        ) {
            when (node) {
                is Invocation -> emitInvocation(mv, node, exactSource, allowDirectStore = allowDirectStore)
                is Sequence -> emitSequence(mv, node, exactSource)
                is Pipeline, is Quotation -> emitEval(mv, exactSource ?: render(node))
                is Conditional -> emitConditional(mv, node)
                is Repeat -> emitRepeat(mv, node)
                is WhileLoop -> emitWhile(mv, node)
                is Procedure -> node.body.forEach { emit(mv, it) }
                is Scope -> node.body.forEach { emit(mv, it) }
            }
        }

        fun emitDirectPlan(mv: MethodVisitor, plan: ExecutionPlan) = emitDirect(mv, plan)

        private fun emitSequence(mv: MethodVisitor, node: Sequence, exactSource: String?) {
            val hasGeneratedStage = node.statements.drop(1).any { statement ->
                if (statement !is Invocation) return@any false
                val pipedSource = normalized("फल + अम् ${render(statement)}")
                registry.detectInvocation(pipedSource)?.kriya?.nameStem in methodsByStem
            }
            if (node.connectors.any { it != "ततः" } || !hasGeneratedStage) {
                emitEval(mv, exactSource ?: render(node))
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
            val continueExecution = Label()
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "isBreakRequested",
                "()Z",
                false,
            )
            mv.visitJumpInsn(IFEQ, continueExecution)
            mv.visitInsn(RETURN)
            mv.visitLabel(continueExecution)
        }

        private fun emitInvocation(
            mv: MethodVisitor,
            node: Invocation,
            exactSource: String?,
            piped: Boolean = false,
            allowDirectStore: Boolean = false,
        ) {
            val rendered = exactSource ?: render(node)
            val alreadyReferencesResult = node.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.SubantaPada &&
                    pada.pratipadika.sourceText.substringBefore('+').trim() == "फल"
            }
            val source = normalized(
                if (piped && !alreadyReferencesResult) "फल + अम् $rendered" else rendered,
            )
            val invocation = registry.detectInvocation(source)
            val method = invocation?.kriya?.nameStem?.let(methodsByStem::get)
            if (method != null) {
                val signature = requireNotNull(invocation).kriya.signature
                val arguments = resolveArguments(invocation)
                emitStringArray(mv, signature.parameters.map { it.nameStem })
                emitStringArray(mv, arguments)
                mv.visitVarInsn(ALOAD, 0)
                mv.visitInsn(DUP_X2)
                mv.visitInsn(POP)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "dev/panini/compiler/CompiledProgramRuntime",
                    "enterFrame",
                    "([Ljava/lang/String;[Ljava/lang/String;)V",
                    false,
                )
                mv.visitVarInsn(ALOAD, 0)
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    className,
                    method,
                    "(Ldev/panini/compiler/CompiledProgramRuntime;)V",
                    false,
                )
                mv.visitVarInsn(ALOAD, 0)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "dev/panini/compiler/CompiledProgramRuntime",
                    "exitFrame",
                    "()V",
                    false,
                )
            } else {
                val directPlan = DirectLeafPlanner.plan(source, allowStore = allowDirectStore)
                if (directPlan != null) emitDirect(mv, directPlan) else emitEval(mv, source)
            }
        }

        private fun emitDirect(mv: MethodVisitor, plan: ExecutionPlan, asBoolean: Boolean = false) {
            val bindingName = if (asBoolean) null else plan.resolved.operation.resultBindingKaraka
                ?.let(plan.resolved.context.bindings::get)
                ?.bindingName()
            val bindings = nextLocal++
            mv.visitTypeInsn(NEW, "java/util/HashMap")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
            mv.visitVarInsn(ASTORE, bindings)
            plan.resolved.context.bindings.forEach { (karaka, expression) ->
                mv.visitVarInsn(ALOAD, bindings)
                mv.visitFieldInsn(
                    GETSTATIC,
                    "dev/panini/core/Karaka",
                    karaka.name,
                    "Ldev/panini/core/Karaka;",
                )
                emitExpression(mv, expression)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/util/HashMap",
                    "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    false,
                )
                mv.visitInsn(POP)
            }
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(plan.resolved.invocation.dhatu.upadesha)
            mv.visitLdcInsn(plan.resolved.operation.name)
            mv.visitLdcInsn(plan.resolved.operation.trigger.requiredSanadi.sorted().joinToString(","))
            mv.visitVarInsn(ALOAD, bindings)
            if (bindingName != null) mv.visitLdcInsn(bindingName)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                when {
                    asBoolean -> "executeDirectBoolean"
                    bindingName != null -> "executeDirectStore"
                    else -> "executeDirect"
                },
                when {
                    asBoolean -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Z"
                    bindingName != null -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;)Ldev/panini/execution/SanskritValue;"
                    else -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;"
                },
                false,
            )
            if (!asBoolean) mv.visitInsn(POP)
        }

        private fun emitExpression(mv: MethodVisitor, expression: ExecutionExpression) {
            when (expression) {
                is ExecutionExpression.Pada -> {
                    mv.visitLdcInsn(expression.prakriti)
                    expression.value?.let { emitValue(mv, it) } ?: mv.visitInsn(ACONST_NULL)
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "dev/panini/compiler/PaniniRuntime",
                        "createPadaExpression",
                        "(Ljava/lang/String;Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/ExecutionExpression\$Pada;",
                        false,
                    )
                }
                is ExecutionExpression.TypedOperand -> {
                    emitValue(mv, expression.value)
                    mv.visitFieldInsn(
                        GETSTATIC,
                        "dev/panini/core/SupAffix",
                        expression.sup.name,
                        "Ldev/panini/core/SupAffix;",
                    )
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "dev/panini/compiler/PaniniRuntime",
                        "createTypedOperandExpression",
                        "(Ldev/panini/execution/SanskritValue;Ldev/panini/core/SupAffix;)Ldev/panini/execution/ExecutionExpression\$TypedOperand;",
                        false,
                    )
                }
                is ExecutionExpression.Coordination -> {
                    mv.visitLdcInsn(expression.members.size)
                    mv.visitTypeInsn(ANEWARRAY, "dev/panini/execution/ExecutionExpression")
                    expression.members.forEachIndexed { index, member ->
                        mv.visitInsn(DUP)
                        mv.visitLdcInsn(index)
                        emitExpression(mv, member)
                        mv.visitInsn(AASTORE)
                    }
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "dev/panini/compiler/PaniniRuntime",
                        "createCoordinationExpression",
                        "([Ldev/panini/execution/ExecutionExpression;)Ldev/panini/execution/ExecutionExpression\$Coordination;",
                        false,
                    )
                }
                is ExecutionExpression.Reference -> {
                    mv.visitLdcInsn(expression.name)
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "dev/panini/compiler/PaniniRuntime",
                        "createReferenceExpression",
                        "(Ljava/lang/String;)Ldev/panini/execution/ExecutionExpression\$Reference;",
                        false,
                    )
                }
            }
        }

        private fun emitValue(mv: MethodVisitor, value: SanskritValue) {
            when (value) {
                is SanskritValue.Sankhya -> {
                    mv.visitLdcInsn(value.value)
                    mv.visitLdcInsn(value.word)
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "dev/panini/compiler/PaniniRuntime",
                        "sankhya",
                        "(JLjava/lang/String;)Ldev/panini/execution/SanskritValue;",
                        false,
                    )
                }
                is SanskritValue.Shabda -> {
                    mv.visitLdcInsn(value.text)
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "dev/panini/compiler/PaniniRuntime",
                        "shabda",
                        "(Ljava/lang/String;)Ldev/panini/execution/SanskritValue;",
                        false,
                    )
                }
                else -> error("Unsupported direct leaf constant: $value")
            }
        }

        private fun emitConditional(mv: MethodVisitor, node: Conditional) {
            val alternate = Label()
            val end = Label()
            emitBoolean(mv, render(node.condition))
            mv.visitJumpInsn(IFEQ, alternate)
            emit(mv, node.consequent)
            mv.visitJumpInsn(GOTO, end)
            mv.visitLabel(alternate)
            node.alternate?.let { emit(mv, it) }
            mv.visitLabel(end)
        }

        private fun emitWhile(mv: MethodVisitor, node: WhileLoop) {
            val condition = Label()
            val victory = Label()
            val exhausted = Label()
            val target = Label()
            val bound = node.maximumIterationStems.takeIf(List<String>::isNotEmpty)?.let {
                dev.panini.sankhya.SankhyaEvaluator().evaluateStems(it).value
            }
            val counter = nextLocal
            nextLocal += 2
            val usesLatestResult = node.condition.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.SubantaPada &&
                    pada.pratipadika.sourceText.substringBefore('+').trim() == "फल"
            }
            val isNegated = node.condition.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.AvyayaPada && pada.form == "न"
            }
            val latestCondition = if (usesLatestResult) nextLocal++ else null
            mv.visitInsn(LCONST_0)
            mv.visitVarInsn(LSTORE, counter)
            latestCondition?.let {
                mv.visitInsn(ICONST_0)
                mv.visitVarInsn(ISTORE, it)
            }
            mv.visitLabel(condition)
            if (bound != null) {
                mv.visitVarInsn(LLOAD, counter)
                mv.visitLdcInsn(bound)
                mv.visitInsn(LCMP)
                mv.visitJumpInsn(IFGE, exhausted)
            }
            if (latestCondition != null) {
                mv.visitVarInsn(ILOAD, latestCondition)
                mv.visitJumpInsn(if (isNegated) IFNE else IFEQ, victory)
            } else {
                emitBoolean(mv, render(node.condition))
                mv.visitJumpInsn(IFEQ, victory)
            }
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "enterConditionIteration",
                "()V",
                false,
            )
            if (latestCondition != null) {
                mv.visitVarInsn(ALOAD, 0)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "dev/panini/compiler/CompiledProgramRuntime",
                    "clearReportedCondition",
                    "()V",
                    false,
                )
            }
            emit(mv, node.body)
            mv.visitVarInsn(LLOAD, counter)
            mv.visitInsn(LCONST_1)
            mv.visitInsn(LADD)
            mv.visitVarInsn(LSTORE, counter)
            if (latestCondition != null) {
                mv.visitVarInsn(ALOAD, 0)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "dev/panini/compiler/CompiledProgramRuntime",
                    "requireReportedCondition",
                    "()Z",
                    false,
                )
                mv.visitVarInsn(ISTORE, latestCondition)
            }
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "consumeBreak",
                "()Z",
                false,
            )
            mv.visitJumpInsn(IFNE, victory)
            mv.visitJumpInsn(GOTO, condition)
            mv.visitLabel(victory)
            emitLoopOutcome(mv, "विजय", counter)
            mv.visitJumpInsn(GOTO, target)
            mv.visitLabel(exhausted)
            node.exhausted?.let { emit(mv, it) }
            emitLoopOutcome(mv, "समाप्ति", counter)
            mv.visitLabel(target)
            node.resultTarget?.let { emitLoopTarget(mv, it) }
        }

        private fun emitLoopOutcome(mv: MethodVisitor, outcome: String, counter: Int) {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(outcome)
            mv.visitVarInsn(LLOAD, counter)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "publishLoopOutcome",
                "(Ljava/lang/String;J)V",
                false,
            )
        }

        private fun emitLoopTarget(mv: MethodVisitor, target: ProgramNode) {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(normalized(render(target)))
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "evaluateLoopTarget",
                "(Ljava/lang/String;)Ldev/panini/execution/SanskritValue;",
                false,
            )
            mv.visitInsn(POP)
        }

        private fun emitRepeat(mv: MethodVisitor, node: Repeat) {
            val counter = nextLocal++
            val start = Label()
            val exit = Label()
            mv.visitInsn(ICONST_0)
            mv.visitVarInsn(ISTORE, counter)
            mv.visitLabel(start)
            mv.visitVarInsn(ILOAD, counter)
            mv.visitLdcInsn(node.count)
            mv.visitJumpInsn(IF_ICMPGE, exit)
            emit(mv, node.body)
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "consumeBreak",
                "()Z",
                false,
            )
            mv.visitJumpInsn(IFNE, exit)
            mv.visitIincInsn(counter, 1)
            mv.visitJumpInsn(GOTO, start)
            mv.visitLabel(exit)
        }

        private fun emitBoolean(mv: MethodVisitor, source: String) {
            val directPlan = DirectLeafPlanner.plan(source)
            if (directPlan != null && dev.panini.shiksha.Samjna.SATYA in directPlan.resolved.operation.resultSamjnas) {
                emitDirect(mv, directPlan, asBoolean = true)
                return
            }
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(normalized(source))
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "evaluateBoolean",
                "(Ljava/lang/String;)Z",
                false,
            )
        }

        private fun emitEval(mv: MethodVisitor, source: String) {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(normalized(source))
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "evaluate",
                "(Ljava/lang/String;)Ldev/panini/execution/SanskritValue;",
                false,
            )
            mv.visitInsn(POP)
        }

        private fun emitStringArray(mv: MethodVisitor, values: List<String>) {
            mv.visitLdcInsn(values.size)
            mv.visitTypeInsn(ANEWARRAY, "java/lang/String")
            values.forEachIndexed { index, value ->
                mv.visitInsn(DUP)
                mv.visitLdcInsn(index)
                mv.visitLdcInsn(value)
                mv.visitInsn(AASTORE)
            }
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
            } else {
                sentence.program?.let {
                    lowering.emit(
                        mv,
                        it,
                        sentence.text,
                        allowDirectStore = index == statements.lastIndex,
                    )
                }
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
                ) ?: return null
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
                environment = ValueEnvironment(
                    environment.values + ("LastResult" to result) +
                        (destination?.let { mapOf(it to result) } ?: emptyMap()),
                )
                add(plan)
            }
        }
    }

}
