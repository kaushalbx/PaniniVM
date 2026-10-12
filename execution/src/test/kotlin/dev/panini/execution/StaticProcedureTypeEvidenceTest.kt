package dev.panini.execution

import kotlin.test.*

class StaticProcedureTypeEvidenceTest {
    @Test fun `synthesized dispatch recognizes truth literals without erasing derived morphology`() {
        val registry = PrakriyaRegistry()
        for (type in listOf(PrakriyaValueType.SATYA, PrakriyaValueType.SHABDA)) {
            registry.register(Prakriya("परीक्षण", "परीक्षण", emptyList(),
                signatureOverride = PrakriyaSignature(parameters = listOf(PrakriyaParameter("मान", type)))))
        }
        for (word in listOf("सत्य", "असत्य", "सत्य + मतुप्")) {
            val pada = dev.panini.vyakaranam.parser.PaniniParser()
                .parse("$word + अम् मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            val argument = PrakriyaArgument(word, pada = pada, origin = PrakriyaArgumentOrigin.WRITTEN)
            val selected = assertNotNull(registry.resolveStructuredInvocation("परीक्षण", null, listOf(argument), ""))
            assertEquals(if ('+' in word) PrakriyaValueType.SHABDA else PrakriyaValueType.SATYA,
                selected.kriya.signature.parameters.single().type, word)
        }
    }
    @Test fun `static ordinary call selection retains ambiguous unknown referent overloads`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ शब्द + सुँ इति मान + सुँ ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            सञ्चित + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val diagnostics = PrakriyaScriptValidator.validate(source)
        assertTrue(diagnostics.any { it.message.contains("Ambiguous") }, diagnostics.toString())
    }

    @Test fun `ordinary call validation retains unknown reference type but rejects known literal mismatch`() {
        val numericProcedure = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val referenceCall = "सञ्चित + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        assertTrue(PrakriyaScriptValidator.validate("$numericProcedure\n$referenceCall").isEmpty())
        val textProcedure = numericProcedure.replace("मान + सुँ सङ्ख्या + सुँ", "मान + सुँ शब्द + सुँ")
        val literalCall = "द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val diagnostics = PrakriyaScriptValidator.validate("$textProcedure\n$literalCall")
        assertTrue(diagnostics.any { it.message.contains("मानप्रकारदोष") }, diagnostics.toString())
    }

    @Test fun `pipeline validation does not classify a named numeric referent as text`() {
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
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
            सञ्चित + अम् त्रि + अम् च गणित + ङस् प्रथमक्रिया + ङस् गणित + ङस् द्वितीयक्रिया + ङस् पूर्व + ङस् पर + ङस् एका + सुँ कृ + लोट् + सिप् ।
        """.trimIndent()
        val diagnostics = PrakriyaScriptValidator.validate(source)
        assertTrue(diagnostics.isEmpty(), diagnostics.toString())
    }

    @Test fun `declared record type selects an overload without fabricating a value`() {
        val registry = PrakriyaRegistry()
        for (type in listOf(PrakriyaValueType.RUPA, PrakriyaValueType.SHABDA)) {
            registry.register(Prakriya(nameSegmented = "परीक्षण", nameStem = "परीक्षण", body = emptyList(),
                signatureOverride = PrakriyaSignature(parameters = listOf(PrakriyaParameter("मान", type)))))
        }
        val argument = PrakriyaArgument("फल", origin = PrakriyaArgumentOrigin.PIPE)
        val selected = assertNotNull(registry.resolveStructuredInvocation("परीक्षण", null,
            listOf(argument), "", analyzedArgumentTypes = listOf(PrakriyaValueType.RUPA)))
        assertEquals(PrakriyaValueType.RUPA, selected.kriya.signature.parameters.single().type)
        assertNull(selected.arguments.single().value)
        assertFailsWith<IllegalArgumentException> {
            registry.resolveStructuredInvocation("परीक्षण", null, listOf(argument), "",
                analyzedArgumentTypes = listOf(PrakriyaValueType.UNCONSTRAINED))
        }
        assertFailsWith<IllegalArgumentException> {
            registry.resolveStructuredInvocation("परीक्षण", null, listOf(argument), "",
                analyzedArgumentTypes = emptyList())
        }
    }
}
