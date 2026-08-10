package dev.panini.execution.binding

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ItiQuotationTest {
    @Test
    fun `iti prints a grammatical command without executing its verb`() {
        val result = PaniniVM().eval(
            "एक + ङसिँ दश + ङि सङ्ख्या + अम् अनुमिनु + लोट् + सिप् " +
                "इति मुद्र् + णिच् + लोट् + सिप् ।",
        )

        assertEquals(
            "एकतः दशपर्यन्तं सङ्ख्याम् अनुमिनु",
            assertIs<ExecutionResult.Success>(result).value,
        )
    }
}
