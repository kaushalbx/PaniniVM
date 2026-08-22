package dev.panini.compiler

import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.NamedSamjnaArgumentResolver
import dev.panini.execution.SamjnaKriya
import dev.panini.execution.SamjnaKriyaRegistry
import dev.panini.execution.SamjnaArgumentResolution
import dev.panini.execution.SamjnaSignatureDeclarationParser
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

        fun emit(mv: MethodVisitor, node: ProgramNode, exactSource: String? = null) {
            when (node) {
                is Invocation -> emitInvocation(mv, node, exactSource)
                is Sequence, is Pipeline, is Quotation -> emitEval(mv, exactSource ?: render(node))
                is Conditional -> emitConditional(mv, node)
                is Repeat -> emitRepeat(mv, node)
                is WhileLoop -> emitWhile(mv, node)
                is Procedure -> node.body.forEach { emit(mv, it) }
                is Scope -> node.body.forEach { emit(mv, it) }
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

        private fun emitInvocation(mv: MethodVisitor, node: Invocation, exactSource: String?) {
            val source = normalized(exactSource ?: render(node))
            val invocation = registry.detectInvocation(source)
            val method = invocation?.kriya?.nameStem?.let(methodsByStem::get)
            if (method != null) {
                val signature = requireNotNull(invocation).kriya.signature
                val resolution = NamedSamjnaArgumentResolver.resolve(invocation.karmaText, signature)
                val arguments = when (resolution) {
                    is SamjnaArgumentResolution.Success -> resolution.terms
                    is SamjnaArgumentResolution.Failure -> error(resolution.message)
                }
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
                emitEval(mv, source)
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
            val normalExit = Label()
            val completion = Label()
            val bound = node.maximumIterationStems.takeIf(List<String>::isNotEmpty)?.let {
                dev.panini.sankhya.SankhyaEvaluator().evaluateStems(it).value
            }
            val counter = bound?.let {
                val local = nextLocal
                nextLocal += 2
                mv.visitInsn(LCONST_0)
                mv.visitVarInsn(LSTORE, local)
                local
            }
            mv.visitLabel(condition)
            if (bound != null && counter != null) {
                mv.visitVarInsn(LLOAD, counter)
                mv.visitLdcInsn(bound)
                mv.visitInsn(LCMP)
                mv.visitJumpInsn(IFGE, completion)
            }
            emitBoolean(mv, render(node.condition))
            mv.visitJumpInsn(IFEQ, normalExit)
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "enterConditionIteration",
                "()V",
                false,
            )
            emit(mv, node.body)
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "dev/panini/compiler/CompiledProgramRuntime",
                "consumeBreak",
                "()Z",
                false,
            )
            mv.visitJumpInsn(IFNE, normalExit)
            counter?.let {
                mv.visitVarInsn(LLOAD, it)
                mv.visitInsn(LCONST_1)
                mv.visitInsn(LADD)
                mv.visitVarInsn(LSTORE, it)
            }
            mv.visitJumpInsn(GOTO, condition)
            mv.visitLabel(completion)
            if (bound != null) node.exhausted?.let { emit(mv, it) }
            mv.visitJumpInsn(GOTO, normalExit)
            mv.visitLabel(normalExit)
            node.resultTarget?.let { emit(mv, it) }
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
        statements.forEach { sentence ->
            sentence.program?.let { lowering.emit(mv, it, sentence.text) }
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

}
