package dev.panini.compiler

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertFailsWith

class CompilerIrTest {
    @Test
    fun `conditional lowers to branch labels and jump`() {
        val condition = CompilerInstruction.Call(
            dhatuUpadesha = "condition",
            operationName = "condition",
            requiredSanadi = "",
            bindings = emptyMap(),
            resultMode = CallResultMode.BOOLEAN,
        )
        val consequent = CompilerInstruction.Call("then", "then", "", emptyMap())
        val alternate = CompilerInstruction.Call("else", "else", "", emptyMap())

        assertEquals(
            listOf(
                condition,
                CompilerInstruction.Branch("test_alternate"),
                consequent,
                CompilerInstruction.Jump("test_end"),
                CompilerInstruction.Label("test_alternate"),
                alternate,
                CompilerInstruction.Label("test_end"),
            ),
            CompilerIrLowering.lowerConditional(
                condition,
                listOf(consequent),
                listOf(alternate),
                "test",
            ),
        )
    }

    @Test
    fun `conditional without alternate still has a valid false target`() {
        val condition = booleanCall("condition")
        val consequent = valueCall("then")

        val instructions = CompilerIrLowering.lowerConditional(
            condition,
            listOf(consequent),
            labelPrefix = "single",
        )

        assertEquals(CompilerInstruction.Branch("single_alternate"), instructions[1])
        assertEquals(CompilerInstruction.Label("single_alternate"), instructions[4])
        CompilerIrVerifier.verify(instructions)
    }

    @Test
    fun `nested conditional labels remain distinct and valid`() {
        val inner = CompilerIrLowering.lowerConditional(
            booleanCall("inner-condition"),
            listOf(valueCall("inner-then")),
            listOf(valueCall("inner-else")),
            "inner",
        )
        val outer = CompilerIrLowering.lowerConditional(
            booleanCall("outer-condition"),
            inner,
            listOf(valueCall("outer-else")),
            "outer",
        )

        CompilerIrVerifier.verify(outer)
        assertEquals(
            outer.filterIsInstance<CompilerInstruction.Label>().map { it.name }.toSet().size,
            outer.filterIsInstance<CompilerInstruction.Label>().size,
        )
    }

    @Test
    fun `IR verifier rejects missing and duplicate labels`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.Jump("missing")))
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(
                listOf(
                    CompilerInstruction.Label("same"),
                    CompilerInstruction.Label("same"),
                ),
            )
        }
    }

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


    private fun booleanCall(name: String) = CompilerInstruction.Call(
        dhatuUpadesha = name,
        operationName = name,
        requiredSanadi = "",
        bindings = emptyMap(),
        resultMode = CallResultMode.BOOLEAN,
    )

    private fun valueCall(name: String) = CompilerInstruction.Call(
        dhatuUpadesha = name,
        operationName = name,
        requiredSanadi = "",
        bindings = emptyMap(),
    )
}
