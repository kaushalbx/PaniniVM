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
        val comparison = context(CopularPredicate.EQUAL).copy(bindings = mapOf(
            Karaka.KARTR to ExecutionExpression.Coordination(
                ExecutionExpression.sankhya(1, "एक"), ExecutionExpression.sankhya(2, "द्वि"),
            ),
        ))
        assertIs<ExecutionResult.Failure>(CopularEqualityAction.execute(comparison, CopularEqualityAction.op()))
    }

    private fun context(predicate: CopularPredicate?) = ExecutionContext(bindings = mapOf(
        Karaka.KARTR to ExecutionExpression.sankhya(1, "एक"),
    ), nominalBindings = mapOf(NominalOperandRole.COMPARISON_STANDARD to ExecutionExpression.sankhya(2, "द्वि")),
        copularPredicate = predicate)

    @Test
    fun `ordering rejects absent or unrelated predicate identities`() {
        for (predicate in listOf(CopularPredicate.EQUAL, null)) {
            assertIs<ExecutionResult.Failure>(CopularOrderAction.execute(context(predicate), CopularOrderAction.op()))
        }
    }

    @Test
    fun `predicate identity is typed and cannot be supplied by a runtime word binding`() {
        val untyped = context(null).copy(bindings = context(null).bindings +
            (Karaka.KARMAN to ExecutionExpression.Pada("न्यून")))
        assertIs<ExecutionResult.Failure>(CopularOrderAction.execute(untyped, CopularOrderAction.op()))
        val result = assertIs<ExecutionResult.Success>(CopularOrderAction.execute(context(CopularPredicate.LESS_THAN), CopularOrderAction.op()))
        assertTrue(assertIs<SanskritValue.Satya>(result.typedValue).boolean)
    }

    @Test
    fun `equality rejects an order adjective`() {
        assertIs<ExecutionResult.Failure>(CopularEqualityAction.execute(
            context(CopularPredicate.GREATER_THAN), CopularEqualityAction.op(),
        ))
    }

    @Test fun `equality does not reinterpret an instrument as its comparison standard`() {
        val unlinked = context(CopularPredicate.EQUAL).copy(nominalBindings = emptyMap(),
            bindings = context(CopularPredicate.EQUAL).bindings +
                (Karaka.KARANA to ExecutionExpression.sankhya(2, "द्वि")))
        assertIs<ExecutionResult.Failure>(CopularEqualityAction.execute(unlinked, CopularEqualityAction.op()))
    }
}
