package dev.panini.execution

import dev.panini.actions.numeric.TrigonometryAction
import dev.panini.actions.numeric.CircumferenceAction
import dev.panini.actions.numeric.HypotenuseAction
import dev.panini.actions.numeric.AreaAction
import dev.panini.core.Karaka
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DhatuGeoActionTest {

    @Test
    fun `executes TrigonometryAction for 90 degrees`() {
        val op = TrigonometryAction.op()
        val ctx = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.sankhya(90L, "नवति"),
            )
        )

        val res = TrigonometryAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val value = assertIs<SanskritValue.Rational>(success.typedValue)
        assertEquals(1L, value.numerator)
        assertEquals(1L, value.denominator)
    }

    @Test
    fun `executes CircumferenceAction for radius 10`() {
        val op = CircumferenceAction.op()
        val ctx = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.sankhya(10L, "दशन्"),
            )
        )

        val res = CircumferenceAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val value = assertIs<SanskritValue.Rational>(success.typedValue)
        assertTrue(kotlin.math.abs(value.numerator.toDouble() / value.denominator - 20.0 * Math.PI) < 1e-8)
    }

    @Test
    fun `executes HypotenuseAction for sides 3 and 4`() {
        val op = HypotenuseAction.op()
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

        val res = HypotenuseAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val valSankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        assertEquals(5L, valSankhya.value)
    }

    @Test
    fun `executes AreaAction for dimension 10`() {
        val op = AreaAction.op()
        val ctx = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.sankhya(10L, "दशन्"),
            )
        )

        val res = AreaAction.execute(ctx, op)
        val success = assertIs<ExecutionResult.Success>(res)
        val value = assertIs<SanskritValue.Rational>(success.typedValue)
        assertTrue(kotlin.math.abs(value.numerator.toDouble() / value.denominator - 100.0 * Math.PI) < 1e-8)
    }
}
