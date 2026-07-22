package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NumericOverflowTest {
    @Test
    fun `addition rejects long overflow instead of wrapping`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(Long.MAX_VALUE, "अधिकतमम्"),
                    ExecutionExpression.sankhya(1, "एक"),
                ),
            ),
            selectedOperation = "सङ्ख्यायोजनम्",
        )

        val failure = assertIs<ExecutionResult.Failure>(ExecutionEngine.execute(YujirDhatu(), context))

        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
        assertTrue("overflow" in failure.message.lowercase())
    }
}
