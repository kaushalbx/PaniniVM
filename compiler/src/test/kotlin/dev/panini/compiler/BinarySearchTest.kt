package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** Binary search: the first place whose member is not less than the sought number, then an equality test. */
class BinarySearchTest {
    private val example = java.io.File("examples/algorithms/binary_search.pvm").readText(Charsets.UTF_8)
    private val numerals = listOf("एक", "द्वि", "त्रि", "चतुर्", "पञ्चन्", "षष्", "सप्तन्", "अष्टन्", "नवन्", "दशन्",
        // Compound numbers are written segmented, units before their ten.
        "एक + दशन्", "द्वि + दशन्", "त्रि + दशन्", "चतुर् + दशन्", "पञ्चन् + दशन्", "षष् + दशन्", "सप्तन् + दशन्",
        "अष्टन् + दशन्", "नवन् + दशन्", "विंशति")

    /** A member in its own number: एक and विंशति singular, द्वि dual, three to nineteen plural. */
    private fun nominative(value: Int) = numerals[value - 1] + when (value) { 1, 20 -> " + सुँ"; 2 -> " + औ"; else -> " + जस्" }
    private fun accusative(value: Int) = numerals[value - 1] + when (value) { 1, 20 -> " + अम्"; 2 -> " + औट्"; else -> " + शस्" }

    private fun program(list: List<Int>, sought: Int): String = example.lines().map { line ->
        when {
            line.endsWith(" इति") -> list.joinToString(" ") { nominative(it) } + " इति"
            line.endsWith("लक्ष्य + ङि स्था + णिच् + लोट् + सिप् ।") -> "${accusative(sought)} लक्ष्य + ङि स्था + णिच् + लोट् + सिप् ।"
            else -> line
        }
    }.joinToString("\n")

    private fun interpreted(source: String): String {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.filterIsInstance<ExecutionResult.Failure>().toString())
        return results.filterIsInstance<ExecutionResult.Success>().last().value
    }

    /** The compiled program's printed word and the place left in आदि. */
    private fun compiled(source: String, name: String): Pair<String, Long> {
        @Suppress("UNCHECKED_CAST")
        val values = BytecodeCompiler.compileAndLoad(source, name).getMethod("execute").invoke(null) as Map<String, SanskritValue>
        return values.getValue("LastResult").toDisplayText() to assertIs<SanskritValue.Sankhya>(values["आदि"]).value
    }

    private fun check(list: List<Int>, sought: Int, name: String) {
        val source = program(list, sought)
        val expected = (list.indexOf(sought) + 1).toLong()
        val printed = interpreted(source)
        val (compiledPrinted, place) = compiled(source, name)
        assertEquals(printed, compiledPrinted, "$list, $sought")
        // The program prints आदि when its member is the sought number, and शून्य otherwise.
        if (expected == 0L) assertEquals("शून्य", printed, "$list, $sought")
        else assertEquals(expected, place, "$list, $sought")
        if (expected != 0L) assertTrue(printed != "शून्य", "$list, $sought")
    }

    @Test fun `the example finds nine at the sixth place in both backends`() {
        assertEquals("षट्", interpreted(example))
        assertEquals("षट्" to 6L, compiled(example, "BinarySearchExample"))
    }

    @Test fun `every member and every absent number of the example list`() {
        val list = listOf(1, 3, 4, 6, 7, 9, 10)
        for (sought in 1..10) check(list, sought, "BinarySearchEach$sought")
    }

    @Test fun `a one member list`() {
        check(listOf(4), 4, "BinarySearchOneFound")
        check(listOf(4), 2, "BinarySearchOneBelow")
        check(listOf(4), 9, "BinarySearchOneAbove")
    }

    @Test fun `random sorted lists agree with a reference search`() {
        val random = Random(48)
        repeat(4) { index ->
            val list = (1..10).filter { random.nextBoolean() }.ifEmpty { listOf(5) }
            check(list, random.nextInt(1, 11), "BinarySearchRandom$index")
        }
    }

    @Test fun `members from eleven to twenty are segmented compound cardinals`() {
        val list = listOf(2, 7, 11, 13, 16, 19, 20)
        for (sought in listOf(11, 13, 16, 19, 20, 12, 1)) check(list, sought, "BinarySearchCompound$sought")
    }

    @Test fun `the readable text renders the halving step`() {
        val spoken = dev.panini.execution.PvmUktiSadhaka().sadhayaScript(example)
        assertTrue("आदिम् अन्तं च योजय ततः योजनस्य फलं द्वे च विभाजय" in spoken, spoken)
        assertTrue("यावद् आदिः अन्ताद् न्यूनः अस्ति तावद् भेदनेन कुरु ।" in spoken, spoken)
    }
}
