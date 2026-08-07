package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SvamRupamLiteralTest {

    private val vm = PaniniVM()

    @Test
    fun testSvamRupamSelfReferentialLiteralEvaluation() {
        val script = """
            # Step 1: Evaluate non-saṃjñā word term 'राम' (1.1.68 स्वं रूपं शब्दस्याशब्दसंज्ञा)
            # Sūtra 1.1.68 evaluates non-technical terms to their self-referential literal form (स्वं रूपम्)
            राम + अम् कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful Svam Rupam literal evaluation: $results")
        assertEquals("रामः", success.last().value, "Expected literal term 'राम' to evaluate to self-referential surface form 'रामः' via Sūtra 1.1.68 (स्वं रूपम्)")
    }
}
