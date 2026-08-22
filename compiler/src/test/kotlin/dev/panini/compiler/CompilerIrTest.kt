package dev.panini.compiler

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class CompilerIrTest {
    @Test
    fun `numeric leaf lowers to a backend neutral call`() {
        val plan = requireNotNull(DirectLeafPlanner.planAny(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।",
        ))

        val call = assertIs<CompilerInstruction.Call>(CompilerIrLowering.lowerLeaf(plan))

        assertEquals("युजिँर्", call.dhatuUpadesha)
        assertEquals("सङ्ख्यायोजनम्", call.operationName)
        assertEquals("", call.requiredSanadi)
        assertEquals(CallResultMode.VALUE, call.resultMode)
        assertNull(call.destination)
        val values = call.bindings.values.flatMap { expression ->
            when (expression) {
                is ExecutionExpression.Coordination -> expression.members
                else -> listOf(expression)
            }
        }.mapNotNull { expression ->
            when (expression) {
                is ExecutionExpression.Pada -> expression.value
                is ExecutionExpression.TypedOperand -> expression.value
                else -> null
            }
        }.filterIsInstance<SanskritValue.Sankhya>().map { it.value }
        assertEquals(listOf(1L, 2L), values)
    }

    @Test
    fun `boolean and loop target modes survive IR lowering`() {
        val condition = requireNotNull(DirectLeafPlanner.planAny(
            "द्वि + अम् एक + अम् च विद् + लोट् + सिप् ।",
        ))
        val target = requireNotNull(DirectLeafPlanner.planAny(
            "चक्रफल + अम् मुद्र् + लोट् + सिप् ।",
        ))

        assertEquals(
            CallResultMode.BOOLEAN,
            assertIs<CompilerInstruction.Call>(
                CompilerIrLowering.lowerLeaf(condition, CallResultMode.BOOLEAN),
            ).resultMode,
        )
        assertEquals(
            CallResultMode.LOOP_TARGET,
            assertIs<CompilerInstruction.Call>(
                CompilerIrLowering.lowerLeaf(target, CallResultMode.LOOP_TARGET),
            ).resultMode,
        )
    }
}
