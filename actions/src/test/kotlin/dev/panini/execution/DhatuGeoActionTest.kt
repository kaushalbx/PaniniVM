package dev.panini.execution

import dev.panini.actions.numeric.SanskritTrigonometryAction
import dev.panini.actions.numeric.SanskritCircumferenceAction
import dev.panini.actions.numeric.SanskritHypotenuseAction
import dev.panini.actions.numeric.SanskritAreaAction
import dev.panini.core.Karaka
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DhatuGeoActionTest {

    @Test
    fun `executes SanskritTrigonometryAction for 90 degrees`() {
        val op = SanskritTrigonometryAction.op()
        val ctx = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.sankhya(90L, "नवति"),
            )
        )

        val res = SanskritTrigonometryAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val value = assertIs<SanskritValue.Rational>(success.typedValue)
        assertEquals(1L, value.numerator)
        assertEquals(1L, value.denominator)
    }

    @Test
    fun `executes SanskritCircumferenceAction for radius 10`() {
        val op = SanskritCircumferenceAction.op()
        val ctx = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.sankhya(10L, "दशन्"),
            )
        )

        val res = SanskritCircumferenceAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val value = assertIs<SanskritValue.Rational>(success.typedValue)
        assertTrue(kotlin.math.abs(value.numerator.toDouble() / value.denominator - 20.0 * Math.PI) < 1e-8)
    }

    @Test
    fun `executes SanskritHypotenuseAction for sides 3 and 4`() {
        val op = SanskritHypotenuseAction.op()
        val ctx = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    listOf(
                        ExecutionExpression.sankhya(3L, "त्रि"),
                        ExecutionExpression.sankhya(4L, "चतुर्"),
                    )
                ),
            )
        )

        val res = SanskritHypotenuseAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val valSankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        assertEquals(5L, valSankhya.value)
    }

    @Test
    fun `executes SanskritAreaAction for dimension 10`() {
        val op = SanskritAreaAction.op()
        val ctx = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.sankhya(10L, "दशन्"),
            )
        )

        val res = SanskritAreaAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val value = assertIs<SanskritValue.Rational>(success.typedValue)
        assertTrue(kotlin.math.abs(value.numerator.toDouble() / value.denominator - 100.0 * Math.PI) < 1e-8)
    }
}
