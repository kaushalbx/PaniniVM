package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NestedGenitiveStructTest {

    private val vm = PaniniVM()

    @Test
    fun `copular field assertion renders with possessive matup and neuter field`() {
        assertEquals(
            "गुणवतः मूल्यं दश अस्ति ।",
            PvmUktiSadhaka().sadhayaLine(
                "गुण + मतुप् + ङस् मूल्य + सुँ दशन् + जस् असँ + लट् + तिप् ।",
            ),
        )
    }

    @Test
    fun `copular field assertion syntax is valid`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val errors = parser.validate(
            "सङ्ख्या + मतुप् + ङस् मूल्य + सुँ पञ्चाशत् + सुँ असँ + लट् + तिप् ।",
        )
        assertTrue(errors.isEmpty(), "Expected zero syntax errors, but got: $errors")
    }

    @Test
    fun testSingleLevelGenitiveAttributeAccess() {
        val script = """
            # Construct base struct 'गुण' with attribute 'मूल्य' = 10 ('दश')
            गुण + मतुप् + ङस् मूल्य + सुँ दश + सुँ असँ + लट् + तिप् ।

            # Single-level genitive query (1.1.49 षष्ठी स्थानेयोगा)
            गुण + मतुप् + ङस् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful attribute query: $results")
        assertEquals("दश", success.last().value, "Expected attribute 'मूल्य' to be 10 (दश)")
    }

    @Test
    fun testMultiLevelNestedGenitiveAttributeAccess() {
        val script = """
            # Inner struct 'सङ्ख्या' with attribute 'मूल्य' = 50 ('पञ्चाशत्')
            सङ्ख्या + मतुप् + ङस् मूल्य + सुँ पञ्चाशत् + सुँ असँ + लट् + तिप् ।

            # Outer struct 'गाणित' with attribute 'मान' = 'सङ्ख्या'
            गाणित + मतुप् + ङस् मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।

            # 2-level nested genitive query (1.1.49 षष्ठी स्थानेयोगा): गाणित -> मान (सङ्ख्या) -> मूल्य -> पञ्चाशत्
            गाणित + मतुप् + ङस् मान + मतुप् + ङस् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful nested genitive attribute query: $results")
        assertEquals("पञ्चाशत्", success.last().value, "Expected 2-level nested attribute 'मूल्य' to return 50 (पञ्चाशत्)")
        assertEquals(50L, assertIs<SanskritValue.Sankhya>(success.last().typedValue).value)
    }
}
