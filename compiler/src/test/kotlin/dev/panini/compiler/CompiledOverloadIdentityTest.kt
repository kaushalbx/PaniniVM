package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class CompiledOverloadIdentityTest {
    @Test fun `ordinary overload ambiguity has one diagnostic across static frontends`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ शब्द + सुँ इति मान + सुँ ।
            मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            सञ्चित + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "AmbiguousOrdinaryOverload")
        }
        assertEquals("AMBIGUOUS_PROCEDURE_OVERLOAD", assertNotNull(compiled.diagnostic).code)
        assertTrue(PrakriyaScriptValidator.validate(source)
            .any { it.code == "AMBIGUOUS_PROCEDURE_OVERLOAD" })
    }

    @Test fun `same file can invoke its private compiled procedure`() {
        val source = """
            रहस्य + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ॥
            रहस्य + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "SameFilePrivateCall")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(values["LastResult"]).value)
    }

    @Test fun `compiled named argument binding rejects plural references to one declared slot`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            मान + सुप् द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val failure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileAndLoad(source, "PluralParameterSlot")
        }
        assertTrue(failure.message.orEmpty().contains("singular"), failure.message)
    }

    @Test fun `legacy numeric guard accepts named numeric values through typed frame checking`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            न प्रथम + अम् सङ्ख्या + त्व + अम् ।
            प्रथम + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            द्वि + अम् सञ्चित + ङे दा + लोट् + सिप् ।
            सञ्चित + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "NamedNumericGuard")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(values["LastResult"]).value)
        assertEquals(interpreted.typedValue, values["LastResult"])
        val invalid = source.replace("द्वि + अम् सञ्चित", "राम + अम् सञ्चित")
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(invalid).last())
        val invalidGenerated = BytecodeCompiler.compileAndLoad(invalid, "NamedTextNumericGuard")
        assertFailsWith<java.lang.reflect.InvocationTargetException> {
            invalidGenerated.getMethod("execute").invoke(null)
        }
    }

    @Test fun `alternate numeral notation retains numeric type evidence at procedure boundary`() {
        val source = """
            वाचन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ शब्द + सुँ इति मान + सुँ ।
            मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            आर्यभटीय गि + अम् वाचन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val failure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileAndLoad(source, "AlternateNumeralParameterType")
        }
        assertTrue(failure.message.orEmpty().contains("मानप्रकारदोष"), failure.message)
    }

    @Test fun `one collection parameter does not implicitly pack multiple call operands`() {
        val declaration = """
            योजन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सूची + सुँ इति मान + सुँ ।
            मान + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        for ((index, operands) in listOf("", "एक + अम् द्वि + अम् च").withIndex()) {
            val source = "$declaration\n$operands योजन + टा डुकृञ् + उ + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            val failure = assertFailsWith<IllegalArgumentException> {
                BytecodeCompiler.compileAndLoad(source, "InvalidCollectionArity$index")
            }
            assertTrue(failure.message.orEmpty().contains("मानसङ्ख्यादोष"), failure.message)
        }
    }

    @Test fun `compiler rejects named slots that a signatureless procedure never declares`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            मान + ङि द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val failure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileAndLoad(source, "UndeclaredNamedSlot")
        }
        assertTrue(failure.message.orEmpty().contains("अज्ञातमानानि"), failure.message)
    }

    @Test fun `project pipeline consumes supplied operands instead of hardcoded procedure values`() {
        // Observe the pipeline's typed result before the display action returns rendered text.
        val example = java.io.File("projects/taddhita_inheritance/purvapara_pipeline.pvm").readText()
            .substringBefore("# Step 5:")
        for ((first, expected) in listOf("पञ्चन् + शस्" to 5L, "नवन् + शस्" to 9L)) {
            val source = example.replace("पञ्चन् + शस् द्वि + औट् च गणित", "$first द्वि + औट् च गणित")
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            val interpreted = assertIs<ExecutionResult.Success>(results.last())
            val generated = BytecodeCompiler.compileAndLoad(source, "ProjectPipeline$expected")
            @Suppress("UNCHECKED_CAST")
            val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(interpreted.typedValue).value)
            assertEquals(interpreted.typedValue, values["LastResult"])
        }
    }

    @Test fun `pipeline rejects ambiguous overload when preceding result type is undeclared`() {
        val source = """
            गणित + सुँ इति अधिकार + सुँ ।
            प्रथमक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
            द्वितीयक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            वाम + अम् दक्षिण + अम् च गण् + णिच् + लोट् + सिप् ॥
            द्वितीयक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ शब्द + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            द्वि + अम् त्रि + अम् च गणित + ङस् प्रथमक्रिया + ङस् गणित + ङस् द्वितीयक्रिया + ङस् पूर्व + ङस् पर + ङस् एका + सुँ कृ + लोट् + सिप् ।
        """.trimIndent()
        val failure = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compileAndLoad(source, "AmbiguousPipelineOverload")
        }
        assertTrue(failure.message.orEmpty().contains("Ambiguous"), failure.message)
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("AMBIGUOUS_PIPELINE_STAGE", diagnostic.code)
        assertNotNull(diagnostic.span)
    }

    @Test fun `pipeline selects overload from the preceding declared result`() {
        val source = """
            गणित + सुँ इति अधिकार + सुँ ।
            प्रथमक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
            द्वितीयक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            वाम + अम् दक्षिण + अम् च गण् + णिच् + लोट् + सिप् ॥
            द्वितीयक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ शब्द + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            द्वि + अम् त्रि + अम् च गणित + ङस् प्रथमक्रिया + ङस् गणित + ङस् द्वितीयक्रिया + ङस् पूर्व + ङस् पर + ङस् एका + सुँ कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "PipelineOverloadIdentity")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(interpreted.typedValue, values["LastResult"])
        assertEquals(15L, assertIs<SanskritValue.Sankhya>(values["LastResult"]).value)
    }

    @Test fun `compiled call targets the selected numeric definition not the last same-named method`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ शब्द + सुँ इति मान + सुँ ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(interpreted.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "SelectedOverloadIdentity")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(interpreted.typedValue, values["LastResult"])
        val unknownReference = source.replace(
            "द्वि + अम् परीक्षण + टा", "सञ्चित + अम् परीक्षण + टा",
        )
        val failure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileAndLoad(unknownReference, "UnknownReferenceOverload")
        }
        assertTrue(failure.message.orEmpty().contains("Ambiguous"), failure.message)
    }
}
