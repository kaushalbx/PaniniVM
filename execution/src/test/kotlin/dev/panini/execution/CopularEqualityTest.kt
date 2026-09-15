package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CopularEqualityTest {
    @Test
    fun `samam asti evaluates grammatical copular equality`() {
        val result = PaniniVM().evalCondition(
            "एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् ।",
        )

        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertTrue(assertIs<SanskritValue.Satya>(success.typedValue).boolean)
    }

    @Test
    fun `nyunam asti evaluates an ablative standard of comparison`() {
        val result = PaniniVM().evalCondition(
            "एक + सुँ दशन् + भ्यस् न्यून + सुँ असँ + लट् + तिप् ।",
        )

        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertTrue(assertIs<SanskritValue.Satya>(success.typedValue).boolean)
    }
}
