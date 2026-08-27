package dev.panini.compiler

import dev.panini.execution.ExecutionExpression
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

/** JVM backend for validated compiler IR. It has no dependency on parser or AST nodes. */
internal class CompilerIrJvmEmitter(
    private val className: String,
    private val mv: MethodVisitor,
    private val allocateLocal: (width: Int) -> Int,
) {
    fun emit(instructions: List<CompilerInstruction>) {
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
        }.distinct().associateWith { allocateLocal(2) }
        val loopConditions = instructions.mapNotNull {
            when (it) {
                is CompilerInstruction.InitializeLoopCondition -> it.name
                is CompilerInstruction.TestLoopCondition -> it.name
                is CompilerInstruction.CaptureReportedCondition -> it.name
                else -> null
            }
        }.distinct().associateWith { allocateLocal(1) }

        instructions.forEach { instruction ->
            when (instruction) {
                is CompilerInstruction.Constant -> StructuredValueBytecodeEmitter.emit(mv, instruction.value)
                is CompilerInstruction.Load -> emitLoad(instruction.name)
                is CompilerInstruction.Store -> emitStore(instruction.name)
                CompilerInstruction.LoadLastResult -> emitLoad("LastResult")
                CompilerInstruction.Duplicate -> mv.visitInsn(DUP)
                is CompilerInstruction.Compare -> emitComparison(instruction.operator)
                is CompilerInstruction.Arithmetic -> emitArithmetic(instruction.operator)
                is CompilerInstruction.Call -> emitCall(instruction)
                is CompilerInstruction.ProcedureCall -> emitProcedureCall(instruction)
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
                    mv.visitInsn(LCONST_0)
                    mv.visitVarInsn(LSTORE, requireNotNull(counters[instruction.name]))
                }
                is CompilerInstruction.TestCounter -> emitCounterTest(
                    requireNotNull(counters[instruction.name]),
                    instruction.limit,
                )
                is CompilerInstruction.IncrementCounter -> {
                    val counter = requireNotNull(counters[instruction.name])
                    mv.visitVarInsn(LLOAD, counter)
                    mv.visitInsn(LCONST_1)
                    mv.visitInsn(LADD)
                    mv.visitVarInsn(LSTORE, counter)
                }
                CompilerInstruction.ConsumeBreak -> emitRuntimeBoolean("consumeBreak")
                CompilerInstruction.EnterConditionIteration -> emitRuntimeVoid("enterConditionIteration")
                is CompilerInstruction.PublishLoopOutcome -> {
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitLdcInsn(instruction.outcome)
                    mv.visitVarInsn(LLOAD, requireNotNull(counters[instruction.counter]))
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        RUNTIME,
                        "publishLoopOutcome",
                        "(Ljava/lang/String;J)V",
                        false,
                    )
                }
                is CompilerInstruction.InitializeLoopCondition -> {
                    mv.visitInsn(ICONST_0)
                    mv.visitVarInsn(ISTORE, requireNotNull(loopConditions[instruction.name]))
                }
                is CompilerInstruction.TestLoopCondition -> emitLoopConditionTest(
                    requireNotNull(loopConditions[instruction.name]),
                    instruction.negated,
                )
                CompilerInstruction.ClearReportedCondition -> emitRuntimeVoid("clearReportedCondition")
                is CompilerInstruction.CaptureReportedCondition -> {
                    emitRuntimeBoolean("requireReportedCondition")
                    mv.visitVarInsn(ISTORE, requireNotNull(loopConditions[instruction.name]))
                }
                CompilerInstruction.RequestBreak -> {
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        RUNTIME,
                        "requestBreak",
                        "()Ldev/panini/execution/SanskritValue;",
                        false,
                    )
                    mv.visitInsn(POP)
                }
                CompilerInstruction.Return -> mv.visitInsn(RETURN)
                CompilerInstruction.ReturnIfBreak -> emitReturnIfBreak()
            }
        }
    }

    private fun emitLoad(name: String) {
        mv.visitVarInsn(ALOAD, 0)
        mv.visitLdcInsn(name)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            RUNTIME,
            "loadValue",
            "(Ljava/lang/String;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitStore(name: String) {
        val value = allocateLocal(1)
        mv.visitVarInsn(ASTORE, value)
        mv.visitVarInsn(ALOAD, 0)
        mv.visitLdcInsn(name)
        mv.visitVarInsn(ALOAD, value)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            RUNTIME,
            "storeValue",
            "(Ljava/lang/String;Ldev/panini/execution/SanskritValue;)V",
            false,
        )
    }

    private fun emitComparison(operator: ComparisonOperator) {
        val method = when (operator) {
            ComparisonOperator.EQUAL -> "equal"
            ComparisonOperator.NOT_EQUAL -> "notEqual"
            ComparisonOperator.LESS_THAN -> "lessThan"
            ComparisonOperator.LESS_THAN_OR_EQUAL -> "lessThanOrEqual"
            ComparisonOperator.GREATER_THAN -> "greaterThan"
            ComparisonOperator.GREATER_THAN_OR_EQUAL -> "greaterThanOrEqual"
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/CompilerValueOperations",
            method,
            "(Ldev/panini/execution/SanskritValue;Ldev/panini/execution/SanskritValue;)Z",
            false,
        )
    }

    private fun emitArithmetic(operator: ArithmeticOperator) {
        val method = when (operator) {
            ArithmeticOperator.ADD -> "add"
            ArithmeticOperator.SUBTRACT -> "subtract"
            ArithmeticOperator.MULTIPLY -> "multiply"
            ArithmeticOperator.DIVIDE -> "divide"
            ArithmeticOperator.REMAINDER -> "remainder"
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/CompilerValueOperations",
            method,
            "(Ldev/panini/execution/SanskritValue;Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitCounterTest(counter: Int, limit: Long) {
        val isBelowLimit = Label()
        val complete = Label()
        mv.visitVarInsn(LLOAD, counter)
        mv.visitLdcInsn(limit)
        mv.visitInsn(LCMP)
        mv.visitJumpInsn(IFLT, isBelowLimit)
        mv.visitInsn(ICONST_0)
        mv.visitJumpInsn(GOTO, complete)
        mv.visitLabel(isBelowLimit)
        mv.visitInsn(ICONST_1)
        mv.visitLabel(complete)
    }

    private fun emitLoopConditionTest(local: Int, negated: Boolean) {
        val matches = Label()
        val complete = Label()
        mv.visitVarInsn(ILOAD, local)
        mv.visitJumpInsn(if (negated) IFEQ else IFNE, matches)
        mv.visitInsn(ICONST_0)
        mv.visitJumpInsn(GOTO, complete)
        mv.visitLabel(matches)
        mv.visitInsn(ICONST_1)
        mv.visitLabel(complete)
    }

    private fun emitReturnIfBreak() {
        val continueExecution = Label()
        emitRuntimeBoolean("isBreakRequested")
        mv.visitJumpInsn(IFEQ, continueExecution)
        mv.visitInsn(RETURN)
        mv.visitLabel(continueExecution)
    }

    private fun emitRuntimeVoid(method: String) {
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKEVIRTUAL, RUNTIME, method, "()V", false)
    }

    private fun emitRuntimeBoolean(method: String) {
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKEVIRTUAL, RUNTIME, method, "()Z", false)
    }

    private fun emitCall(call: CompilerInstruction.Call) {
        val bindingName = call.destination
        val bindings = allocateLocal(1)
        mv.visitTypeInsn(NEW, "java/util/HashMap")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
        mv.visitVarInsn(ASTORE, bindings)
        call.bindings.forEach { (karaka, expression) ->
            mv.visitVarInsn(ALOAD, bindings)
            mv.visitFieldInsn(GETSTATIC, "dev/panini/core/Karaka", karaka.name, "Ldev/panini/core/Karaka;")
            emitExpression(expression)
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
            RUNTIME,
            when {
                call.resultMode == CallResultMode.BOOLEAN -> "executeDirectBoolean"
                call.resultMode == CallResultMode.STACK_VALUE -> "executeDirectValue"
                call.resultMode == CallResultMode.LOOP_TARGET -> "executeDirectLoopTarget"
                bindingName != null -> "executeDirectStore"
                else -> "executeDirect"
            },
            when {
                call.resultMode == CallResultMode.BOOLEAN -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Z"
                call.resultMode == CallResultMode.STACK_VALUE -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;"
                call.resultMode == CallResultMode.LOOP_TARGET -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;"
                bindingName != null -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;)Ldev/panini/execution/SanskritValue;"
                else -> "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;"
            },
            false,
        )
        if (call.resultMode !in setOf(CallResultMode.BOOLEAN, CallResultMode.STACK_VALUE)) {
            mv.visitInsn(POP)
        }
    }

    private fun emitProcedureCall(call: CompilerInstruction.ProcedureCall) {
        val parameterNames = allocateLocal(1)
        val arguments = allocateLocal(1)
        val argumentValues = allocateLocal(1)
        emitStringArray(call.parameterNames)
        mv.visitVarInsn(ASTORE, parameterNames)
        emitStringArray(call.arguments)
        mv.visitVarInsn(ASTORE, arguments)
        emitNullableValueArray(call.argumentValues, call.parameterNames.size)
        mv.visitVarInsn(ASTORE, argumentValues)
        mv.visitVarInsn(ALOAD, 0)
        mv.visitVarInsn(ALOAD, parameterNames)
        mv.visitVarInsn(ALOAD, arguments)
        mv.visitVarInsn(ALOAD, argumentValues)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            RUNTIME,
            "enterFrame",
            "([Ljava/lang/String;[Ljava/lang/String;[Ldev/panini/execution/SanskritValue;)V",
            false,
        )
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(
            INVOKESTATIC,
            className,
            call.methodName,
            "(Ldev/panini/compiler/CompiledProgramRuntime;)V",
            false,
        )
        emitRuntimeVoid("exitFrame")
    }

    private fun emitExpression(expression: ExecutionExpression) {
        when (expression) {
            is ExecutionExpression.Pada -> {
                mv.visitLdcInsn(expression.prakriti)
                expression.value?.let { StructuredValueBytecodeEmitter.emit(mv, it) }
                    ?: mv.visitInsn(ACONST_NULL)
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/PaniniRuntime",
                    "createPadaExpression",
                    "(Ljava/lang/String;Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/ExecutionExpression\$Pada;",
                    false,
                )
            }
            is ExecutionExpression.TypedOperand -> {
                StructuredValueBytecodeEmitter.emit(mv, expression.value)
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
                    emitExpression(member)
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

    private fun emitStringArray(values: List<String>) {
        mv.visitLdcInsn(values.size)
        mv.visitTypeInsn(ANEWARRAY, "java/lang/String")
        values.forEachIndexed { index, value ->
            mv.visitInsn(DUP)
            mv.visitLdcInsn(index)
            mv.visitLdcInsn(value)
            mv.visitInsn(AASTORE)
        }
    }

    private fun emitNullableValueArray(values: List<dev.panini.execution.SanskritValue?>, size: Int) {
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

    private companion object {
        const val RUNTIME = "dev/panini/compiler/CompiledProgramRuntime"
    }
}
