package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.execution.planning.ResolvedLeafPlanner
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ResolvedOperandProvenanceTest {
    @Test fun `typed reference observes current value while literal stays captured`() {
        val captured = SanskritValue.Sankhya(1L, "एक")
        val current = SanskritValue.Sankhya(2L, "द्वि")
        val reference = ExecutionExpression.TypedOperand(captured, SupAffix.AM, "क्रम")
        val literal = ExecutionExpression.TypedOperand(captured, SupAffix.AM)
        val context = ExecutionContext(variables = mapOf("क्रम" to current))
        assertEquals("क्रम", reference.bindingName())
        assertNull(literal.bindingName())
        assertEquals(listOf(current), context.resolveValues(reference))
        assertEquals(listOf(captured), context.resolveValues(literal))
        assertEquals(current, context.literals(reference)?.single()?.value)
    }

    private fun plan(environment: ValueEnvironment, injected: Map<Karaka, ExecutionExpression> = emptyMap()): ExecutionExpression {
        val parsed = PaniniParser().parse("क्रम + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val invocation = assertIs<Invocation>(parsed.body)
        val plan = assertNotNull(ResolvedLeafPlanner.planAny(invocation, environment, injected))
        return plan.resolved.context.bindings.getValue(Karaka.KARMAN)
    }

    @Test fun `named empty list remains a reference instead of a planning constant`() {
        assertEquals(
            ExecutionExpression.Reference("क्रम"),
            plan(ValueEnvironment(mapOf("क्रम" to SanskritValue.Suchi(emptyList())))),
        )
    }

    @Test fun `injected literal matching a symbolic operand remains literal`() {
        val literal = ExecutionExpression.TypedOperand(SanskritValue.Shabda("क्रम"), SupAffix.AM)
        val coordinated = assertIs<ExecutionExpression.Coordination>(
            plan(ValueEnvironment(), mapOf(Karaka.KARMAN to literal)),
        )
        assertEquals(listOf(literal, ExecutionExpression.Reference("क्रम")), coordinated.members)
    }
}
