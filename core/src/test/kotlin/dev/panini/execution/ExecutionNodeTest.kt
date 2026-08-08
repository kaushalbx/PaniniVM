package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class ExecutionNodeTest {
    @Test
    fun `conditional branches depend on condition but not each other`() {
        val control = ExecuteConditional(
            condition = ExecuteInvocation("condition"),
            consequent = ExecuteInvocation("then"),
            alternate = ExecuteInvocation("else"),
        )

        assertEquals(
            setOf(ActionDependency("condition", "then"), ActionDependency("condition", "else")),
            control.dependencies(),
        )
        assertEquals(
            mapOf(
                "then" to ExecutionBranchGuard("condition", true),
                "else" to ExecutionBranchGuard("condition", false),
            ),
            control.branchGuards(),
        )
    }

    @Test
    fun `repeat iterations retain sequential execution boundaries`() {
        val control = ExecuteRepeat(
            listOf(ExecuteInvocation("first"), ExecuteInvocation("second")),
        )

        assertEquals(setOf(ActionDependency("first", "second")), control.dependencies())
    }
}
