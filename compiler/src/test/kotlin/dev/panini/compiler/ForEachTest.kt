package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ForEachTest {
    @Test fun `indexed distributive iteration exposes one based member positions`() {
        val source = """
            $declaration
            सूची + ङस् प्रत्येक + अम् सदस्य + अम् तद् + ङस् क्रम + अम् च ग्रह् + क्त्वा
                क्रम + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals(listOf("एकम्", "द्वे", "त्रीणि"),
            interpreted.filterIsInstance<ExecutionResult.Success>()
                .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })

        val values = compiled(source, "IndexedForEach")
        assertEquals("त्रीणि", values.getValue("LastResult").toDisplayText())
        assertFalse("सदस्य" in values)
        assertFalse("क्रम" in values)
    }

    @Test fun `adjacent foreach members have interpreter compiler parity and remain scoped`() {
        for ((stem, expected) in listOf("उत्तर" to "त्रि", "पूर्व" to "द्वि")) {
            val source = """
                $declaration
                सूची + ङस् प्रत्येक + अम् सदस्य + अम् तद् + ङस् क्रम + अम्
                तद् + ङस् $stem + अम् च ग्रह् + क्त्वा
                $stem + अम् मुद्र् + णिच् + लोट् + सिप् ।
            """.trimIndent()
            val interpreted = PaniniVM().evalScript(source)
            assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
            assertEquals(expected, interpreted.filterIsInstance<ExecutionResult.Success>()
                .filter { it.outputKind == OutputKind.CONSOLE }.last().value)
            val values = compiled(source, "AdjacentForEach$stem")
            assertEquals(expected, values.getValue("LastResult").toDisplayText())
            assertFalse(stem in values)
        }
    }

    @Test fun `plural neighbour bindings expose typed prefixes and suffixes in both backends`() {
        for ((stem, expected) in listOf("उत्तर" to "[]", "पूर्व" to "[एक, द्वि]")) {
            val source = """
                $declaration
                सूची + ङस् प्रत्येक + अम् सदस्य + अम् तद् + ङस् क्रम + अम्
                तद् + ङस् $stem + शस् च ग्रह् + क्त्वा
                $stem + शस् मुद्र् + णिच् + लोट् + सिप् ।
            """.trimIndent()
            val interpreted = PaniniVM().evalScript(source)
            assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
            assertEquals(expected, interpreted.filterIsInstance<ExecutionResult.Success>()
                .filter { it.outputKind == OutputKind.CONSOLE }.last().value)
            val values = compiled(source, "PluralAdjacentForEach$stem")
            assertEquals(expected, values.getValue("LastResult").toDisplayText())
            assertFalse(stem in values)
        }
    }

    @Test fun `legacy indexed prati iteration remains compatible`() {
        val source = """
            $declaration
            सूची + ङस् सदस्य + अम् क्रम + अम् प्रति
                क्रम + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals("त्रीणि", compiled(source, "LegacyIndexedForEach").getValue("LastResult").toDisplayText())
    }

    @Test fun `genitive pronoun names a local list as the distributive whole`() {
        val source = """
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सूची + सुँ इति मान + सुँ ।
            तद् + ङस् मूल्य + अम् प्रति मूल्य + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            $declaration
            सूची + अम् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals("त्रि", assertIs<ExecutionResult.Success>(interpreted.last()).value)
        assertEquals("त्रि", compiled(source, "PronounWholeForEach").getValue("LastResult").toDisplayText())
    }

    @Test fun `local pronoun example executes in both backends`() {
        val source = java.io.File("examples/control_flow/local_pronouns.pvm").readText(Charsets.UTF_8)
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals(listOf("द्वि", "एक", "द्वि", "त्रि"),
            interpreted.filterIsInstance<ExecutionResult.Success>().filter { it.outputKind == OutputKind.CONSOLE }
                .map { it.value })
        assertEquals("त्रि", compiled(source, "LocalPronounExample").getValue("LastResult").toDisplayText())
    }

    @Test fun `foreach pronoun reads the grammatical member and does not escape`() {
        val source = "$declaration\nसूची + ङस् मान + अम् प्रति तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals(listOf("एक", "द्वि", "त्रि"), interpreted.takeLast(3).map {
            assertIs<ExecutionResult.Success>(it).value
        })
        val values = compiled(source, "PronounForEach")
        assertEquals("त्रि", values.getValue("LastResult").toDisplayText())
        assertFalse("मान" in values)
        assertFalse(values.keys.any { it.startsWith("__pvm_anaphor_") })
    }

    @Test fun `singular collection participant competes with a singular foreach member`() {
        val source = """
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मूल्य + सुँ सूची + सुँ इति मान + सुँ ।
            मूल्य + ङस् मान + अम् प्रति तद् + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            $declaration
            सूची + अम् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.filterIsInstance<ExecutionResult.Failure>().any {
            it.message.contains("Ambiguous तद् antecedent")
        }, interpreted.toString())
        assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "AmbiguousMemberPronoun") }
    }

    @Test fun `distributive clauses compose with both conditional branches`() {
        for ((word, expected) in listOf("एक" to "त्रि", "द्वि" to "नव")) {
            val source = "$declaration\nयदि एक + सुँ $word + टा सम + सुँ असँ + लट् + तिप् तर्हि $loop".removeSuffix(" ।") +
                " अन्यथा नव + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals(expected, assertIs<ExecutionResult.Success>(results.last()).value)
            assertEquals(expected, compiled(source, "ConditionalForEach$word").getValue("LastResult").toDisplayText())
        }
        val alternate = "$declaration\nयदि एक + सुँ द्वि + टा सम + सुँ असँ + लट् + तिप् तर्हि नव + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा $loop"
        assertEquals("त्रि", assertIs<ExecutionResult.Success>(PaniniVM().evalScript(alternate).last()).value)
        assertEquals("त्रि", compiled(alternate, "AlternateForEach").getValue("LastResult").toDisplayText())
        val unselected = "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि नव + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा $loop"
        assertTrue(PaniniVM().evalScript(unselected).all { it is ExecutionResult.Success })
        assertEquals("नव", compiled(unselected, "UnselectedForEach").getValue("LastResult").toDisplayText())
    }
    @Test fun `snapshot preserves typed empty lists and rejects participant groups`() {
        val empty = SanskritValue.Suchi(emptyList(), ListMemberType.NUMBER)
        assertEquals(empty, CompilerValueOperations.listSnapshot(empty))
        assertFailsWith<CompiledPaniniExecutionException> {
            CompilerValueOperations.listSnapshot(SanskritValue.Gana(emptyList()))
        }
    }

    @Test fun `scoped runtime binding restores assignment target`() {
        val runtime = CompiledProgramRuntime()
        val original = SanskritValue.Sankhya(9, "नव")
        runtime.storeValue("सदस्य", original)
        runtime.enterBindingFrame(arrayOf("सदस्य"), arrayOf(SanskritValue.Sankhya(1, "एक")))
        runtime.storeValue("सदस्य", SanskritValue.Sankhya(2, "द्वि"))
        runtime.exitFrame()
        assertEquals(original, runtime.loadValue("सदस्य"))
    }

    @Test fun `invalid distributive cases are rejected`() {
        for (source in listOf(
            "सूची + अम् सदस्य + अम् प्रति सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            "सूची + ङस् सदस्य + ङे प्रति सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )) assertFails { PvmScript.parseNative(source) }
    }
    private val declaration = "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।"
    private val loop = "सूची + ङस् सदस्य + अम् प्रति सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"

    @Test fun `host budget stops iteration in both backends`() {
        val source = "$declaration\n$loop"
        val results = PaniniVM(executionLimits = ExecutionLimits(maxConditionIterations = 2L)).evalScript(source)
        assertEquals(ExecutionError.ACTION_FAILED, assertIs<ExecutionResult.Failure>(results.last()).error)
        val generated = BytecodeCompiler.compileAndLoad(source, "BudgetForEach")
        val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
            generated.getMethod("execute", Long::class.javaPrimitiveType).invoke(null, 2L)
        }
        assertIs<CompiledExecutionLimitExceededException>(failure.cause)
    }

    @Test fun `empty list leaves prior result unchanged`() {
        val generated = BytecodeCompiler.compileAndLoad(loop, "EmptyForEach")
        val last = SanskritValue.Shabda("पूर्वम्")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute", Map::class.java).invoke(null,
            mapOf("सूची" to SanskritValue.Suchi(emptyList()), "LastResult" to last)) as Map<String, SanskritValue>
        assertEquals(last, values["LastResult"])
        assertFalse("सदस्य" in values)
    }

    private fun compiled(source: String, name: String): Map<String, SanskritValue> {
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
    }

    @Test fun `members execute in order and do not leak`() {
        val source = "$declaration\n$loop"
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals(listOf("एक", "द्वि", "त्रि"), interpreted.takeLast(3).map {
            assertIs<ExecutionResult.Success>(it).value
        })
        val values = compiled(source, "NativeForEach")
        assertEquals("त्रि", values.getValue("LastResult").toDisplayText())
        assertFalse("सदस्य" in values)
    }

    @Test fun `direct singular next item action has interpreter compiler parity`() {
        val source = """
            एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम्
            तद् + ङस् + लुक् - उत्तर + टाप् + टा सङ्ख्या + टा
            युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals(7L, assertIs<SanskritValue.Sankhya>(
            interpreted.filterIsInstance<ExecutionResult.Success>().last().typedValue).value)
        assertEquals(7L, assertIs<SanskritValue.Sankhya>(
            compiled(source, "DirectSingularNext").getValue("LastResult")).value)
    }

    @Test fun `plural relative operands reuse multiplication with compiler parity`() {
        val source = """
            एक + ङस् द्वि + ओस् त्रि + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम्
            तद् + ङस् + लुक् - उत्तर + टाप् + भिस्
            गण + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(
            interpreted.filterIsInstance<ExecutionResult.Success>().last().typedValue).value)
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(
            compiled(source, "RelativeGenericAction").getValue("LastResult")).value)
    }

    @Test fun `nested loops restore outer member`() {
        val source = "$declaration\nसूची + ङस् सदस्य + अम् प्रति $loop"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(10, results.size)
        assertEquals("त्रि", compiled(source, "NestedForEach").getValue("LastResult").toDisplayText())
    }

    @Test fun `nonlist source fails`() {
        val results = PaniniVM().evalScript("एक + अम् सूची + ङे दा + लोट् + सिप् ।\n$loop")
        assertIs<ExecutionResult.Failure>(results.last())
        assertFails { compiled("एक + अम् सूची + ङे दा + लोट् + सिप् ।\n$loop", "InvalidForEach") }
    }

    @Test fun `body replacement does not change snapshot membership`() {
        val source = "$declaration\nसूची + ङस् सदस्य + अम् प्रति सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ततः एक + अम् सूची + ङे दा + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(4, results.size) // declaration plus three body-sequence results
        assertEquals(2, assertIs<SanskritValue.Suchi>(compiled(source, "SnapshotForEach")["सूची"]).items.size)
    }

    @Test fun `sequential adjacent traversal threads collection updates through a dynamic bubble sort`() {
        val source = java.io.File("examples/algorithms/bubble_sort.pvm").readText(Charsets.UTF_8)
        val expected = listOf(1L, 2L, 3L, 4L, 5L, 8L)
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals("[एक, द्वि, त्रि, चतुर्, पञ्चन्, अष्टन्]",
            assertIs<ExecutionResult.Success>(interpreted.last()).value)
        assertEquals(expected, assertIs<SanskritValue.Suchi>(
            compiled(source, "SequentialBubbleSort").getValue("दत्तसूची")).items.map {
            assertIs<SanskritValue.Sankhya>(it).value
        })
    }

    @Test fun `outer member name survives the loop`() {
        val source = "नव + अम् सदस्य + ङे दा + लोट् + सिप् ।\n$declaration\n$loop\nसदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val values = compiled(source, "ShadowedForEach")
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(values["सदस्य"]).value)
        assertEquals(assertIs<ExecutionResult.Success>(results.last()).value, values.getValue("LastResult").toDisplayText())
    }

    @Test fun `break is consumed by nearest loop`() {
        val source = "$declaration\nसूची + ङस् सदस्य + अम् प्रति वि + स्था + लोट् + सिप् ।\nद्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(3, results.size)
        assertNull(assertIs<ExecutionResult.Success>(results[1]).controlSignal)
        assertEquals(assertIs<ExecutionResult.Success>(results.last()).value,
            compiled(source, "BreakForEach").getValue("LastResult").toDisplayText())
    }

    @Test fun `procedure collection parameter supports iteration`() {
        val source = """
            प्रदर्शन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सूची + सुँ इति मान + सुँ ।
            मान + ङस् सदस्य + अम् प्रति सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            $declaration
            सूची + अम् प्रदर्शन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals("त्रि", compiled(source, "ProcedureForEach").getValue("LastResult").toDisplayText())
    }
}
