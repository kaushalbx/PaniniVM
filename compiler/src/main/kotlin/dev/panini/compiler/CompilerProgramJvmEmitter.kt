package dev.panini.compiler

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*

/** JVM class backend. Its input is compiler IR and it has no parser or AST dependency. */
internal object CompilerProgramJvmEmitter {
    private const val RUNTIME = "dev/panini/compiler/CompiledProgramRuntime"
    private const val RUNTIME_DESCRIPTOR = "Ldev/panini/compiler/CompiledProgramRuntime;"

    fun emit(program: CompilerProgram): ByteArray {
        CompilerProgramVerifier.verify(program)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        writer.visit(V1_8, ACC_PUBLIC or ACC_SUPER, program.className, null, "java/lang/Object", null)
        emitConstructor(writer)
        emitExecute(writer, program, withLimit = false)
        emitExecute(writer, program, withLimit = true)
        emitExecuteWithInitialState(writer, program)
        program.procedures.forEach { procedure ->
            val method = writer.visitMethod(
                ACC_PUBLIC or ACC_STATIC,
                procedure.methodName,
                "($RUNTIME_DESCRIPTOR)V",
                null,
                null,
            )
            method.visitCode()
            var nextLocal = 1
            CompilerIrJvmEmitter(program.className, method) { width ->
                nextLocal.also { nextLocal += width }
            }.emit(procedure.instructions)
            method.visitMaxs(0, 0)
            method.visitEnd()
        }
        emitMain(writer, program.className)
        writer.visitEnd()
        return writer.toByteArray()
    }

    private fun emitExecuteWithInitialState(writer: ClassWriter, program: CompilerProgram) {
        val method = writer.visitMethod(
            ACC_PUBLIC or ACC_STATIC,
            "execute",
            "(Ljava/util/Map;)Ljava/util/Map;",
            null,
            null,
        )
        method.visitCode()
        method.visitTypeInsn(NEW, RUNTIME)
        method.visitInsn(DUP)
        method.visitVarInsn(ALOAD, 0)
        method.visitMethodInsn(INVOKESPECIAL, RUNTIME, "<init>", "(Ljava/util/Map;)V", false)
        method.visitVarInsn(ASTORE, 0)
        var nextLocal = 1
        CompilerIrJvmEmitter(program.className, method) { width -> nextLocal.also { nextLocal += width } }
            .emit(program.entryPoint)
        method.visitVarInsn(ALOAD, 0)
        method.visitMethodInsn(INVOKEVIRTUAL, RUNTIME, "snapshot", "()Ljava/util/Map;", false)
        method.visitInsn(ARETURN)
        method.visitMaxs(0, 0)
        method.visitEnd()
    }

    private fun emitConstructor(writer: ClassWriter) {
        val method = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        method.visitCode()
        method.visitVarInsn(ALOAD, 0)
        method.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        method.visitInsn(RETURN)
        method.visitMaxs(0, 0)
        method.visitEnd()
    }

    private fun emitExecute(writer: ClassWriter, program: CompilerProgram, withLimit: Boolean) {
        val descriptor = if (withLimit) "(J)Ljava/util/Map;" else "()Ljava/util/Map;"
        val method = writer.visitMethod(ACC_PUBLIC or ACC_STATIC, "execute", descriptor, null, null)
        method.visitCode()
        method.visitTypeInsn(NEW, RUNTIME)
        method.visitInsn(DUP)
        if (withLimit) method.visitVarInsn(LLOAD, 0)
        method.visitMethodInsn(INVOKESPECIAL, RUNTIME, "<init>", if (withLimit) "(J)V" else "()V", false)
        method.visitVarInsn(ASTORE, 0)
        var nextLocal = 1
        CompilerIrJvmEmitter(program.className, method) { width ->
            nextLocal.also { nextLocal += width }
        }.emit(program.entryPoint)
        method.visitVarInsn(ALOAD, 0)
        method.visitMethodInsn(INVOKEVIRTUAL, RUNTIME, "snapshot", "()Ljava/util/Map;", false)
        method.visitInsn(ARETURN)
        method.visitMaxs(0, 0)
        method.visitEnd()
    }

    private fun emitMain(writer: ClassWriter, className: String) {
        val method = writer.visitMethod(ACC_PUBLIC or ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        method.visitCode()
        method.visitMethodInsn(INVOKESTATIC, className, "execute", "()Ljava/util/Map;", false)
        method.visitInsn(POP)
        method.visitInsn(RETURN)
        method.visitMaxs(0, 0)
        method.visitEnd()
    }
}
