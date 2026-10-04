package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LopaNullSafetyTest {

    private val vm = PaniniVM()

    @Test
    fun testLopaNullSafetyAbsentAttributeQuery() {
        val script = """
            # Step 1: Construct struct 'सङ्ख्या' with attribute 'मूल्य' = 50 ('पञ्चाशत्')
            सङ्ख्या + मतुप् + ङस् मूल्य + सुँ पञ्चाशत् + सुँ असँ + लट् + तिप् ।

            # Step 2: Query absent attribute 'ज्ञाता' on struct 'सङ्ख्या' (1.1.60 अदर्शनं लोपः)
            # Sūtra 1.1.60 defines non-perception (अदर्शनम्) as Lopa (लोपः)
            सङ्ख्या + मतुप् + ङस् ज्ञा + क्त + टाप् + अम् ग्रहँ + श्ना + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful Lopa query: $results")
        assertEquals("लोपः", success.last().value, "Expected absent attribute query to return 'लोपः' via Sūtra 1.1.60 (अदर्शनं लोपः)")
        assertEquals(SanskritValue.Lopa, success.last().typedValue, "Expected typed value to be SanskritValue.Lopa")
    }
}
