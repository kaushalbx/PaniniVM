package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AntaratamaOverloadTest {

    private val vm = PaniniVM()

    @Test
    fun testSyntaxValidation() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val lines = listOf(
            "गणित + सुँ इति अधि + कृ + घञ् + सुँ ।",
            "गणित + ङस् युज् + ल्युट् + सुँ इति संज्ञा + सुँ ।",
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
            गणित + ङस् युज् + ल्युट् + सुँ इति संज्ञा + सुँ ।
            न प्रथम + अम् सङ्ख्या + त्व + अम् ।
            प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Overload 2 (Text constraint): Concatenates text ("राम" + "कृष्ण" -> "रामकृष्ण")
            गणित + ङस् युज् + ल्युट् + सुँ इति संज्ञा + सुँ ।
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
}
