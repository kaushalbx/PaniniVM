package dev.panini.execution

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ProcedureArgumentMorphologyTest {
    @Test fun `one named slot and value attach without an adjacency requirement`() {
        val signature = PrakriyaSignature(parameters = listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)))
        for (phrase in listOf("मान + ङि राम + अम्", "राम + अम् मान + ङि")) {
            val padas = PaniniParser().parse("$phrase ।").grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>()
            val result = assertIs<PrakriyaArgumentResolution.Success>(NamedPrakriyaArgumentResolver.resolve(padas, signature))
            assertEquals(listOf("राम"), result.terms)
            assertTrue(result.named)
            assertEquals("मान", result.arguments.single().parameter?.nameStem)
        }
    }

    @Test fun `unsupported case cannot disappear from positional procedure operands`() {
        for (ending in listOf("सुँ", "टा", "ङे", "ङसिँ", "भ्याम्", "भ्यस्")) {
            val padas = PaniniParser().parse("राम + $ending एक + अम् ।")
                .grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>()
            assertIs<PrakriyaArgumentResolution.Failure>(
                NamedPrakriyaArgumentResolver.resolve(padas, PrakriyaSignature()), ending)
        }
    }

    @Test fun `accusative procedure operand remains positional`() {
        val padas = PaniniParser().parse("राम + अम् ।").grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>()
        val resolved = assertIs<PrakriyaArgumentResolution.Success>(
            NamedPrakriyaArgumentResolver.resolve(padas, PrakriyaSignature()))
        assertEquals(listOf("राम"), resolved.terms)
        assertFalse(resolved.named)
    }
}
