package dev.panini.execution

import java.io.File
import kotlin.test.*

class ReviewedLoopExamplesTest {
    private fun run(path: String): List<ExecutionResult.Success> {
        val results = PaniniVM().evalScript(File(path).readText())
        assertTrue(results.all { it is ExecutionResult.Success }, "$path: $results")
        return results.filterIsInstance<ExecutionResult.Success>()
    }

    @Test fun `bounded loop example constructs exactly one through ten`() {
        val results = run("examples/control_flow/print_1_to_10.pvm")
        val collection = results.mapNotNull { it.typedValue }.last { it is SanskritValue.Suchi || it is SanskritValue.Gana }
        val members = when (collection) {
            is SanskritValue.Suchi -> collection.items
            is SanskritValue.Gana -> collection.elements
            else -> error("Expected a collection")
        }
        assertEquals((1L..10L).toList(), members.map { assertIs<SanskritValue.Sankhya>(it).value })
        assertEquals(listOf(collection.toDisplayText()), results.filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test fun `punah repeats once and foreach visits each element once`() {
        val repeated = run("examples/control_flow/punah.pvm")
        assertEquals(2L, repeated.mapNotNull { it.typedValue as? SanskritValue.Sankhya }.last().value)
        assertEquals(listOf("एक", "द्वि", "त्रि"), run("examples/control_flow/foreach.pvm")
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test fun `yada tada examples respect true and false guards with dual numeral operands`() {
        val source = File("examples/control_flow/yada_tada.pvm").readText()
        assertEquals(listOf("एक", "द्वि"), run("examples/control_flow/yada_tada.pvm")
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
        val falseResults = PaniniVM().evalScript(source.replace("एक + टा सम", "द्वि + भ्याम् सम"))
        assertTrue(falseResults.all { it is ExecutionResult.Success }, falseResults.toString())
        assertTrue(falseResults.filterIsInstance<ExecutionResult.Success>().none { it.outputKind == OutputKind.CONSOLE })
        assertEquals(listOf("द्वि", "एक", "द्वि", "त्रि"), run("examples/control_flow/local_pronouns.pvm")
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }
}
