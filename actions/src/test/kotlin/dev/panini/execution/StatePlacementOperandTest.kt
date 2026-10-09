package dev.panini.execution

import dev.panini.actions.state.VariableAssignAction
import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import kotlin.test.*

class StatePlacementOperandTest {
    @Test
    fun `assignment rejects partly resolved coordination instead of storing a different value`() {
        val operation = VariableAssignAction.op()
        val number = ExecutionExpression.sankhya(1, "एक")
        for (expression in listOf(
            ExecutionExpression.Reference("missing"),
            ExecutionExpression.Coordination(number, ExecutionExpression.Reference("missing")),
            ExecutionExpression.Coordination(number,
                ExecutionExpression.Coordination(number, ExecutionExpression.Reference("missing"))),
        )) {
            val result = VariableAssignAction.execute(ExecutionContext(bindings = mapOf(
                Karaka.KARMAN to expression,
                Karaka.ADHIKARANA to ExecutionExpression.Pada("स्थान"),
            )), operation)
            assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
        }
    }

    @Test
    fun `assignment preserves an explicitly supplied empty typed list`() {
        val operation = VariableAssignAction.op()
        val value = SanskritValue.Suchi(emptyList(), ListMemberType.NUMBER)
        val result = VariableAssignAction.execute(ExecutionContext(bindings = mapOf(
            Karaka.KARMAN to ExecutionExpression.TypedOperand(value, SupAffix.AM),
            Karaka.ADHIKARANA to ExecutionExpression.Pada("स्थान"),
        )), operation)
        assertEquals(value, assertIs<ExecutionResult.Success>(result).typedValue)
    }
}
