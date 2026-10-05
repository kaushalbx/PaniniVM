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
import org.objectweb.asm.Opcodes.ALOAD
import java.io.File
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class StructuredBytecodeCompilerTest {
    @Test
    fun `una and adhika numeral constructions preserve values in both backends`() {
        for ((construction, expected) in listOf(
            "एक + ऊन + विंशति" to 19L,
            "द्वि + विंशति + अधिक + शत" to 122L,
        )) {
            val source = "$construction + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।"
            val compiled = compileAndInspect(source, "CompiledConstructedNumeral$expected")
            assertEquals(expected, (compiled.values.getValue("परिणाम") as SanskritValue.Sankhya).value)
            val interpreted = PaniniVM().evalScript(source)
            assertTrue(interpreted.none { it is ExecutionResult.Failure }, interpreted.toString())
            assertEquals(expected, ((interpreted.last() as ExecutionResult.Success).typedValue as SanskritValue.Sankhya).value)
            assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        }
    }
    @Test
    fun `natural membership uses locative collection in compiled and interpreted execution`() {
        val source = """
            एक + अम् त्रि + शस् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।
            यदि एक + सुँ सूची + ङि असँ + लट् + तिप् तर्हि सत्य + सुँ अन्यथा असत्य + सुँ
                ततः फल + अम् उपस्थित + ङि स्था + णिच् + लोट् + सिप् ।
            यदि शून्य + सुँ सूची + ङि असँ + लट् + तिप् तर्हि सत्य + सुँ अन्यथा असत्य + सुँ ।
        """.trimIndent()
        val compiled = compileAndInspect(source, "CompiledNaturalMembership")
        assertEquals(SanskritValue.Satya(true), compiled.values.getValue("उपस्थित"))
        assertEquals(SanskritValue.Satya(false), compiled.values.getValue("LastResult"))
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.none { it is ExecutionResult.Failure }, interpreted.toString())
        assertEquals(SanskritValue.Satya(false), (interpreted.last() as ExecutionResult.Success).typedValue)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `locative ordinal lowers to a direct one based collection index`() {
        val source = """
            एक + अम् द्वि + अम् त्रि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।
            सूची + ङसिँ द्वि + तीय + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
        """.trimIndent()
        val compiled = compileAndInspect(source, "CompiledOrdinalCollectionIndex")
        assertEquals(2L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `bare numeric conditional values remain typed compiler constants`() {
        val source =
            "यदि एक + अम् एक + अम् च विद् + लोट् + सिप् " +
                "तर्हि द्वि अन्यथा त्रि ।"

        val compiled = compileAndInspect(source, "CompiledBareNumericConditional")

        assertEquals(3L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `natural indexed retrieval lowers source and position to index IR`() {
        val source = """
            एक + अम् क्षिप् + णिच् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् क्रम + ङि स्था + णिच् + लोट् + सिप् ।
            द्वि + अम् क्रम + ङि नि + क्षिप् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् क्रम + ङि स्था + णिच् + लोट् + सिप् ।
            द्वि + अम् क्रमाङ्क + ङि स्था + णिच् + लोट् + सिप् ।
            क्रम + ङसिँ क्रमाङ्क + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledNaturalIndexedRetrieval")

        assertEquals(2L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `existential clause branches directly on a named truth state`() {
        val source = """
            सत्य + अम् अवस्था + ङि स्था + णिच् + लोट् + सिप् ।
            यदि अवस्था + सुँ असँ + लट् + तिप्
                तर्हि सत्य + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप्
                अन्यथा असत्य + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledExistentialTruthClause")

        assertEquals(true, (compiled.values.getValue("परिणाम") as SanskritValue.Satya).boolean)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `natural locative insertion appends through explicit compiler IR`() {
        val source = """
            एक + अम् क्षिप् + णिच् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् क्रम + ङि स्था + णिच् + लोट् + सिप् ।
            द्वि + अम् क्रम + ङि नि + क्षिप् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् नवीनक्रम + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledNaturalLocativeInsertion")
        val result = requireNotNull(compiled.values["नवीनक्रम"]) { compiled.values.toString() } as SanskritValue.Suchi

        assertEquals(listOf(1L, 2L), result.items.map { (it as SanskritValue.Sankhya).value })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `natural range choice excludes values in ablative collection`() {
        val source = """
            एक + ङसिँ द्वि + शस् परि + अन्त + अम् इति सीमा + सुँ ।
            एक + अम् क्षिप् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् क्षिप् + घञ् + ङस् फल + अम् क्रम + ङे ।
            क्रम + अम् वृज् + णिच् + क्त्वा चिञ् + श्नु + लोट् + सिप् ततः चयन + ङे दा + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledNaturalExcludedChoice")

        assertEquals(2L, (compiled.values.getValue("चयन") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `compiled execution preserves interpreter failure details`() {
        val source = collectionProgram("सूची + अम् चतुर् + टा स्था + लोट् + सिप्")
        val interpreted = PaniniVM().evalScript(source).filterIsInstance<ExecutionResult.Failure>().last()
        val bytes = BytecodeCompiler.compile(source, "CompiledInvalidListIndex")
        val runtimeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? = object : MethodVisitor(ASM9) {
                    override fun visitMethodInsn(
                        opcode: Int,
                        owner: String?,
                        name: String?,
                        descriptor: String?,
                        isInterface: Boolean,
                    ) {
                        if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                            name?.let(runtimeCalls::add)
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledInvalidListIndex", bytes)

        val invocation = assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }
        val compiled = invocation.targetException as CompiledPaniniExecutionException

        assertTrue("storeValue" in runtimeCalls, runtimeCalls.toString())
        assertTrue(runtimeCalls.none { it.startsWith("evaluate") }, runtimeCalls.toString())
        assertEquals(interpreted.error, compiled.error)
        assertEquals(interpreted.message, compiled.message)
        assertEquals(interpreted.trace, compiled.trace)
    }

    @Test
    fun `state backed for each accumulation executes directly`() {
        val source = collectionProgram("सूची + अम् यु + टा दशन् + ङे अनु + वृत् + लोट् + सिप्")
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListForEach")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(16L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(3, compiled.runtimeCalls.count { it == "storeValue" })
        assertEquals(2, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `higher order list operations execute directly`() {
        val cases = listOf(
            CollectionResultCase(
                name = "Map",
                operation = "सूची + अम् एधँ + टा सम् + यु + लोट् + सिप्",
                expected = SanskritValue.Suchi(
                    listOf(
                        SanskritValue.Sankhya(2L, "द्वे"),
                        SanskritValue.Sankhya(4L, "चत्वारि"),
                        SanskritValue.Sankhya(6L, "षट्"),
                    ),
                ),
            ),
            CollectionResultCase(
                name = "Filter",
                operation = "सूची + अम् यु + टा वि + वृज् + लोट् + सिप्",
                expected = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(2L, "द्वि"))),
            ),
            CollectionResultCase(
                name = "Fold",
                operation = "सूची + अम् यु + टा शून्य + ङे सम् + क्षिप् + लोट् + सिप्",
                expected = SanskritValue.Sankhya(6L, "षट्"),
            ),
        )

        cases.forEach { case ->
            val source = collectionProgram(case.operation)
            val interpretedResults = PaniniVM().evalScript(source)
            assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
            val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
            val compiled = compileAndInspect(source, "CompiledDirectList${case.name}")

            assertEquals(interpreted, compiled.values.getValue("LastResult"), case.name)
            assertEquals(case.expected, compiled.values.getValue("LastResult"), case.name)
            assertTrue("evaluate" !in compiled.runtimeCalls, "$case: ${compiled.runtimeCalls}")
        }
    }

    @Test
    fun `nested stored lists flatten directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च प्रथमा + ङे दा + लोट् + सिप् ।
            त्रि + अम् चतुर् + अम् च द्वितीया + ङे दा + लोट् + सिप् ।
            प्रथमा + अम् द्वितीया + अम् च संयुक्ता + ङे दा + लोट् + सिप् ।
            संयुक्ता + अम् तन् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListFlatten")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        val nested = compiled.values.getValue("संयुक्ता") as SanskritValue.Suchi
        assertTrue(nested.items.all { it is SanskritValue.Suchi }, nested.toString())
        val flattened = compiled.values.getValue("LastResult") as SanskritValue.Suchi
        assertEquals(listOf(1L, 2L, 3L, 4L), flattened.items.map { (it as SanskritValue.Sankhya).value })
        assertEquals(7, compiled.runtimeCalls.count { it == "storeValue" })
        assertEquals(2, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list transformations execute directly`() {
        val cases = listOf(
            CollectionCase(
                name = "Reverse",
                operation = "सूची + अम् प्रति + वृत् + लोट् + सिप्",
                expected = listOf(3L, 2L, 1L),
            ),
            CollectionCase(
                name = "Append",
                operation = "सूची + अम् चतुर् + अम् च क्षिप् + लोट् + सिप्",
                expected = listOf(1L, 2L, 3L, 4L),
            ),
            CollectionCase(
                name = "Slice",
                operation = "सूची + ङस् द्वि + तीय + ङसिँ त्रि + तीय + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप्",
                expected = listOf(2L, 3L),
            ),
        )

        cases.forEach { case ->
            val source = collectionProgram(case.operation)
            val interpretedResults = PaniniVM().evalScript(source)
            assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
            val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
            val compiled = compileAndInspect(source, "CompiledDirectList${case.name}")

            assertEquals(interpreted, compiled.values.getValue("LastResult"), case.name)
            val result = compiled.values.getValue("LastResult") as SanskritValue.Suchi
            assertEquals(case.expected, result.items.map { (it as SanskritValue.Sankhya).value }, case.name)
            assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" }, case.name)
            assertTrue("evaluate" !in compiled.runtimeCalls, "$case: ${compiled.runtimeCalls}")
        }
    }

    @Test
    fun `state backed list pop executes directly`() {
        val source = collectionProgram("सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप्")
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListPop")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(3L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(listOf(1L, 2L, 3L), (compiled.values.getValue("सूची") as SanskritValue.Suchi).items.map { (it as SanskritValue.Sankhya).value })
        assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `two stored lists concatenate directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् पूर्वसूची + ङि स्था + णिच् + लोट् + सिप् ।
            त्रि + अम् चतुर् + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् उत्तरसूची + ङि स्था + णिच् + लोट् + सिप् ।
            पूर्वसूची + अम् उत्तरसूची + टा सम् + युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListConcat")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        val result = compiled.values.getValue("LastResult") as SanskritValue.Suchi
        assertEquals(listOf(1L, 2L, 3L, 4L), result.items.map { (it as SanskritValue.Sankhya).value })
        assertTrue("executeDirectValue" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `constructed list and length execute directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् त्रि + अम् च सूची + ङे दा + लोट् + सिप् ।
            सूची + अम् गण् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListLength")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(3L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        val list = compiled.values.getValue("सूची") as SanskritValue.Suchi
        assertEquals(listOf(1L, 2L, 3L), list.items.map { (it as SanskritValue.Sankhya).value })
        assertEquals(3, compiled.runtimeCalls.count { it == "storeValue" })
        assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list indexing executes directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            दशन् + अम् विंशति + अम् त्रिंशत् + अम् च सूची + ङे दा + लोट् + सिप् ।
            सूची + अम् द्वि + टा स्था + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListIndex")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(20L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list indexing accepts a numeric index stored under a symbolic name`() {
        val source = """
            दशन् + अम् विंशति + अम् त्रिंशत् + अम् च सूची + ङे दा + लोट् + सिप् ।
            द्वि + अम् क्रमाङ्क + ङे दा + लोट् + सिप् ।
            सूची + अम् क्रमाङ्क + टा स्था + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectNamedListIndex")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(20L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(2, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list containment executes directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् त्रि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।
            यदि द्वि + सुँ सूची + ङि असँ + लट् + तिप्
                तर्हि सत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप्
                अन्यथा असत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListContains")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(true, (compiled.values.getValue("LastResult") as SanskritValue.Satya).boolean)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `implicit tatah print consumes the direct numeric result`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectTatahPrint")
        val runtimeCalls = mutableListOf<String>()
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
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectTatahPrint", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals("पञ्च", values.getValue("LastResult").toDisplayText())
        assertEquals(0, runtimeCalls.count { it == "executeDirectValue" }, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
    }

    @Test
    fun `print leaf loads previously compiled state directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            सप्त + अम् अवस्था + ङे दा + लोट् + सिप् ।
            अवस्था + अम् मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectStatePrint")
        val runtimeCalls = mutableListOf<String>()
        var referencesState = false
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
                            if (value == "अवस्था") referencesState = true
                        }

                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectStatePrint", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(7L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertEquals("सप्त", values.getValue("LastResult").toDisplayText())
        assertTrue("storeValue" in runtimeCalls, runtimeCalls.toString())
        assertTrue("executeDirectValue" in runtimeCalls, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
        assertTrue(referencesState)
    }

    @Test
    fun `simple print leaf executes directly without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            पञ्च + अम् मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectPrint")
        val runtimeCalls = mutableListOf<String>()
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
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectPrint", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertTrue("executeDirectValue" !in runtimeCalls, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
    }

    @Test
    fun `tatah numeric pipeline stores its result without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        assertTrue("executeDirectValue" !in executeCalls, executeCalls.toString())
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `explicit phala stores the previous direct result without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        assertTrue("executeDirectValue" !in executeCalls, executeCalls.toString())
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateAndStore" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `fixed repetition loads compiled state directly on every iteration`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् अवस्था + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
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
        assertTrue(executeCalls.count { it == "storeValue" } == 3, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirectValue" } == 1, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `simple loop result target executes without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ततः परिणाम + ङे दा + लोट् + सिप् ।
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
        assertTrue("executeDirectLoopTarget" !in executeCalls, executeCalls.toString())
        assertTrue("executeDirectValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluateLoopTarget" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `bounded direct state loop lowers its exhaustion leaf directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            एक + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् अन्यथा द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।
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
        assertTrue(executeCalls.count { it == "storeValue" } == 11, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirectValue" } == 2, executeCalls.toString())
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `bounded loop reads a negated named truth state directly`() {
        val source = """
            शून्य + अम् एक + अम् च विद् + लोट् + सिप् ।
            फल + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् यावत् अवस्था + सुँ न भू + लट् + तिप् तावत् एक + अम् मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledNegatedNamedTruthLoop")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(false, (compiled.values.getValue("अवस्था") as SanskritValue.Satya).boolean)
        val outcome = compiled.values.getValue("LastResult") as SanskritValue.Rupa
        assertEquals("समाप्ति", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(3L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue("evaluateBoolean" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `bounded state loop loads and stores directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ।
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
        assertTrue(executeCalls.count { it == "storeValue" } == 10, executeCalls.toString())
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `assigned state feeds a directly compiled final conditional`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue(!createsReference)
    }

    @Test
    fun `straight line assignment is loaded directly by a later numeric leaf`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirectValue" } == 1, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateAndStore" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `terminal literal assignment stores compiled state directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `literal numeric condition branches without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `pure numeric leaves execute directly without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
        assertEquals(0, executeCalls.count { it == "executeDirectValue" }, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `compiled parameter frames preserve structured values`() {
        val runtime = CompiledProgramRuntime()
        val structuredOutcome = SanskritValue.Rupa(
            "परिणाम",
            mapOf(
                "अवस्था" to SanskritValue.Shabda("विजय"),
                "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(3L, "त्रीणि"),
            ),
        )
        runtime.storeValue("LastResult", structuredOutcome)
        runtime.enterFrame(arrayOf("मान"), arrayOf(structuredOutcome))

        val returned = runtime.resolveValue("मान")

        runtime.exitFrame()
        val structured = returned as SanskritValue.Rupa
        assertEquals("परिणाम", structured.schema)
        assertEquals("विजय", structured.fields.getValue("अवस्था").toDisplayText())
        assertEquals(3L, (structured.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `compiled runtime exposes only the unified action value entry point`() {
        val methods = CompiledProgramRuntime::class.java.declaredMethods.map { it.name }.toSet()

        assertTrue("executeDirectValue" in methods)
        assertTrue("executeDirect" !in methods)
        assertTrue("executeDirectBoolean" !in methods)
        assertTrue("executeDirectStore" !in methods)
        assertTrue("executeDirectLoopTarget" !in methods)
        assertTrue("publishLoopOutcome" !in methods)
    }

    @Test
    fun `large grammatical loop bounds use explicit numeric value comparison`() {
        val source = """
            शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ।
            कोटि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val bytes = BytecodeCompiler.compile(source, "CompiledLargeLoopBound")
        val instructions = mutableListOf<Int>()
        val valueCalls = mutableListOf<String>()
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

                    override fun visitMethodInsn(
                        opcode: Int,
                        owner: String?,
                        name: String?,
                        descriptor: String?,
                        isInterface: Boolean,
                    ) {
                        if (owner == "dev/panini/compiler/CompilerValueOperations") {
                            name?.let(valueCalls::add)
                        }
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

        assertTrue(ALOAD in instructions)
        assertTrue("lessThan" in valueCalls, valueCalls.toString())
        assertEquals(0L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `nested compiled loops preserve interpreter state parity`() {
        val source = """
            हृ + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अन्तरावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अन्तरावस्था + ङे ॥

            क्षि + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            बाह्यावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् बाह्यावस्था + ङे ॥

            अन्तरचक्र + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            द्वि + अम् अन्तरावस्था + ङे दा + लोट् + सिप् ।
            यावत् अन्तरावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् ॥

            बाह्यचक्र + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अन्तरचक्र + टा कृ + लोट् + सिप् ।
            क्षि + ल्युट् + टा कृ + लोट् + सिप् ॥

            द्वि + अम् बाह्यावस्था + ङे दा + लोट् + सिप् ।
            यावत् बाह्यावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् बाह्यचक्र + टा कृ + लोट् + सिप् ।
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
            हृ + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ।
            गण् + ल्युट् + टा कृ + लोट् + सिप् ॥

            गण् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
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
            गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् द्वि + अम् च गुण् + णिच् + लोट् + सिप् ॥
            राम + अम् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val prohibited = """
            विभाज् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भज् + णिच् + लोट् + सिप् ॥
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
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वसुच् यावत् फल + सुँ न तावत् प्रयत्न + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val victory = """
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            द्वि + अम् एक + अम् च विद् + लोट् + सिप् ॥
            पञ्च + कृत्वसुच् यावत् फल + सुँ न तावत् प्रयत्न + टा कृ + लोट् + सिप् ।
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
            हृ + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            द्वि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
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
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + स्था + लोट् + सिप् ॥

            पञ्च + कृत्वसुच् प्रयत्न + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>()
            .single { it.controlSignal != null }
            .typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledBreakRepetition")
        val runtimeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitMethodInsn(
                        opcode: Int,
                        owner: String?,
                        name: String?,
                        descriptor: String?,
                        isInterface: Boolean,
                    ) {
                        if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                            name?.let(runtimeCalls::add)
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledBreakRepetition", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertTrue("requestBreak" in runtimeCalls, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
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
            व्यवकलन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङस् द्वि + अम् वाम + ङस् पञ्च + अम् व्यवकलन + टा कृ + लोट् + सिप् ।
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
            वर्धन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ततः वर्धन + टा कृ + लोट् + सिप् ।
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
        assertTrue("executeDirectValue" !in executeCalls, executeCalls.toString())
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

    private fun compileAndInspect(source: String, className: String): CompiledExecution {
        val bytes = BytecodeCompiler.compile(source, className)
        val runtimeCalls = mutableListOf<String>()
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
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(className, bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        return CompiledExecution(values, runtimeCalls)
    }

    private data class CompiledExecution(
        val values: Map<String, SanskritValue>,
        val runtimeCalls: List<String>,
    )

    private fun collectionProgram(operation: String): String = """
        परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
        एक + अम् मुद्र् + लोट् + सिप् ॥

        एक + अम् द्वि + अम् त्रि + अम् च सूची + ङे दा + लोट् + सिप् ।
        $operation ।
    """.trimIndent()

    private data class CollectionCase(
        val name: String,
        val operation: String,
        val expected: List<Long>,
    )

    private data class CollectionResultCase(
        val name: String,
        val operation: String,
        val expected: SanskritValue,
    )
}
