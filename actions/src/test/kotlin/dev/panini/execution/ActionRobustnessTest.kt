package dev.panini.execution

import dev.panini.actions.numeric.SanskritAdditionAction
import dev.panini.actions.numeric.SanskritHypotenuseAction
import dev.panini.actions.numeric.SanskritSquareRootAction
import dev.panini.actions.numeric.SanskritTrigonometryAction
import dev.panini.core.Karaka
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ActionRobustnessTest {
    @Test
    fun `direct invocation reports a missing karman instead of throwing`() {
        val result = SanskritAdditionAction.execute(
            ExecutionContext(),
            SanskritAdditionAction.numericOp(),
        )

        assertEquals(ExecutionError.MISSING_KARAKA, assertIs<ExecutionResult.Failure>(result).error)
    }

    @Test
    fun `square root rejects an inexact integer result`() {
        val context = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to ExecutionExpression.sankhya(2, "द्वि")),
        )
        val result = SanskritSquareRootAction.execute(
            context,
            SanskritSquareRootAction.numericOp(minimum = 1),
        )

        assertTrue("not an exact integer" in assertIs<ExecutionResult.Failure>(result).message)
    }

    @Test
    fun `hypotenuse detects overflow before squaring`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(Long.MAX_VALUE, "अधिकतमम्"),
                    ExecutionExpression.sankhya(1, "एक"),
                ),
            ),
        )
        val result = SanskritHypotenuseAction.execute(
            context,
            SanskritHypotenuseAction.numericOp(),
        )

        assertTrue("overflow" in assertIs<ExecutionResult.Failure>(result).message.lowercase())
    }

    @Test
    fun `trigonometry preserves a fractional result`() {
        val context = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to ExecutionExpression.sankhya(30, "त्रिंशत्")),
        )
        val result = assertIs<ExecutionResult.Success>(
            SanskritTrigonometryAction.execute(
                context,
                SanskritTrigonometryAction.numericOp(minimum = 1),
            ),
        )

        val typed = assertIs<SanskritValue.Rational>(result.typedValue)
        assertEquals(1L, typed.numerator)
        assertEquals(2L, typed.denominator)
    }
}
