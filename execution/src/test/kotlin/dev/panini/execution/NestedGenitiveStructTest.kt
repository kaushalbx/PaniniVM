package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NestedGenitiveStructTest {

    private val vm = PaniniVM()

    @Test
    fun testStructConstructionSyntaxValidation() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val errors = parser.validate("पञ्चाशत् + अम् मूल्य + अम् सङ्ख्या + मतुप् + सुँ ।")
        assertTrue(errors.isEmpty(), "Expected zero syntax errors, but got: $errors")
    }

    @Test
    fun testSingleLevelGenitiveAttributeAccess() {
        val script = """
            # Construct base struct 'गुण' with attribute 'मूल्य' = 10 ('दश')
            दश + अम् मूल्य + अम् गुण + मतुप् + सुँ ।

            # Single-level genitive query (1.1.49 षष्ठी स्थानेयोगा)
            गुण + मतुप् + ङस् मूल्य + अम् ।
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
            पञ्चाशत् + अम् मूल्य + अम् सङ्ख्या + मतुप् + सुँ ।

            # Outer struct 'गाणित' with attribute 'मान' = 'सङ्ख्या'
            सङ्ख्या + अम् मान + अम् गाणित + मतुप् + सुँ ।

            # 2-level nested genitive query (1.1.49 षष्ठी स्थानेयोगा): गाणित -> मान (सङ्ख्या) -> मूल्य -> पञ्चाशत्
            गाणित + मतुप् + ङस् मान + मतुप् + ङस् मूल्य + अम् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful nested genitive attribute query: $results")
        assertEquals("पञ्चाशत्", success.last().value, "Expected 2-level nested attribute 'मूल्य' to return 50 (पञ्चाशत्)")
    }
}
