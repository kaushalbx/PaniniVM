package dev.panini.execution

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class SemanticOperandTest {
    @Test fun `unresolved reference is a structured semantic outcome`() {
        val operand = SemanticOperand.Reference("अज्ञात")
        val outcome = assertIs<SemanticOperandResolution.Unresolved>(operand.resolveResult { null })
        assertEquals(operand, outcome.reference)
    }

    private val parser = PaniniParser()

    private fun nominal(stem: String): SubantaPada = parser
        .parse("$stem + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        .grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>().single()

    @Test fun `intrinsic literals retain source provenance and outrank contextual values`() {
        val pada = nominal("एक")
        val parameter = PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)
        val resolved = ResolvedPrakriyaArgument(
            parameter,
            PrakriyaArgument("एक", pada = pada, value = SanskritValue.Shabda("host"),
                origin = PrakriyaArgumentOrigin.WRITTEN),
            PrakriyaArgumentBindingKind.POSITIONAL,
        )

        val operand = assertIs<SemanticOperand.Literal>(resolved.semanticOperand())

        assertSame(pada, operand.source)
        assertEquals(SanskritValue.Sankhya(1, "एक"), operand.value)
        assertEquals(PrakriyaValueType.SANKHYA, operand.typeConstraint)
    }

    @Test fun `references retain grammatical fallback while live lookup stays authoritative`() {
        val pada = nominal("प्रथम")
        val operand = assertIs<SemanticOperand.Reference>(SemanticOperandAnalysis.nominal(pada))
        val live = SanskritValue.Shabda("live")

        assertSame(pada, operand.source)
        assertEquals("प्रथम", operand.name)
        assertIs<SanskritValue.Purana>(operand.fallback)
        assertSame(live, operand.resolve({ live }) { error("fallback should not run") })
        assertEquals(operand.fallback, operand.resolve({ null }) { error("fallback should resolve") })
    }

    @Test fun `resolved contextual evidence is distinct from a symbolic reference`() {
        val pada = nominal("राम")
        val supplied = SanskritValue.Suchi(listOf(SanskritValue.Shabda("क")))
        val argument = ResolvedPrakriyaArgument(
            PrakriyaParameter("मान", PrakriyaValueType.SUCHI),
            PrakriyaArgument("राम", pada = pada, origin = PrakriyaArgumentOrigin.WRITTEN),
            PrakriyaArgumentBindingKind.POSITIONAL,
        )

        val operand = assertIs<SemanticOperand.Resolved>(argument.semanticOperand(supplied))

        assertSame(pada, operand.source)
        assertSame(supplied, operand.value)
        assertEquals(PrakriyaValueType.SUCHI, operand.typeConstraint)
    }
}

