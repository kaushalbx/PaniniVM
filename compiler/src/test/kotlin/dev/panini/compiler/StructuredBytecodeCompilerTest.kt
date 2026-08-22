package dev.panini.compiler

import dev.panini.execution.SanskritValue
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Opcodes.IFEQ
import org.objectweb.asm.Opcodes.LCMP
import org.objectweb.asm.Opcodes.LLOAD
import java.io.File
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class StructuredBytecodeCompilerTest {
    @Test
    fun `tatah numeric pipeline stores its result without the interpreter bridge`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectTatahPipeline")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectTatahPipeline", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(interpreted, values.getValue("अवस्था"))
        assertEquals(5L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue("executeDirect" in executeCalls, executeCalls.toString())
        assertTrue("executeDirectStore" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `explicit phala stores the previous direct result without the interpreter bridge`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।
            फल + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectPhalaStore")
        val executeCalls = mutableListOf<String>()
        var referencesLastResult = false
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitLdcInsn(value: Any?) {
                            if (value == "LastResult") referencesLastResult = true
                        }

                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectPhalaStore", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(interpreted, values.getValue("अवस्था"))
        assertEquals(5L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue("executeDirect" in executeCalls, executeCalls.toString())
        assertTrue("executeDirectStore" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue(referencesLastResult)
    }

    @Test
    fun `fixed repetition loads compiled state directly on every iteration`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वः अवस्था + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectStateRepeat")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectStateRepeat", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(3L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(2L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue(executeCalls.count { it == "executeDirectStore" } == 1, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirect" } == 3, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `simple loop result target executes without the interpreter bridge`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वः यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ततः परिणाम + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectLoopTarget")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectLoopTarget", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa

        assertEquals(outcome, values.getValue("LastResult"))
        assertEquals("विजय", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(1L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue("executeDirectLoopTarget" in executeCalls, executeCalls.toString())
        assertTrue("evaluateLoopTarget" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `bounded direct state loop lowers its exhaustion leaf directly`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            एक + कृत्वः यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् अन्यथा द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectExhaustion")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectExhaustion", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(1L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals("समाप्ति", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(1L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue(executeCalls.count { it == "executeDirectStore" } == 2, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirect" } == 1, executeCalls.toString())
        assertTrue("executeDirectBoolean" in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `bounded state loop loads and stores directly`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वः यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectStateLoop")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectStateLoop", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(0L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals("विजय", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(1L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue(executeCalls.count { it == "executeDirectStore" } == 2, executeCalls.toString())
        assertTrue("executeDirectBoolean" in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `assigned state feeds a directly compiled final conditional`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            यदि अवस्था + अम् एक + अम् च विद् + लोट् + सिप् तर्हि द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् अन्यथा शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledStateConditional")
        val executeCalls = mutableListOf<String>()
        var createsReference = false
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                            if (owner == "dev/panini/compiler/PaniniRuntime" &&
                                name == "createReferenceExpression"
                            ) {
                                createsReference = true
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledStateConditional", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(5L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(2L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue("executeDirectStore" in executeCalls, executeCalls.toString())
        assertTrue("executeDirectBoolean" in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue(createsReference)
    }

    @Test
    fun `straight line assignment is loaded directly by a later numeric leaf`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            अवस्था + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectLoad")
        val executeCalls = mutableListOf<String>()
        var createsReference = false
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                            if (owner == "dev/panini/compiler/PaniniRuntime" &&
                                name == "createReferenceExpression"
                            ) {
                                createsReference = true
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectLoad", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(3L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertEquals(5L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("executeDirectStore" in executeCalls, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirect" } == 1, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue(createsReference)
    }

    @Test
    fun `terminal literal assignment stores compiled state directly`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledTerminalStore")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledTerminalStore", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(interpreted, values.getValue("अवस्था"))
        assertTrue("executeDirectStore" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `literal numeric condition branches without the interpreter bridge`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् तर्हि द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् अन्यथा शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledLiteralCondition")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledLiteralCondition", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(5L, (compiled as SanskritValue.Sankhya).value)
        assertTrue("executeDirectBoolean" in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `pure numeric leaves execute directly without the interpreter bridge`() {
        val source = """
            परिचय + ल्युट् + सुँ ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
            दशन् + शस् त्रि + शस् च वि + युज् + णिच् + लोट् + सिप् ।
            त्रि + शस् द्वि + औट् च गण् + णिच् + लोट् + सिप् ।
            दशन् + शस् द्वि + औट् च हृ + लोट् + सिप् ।
            दशन् + शस् त्रि + शस् च शिष् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledNumericLeaves")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledNumericLeaves", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(1L, (compiled as SanskritValue.Sankhya).value)
        assertEquals(5, executeCalls.count { it == "executeDirect" }, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `compiled parameter frames preserve structured values`() {
        val runtime = CompiledProgramRuntime()
        runtime.publishLoopOutcome("विजय", 3L)
        runtime.enterFrame(arrayOf("मान"), arrayOf("फल"))

        val returned = runtime.evaluate("मान + अम् सञ्चित + ङे दा + लोट् + सिप् ।")

        runtime.exitFrame()
        val structured = returned as SanskritValue.Rupa
        assertEquals("परिणाम", structured.schema)
        assertEquals("विजय", structured.fields.getValue("अवस्था").toDisplayText())
        assertEquals(3L, (structured.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `large grammatical loop bounds use JVM long counters`() {
        val source = """
            शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ।
            कोटि + कृत्वः यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val bytes = BytecodeCompiler.compile(source, "CompiledLargeLoopBound")
        val instructions = mutableListOf<Int>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitInsn(opcode: Int) {
                        instructions += opcode
                    }

                    override fun visitVarInsn(opcode: Int, varIndex: Int) {
                        instructions += opcode
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledLargeLoopBound", bytes)
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = result.getValue("परिणाम") as SanskritValue.Rupa

        assertTrue(LLOAD in instructions)
        assertTrue(LCMP in instructions)
        assertEquals(0L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `nested compiled loops preserve interpreter state parity`() {
        val source = """
            हृ + ल्युट् + सुँ ।
            अन्तरावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अन्तरावस्था + ङे ॥

            क्षि + ल्युट् + सुँ ।
            बाह्यावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् बाह्यावस्था + ङे ॥

            अन्तरचक्र + ल्युट् + सुँ ।
            द्वि + अम् अन्तरावस्था + ङे दा + लोट् + सिप् ।
            यावत् अन्तरावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् ॥

            बाह्यचक्र + ल्युट् + सुँ ।
            अन्तरचक्र + ल्युट् + टा कृ + लोट् + सिप् ।
            क्षि + ल्युट् + टा कृ + लोट् + सिप् ॥

            द्वि + अम् बाह्यावस्था + ङे दा + लोट् + सिप् ।
            यावत् बाह्यावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् बाह्यचक्र + ल्युट् + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् बाह्यावस्था + अम् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledNestedLoops")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals("शून्यम्", compiled.toDisplayText())
    }

    @Test
    fun `recursive generated samjna calls unwind and resume top-level execution`() {
        val source = """
            हृ + ल्युट् + सुँ ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ।
            गण् + ल्युट् + टा कृ + लोट् + सिप् ॥

            गण् + ल्युट् + सुँ ।
            यदि अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तर्हि हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा वि + स्था + लोट् + सिप् ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            गण् + ल्युट् + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् अवस्था + अम् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledRecursiveCountdown")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals("शून्यम्", compiled.toDisplayText())
    }

    @Test
    fun `compiled named calls enforce signatures and prohibitions`() {
        val wrongType = """
            द्विगुणन + ल्युट् + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् द्वि + अम् च गुण् + णिच् + लोट् + सिप् ॥
            राम + अम् द्विगुणन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val prohibited = """
            विभाज् + ल्युट् + सुँ ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भाज् + णिच् + लोट् + सिप् ॥
            दश + अम् शून्य + अम् च विभाज् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val typeFailure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compile(wrongType, "RejectedCompiledType")
        }
        val prohibitionFailure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compile(prohibited, "RejectedCompiledProhibition")
        }

        assertTrue(typeFailure.message.orEmpty().contains("मानप्रकार"))
        assertTrue(prohibitionFailure.message.orEmpty().contains("निषेध-प्रतिषेध"))
    }

    @Test
    fun `phala controlled loops have interpreter compiler parity`() {
        fun execute(source: String, className: String): Pair<SanskritValue, SanskritValue> {
            val interpreted = PaniniVM().evalScript(source)
                .filterIsInstance<ExecutionResult.Success>().last().typedValue!!
            val generated = BytecodeCompiler.compileAndLoad(source, className)
            @Suppress("UNCHECKED_CAST")
            val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
                .getValue("LastResult")
            return interpreted to compiled
        }

        val exhausted = """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val victory = """
            प्रयत्न + ल्युट् + सुँ ।
            द्वि + अम् एक + अम् च विद् + लोट् + सिप् ॥
            पञ्च + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val exhaustedResults = execute(exhausted, "CompiledPhalaExhaustion")
        val victoryResults = execute(victory, "CompiledPhalaVictory")
        assertEquals(exhaustedResults.first, exhaustedResults.second)
        assertEquals(victoryResults.first, victoryResults.second)
        assertEquals(
            "समाप्ति",
            (exhaustedResults.second as SanskritValue.Rupa).fields.getValue("अवस्था").toDisplayText(),
        )
        assertEquals(
            "विजय",
            (victoryResults.second as SanskritValue.Rupa).fields.getValue("अवस्था").toDisplayText(),
        )
    }

    @Test
    fun `bounded compiled loop publishes and pipes its exhaustion outcome`() {
        val source = """
            हृ + ल्युट् + सुँ ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            द्वि + कृत्वः यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledLoopOutcome")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals("समाप्ति", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(2L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `break signal exits the nearest compiled repetition`() {
        val source = """
            प्रयत्न + ल्युट् + सुँ ।
            वि + स्था + लोट् + सिप् ॥

            पञ्च + कृत्वः प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>()
            .single { it.controlSignal != null }
            .typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledBreakRepetition")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
    }

    @Test
    fun `compiled non-halting loop obeys an explicit host budget`() {
        val source = """
            एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
            यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledBudgetedLoop")

        val thrown = assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute", java.lang.Long.TYPE).invoke(null, 3L)
        }

        val limit = thrown.targetException as CompiledExecutionLimitExceededException
        assertTrue(limit.message.orEmpty().contains("3 iterations"))
    }

    @Test
    fun `parameterized named operation has interpreter compiler parity`() {
        val source = """
            व्यवकलन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङस् द्वि + अम् वाम + ङस् पञ्च + अम् व्यवकलन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledParameterizedSamjna")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(3L, (compiled as SanskritValue.Sankhya).value)
    }

    @Test
    fun `pipeline result enters a typed generated samjna operation`() {
        val source = """
            वर्धन + ल्युट् + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ततः वर्धन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(
            interpretedResults.none { it is ExecutionResult.Failure },
            interpretedResults.joinToString(),
        )
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledPipedSamjna")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledPipedSamjna", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(4L, (compiled as SanskritValue.Sankhya).value)
        assertTrue("executeDirect" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `two-counter proof compiles to JVM branches and executes`() {
        val source = File("examples/control_flow/two_counter_machine.pvm").readText()
        val bytes = BytecodeCompiler.compile(source, "CompiledTwoCounterMachine")
        val jumps = mutableListOf<Int>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitJumpInsn(opcode: Int, label: Label?) {
                        jumps += opcode
                    }
                }
            },
            0,
        )

        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledTwoCounterMachine", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertTrue(IFEQ in jumps, "Conditional and loop exits must use JVM conditional branches.")
        assertTrue(GOTO in jumps, "The unbounded loop must contain a JVM backward branch.")
        assertEquals("त्रीणि", values.getValue("LastResult").toDisplayText())
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals("विजय", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(7L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }
}
