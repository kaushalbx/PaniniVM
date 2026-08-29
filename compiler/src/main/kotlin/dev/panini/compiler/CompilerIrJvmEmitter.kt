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
        val locals = instructions.mapNotNull {
            when (it) {
                is CompilerInstruction.LoadLocal -> it.name
                is CompilerInstruction.StoreLocal -> it.name
                else -> null
            }
        }.distinct().associateWith { allocateLocal(1) }
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
                is CompilerInstruction.LoadLocal -> mv.visitVarInsn(
                    ALOAD,
                    requireNotNull(locals[instruction.name]),
                )
                is CompilerInstruction.StoreLocal -> mv.visitVarInsn(
                    ASTORE,
                    requireNotNull(locals[instruction.name]),
                )
                CompilerInstruction.LoadLastResult -> emitLoad("LastResult")
                CompilerInstruction.Duplicate -> mv.visitInsn(DUP)
                CompilerInstruction.Pop -> mv.visitInsn(POP)
                is CompilerInstruction.BuildList -> emitBuildList(instruction.size)
                is CompilerInstruction.BuildRecord -> emitBuildRecord(instruction.schema, instruction.fields)
                is CompilerInstruction.LoadField -> emitLoadField(instruction.name)
                is CompilerInstruction.ResolveArgument -> emitResolveArgument(instruction)
                is CompilerInstruction.RenderText -> emitRenderText(instruction.size)
                CompilerInstruction.IsEven -> mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/CompilerValueOperations",
                    "isEven",
                    "(Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
                    false,
                )
                is CompilerInstruction.Collection -> emitCollection(instruction.operator)
                is CompilerInstruction.Compare -> emitComparison(instruction.operator)
                is CompilerInstruction.Arithmetic -> emitArithmetic(instruction.operator)
                is CompilerInstruction.NumericUnary -> emitNumericUnary(instruction.operator)
                CompilerInstruction.Cardinalize -> mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/CompilerValueOperations",
                    "cardinalize",
                    "(Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
                    false,
                )
                CompilerInstruction.Booleanize -> mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/CompilerValueOperations",
                    "booleanize",
                    "(Ldev/panini/execution/SanskritValue;)Z",
                    false,
                )
                is CompilerInstruction.Call -> emitCall(instruction)
                is CompilerInstruction.EnterFrame -> emitEnterFrame(instruction)
                is CompilerInstruction.InvokeProcedure -> {
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        className,
                        instruction.methodName,
                        "(Ldev/panini/compiler/CompiledProgramRuntime;)V",
                        false,
                    )
                }
                CompilerInstruction.ExitFrame -> emitRuntimeVoid("exitFrame")
                is CompilerInstruction.Branch -> mv.visitJumpInsn(
                    if (instruction.whenTrue) IFNE else IFEQ,
                    requireNotNull(labels[instruction.target]),
                )
                is CompilerInstruction.Jump -> mv.visitJumpInsn(
                    GOTO,
                    requireNotNull(labels[instruction.target]),
                )
                is CompilerInstruction.Label -> mv.visitLabel(requireNotNull(labels[instruction.name]))
                CompilerInstruction.ConsumeBreak -> emitRuntimeBoolean("consumeBreak")
                CompilerInstruction.EnterConditionIteration -> emitRuntimeVoid("enterConditionIteration")
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

    private fun emitBuildList(size: Int) {
        val values = List(size) { allocateLocal(1) }
        values.asReversed().forEach { local -> mv.visitVarInsn(ASTORE, local) }
        mv.visitLdcInsn(size)
        mv.visitTypeInsn(ANEWARRAY, "dev/panini/execution/SanskritValue")
        values.forEachIndexed { index, local ->
            mv.visitInsn(DUP)
            mv.visitLdcInsn(index)
            mv.visitVarInsn(ALOAD, local)
            mv.visitInsn(AASTORE)
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/PaniniRuntime",
            "suchi",
            "([Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitBuildRecord(schema: String, fields: List<String>) {
        val values = List(fields.size) { allocateLocal(1) }
        values.asReversed().forEach { local -> mv.visitVarInsn(ASTORE, local) }
        mv.visitLdcInsn(schema)
        emitStringArray(fields)
        mv.visitLdcInsn(fields.size)
        mv.visitTypeInsn(ANEWARRAY, "dev/panini/execution/SanskritValue")
        values.forEachIndexed { index, local ->
            mv.visitInsn(DUP)
            mv.visitLdcInsn(index)
            mv.visitVarInsn(ALOAD, local)
            mv.visitInsn(AASTORE)
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/PaniniRuntime",
            "rupa",
            "(Ljava/lang/String;[Ljava/lang/String;[Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitLoadField(name: String) {
        mv.visitLdcInsn(name)
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/CompilerValueOperations",
            "recordField",
            "(Ldev/panini/execution/SanskritValue;Ljava/lang/String;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitRenderText(size: Int) {
        val values = List(size) { allocateLocal(1) }
        values.asReversed().forEach { local -> mv.visitVarInsn(ASTORE, local) }
        mv.visitLdcInsn(size)
        mv.visitTypeInsn(ANEWARRAY, "dev/panini/execution/SanskritValue")
        values.forEachIndexed { index, local ->
            mv.visitInsn(DUP)
            mv.visitLdcInsn(index)
            mv.visitVarInsn(ALOAD, local)
            mv.visitInsn(AASTORE)
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/CompilerValueOperations",
            "renderText",
            "([Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitCollection(operator: CollectionOperator) {
        val method = when (operator) {
            CollectionOperator.LENGTH -> "listLength"
            CollectionOperator.REVERSE -> "listReverse"
            CollectionOperator.CONCAT -> "listConcat"
            CollectionOperator.INDEX -> "listIndex"
            CollectionOperator.CONTAINS -> "listContains"
            CollectionOperator.APPEND -> "listAppend"
            CollectionOperator.POP -> "listPop"
            CollectionOperator.SLICE -> "listSlice"
            CollectionOperator.FLATTEN -> "listFlatten"
        }
        val value = "Ldev/panini/execution/SanskritValue;"
        val descriptor = when (operator) {
            CollectionOperator.CONCAT,
            CollectionOperator.INDEX,
            CollectionOperator.CONTAINS,
            CollectionOperator.APPEND,
            -> "($value$value)$value"
            CollectionOperator.SLICE -> "($value$value$value)$value"
            CollectionOperator.LENGTH,
            CollectionOperator.REVERSE,
            CollectionOperator.POP,
            CollectionOperator.FLATTEN,
            -> "($value)$value"
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/CompilerValueOperations",
            method,
            descriptor,
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
            ArithmeticOperator.MINIMUM -> "minimum"
            ArithmeticOperator.POWER -> "power"
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/CompilerValueOperations",
            method,
            "(Ldev/panini/execution/SanskritValue;Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitNumericUnary(operator: NumericUnaryOperator) {
        val method = when (operator) {
            NumericUnaryOperator.SCALE_DOUBLE -> "scaleDouble"
            NumericUnaryOperator.EXACT_SQUARE_ROOT -> "exactSquareRoot"
        }
        mv.visitMethodInsn(
            INVOKESTATIC,
            "dev/panini/compiler/CompilerValueOperations",
            method,
            "(Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
            false,
        )
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
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            RUNTIME,
            "executeDirectValue",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;",
            false,
        )
    }

    private fun emitEnterFrame(call: CompilerInstruction.EnterFrame) {
        val parameterNames = allocateLocal(1)
        val values = List(call.parameterNames.size) { allocateLocal(1) }
        values.asReversed().forEach { local -> mv.visitVarInsn(ASTORE, local) }
        emitStringArray(call.parameterNames)
        mv.visitVarInsn(ASTORE, parameterNames)
        mv.visitVarInsn(ALOAD, 0)
        mv.visitVarInsn(ALOAD, parameterNames)
        mv.visitLdcInsn(values.size)
        mv.visitTypeInsn(ANEWARRAY, "dev/panini/execution/SanskritValue")
        values.forEachIndexed { index, local ->
            mv.visitInsn(DUP)
            mv.visitLdcInsn(index)
            mv.visitVarInsn(ALOAD, local)
            mv.visitInsn(AASTORE)
        }
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            RUNTIME,
            "enterFrame",
            "([Ljava/lang/String;[Ldev/panini/execution/SanskritValue;)V",
            false,
        )
    }

    private fun emitResolveArgument(argument: CompilerInstruction.ResolveArgument) {
        mv.visitVarInsn(ALOAD, 0)
        mv.visitLdcInsn(argument.name)
        argument.fallback?.let { StructuredValueBytecodeEmitter.emit(mv, it) } ?: mv.visitInsn(ACONST_NULL)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            RUNTIME,
            "resolveArgument",
            "(Ljava/lang/String;Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/SanskritValue;",
            false,
        )
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

    private companion object {
        const val RUNTIME = "dev/panini/compiler/CompiledProgramRuntime"
    }
}
