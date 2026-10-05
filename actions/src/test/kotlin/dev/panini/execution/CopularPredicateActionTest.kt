package dev.panini.execution

import dev.panini.actions.comparison.CopularOrderAction
import dev.panini.actions.comparison.CopularEqualityAction
import dev.panini.core.Karaka
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CopularPredicateActionTest {
    @Test
    fun `scalar equality does not invent existential meaning for coordinated operands`() {
        val comparison = context(ExecutionExpression.Pada("सम")).copy(bindings = mapOf(
            Karaka.KARMAN to ExecutionExpression.Pada("सम"),
            Karaka.KARTR to ExecutionExpression.Coordination(
                ExecutionExpression.sankhya(1, "एक"), ExecutionExpression.sankhya(2, "द्वि"),
            ),
            Karaka.KARANA to ExecutionExpression.sankhya(2, "द्वि"),
        ))
        assertIs<ExecutionResult.Failure>(CopularEqualityAction.execute(comparison, CopularEqualityAction.op()))
    }

    private fun context(predicate: ExecutionExpression) = ExecutionContext(bindings = mapOf(
        Karaka.KARTR to ExecutionExpression.sankhya(1, "एक"),
        Karaka.APADANA to ExecutionExpression.sankhya(2, "द्वि"),
        Karaka.KARANA to ExecutionExpression.sankhya(2, "द्वि"),
        Karaka.KARMAN to predicate,
    ))

    @Test
    fun `ordering rejects absent or unrelated predicate identities`() {
        for (predicate in listOf(ExecutionExpression.Pada("सम"), ExecutionExpression.Pada("फल"),
            ExecutionExpression.Reference("न्यून"))) {
            assertIs<ExecutionResult.Failure>(CopularOrderAction.execute(context(predicate), CopularOrderAction.op()))
        }
    }

    @Test
    fun `predicate identity does not depend on a mutable runtime value`() {
        val predicate = ExecutionExpression.Pada("न्यून", value = SanskritValue.Shabda("अधिक"))
        val result = assertIs<ExecutionResult.Success>(CopularOrderAction.execute(context(predicate), CopularOrderAction.op()))
        assertTrue(assertIs<SanskritValue.Satya>(result.typedValue).boolean)
    }

    @Test
    fun `equality rejects an order adjective`() {
        assertIs<ExecutionResult.Failure>(CopularEqualityAction.execute(
            context(ExecutionExpression.Pada("अधिक")), CopularEqualityAction.op(),
        ))
    }
}
