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
            mv.visitInsn(RETURN)
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

        fun emitDirectConditional(
            mv: MethodVisitor,
            condition: ExecutionPlan,
            consequent: ExecutionPlan,
            alternate: ExecutionPlan?,
        ) {
            val id = nextLabel++
            val instructions = CompilerIrLowering.lowerConditional(
                condition = CompilerIrLowering.lowerLeaf(
                    condition,
                    CallResultMode.BOOLEAN,
                ) as CompilerInstruction.Call,
                consequent = listOf(CompilerIrLowering.lowerLeaf(consequent)),
                alternate = alternate?.let { listOf(CompilerIrLowering.lowerLeaf(it)) }.orEmpty(),
                labelPrefix = "conditional_$id",
            )
            emitIr(mv, instructions)
        }

        private fun emitIr(mv: MethodVisitor, instructions: List<CompilerInstruction>) {
            CompilerIrVerifier.verify(instructions)
            val labels = instructions.filterIsInstance<CompilerInstruction.Label>()
                .associate { it.name to Label() }
            val counters = instructions.mapNotNull {
                when (it) {
                    is CompilerInstruction.InitializeCounter -> it.name
                    is CompilerInstruction.TestCounter -> it.name
                    is CompilerInstruction.IncrementCounter -> it.name
                    else -> null
                }
            }.distinct().associateWith { nextLocal++ }
            instructions.forEach { instruction ->
                when (instruction) {
                    is CompilerInstruction.Call -> emitCall(mv, instruction)
                    is CompilerInstruction.Branch -> mv.visitJumpInsn(
                        if (instruction.whenTrue) IFNE else IFEQ,
                        requireNotNull(labels[instruction.target]),
                    )
                    is CompilerInstruction.Jump -> mv.visitJumpInsn(
                        GOTO,
                        requireNotNull(labels[instruction.target]),
                    )
                    is CompilerInstruction.Label -> mv.visitLabel(requireNotNull(labels[instruction.name]))
                    is CompilerInstruction.InitializeCounter -> {
                        mv.visitInsn(ICONST_0)
                        mv.visitVarInsn(ISTORE, requireNotNull(counters[instruction.name]))
                    }
                    is CompilerInstruction.TestCounter -> {
                        val isBelowLimit = Label()
                        val complete = Label()
                        mv.visitVarInsn(ILOAD, requireNotNull(counters[instruction.name]))
                        mv.visitLdcInsn(instruction.limit)
                        mv.visitJumpInsn(IF_ICMPLT, isBelowLimit)
                        mv.visitInsn(ICONST_0)
                        mv.visitJumpInsn(GOTO, complete)
                        mv.visitLabel(isBelowLimit)
                        mv.visitInsn(ICONST_1)
                        mv.visitLabel(complete)
                    }
                    is CompilerInstruction.IncrementCounter -> mv.visitIincInsn(
                        requireNotNull(counters[instruction.name]),
                        1,
                    )
                    CompilerInstruction.ConsumeBreak -> {
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitMethodInsn(
                            INVOKEVIRTUAL,
                            "dev/panini/compiler/CompiledProgramRuntime",
                            "consumeBreak",
                            "()Z",
                            false,
                        )
                    }
                    CompilerInstruction.RequestBreak -> {
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitMethodInsn(
                            INVOKEVIRTUAL,
                            "dev/panini/compiler/CompiledProgramRuntime",
                            "requestBreak",
                            "()Ldev/panini/execution/SanskritValue;",
                            false,
                        )
                        mv.visitInsn(POP)
                    }
                    else -> error("IR instruction is not supported by the JVM backend yet: $instruction")
                }
            }
        }

        fun emitDirectWhile(
            mv: MethodVisitor,
            node: WhileLoop,
            condition: ExecutionPlan,
            body: ExecutionPlan,
            exhausted: ExecutionPlan?,
            resultTarget: ExecutionPlan?,
        ) = emitWhile(mv, node, condition, body, exhausted, resultTarget)

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
            val repetition = Regex("^([^\\s+]+)\\s*\\+\\s*[^\\s]*कृत्व[^\\s]*\\s+(.+)$")
                .find(rendered.trim())
            if (repetition != null) {
                val count = dev.panini.sankhya.SankhyaEvaluator()
                    .evaluateStems(listOf(repetition.groupValues[1])).value.toInt()
                val counter = nextLocal++
                val start = Label()
                val exit = Label()
                mv.visitInsn(ICONST_0)
                mv.visitVarInsn(ISTORE, counter)
                mv.visitLabel(start)
                mv.visitVarInsn(ILOAD, counter)
                mv.visitLdcInsn(count)
                mv.visitJumpInsn(IF_ICMPGE, exit)
                emitInvocation(mv, node, exactSource = repetition.groupValues[2])
                mv.visitIincInsn(counter, 1)
                mv.visitJumpInsn(GOTO, start)
                mv.visitLabel(exit)
                return
            }
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
                val parameterNamesLocal = nextLocal++
                val argumentsLocal = nextLocal++
                val argumentValuesLocal = nextLocal++
                emitStringArray(mv, signature.parameters.map { it.nameStem })
                mv.visitVarInsn(ASTORE, parameterNamesLocal)
                emitStringArray(mv, arguments)
                mv.visitVarInsn(ASTORE, argumentsLocal)
                emitNullableValueArray(mv, invocation.argumentValues, signature.parameters.size)
                mv.visitVarInsn(ASTORE, argumentValuesLocal)
                mv.visitVarInsn(ALOAD, 0)
                mv.visitVarInsn(ALOAD, parameterNamesLocal)
                mv.visitVarInsn(ALOAD, argumentsLocal)
                mv.visitVarInsn(ALOAD, argumentValuesLocal)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "dev/panini/compiler/CompiledProgramRuntime",
                    "enterFrame",
                    "([Ljava/lang/String;[Ljava/lang/String;[Ldev/panini/execution/SanskritValue;)V",
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
                if (directPlan != null) {
                    emitDirect(mv, directPlan)
                } else {
                    val generalPlan = DirectLeafPlanner.planAny(source)
                        ?: error("The JVM compiler cannot preplan invocation: $source")
                    emitDirect(mv, generalPlan)
                }
            }
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
            if (instruction == CompilerInstruction.RequestBreak) {
                mv.visitVarInsn(ALOAD, 0)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "dev/panini/compiler/CompiledProgramRuntime",
                    "requestBreak",
                    "()Ldev/panini/execution/SanskritValue;",
                    false,
                )
                mv.visitInsn(POP)
                return
            }
            emitCall(mv, instruction as CompilerInstruction.Call)
        }

        private fun emitCall(mv: MethodVisitor, call: CompilerInstruction.Call) {
            val bindingName = call.destination
            val bindings = nextLocal++
            mv.visitTypeInsn(NEW, "java/util/HashMap")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
            mv.visitVarInsn(ASTORE, bindings)
            call.bindings.forEach { (karaka, expression) ->
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
            mv.visitLdcInsn(call.dhatuUpadesha)
            mv.visitLdcInsn(call.operationName)
            mv.visitLdcInsn(call.requiredSanadi)
            mv.visitVarInsn(ALOAD, bindings)
            if (bindingName != null) mv.visitLdcInsn(bindingName)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                when {
                    call.resultMode == CallResultMode.BOOLEAN -> "executeDirectBoolean"
                    call.resultMode == CallResultMode.LOOP_TARGET -> "executeDirectLoopTarget"
                    bindingName != null -> "executeDirectStore"
                    else -> "executeDirect"
                },
                when {
                    call.resultMode == CallResultMode.BOOLEAN -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Z"
                    call.resultMode == CallResultMode.LOOP_TARGET -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;"
                    bindingName != null -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;)Ldev/panini/execution/SanskritValue;"
                    else -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;"
                },
                false,
            )
            if (call.resultMode != CallResultMode.BOOLEAN) mv.visitInsn(POP)
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
            StructuredValueBytecodeEmitter.emit(mv, value)
        }

        private fun emitConditional(mv: MethodVisitor, node: Conditional) {
            lowerConditionalIr(node)?.let {
                emitIr(mv, it)
                return
            }
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
            is Invocation -> DirectLeafPlanner.planAny(render(node))
                ?.let(CompilerIrLowering::lowerLeaf)
                ?.let(::listOf)
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
            is Pipeline, is Quotation, is Repeat, is WhileLoop -> null
        }

        private fun emitWhile(
            mv: MethodVisitor,
            node: WhileLoop,
            directCondition: ExecutionPlan? = null,
            directBody: ExecutionPlan? = null,
            directExhausted: ExecutionPlan? = null,
            directResultTarget: ExecutionPlan? = null,
        ) {
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
            if (latestCondition != null) {
                mv.visitVarInsn(ILOAD, latestCondition)
                mv.visitJumpInsn(if (isNegated) IFNE else IFEQ, victory)
            } else {
                if (directCondition != null) {
                    emitDirect(mv, directCondition, asBoolean = true)
                } else {
                    emitBoolean(mv, render(node.condition))
                }
                mv.visitJumpInsn(IFEQ, victory)
            }
            if (bound != null) {
                mv.visitVarInsn(LLOAD, counter)
                mv.visitLdcInsn(bound)
                mv.visitInsn(LCMP)
                mv.visitJumpInsn(IFGE, exhausted)
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
            if (directBody != null) emitDirect(mv, directBody) else emit(mv, node.body)
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
            if (directExhausted != null) {
                emitDirect(mv, directExhausted)
            } else {
                node.exhausted?.let { emit(mv, it) }
            }
            emitLoopOutcome(mv, "समाप्ति", counter)
            mv.visitLabel(target)
            node.resultTarget?.let { emitLoopTarget(mv, it, directResultTarget) }
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

        private fun emitLoopTarget(
            mv: MethodVisitor,
            target: ProgramNode,
            directPlan: ExecutionPlan? = null,
        ) {
            if (directPlan != null) {
                emitDirect(mv, directPlan, asLoopTarget = true)
                return
            }
            val rendered = render(target)
            val plan = DirectLeafPlanner.planAny(rendered)
                ?: DirectLeafPlanner.planAny("चक्रफल + अम् $rendered")
                ?: error("The JVM compiler cannot preplan loop result target: ${render(target)}")
            emitDirect(mv, plan, asLoopTarget = true)
        }

        private fun emitRepeat(mv: MethodVisitor, node: Repeat) {
            lowerRepeatIr(node)?.let {
                emitIr(mv, it)
                return
            }
            val counter = nextLocal++
            val start = Label()
            val exit = Label()
            mv.visitInsn(ICONST_0)
            mv.visitVarInsn(ISTORE, counter)
            mv.visitLabel(start)
            mv.visitVarInsn(ILOAD, counter)
            mv.visitLdcInsn(node.count)
            mv.visitJumpInsn(IF_ICMPGE, exit)
            val body = node.body
            if (body is Invocation) {
                val renderedBody = render(body)
                val bodySource = renderedBody.split(Regex("\\s+")).drop(3).joinToString(" ")
                emitInvocation(mv, body, exactSource = bodySource)
            } else {
                emit(mv, body)
            }
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

        private fun lowerRepeatIr(node: Repeat): List<CompilerInstruction>? {
            val body = node.body
            val bodyInstructions = if (body is Invocation) {
                val renderedBody = render(body)
                val bodySource = renderedBody.split(Regex("\\s+")).drop(3).joinToString(" ")
                DirectLeafPlanner.planAny(bodySource)
                    ?.let(CompilerIrLowering::lowerLeaf)
                    ?.let(::listOf)
            } else {
                lowerPrimitiveBranchIr(body)
            } ?: return null
            return CompilerIrLowering.lowerRepeat(
                count = node.count,
                body = bodyInstructions,
                namePrefix = "repeat_${nextLabel++}",
            )
        }

        private fun emitBoolean(mv: MethodVisitor, source: String) {
            val plan = DirectLeafPlanner.plan(source) ?: DirectLeafPlanner.planAny(source)
                ?: error("The JVM compiler cannot preplan condition: $source")
            require(dev.panini.shiksha.Samjna.SATYA in plan.resolved.operation.resultSamjnas) {
                "Compiled condition must produce सत्य/असत्य: $source"
            }
            emitDirect(mv, plan, asBoolean = true)
        }

        private fun emitPlannedSource(mv: MethodVisitor, source: String) {
            val plans = DirectLeafPlanner.plansAny(source)
                ?: error("The JVM compiler cannot preplan source without the interpreter bridge: $source")
            plans.forEach { emitDirect(mv, it) }
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

        private fun emitNullableValueArray(
            mv: MethodVisitor,
            values: List<SanskritValue?>,
            size: Int,
        ) {
            mv.visitLdcInsn(size)
            mv.visitTypeInsn(ANEWARRAY, "dev/panini/execution/SanskritValue")
            repeat(size) { index ->
                mv.visitInsn(DUP)
                mv.visitLdcInsn(index)
                values.getOrNull(index)?.let { StructuredValueBytecodeEmitter.emit(mv, it) }
                    ?: mv.visitInsn(ACONST_NULL)
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

    /** Directly lowers a final simple branch after a fully direct state-building prefix. */
    private fun planDirectFinalConditional(
        statements: List<PvmScriptStatement.Sentence>,
    ): DirectFinalConditional? {
        if (statements.size < 2) return null
        val conditional = statements.last().program as? Conditional ?: return null
        val consequent = conditional.consequent as? Invocation ?: return null
        val alternate = conditional.alternate as? Invocation ?: return null
        var environment = ValueEnvironment()
        val prefix = buildList {
            for (sentence in statements.dropLast(1)) {
                if (sentence.program !is Invocation) return null
                val plan = DirectLeafPlanner.plan(
                    sentence.text,
                    environment = environment,
                    allowStore = true,
                ) ?: return null
                environment = advancePlanningEnvironment(plan, environment) ?: return null
                add(plan)
            }
        }
        val conditionPlan = DirectLeafPlanner.plan(render(conditional.condition), environment)
            ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
            ?: return null
        val consequentPlan = DirectLeafPlanner.plan(render(consequent), environment) ?: return null
        val alternatePlan = DirectLeafPlanner.plan(render(alternate), environment) ?: return null
        return DirectFinalConditional(prefix, conditionPlan, consequentPlan, alternatePlan)
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

    private data class DirectFinalConditional(
        val prefix: List<ExecutionPlan>,
        val condition: ExecutionPlan,
        val consequent: ExecutionPlan,
        val alternate: ExecutionPlan?,
    )

    /** Directly lowers a final simple state loop after a fully direct initialization prefix. */
    private fun planDirectFinalWhile(
        statements: List<PvmScriptStatement.Sentence>,
    ): DirectFinalWhile? {
        if (statements.size < 2) return null
        val loop = statements.last().program as? WhileLoop ?: return null
        val body = loop.body as? Invocation ?: return null
        val usesLatestResult = loop.condition.vakya.padas.any { pada ->
            pada is dev.panini.vyakaranam.ast.SubantaPada &&
                pada.pratipadika.sourceText.substringBefore('+').trim() == "फल"
        }
        if (usesLatestResult) return null
        var environment = ValueEnvironment()
        val prefix = buildList {
            for (sentence in statements.dropLast(1)) {
                if (sentence.program !is Invocation) return null
                val plan = DirectLeafPlanner.plan(
                    sentence.text,
                    environment = environment,
                    allowStore = true,
                ) ?: return null
                environment = advancePlanningEnvironment(plan, environment) ?: return null
                add(plan)
            }
        }
        val conditionPlan = DirectLeafPlanner.plan(render(loop.condition), environment)
            ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas }
            ?: return null
        val bodyPlan = DirectLeafPlanner.plan(
            render(body),
            environment = environment,
            allowStore = true,
        ) ?: return null
        val exhaustedPlan = when (val exhausted = loop.exhausted) {
            null -> null
            is Invocation -> DirectLeafPlanner.plan(render(exhausted), environment) ?: return null
            else -> return null
        }
        val resultTargetPlan = when (val target = loop.resultTarget) {
            null -> null
            is Invocation -> DirectLeafPlanner.plan(
                "चक्रफल + अम् ${render(target)}",
                environment = environment.with("चक्रफल", SanskritValue.Shabda("विजय")),
                allowStore = true,
            ) ?: return null
            else -> return null
        }
        return DirectFinalWhile(prefix, loop, conditionPlan, bodyPlan, exhaustedPlan, resultTargetPlan)
    }

    private data class DirectFinalWhile(
        val prefix: List<ExecutionPlan>,
        val loop: WhileLoop,
        val condition: ExecutionPlan,
        val body: ExecutionPlan,
        val exhausted: ExecutionPlan?,
        val resultTarget: ExecutionPlan?,
    )

    /** Directly unrolls a final fixed-count utterance after a direct state-building prefix. */
    private fun planDirectFinalRepeat(
        statements: List<PvmScriptStatement.Sentence>,
    ): DirectFinalRepeat? {
        if (statements.size < 2) return null
        if (statements.last().program !is Invocation) return null
        var environment = ValueEnvironment()
        val prefix = buildList {
            for (sentence in statements.dropLast(1)) {
                if (sentence.program !is Invocation) return null
                val plan = DirectLeafPlanner.plan(
                    sentence.text,
                    environment = environment,
                    allowStore = true,
                ) ?: return null
                environment = advancePlanningEnvironment(plan, environment) ?: return null
                add(plan)
            }
        }
        val repeatedPlans = DirectLeafPlanner.plans(
            statements.last().text,
            environment = environment,
            allowStore = true,
        )?.takeIf { it.size > 1 } ?: return null
        return DirectFinalRepeat(prefix, repeatedPlans)
    }

    private data class DirectFinalRepeat(
        val prefix: List<ExecutionPlan>,
        val plans: List<ExecutionPlan>,
    )

    /** Directly lowers a final ततः pipeline after a fully direct prefix. */
    private fun planDirectFinalSequence(
        statements: List<PvmScriptStatement.Sentence>,
    ): DirectFinalSequence? {
        if (statements.isEmpty()) return null
        val sequence = statements.last().program as? Sequence ?: return null
        if (sequence.connectors.any { it != "ततः" } || sequence.statements.any { it !is Invocation }) {
            return null
        }
        var environment = ValueEnvironment()
        val prefix = buildList {
            for (sentence in statements.dropLast(1)) {
                if (sentence.program !is Invocation) return null
                val plan = DirectLeafPlanner.plan(
                    sentence.text,
                    environment = environment,
                    allowStore = true,
                ) ?: return null
                environment = advancePlanningEnvironment(plan, environment) ?: return null
                add(plan)
            }
        }
        val planned = planDirectSequence(sequence, environment, allowStore = true) ?: return null
        return DirectFinalSequence(prefix, planned.plans)
    }

    private fun planDirectSequence(
        sequence: Sequence,
        initialEnvironment: ValueEnvironment,
        allowStore: Boolean,
    ): PlannedDirectSequence? {
        if (sequence.connectors.any { it != "ततः" } || sequence.statements.any { it !is Invocation }) {
            return null
        }
        var environment = initialEnvironment
        val plans = buildList {
            sequence.statements.filterIsInstance<Invocation>().forEachIndexed { index, invocation ->
                val alreadyReferencesResult = invocation.vakya.padas.any { pada ->
                    pada is dev.panini.vyakaranam.ast.SubantaPada &&
                        pada.pratipadika.sourceText.substringBefore('+').trim() == "फल"
                }
                val source = if (index > 0 && !alreadyReferencesResult) {
                    "फल + अम् ${render(invocation)}"
                } else {
                    render(invocation)
                }
                val plan = DirectLeafPlanner.plan(
                    source,
                    environment = environment,
                    allowStore = allowStore,
                ) ?: return null
                environment = advancePlanningEnvironment(plan, environment) ?: return null
                add(plan)
            }
        }
        return PlannedDirectSequence(plans, environment)
    }

    private data class DirectFinalSequence(
        val prefix: List<ExecutionPlan>,
        val plans: List<ExecutionPlan>,
    )

    private data class PlannedDirectSequence(
        val plans: List<ExecutionPlan>,
        val environment: ValueEnvironment,
    )

}
