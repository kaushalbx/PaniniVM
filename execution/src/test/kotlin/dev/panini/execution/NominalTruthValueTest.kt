package dev.panini.execution

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class NominalTruthValueTest {
    @Test
    fun `derived loop truth subject cannot reuse plain base state`() {
        val results = PaniniVM().evalScript(
            "सत्य + अम् ध्वज + ङे दा + लोट् + सिप् ।\n" +
                "एक + कृत्वसुच् यावत् ध्वज + मतुप् + सुँ भू + लट् + तिप् तावत् " +
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertIs<ExecutionResult.Success>(results.first())
        assertIs<ExecutionResult.NeedsInput>(results.last(), results.toString())
        assertTrue(results.none { it is ExecutionResult.Success && it.outputKind == OutputKind.CONSOLE })
    }

    @Test
    fun `derived truth subject does not read a separately bound base variable`() {
        val results = PaniniVM().evalScript(
            "सत्य + अम् विजय + ङे दा + लोट् + सिप् ।\n" +
                "यदि विजय + मतुप् + सुँ भू + लट् + तिप् तर्हि " +
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा " +
                "द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertIs<ExecutionResult.Success>(results.first())
        assertIs<ExecutionResult.Failure>(results.last(), results.toString())
    }

    @Test
    fun `derived victory nominal is not a reported outcome shortcut`() {
        for (affix in listOf("मतुप्", "तरप्")) {
            val invocation = PaniniParser().parse(
                "विजय + $affix + सुँ न भू + लट् + तिप् ।",
            ).body as dev.panini.vyakaranam.ast.Invocation
            val operation = assertIs<NaturalSemanticNormalizer.Operation.TruthTest>(
                NaturalSemanticNormalizer.normalize(invocation))
            assertTrue(operation.stateName.contains(affix), operation.toString())
        }
        val plain = PaniniParser().parse("विजय + सुँ न भू + लट् + तिप् ।").body
            as dev.panini.vyakaranam.ast.Invocation
        assertIs<NaturalSemanticNormalizer.Operation.ReportedOutcomeTest>(NaturalSemanticNormalizer.normalize(plain))
    }

    @Test
    fun `truth identity retains nominal derivation`() {
        for ((base, expected) in listOf("सत्य" to true, "असत्य" to false)) {
            for (affix in listOf("", " + मतुप्", " + तरप्", " + टाप्")) {
                val pada = PaniniParser().parse("$base$affix + सुँ ।")
                    .grammaticalVakyas().single().padas.single() as SubantaPada
                val value = nominalTruthValue(pada.pratipadika)
                if (affix.isEmpty()) {
                    assertEquals(expected, assertNotNull(value).boolean)
                    assertEquals(if (expected) "सत्यम्" else "असत्यम्", value.toDisplayText())
                }
                else assertNull(value, "$base$affix")
            }
        }
    }
}
