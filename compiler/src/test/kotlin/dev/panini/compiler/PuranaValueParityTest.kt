package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class PuranaValueParityTest {
    @Test fun `productive ordinal phrases coordinate as native typed procedure inputs`() {
        val source = "योग + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "प्रथ् + अमच् + टाप् + अम् सङ्ख्या + अम् च द्वि + तीय + टाप् + अम् सङ्ख्या + अम् च युज् + णिच् + लोट् + सिप् ॥\n" +
            "द्वि + औट् त्रि + शस् च योग + टा डुकृञ् + उ + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
        assertEquals(5L, assertIs<SanskritValue.Sankhya>(result).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "ProductiveOrdinalCoordination")
        assertEquals(result, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test fun `invalid bare ordinal cannot become an inferred typed input slot`() {
        val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "द्वि + तम + अम् शब्द + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "वार्ता + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertIs<ExecutionResult.Failure>(results.last(), results.toString())
        assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lower(source, "InvalidOrdinalInput")
        }
    }

    @Test fun `invalid ordinal cannot become an untyped positional input slot`() {
        val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "द्वि + तम + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् द्वि + औट् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "InvalidPositionalOrdinal") }
    }

    @Test fun `fourth ordinal in general numeral syntax is not cardinal four`() {
        val source = "चतुर् + डट् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val bound = assertIs<ExecutionBindingResult.Bound>(
            dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", source),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्")))
        val operand = assertIs<ExecutionExpression.Pada>(
            bound.ukti.invocations.single().bindings[dev.panini.core.Karaka.KARMAN])
        val ordinal = assertIs<SanskritValue.Purana>(operand.value)
        assertEquals(4L, ordinal.value)
        val results = PaniniVM().evalScript(source)
        val actual = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
        assertEquals(SanskritValue.Shabda(ordinal.word), actual)
        val generated = BytecodeCompiler.compileAndLoad(source, "FourthOrdinalOperand")
        assertEquals(actual, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }

    @Test fun `procedure arguments preserve ordinal fallback and live named overrides`() {
        for ((index, phrase) in listOf("द्वितीय + अम्", "द्वि + तीय + अम्").withIndex()) {
            val source = "परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "फल + सुँ प्रथम + सुँ असँ + लट् + तिप् ॥\n" +
                "$phrase परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
            val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalArgumentValue$index")
            for (initial in listOf(emptyMap(), mapOf("द्वितीय" to SanskritValue.Sankhya(7, "host")))) {
                val results = PaniniVM().evalScript(source,
                    scope = ExecutionScope(environment = ValueEnvironment(initial)))
                val actual = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
                val expected = if (index == 0 && initial.isNotEmpty()) SanskritValue.Sankhya(7, "host")
                    else SanskritValue.Purana(2, "द्वितीय")
                assertEquals(expected, actual)
                assertEquals(actual, (generated.getMethod("execute", Map::class.java)
                    .invoke(null, initial) as Map<*, *>)["LastResult"])
            }
        }
    }

    @Test fun `lexical and productive ordinal action operands remain ordinals`() {
      for ((index, phrase) in listOf("द्वि + तीय + अम्", "द्वितीय + अम्").withIndex()) {
        val source = "$phrase मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        val expected = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
        assertEquals(SanskritValue.Shabda("द्वितीय"), expected)
        val bound = assertIs<ExecutionBindingResult.Bound>(
            dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", source),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्")))
        val operand = assertIs<ExecutionExpression.Pada>(
            bound.ukti.invocations.single().bindings[dev.panini.core.Karaka.KARMAN])
        assertIs<SanskritValue.Purana>(operand.value)
        val generated = BytecodeCompiler.compileAndLoad(source, "PrintedOrdinal$index")
        assertEquals(expected, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
      }
    }

    @Test fun `lexical and productive ordinal selectors access the same one based member`() {
        for ((index, selector) in listOf("द्वितीय + अम्", "द्वि + तीय + अम्").withIndex()) {
            val source = "एक + सुँ द्वि + औ च + इति सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् $selector उद् + हृ + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            val expected = assertIs<ExecutionResult.Success>(results.last()).typedValue
            assertEquals(2L, assertIs<SanskritValue.Sankhya>(expected).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalMember$index")
            assertEquals(expected, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
        }
    }

    @Test fun `procedure result retains ordinal type rather than becoming cardinal two`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फल + सुँ द्वि + तीय + सुँ असँ + लट् + तिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(SanskritValue.Purana(2, "द्वितीय"), result.typedValue)
        val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalResultValue")
        assertEquals(result.typedValue, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
        val ir = CompilerFrontend.lower(source, "OrdinalResultIr")
        assertTrue(ir.procedures.single().instructions.any {
            it is CompilerInstruction.Constant && it.value is SanskritValue.Purana
        })
    }

    @Test fun `ordinal payloads retain their type inside records and lists`() {
        val value = SanskritValue.Rupa("position", mapOf("rank" to SanskritValue.Suchi(listOf(
            SanskritValue.Purana(2, "द्वितीय"), SanskritValue.Sankhya(2, "द्वि")))))
        val serialized = dev.panini.execution.sutra.ProgramSutraArthaCodec.encodeValue(value)
        assertEquals(value, dev.panini.execution.sutra.ProgramSutraArthaCodec.decodeValue(serialized))
        val program = CompilerProgram("NestedOrdinalConstant", listOf(CompilerInstruction.Constant(value),
            CompilerInstruction.Store("LastResult")))
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(program.className, CompilerProgramJvmEmitter.emit(program))
        assertEquals(value, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }
}
