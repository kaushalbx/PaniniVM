package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.dhatupatha.adadi.YuDhatu
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class EvennessOperandTest {
    private fun resolve(expression: ExecutionExpression, values: Map<String, SanskritValue> = emptyMap()) =
        OperationResolver.resolve(
            DhatuInvocation("test", YuDhatu(), mapOf(Karaka.KARMAN to expression), selectedOperation = "युग्मत्वम्"),
            values,
        )

    @Test fun `single integer eligibility is independent of operand representation`() {
        val value = SanskritValue.Sankhya(2, "द्वि")
        for (expression in listOf(
            ExecutionExpression.sankhya(2, "द्वि"),
            ExecutionExpression.TypedOperand(value, SupAffix.AM),
            ExecutionExpression.TypedOperand(value, SupAffix.AM, "क्रम"),
            ExecutionExpression.Reference("क्रम"),
        )) {
            val resolved = assertIs<OperationResolution.Resolved>(resolve(expression, mapOf("क्रम" to value))).value
            assertTrue(Samjna.SATYA in resolved.operation.resultSamjnas)
            val result = assertIs<ExecutionResult.Success>(resolved.operation.action.execute(resolved.context, resolved.operation))
            assertEquals(SanskritValue.Satya(true), result.typedValue)
        }
    }

    @Test fun `nonintegers and multiple operands are ineligible`() {
        for (value in listOf(
            SanskritValue.Rational(1, 2, "अर्ध"),
            SanskritValue.Shabda("द्वि"),
            SanskritValue.Suchi(listOf(SanskritValue.Sankhya(2, "द्वि"))),
        )) assertIs<OperationResolution.Invalid>(resolve(ExecutionExpression.TypedOperand(value, SupAffix.AM)))
        assertIs<OperationResolution.Invalid>(resolve(ExecutionExpression.Coordination(
            ExecutionExpression.sankhya(2, "द्वि"), ExecutionExpression.sankhya(3, "त्रि"),
        )))
    }

    @Test fun `direct action calls cannot discard extra operands`() {
        val operation = YuDhatu().operations.single { it.name == "युग्मत्वम्" }
        val context = ExecutionContext(bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Coordination(
            ExecutionExpression.sankhya(2, "द्वि"), ExecutionExpression.sankhya(3, "त्रि"),
        )))
        assertIs<ExecutionResult.Failure>(operation.action.execute(context, operation))
    }
}
