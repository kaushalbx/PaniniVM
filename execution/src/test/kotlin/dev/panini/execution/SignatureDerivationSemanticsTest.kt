package dev.panini.execution

import kotlin.test.*

class SignatureDerivationSemanticsTest {
    @Test fun `type guard inference consumes the full prohibition clause`() {
        for (source in listOf(
            "प्रथम + सुँ सङ्ख्या + त्व + सुँ न न भू + लिङ् + तिप् ।",
            "प्रथम + अम् सङ्ख्या + त्व + अम् राम + टा न भू + लिङ् + तिप् ।",
            "प्रथम + अम् सङ्ख्या + त्व + ङस् न भू + लिङ् + तिप् ।",
            "न प्रथम + अम् सङ्ख्या + त्व + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )) assertNull(PrakriyaSignatureCompiler.inferGuardType(sentence(source)), source)
    }

    @Test fun `plural nominals do not silently declare a singular contract`() {
        for (source in listOf(
            "सङ्ख्या + सुँ इति परिणाम + जस् असँ + लट् + तिप् ।",
            "सङ्ख्या + सुँ इति परिणाम + औ असँ + लट् + तिप् ।",
            "सङ्ख्या + जस् इति परिणाम + सुँ असँ + लट् + तिप् ।",
            "वाम + जस् सङ्ख्या + सुँ इति मान + सुँ असँ + लट् + तिप् ।",
            "वाम + सुँ सङ्ख्या + औ इति मान + सुँ असँ + लट् + तिप् ।",
        )) assertFalse(PrakriyaSignatureDeclarationParser.isDeclaration(sentence(source)), source)
        assertEquals(PrakriyaValueType.SUCHI, PrakriyaSignatureDeclarationParser.resultType(
            sentence("परिणाम + सुँ सूची + सुँ असँ + लट् + तिप् ।")))
    }

    private fun sentence(text: String): PvmScriptStatement.Sentence =
        assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(text).single())

    @Test fun `derived type nouns do not inherit primitive signature meaning`() {
        for (type in listOf("सङ्ख्या", "शब्द", "सूची")) {
            val plain = sentence("वाम + सुँ नाम मान + सुँ $type + सुँ असँ + लट् + तिप् ।")
            assertNotNull(PrakriyaSignatureDeclarationParser.parameter(plain))
            val derived = sentence("वाम + सुँ नाम मान + सुँ $type + त्व + सुँ असँ + लट् + तिप् ।")
            assertNull(PrakriyaSignatureDeclarationParser.parameter(derived))
            val result = sentence("परिणाम + सुँ $type + त्व + सुँ असँ + लट् + तिप् ।")
            val declaration = assertNotNull(PrakriyaSignatureDeclarationParser.result(result))
            assertNull(declaration.type)
            assertEquals("$type + त्व", declaration.schema)
        }
    }

    @Test fun `declarations do not discard additional participants`() {
        for (source in listOf(
            "वाम + सुँ सङ्ख्या + सुँ नाम मान + सुँ एक + सुँ असँ + लट् + तिप् ।",
            "सङ्ख्या + सुँ परिणाम + सुँ एक + सुँ असँ + लट् + तिप् ।",
            "परिणाम + सुँ सङ्ख्या + सुँ एक + सुँ असँ + लट् + तिप् ।",
            "परिणाम + सुँ सङ्ख्या + सुँ मुद्र् + णिच् + लट् + तिप् ।",
        )) assertFalse(PrakriyaSignatureDeclarationParser.isDeclaration(sentence(source)), source)
    }

    @Test fun `a genuine finite copula can assert a signature declaration`() {
        val result = sentence("परिणाम + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।")
        assertEquals(PrakriyaValueType.SANKHYA, PrakriyaSignatureDeclarationParser.resultType(result))
        val negated = sentence("परिणाम + सुँ सङ्ख्या + सुँ न असँ + लट् + तिप् ।")
        assertFalse(PrakriyaSignatureDeclarationParser.isDeclaration(negated))
    }

    @Test fun `derived markers do not introduce signature declarations`() {
        assertFalse(PrakriyaSignatureDeclarationParser.isDeclaration(
            sentence("वाम + सुँ सङ्ख्या + सुँ इति मान + त्व + सुँ असँ + लट् + तिप् ।")))
        assertFalse(PrakriyaSignatureDeclarationParser.isDeclaration(
            sentence("सङ्ख्या + सुँ इति परिणाम + त्व + सुँ असँ + लट् + तिप् ।")))
    }

    @Test fun `guard inference consumes the complete derivation not any bhava suffix`() {
        val plain = sentence("प्रथम + सुँ सङ्ख्या + त्व + सुँ न भू + लिङ् + तिप् ।")
        assertEquals(PrakriyaValueType.SANKHYA, PrakriyaSignatureCompiler.inferGuardType(plain))
        val furtherDerived = sentence("प्रथम + सुँ सङ्ख्या + त्व + मतुप् + सुँ न भू + लिङ् + तिप् ।")
        assertNull(PrakriyaSignatureCompiler.inferGuardType(furtherDerived))
        assertNull(PrakriyaSignatureCompiler.compile(listOf(furtherDerived)).argumentType)
    }

    @Test fun `derived schema declaration retains the same identity as its result contract`() {
        val schemaSentence = sentence("अवस्था + सुँ मूल्य + सुँ च शब्द + त्व + ङस् क्षेत्र + जस् असँ + लट् + झि ।")
        val schema = assertNotNull(TaddhitaStructEngine.detectResultSchema(schemaSentence.text, schemaSentence.ukti))
        val result = assertNotNull(PrakriyaSignatureDeclarationParser.result(
            sentence("परिणाम + सुँ शब्द + त्व + सुँ असँ + लट् + तिप् ।")))
        assertEquals("शब्द + त्व", schema.nameStem)
        assertEquals(schema.nameStem, result.schema)
        val registry = PrakriyaRegistry()
        registry.registerSchema(schema)
        assertNull(registry.resolveSchema("शब्द"))
        assertEquals(schema, registry.resolveSchema(result.schema!!))
    }
}
