package dev.panini.compiler

import dev.panini.execution.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class PositionalProcedureSlotTest {
    @Test fun `ordinal position alone does not impose numeric argument type`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            वार्ता + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(listOf(CompilerValueKind.UNKNOWN),
            CompilerFrontend.lower(source, "UntypedOrdinalSignature").procedures.single().parameterKinds)
        assertIs<SanskritValue.Shabda>(assertParity(source, "UntypedOrdinalCall"))
    }

    @Test fun `positional arithmetic preserves an additional cardinal operand`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् द्वितीय + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            द्वि + अम् पञ्च + अम् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(8L, assertIs<SanskritValue.Sankhya>(assertParity(source, "MixedSlotOperands")).value)
    }

    @Test fun `huge inferred signature fails with an analysis budget diagnostic`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val definition = PvmScript.parse(source).filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().single()
        val sentence = definition.body.single()
        val invocation = assertIs<Invocation>(sentence.program)
        val nominal = assertIs<SubantaPada>(invocation.vakya.padas.first())
        val huge = SankhyaPuranaPada(nominal.sourceText, listOf("प्रथ्", "अमच्"), Long.MAX_VALUE, nominal.sup)
        val vakya = assertIs<AkhyataVakya>(invocation.vakya).copy(padas = listOf(huge) + invocation.vakya.padas.drop(1))
        val altered = definition.copy(body = listOf(sentence.copy(ukti = sentence.ukti!!.copy(body = invocation.copy(vakya = vakya)))))
        val descriptor = PaniniModuleDescriptor("huge", listOf(PaniniModuleSource("huge.pvm", source, false)))
        val failure = assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lowerModule(descriptor, "HugeSignature", parseSource = { listOf(altered) })
        }
        assertTrue(failure.message.orEmpty().contains("analysis budget"), failure.message)
    }

    @Test fun `second positional reference retains the unused first slot`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            द्वितीय + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            एक + अम् पञ्च + अम् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(2, CompilerFrontend.lower(source, "SecondSlotSignature").procedures.single().parameterNames.size)
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(assertParity(source, "SecondSlotCall")).value)
    }

    @Test fun `productive fourth ordinal is a real fourth slot`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् चतुर् + डट् + अम् च युज् + णिच् + लोट् + सिप् ॥
            एक + अम् द्वि + औट् त्रि + शस् चतुर् + शस् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(4, CompilerFrontend.lower(source, "FourthSlotSignature").procedures.single().parameterNames.size)
        val result = assertParity(source, "FourthSlotCall")
        assertEquals(5L, assertIs<SanskritValue.Sankhya>(result).value)
    }

    @Test fun `scoped member does not capture procedure positional alias`() {
        val runtime = CompiledProgramRuntime()
        runtime.enterFrame(arrayOf("मान"), arrayOf(SanskritValue.Sankhya(5L, "पञ्च")))
        runtime.enterBindingFrame(arrayOf("मान"), arrayOf(SanskritValue.Sankhya(1L, "एक")))
        assertEquals(5L, assertIs<SanskritValue.Sankhya>(
            runtime.loadValue(PrakriyaAstArgumentBinder.referenceKey(0))).value)
        runtime.storeValue(PrakriyaAstArgumentBinder.referenceKey(0), SanskritValue.Sankhya(3L, "त्रि"))
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(runtime.loadValue("मान")).value)
        runtime.exitFrame()
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(runtime.loadValue("मान")).value)
        runtime.exitFrame()
    }

    private fun assertParity(source: String, name: String, display: String? = null): SanskritValue {
        val results = PaniniVM().evalScript(source)
        val interpreted = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue!!
        if (display != null) assertEquals(display, interpreted.toDisplayText())
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(interpreted, values["LastResult"])
        return interpreted
    }
}
