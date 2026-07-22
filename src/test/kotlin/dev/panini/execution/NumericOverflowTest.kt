package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NumericOverflowTest {
    @Test
    fun `yuj operation is selected from grammatical upasarga`() {
        fun resolve(upasargas: Set<String>): DhatuOperation {
            val expression = ExecutionExpression.Coordination(
                ExecutionExpression.sankhya(3, "त्रि"),
                ExecutionExpression.sankhya(1, "एक"),
            )
            val invocation = DhatuInvocation(
                id = "test",
                dhatu = YujirDhatu(),
                bindings = mapOf(Karaka.KARMAN to expression),
                grammaticalFeatures = GrammaticalFeatures(upasargas = upasargas),
            )
            return assertIs<OperationResolution.Resolved>(
                OperationResolver.resolve(invocation, emptyMap<String, SanskritValue>()),
            ).value.operation
        }

        assertEquals("सङ्ख्यायोजनम्", resolve(emptySet()).id)
        assertEquals("सङ्ख्यावियोगः", resolve(setOf("वि")).id)
    }

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
