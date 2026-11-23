package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.OutputKind
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Quick sort by recursion: a procedure divides each list as partition callback, in both backends. */
class QuickSortTest {
    private val example = java.io.File("examples/algorithms/quick_sort.pvm").readText(Charsets.UTF_8)
    private val numerals = listOf("एक", "द्वि", "त्रि", "चतुर्", "पञ्चन्", "षष्", "सप्तन्", "अष्टन्", "नवन्", "दशन्")

    /** A member in its own number: एक singular, द्वि dual, three to ten plural. */
    private fun nominative(value: Int) = numerals[value - 1] + when (value) { 1 -> " + सुँ"; 2 -> " + औ"; else -> " + जस्" }

    private fun program(list: List<Int>): String {
        val lines = example.lines()
        val declaration = lines.indexOfFirst { it.endsWith(" इति") }
        return (lines.take(declaration) + (list.joinToString(" ") { nominative(it) } + " इति") +
            lines.drop(declaration + 1)).joinToString("\n")
    }

    private fun interpreted(source: String): String {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.filterIsInstance<ExecutionResult.Failure>().toString())
        return results.filterIsInstance<ExecutionResult.Success>().last { it.outputKind == OutputKind.CONSOLE }.value
    }

    private fun compiled(source: String, name: String): String {
        @Suppress("UNCHECKED_CAST")
        val values = BytecodeCompiler.compileAndLoad(source, name).getMethod("execute").invoke(null) as Map<String, SanskritValue>
        return values.getValue("LastResult").toDisplayText()
    }

    private fun check(list: List<Int>, name: String) {
        val source = program(list)
        val expected = list.sorted().joinToString(", ", "[", "]") { numerals[it - 1] }
        assertEquals(expected, interpreted(source), "$list")
        assertEquals(expected, compiled(source, name), "$list")
    }

    @Test fun `the example sorts its list`() = check(listOf(5, 1, 4, 2, 8, 3), "QuickSortExample")

    @Test fun `one member, equal members and reversed order`() {
        check(listOf(7), "QuickSortSingle")
        check(listOf(3, 3, 1, 3), "QuickSortEqual")
        check(listOf(9, 7, 5, 3, 1), "QuickSortReversed")
    }

    @Test fun `random lists agree with a plain sort`() {
        val random = Random(42)
        repeat(4) { round ->
            check(List(random.nextInt(2, 9)) { random.nextInt(1, 11) }, "QuickSortRandom$round")
        }
    }
}
