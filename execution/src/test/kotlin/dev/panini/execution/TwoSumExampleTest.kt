package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.CollectionRelativeAction
import dev.panini.vyakaranam.ast.depthFirst

class TwoSumExampleTest {
    private fun printed(source: String): List<String> {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        return results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE && it.value != "विजयः" }
            .map { it.value }
    }

    @Test
    fun `two sum uses following items and remembered addition operands`() {
        val program = assertIs<PvmScriptStatement.Sentence>(
            PvmScript.parse(File("examples/algorithms/two_sum.pvm").readText()).last(),
        ).program!!
        val conditionals = program.depthFirst().filterIsInstance<Conditional>().toList()
        assertEquals(1, program.depthFirst().filterIsInstance<CollectionRelativeAction>().count())
        assertTrue(program.sourceText.contains("कर्मन्"))
        assertTrue(!program.sourceText.contains("क्रम"))
        assertTrue(conditionals.any { condition -> condition.consequent.depthFirst()
            .filterIsInstance<dev.panini.vyakaranam.ast.Invocation>()
            .mapNotNull { (it.vakya as? dev.panini.vyakaranam.ast.AkhyataVakya)?.tinganta }
            .any(NaturalScriptStop::matchesHead) })
    }

    @Test
    fun `two sum example derives and prints the matching addition operands`() {
        val source = File("examples/algorithms/two_sum.pvm").readText()
        val readable = PvmUktiSadhaka().sadhayaScript(source)
        assertTrue(readable.contains("एकैकां सङ्ख्यां तदुत्तराभिः योजय"), readable)
        assertTrue(readable.contains("योजनस्य कर्मणी"), readable)
        assertEquals(
            listOf("एक चतुर्"),
            printed(source),
        )
    }

    @Test
    fun `two sum uses two distinct positions for duplicate values`() {
        val source = File("examples/algorithms/two_sum.pvm").readText()
            .replace(
                "एक + सुँ द्वि + औ त्रि + जस् चतुर् + जस् इति",
                "त्रि + सुँ त्रि + औ चतुर् + जस् पञ्चन् + जस् इति",
            )
            .replace(
                "पञ्चन् + भिस् सम",
                "षष् + भिस् सम",
            )

        assertEquals(listOf("त्रि त्रि"), printed(source))
    }
}
