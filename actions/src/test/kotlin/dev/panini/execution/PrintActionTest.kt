package dev.panini.execution

import dev.panini.actions.io.PrintAction
import dev.panini.core.Karaka
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PrintActionTest {
    @Test
    fun `prints a semantic range before its instruction operands`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.APADANA to ExecutionExpression.sankhya(1, "एक"),
                Karaka.ADHIKARANA to ExecutionExpression.sankhya(10, "दश"),
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("सङ्ख्याम्"),
                    ExecutionExpression.Pada("अनुमिनु"),
                ),
            ),
        )

        val result = assertIs<ExecutionResult.Success>(
            PrintAction.execute(context, PrintAction.op()),
        )

        assertEquals("एकतः दशपर्यन्तं सङ्ख्याम् अनुमिनु", result.value)
    }
}
