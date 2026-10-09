package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AntaratamaOverloadTest {
    @Test
    fun `named action result type participates in overload selection`() {
        val source = """
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ शब्द + सुँ इति मान + सुँ ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।
            युज् + ल्युट् + ङस् फल + अम् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals(4L, ((results.last() as ExecutionResult.Success).typedValue as SanskritValue.Sankhya).value)
    }

    private val vm = PaniniVM()

    @Test
    fun testSyntaxValidation() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val lines = listOf(
            "गणित + सुँ इति अधि + कृ + घञ् + सुँ ।",
            "गणित + ङस् युज् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।",
            "न प्रथम + अम् सङ्ख्या + त्व + अम् ।",
            "प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ॥",
            "पञ्च + अम् पञ्च + अम् च गणित + ङस् युज् + ल्युट् + टा कृ + लोट् + सिप् ।",
            "मुद्र् + णिच् + लोट् + सिप् फल + अम् ।"
        )
        for (line in lines) {
            val errors = parser.validate(line)
            assertTrue(errors.isEmpty(), "Syntax error on '$line': $errors")
        }
    }

    @Test
    fun testAntaratamaTypeProximityOverloadDispatch() {
        val script = """
            # Domain declaration
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।

            # Overload 1 (Numeric constraint): Adds numbers (5 + 5 = 10 -> दश)
            गणित + ङस् युज् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            न प्रथम + अम् सङ्ख्या + त्व + अम् ।
            प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Overload 2 (Text constraint): Concatenates text ("राम" + "कृष्ण" -> "रामकृष्ण")
            गणित + ङस् युज् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            न प्रथम + अम् शब्द + त्व + अम् ।
            राम + अम् कृष्ण + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Test 1: Numeric arguments [पञ्च, पञ्च] dispatch Overload 1 via Sūtra 1.1.50 (स्थानेऽन्तरतमः) -> 10 (दश)
            पञ्च + अम् पञ्च + अम् च गणित + ङस् युज् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful overload dispatch: $results")
        assertEquals("दश", success.last().value, "Expected numeric overload 1 (दश) via Sūtra 1.1.50 (स्थानेऽन्तरतमः)")
    }

    @Test
    fun `typed signatures are compiled once and ranked structurally`() {
        fun guard(source: String): PvmScriptStatement.Sentence =
            PvmScript.parse(source).filterIsInstance<PvmScriptStatement.Sentence>().single()
        val numeric = PrakriyaSignatureCompiler.compile(
            listOf(guard("न प्रथम + अम् सङ्ख्या + त्व + अम् ।")),
        )
        val text = PrakriyaSignatureCompiler.compile(
            listOf(guard("न प्रथम + अम् शब्द + त्व + अम् ।")),
        )

        assertEquals(PrakriyaValueType.SANKHYA, numeric.argumentType)
        assertEquals(
            AntaratamaOverloadEngine.TypeMatch.EXACT,
            AntaratamaOverloadEngine.matchTypes(numeric, listOf(PrakriyaValueType.SANKHYA)),
        )
        assertEquals(
            AntaratamaOverloadEngine.TypeMatch.MISMATCH,
            AntaratamaOverloadEngine.matchTypes(text, listOf(PrakriyaValueType.SANKHYA)),
        )
        assertNotEquals(numeric, text)
    }
}
