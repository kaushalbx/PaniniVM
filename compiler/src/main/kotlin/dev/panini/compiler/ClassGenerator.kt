package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.SanskritValue
import dev.panini.execution.bindingName
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type

object ClassGenerator {

    fun generateClass(
        className: String,
        statements: List<List<ExecutionPlan>>,
        turnResultIds: List<List<String>>,
    ): ByteArray {
        val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        
        // 1. Collect all unique SanskritValue constants
        val uniqueConstants = mutableSetOf<SanskritValue>()
        statements.forEach { plans ->
            plans.forEach { plan ->
                plan.resolved.context.bindings.values.forEach { expr ->
                    collectConstants(expr, uniqueConstants)
                }
            }
        }
        val constantFields = uniqueConstants.mapIndexed { index, value -> value to "sanskrit_const_$index" }.toMap()

        // Define class header: public class <className> extends java.lang.Object
        cw.visit(
            V1_8,
            ACC_PUBLIC or ACC_SUPER,
            className,
            null,
            "java/lang/Object",
            null
        )

        // 2. Declare private static final fields for pooled constants
        constantFields.forEach { (_, fieldName) ->
            cw.visitField(
                ACC_PRIVATE or ACC_STATIC or ACC_FINAL,
                fieldName,
                "Ldev/panini/execution/SanskritValue;",
                null,
                null
            ).visitEnd()
        }

        // 3. Generate static initializer (<clinit>) to instantiate pooled constants
        if (constantFields.isNotEmpty()) {
            val clinit = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null)
            clinit.visitCode()
            constantFields.forEach { (value, fieldName) ->
                when (value) {
                    is SanskritValue.Sankhya -> {
                        clinit.visitLdcInsn(value.value)
                        clinit.visitLdcInsn(value.word)
                        clinit.visitMethodInsn(
                            INVOKESTATIC,
                            "dev/panini/compiler/PaniniRuntime",
                            "sankhya",
                            "(JLjava/lang/String;)Ldev/panini/execution/SanskritValue;",
                            false
                        )
                    }
                    is SanskritValue.Shabda -> {
                        clinit.visitLdcInsn(value.text)
                        clinit.visitMethodInsn(
                            INVOKESTATIC,
                            "dev/panini/compiler/PaniniRuntime",
                            "shabda",
                            "(Ljava/lang/String;)Ldev/panini/execution/SanskritValue;",
                            false
                        )
                    }
                    else -> {
                        clinit.visitLdcInsn(value.toDisplayText())
                        clinit.visitMethodInsn(
                            INVOKESTATIC,
                            "dev/panini/compiler/PaniniRuntime",
                            "shabda",
                            "(Ljava/lang/String;)Ldev/panini/execution/SanskritValue;",
                            false
                        )
                    }
                }
                clinit.visitFieldInsn(
                    PUTSTATIC,
                    className,
                    fieldName,
                    "Ldev/panini/execution/SanskritValue;"
                )
            }
            clinit.visitInsn(RETURN)
            clinit.visitMaxs(0, 0)
            clinit.visitEnd()
        }

        // Generate constructor: public <init>()
        var mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(RETURN)
        mv.visitMaxs(1, 1)
        mv.visitEnd()

        // Generate execute method: public static java.util.Map execute()
        mv = cw.visitMethod(ACC_PUBLIC or ACC_STATIC, "execute", "()Ljava/util/Map;", null, null)
        mv.visitCode()

        // Local variable 1: variablesMap = new HashMap<String, SanskritValue>()
        mv.visitTypeInsn(NEW, "java/util/HashMap")
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
        mv.visitVarInsn(ASTORE, 1)

        // Iterate through each statement
        statements.forEachIndexed { stmtIdx, plans ->
            plans.forEachIndexed { planIdx, plan ->
                // Local variable 2: bindingsMap = new HashMap<Karaka, ExecutionExpression>()
                mv.visitTypeInsn(NEW, "java/util/HashMap")
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false)
                mv.visitVarInsn(ASTORE, 2)

                // Populate bindingsMap
                plan.resolved.context.bindings.forEach { (karaka, expr) ->
                    mv.visitVarInsn(ALOAD, 2)
                    // Push Karaka field
                    mv.visitFieldInsn(GETSTATIC, "dev/panini/core/Karaka", karaka.name, "Ldev/panini/core/Karaka;")
                    // Push Expression
                    compileExpression(mv, expr, constantFields, className)
                    // Put in map
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/util/HashMap",
                        "put",
                        "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                        false
                    )
                    mv.visitInsn(POP)
                }

                // Execute action
                mv.visitLdcInsn(plan.resolved.invocation.dhatu.upadesha)
                mv.visitLdcInsn(plan.resolved.operation.name)
                mv.visitLdcInsn(plan.resolved.operation.trigger.requiredSanadi.sorted().joinToString(","))
                mv.visitVarInsn(ALOAD, 2)
                mv.visitVarInsn(ALOAD, 1)
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/PaniniRuntime",
                    "execute",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)Ldev/panini/execution/SanskritValue;",
                    false
                )

                // Store result in variablesMap under plan.invocationId
                mv.visitInsn(DUP) // Duplicate result reference
                mv.visitVarInsn(ALOAD, 1) // Load variablesMap
                mv.visitInsn(SWAP) // Swap to [result, map, result]
                mv.visitLdcInsn(plan.invocationId)
                mv.visitInsn(SWAP) // Swap to [result, map, id, result]
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/util/HashMap",
                    "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    false
                )
                mv.visitInsn(POP)

                // Store result in variablesMap under historical turn IDs if available
                val turnId = turnResultIds.getOrNull(stmtIdx)?.getOrNull(planIdx)
                if (turnId != null) {
                    mv.visitInsn(DUP)
                    mv.visitVarInsn(ALOAD, 1)
                    mv.visitInsn(SWAP)
                    mv.visitLdcInsn(turnId)
                    mv.visitInsn(SWAP)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/util/HashMap",
                        "put",
                        "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                        false
                    )
                    mv.visitInsn(POP)
                }

                val bindingKaraka = plan.resolved.operation.resultBindingKaraka
                val localBindingName = bindingKaraka
                    ?.let(plan.resolved.context.bindings::get)
                    ?.bindingName()
                if (localBindingName != null) {
                    mv.visitInsn(DUP)
                    mv.visitVarInsn(ALOAD, 1)
                    mv.visitInsn(SWAP)
                    mv.visitLdcInsn(localBindingName)
                    mv.visitInsn(SWAP)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/util/HashMap",
                        "put",
                        "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                        false
                    )
                    mv.visitInsn(POP)
                }

                // Store under "LastResult" key to handle general "फल" references
                mv.visitInsn(DUP)
                mv.visitVarInsn(ALOAD, 1)
                mv.visitInsn(SWAP)
                mv.visitLdcInsn("LastResult")
                mv.visitInsn(SWAP)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/util/HashMap",
                    "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    false
                )
                mv.visitInsn(POP)

                // Pop the original result off the stack
                mv.visitInsn(POP)

            }
        }

        mv.visitVarInsn(ALOAD, 1)
        mv.visitInsn(ARETURN)
        mv.visitMaxs(0, 0)
        mv.visitEnd()

        // Generate main method: public static void main(String[] args)
        mv = cw.visitMethod(ACC_PUBLIC or ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        mv.visitCode()
        mv.visitMethodInsn(INVOKESTATIC, className, "execute", "()Ljava/util/Map;", false)
        mv.visitInsn(POP)
        mv.visitInsn(RETURN)
        mv.visitMaxs(0, 0)
        mv.visitEnd()

        cw.visitEnd()
        return cw.toByteArray()
    }

    private fun collectConstants(expr: ExecutionExpression, collected: MutableSet<SanskritValue>) {
        when (expr) {
            is ExecutionExpression.Pada -> {
                expr.value?.let { collected.add(it) }
            }
            is ExecutionExpression.Coordination -> {
                expr.members.forEach { collectConstants(it, collected) }
            }
            is ExecutionExpression.Reference -> {}
        }
    }

    private fun compileExpression(
        mv: MethodVisitor,
        expr: ExecutionExpression,
        constantFields: Map<SanskritValue, String>,
        className: String
    ) {
        when (expr) {
            is ExecutionExpression.Pada -> {
                mv.visitLdcInsn(expr.prakriti)
                val value = expr.value
                val fieldName = constantFields[value]
                if (value == null || fieldName == null) {
                    mv.visitInsn(ACONST_NULL)
                } else {
                    mv.visitFieldInsn(
                        GETSTATIC,
                        className,
                        fieldName,
                        "Ldev/panini/execution/SanskritValue;"
                    )
                }
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/PaniniRuntime",
                    "createPadaExpression",
                    "(Ljava/lang/String;Ldev/panini/execution/SanskritValue;)Ldev/panini/execution/ExecutionExpression\$Pada;",
                    false
                )
            }
            is ExecutionExpression.Coordination -> {
                // Create Array: ExecutionExpression[expr.members.size]
                mv.visitLdcInsn(expr.members.size)
                mv.visitTypeInsn(ANEWARRAY, "dev/panini/execution/ExecutionExpression")
                expr.members.forEachIndexed { i, member ->
                    mv.visitInsn(DUP)
                    mv.visitLdcInsn(i)
                    compileExpression(mv, member, constantFields, className)
                    mv.visitInsn(AASTORE)
                }
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/PaniniRuntime",
                    "createCoordinationExpression",
                    "([Ldev/panini/execution/ExecutionExpression;)Ldev/panini/execution/ExecutionExpression\$Coordination;",
                    false
                )
            }
            is ExecutionExpression.Reference -> {
                mv.visitLdcInsn(expr.name)
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "dev/panini/compiler/PaniniRuntime",
                    "createReferenceExpression",
                    "(Ljava/lang/String;)Ldev/panini/execution/ExecutionExpression\$Reference;",
                    false
                )
            }
        }
    }
}
