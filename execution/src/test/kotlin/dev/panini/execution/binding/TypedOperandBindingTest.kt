package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.ValueEnvironment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TypedOperandBindingTest {
    @Test
    fun `scope value binds as a first class typed operand`() {
        val value = SanskritValue.Sankhya(6, "षट्")
        val binding = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "विशेषणफल + अम् मुद्र् + लोट् + सिप् ।"),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                environment = ValueEnvironment(mapOf("विशेषणफल" to value)),
            ),
        )

        val operand = assertIs<ExecutionExpression.TypedOperand>(
            binding.ukti.invocations.single().bindings.getValue(Karaka.KARMAN),
        )
        assertEquals(value, operand.value)
        assertEquals(SupAffix.AM, operand.sup)
    }
}
