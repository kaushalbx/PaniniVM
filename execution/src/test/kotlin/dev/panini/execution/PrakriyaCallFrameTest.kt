package dev.panini.execution

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class PrakriyaCallFrameTest {
    @Test fun `native call frames preserve literal priority without registry preprocessing`() {
        val parameter = PrakriyaParameter("मान", PrakriyaValueType.UNCONSTRAINED)
        val procedure = Prakriya("परीक्षण", "परीक्षण", emptyList(),
            signatureOverride = PrakriyaSignature(parameters = listOf(parameter)))
        for ((word, expected) in listOf("एक" to SanskritValue.Sankhya(1, "एक"),
            "सत्य" to SanskritValue.Satya(true), "असत्य" to SanskritValue.Satya(false))) {
            val pada = PaniniParser().parse("$word + अम् मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>().single()
            val invocation = PrakriyaInvocation(procedure, word, "native call")
            val argument = ResolvedPrakriyaArgument(parameter, PrakriyaArgument(word, pada = pada,
                origin = PrakriyaArgumentOrigin.WRITTEN),
                PrakriyaArgumentBindingKind.POSITIONAL)
            val frame = PrakriyaCallFrame.createResolved(invocation, listOf(argument),
                ExecutionScope(environment = ValueEnvironment(mapOf(word to SanskritValue.Shabda("host")))))
            assertEquals(expected, frame.arguments.single(), word)
        }
    }
}
