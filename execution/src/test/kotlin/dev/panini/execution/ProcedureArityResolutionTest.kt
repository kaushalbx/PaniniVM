package dev.panini.execution

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ProcedureArityResolutionTest {
    @Test fun `named binding cannot silently discard an extra piped value`() {
        val padas = PaniniParser().parse("मान + ङि राम + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>()
        val value = padas.last()
        val invocation = PrakriyaInvocation(
            kriya = Prakriya("परीक्षण", "परीक्षण", emptyList(), signatureOverride = PrakriyaSignature(
                parameters = listOf(PrakriyaParameter("मान", PrakriyaValueType.SHABDA)))),
            karmaText = "", fullText = "", argumentSyntax = padas,
            arguments = listOf(
                PrakriyaArgument("राम", pada = value, origin = PrakriyaArgumentOrigin.WRITTEN),
                PrakriyaArgument("फल", value = SanskritValue.Shabda("extra"), origin = PrakriyaArgumentOrigin.PIPE)),
        )
        val failure = assertIs<PrakriyaArgumentResolution.Failure>(PrakriyaInvocationArgumentResolver.resolve(invocation))
        assertTrue(failure.message.contains("1 मानानि अपेक्षितानि, 2 प्राप्तानि"), failure.message)
        assertEquals("PROCEDURE_ARITY_MISMATCH", failure.diagnostic.code)
    }
}
