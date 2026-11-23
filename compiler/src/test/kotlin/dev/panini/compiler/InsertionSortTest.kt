package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** Insertion sort: each member finds its place among the members before it and is removed and inserted there. */
class InsertionSortTest {
    private val example = java.io.File("examples/algorithms/insertion_sort.pvm").readText(Charsets.UTF_8)

    private fun interpreted(source: String): List<ExecutionResult> = PaniniVM().evalScript(source).also { results ->
        assertTrue(results.all { it is ExecutionResult.Success }, results.filterIsInstance<ExecutionResult.Failure>().toString())
    }

    private fun compiled(source: String, name: String): Map<String, SanskritValue> {
        @Suppress("UNCHECKED_CAST")
        return BytecodeCompiler.compileAndLoad(source, name).getMethod("execute").invoke(null) as Map<String, SanskritValue>
    }

    private fun numbers(value: SanskritValue?) = assertIs<SanskritValue.Suchi>(value).items.map { assertIs<SanskritValue.Sankhya>(it).value }

    private fun withList(declared: String) = example.lines().let { lines ->
        val at = lines.indexOfFirst { it.endsWith(" इति") }
        (lines.take(at) + "$declared इति" + lines.drop(at + 1)).joinToString("\n")
    }

    @Test fun `insertion sort orders the list in both backends`() {
        assertEquals("[एक, द्वि, त्रि, चतुर्, पञ्चन्, अष्टन्]", assertIs<ExecutionResult.Success>(interpreted(example).last()).value)
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 8L), numbers(compiled(example, "InsertionSort")["दत्तसूची"]))
    }

    @Test fun `equal members, sorted, reversed and one-member lists stay correct`() {
        for ((declared, expected) in listOf(
            "त्रि + जस् एक + सुँ द्वि + औ एक + सुँ त्रि + जस्" to listOf(1L, 1L, 2L, 3L, 3L),
            "एक + सुँ द्वि + औ त्रि + जस्" to listOf(1L, 2L, 3L),
            "चतुर् + जस् त्रि + जस् द्वि + औ एक + सुँ" to listOf(1L, 2L, 3L, 4L),
            "सप्तन् + जस्" to listOf(7L),
        )) {
            val source = withList(declared)
            interpreted(source)
            assertEquals(expected, numbers(compiled(source, "InsertionSortCase${expected.size}")["दत्तसूची"]), declared)
        }
    }

    @Test fun `न over a compared loop condition inverts it in both backends`() {
        // While three is not less than the counter, increment it: the loop stops at four.
        val source = """
            वृध् + ल्युट् + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप्
                ततः युज् + णिच् + ल्युट् + ङस् फल + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ॥
            एक + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
            यावत् त्रि + जस् गणना + ङसिँ न्यून + जस् न असँ + लट् + झि तावत् वृध् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
            गणना + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        assertEquals("चत्वारि", assertIs<ExecutionResult.Success>(interpreted(source).last()).value)
        assertEquals(4L, assertIs<SanskritValue.Sankhya>(compiled(source, "NegatedLoop")["गणना"]).value)
    }

    @Test fun `न over a compared यदि condition inverts it in both backends`() {
        for ((count, expected) in listOf("एक + अम्" to "सत्यम्", "पञ्चन् + शस्" to "असत्यम्")) {
            val source = """
                $count गणना + ङि स्था + णिच् + लोट् + सिप् ।
                यदि त्रि + जस् गणना + ङसिँ न्यून + जस् न असँ + लट् + झि तर्हि सत्य + अम् निर्णय + ङि स्था + णिच् + लोट् + सिप् अन्यथा असत्य + अम् निर्णय + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent()
            interpreted(source)
            assertEquals(expected, compiled(source, "NegatedIf$count")["निर्णय"]?.toDisplayText(), count)
            assertEquals(expected, PaniniVM().evalScript(source).let { assertIs<ExecutionResult.Success>(it.last()).typedValue?.toDisplayText() }, count)
        }
    }

    @Test fun `च after a finite verb conjoins condition clauses in both backends`() {
        // यदि गणना is greater than one and less than five: true only for two through four.
        for ((count, expected) in listOf("एक + अम्" to "असत्यम्", "त्रि + शस्" to "सत्यम्", "पञ्चन् + शस्" to "असत्यम्")) {
            val source = """
                $count गणना + ङि स्था + णिच् + लोट् + सिप् ।
                यदि एक + सुँ गणना + ङसिँ न्यून + सुँ असँ + लट् + तिप् च गणना + सुँ पञ्चन् + भ्यस् न्यून + सुँ असँ + लट् + तिप् तर्हि सत्य + अम् निर्णय + ङि स्था + णिच् + लोट् + सिप् अन्यथा असत्य + अम् निर्णय + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent()
            assertEquals(expected, assertIs<ExecutionResult.Success>(interpreted(source).last()).typedValue?.toDisplayText(), count)
            assertEquals(expected, compiled(source, "Conjoined$count")["निर्णय"]?.toDisplayText(), count)
        }
    }

    @Test fun `a conjoined loop condition stops at the first false clause`() {
        // Count up while गणना is less than ten and less than four: the second clause ends it at four.
        val source = """
            वृध् + ल्युट् + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप्
                ततः युज् + णिच् + ल्युट् + ङस् फल + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ॥
            एक + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
            यावत् गणना + सुँ दशन् + भ्यस् न्यून + सुँ असँ + लट् + तिप् च गणना + सुँ चतुर् + भ्यस् न्यून + सुँ असँ + लट् + तिप् तावत् वृध् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
            गणना + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        assertEquals("चत्वारि", assertIs<ExecutionResult.Success>(interpreted(source).last()).value)
        assertEquals(4L, assertIs<SanskritValue.Sankhya>(compiled(source, "ConjoinedLoop")["गणना"]).value)
    }

    @Test fun `the readable text renders both conjoined clauses`() {
        val line = "यदि एक + सुँ स्थान + ङसिँ न्यून + सुँ असँ + लट् + तिप् च अङ्क + सुँ पूर्वाङ्क + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि स्थान + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val spoken = dev.panini.execution.PvmUktiSadhaka().sadhayaLine(line)
        assertEquals("यदि एकं स्थानाद् न्यूनम् अस्ति च अङ्कं पूर्वाङ्काद् न्यूनम् अस्ति तर्हि स्थानं मुद्रय ।", spoken)
    }

    @Test fun `a predicate agrees with a feminine aa-stem subject`() {
        val render = dev.panini.execution.PvmUktiSadhaka()::sadhayaLine
        assertEquals("यदि गणना पञ्चभ्यः न्यूना अस्ति तर्हि गणनां मुद्रय ।",
            render("यदि गणना + सुँ पञ्चन् + भ्यस् न्यून + सुँ असँ + लट् + तिप् तर्हि गणना + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
        assertEquals("यदि गणना त्रिभ्यः अधिका अस्ति तर्हि गणनां मुद्रय ।",
            render("यदि गणना + सुँ त्रि + भ्यस् अधिक + सुँ असँ + लट् + तिप् तर्हि गणना + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
    }

    private val numerals = listOf("एक", "द्वि", "त्रि", "चतुर्", "पञ्चन्", "षष्", "सप्तन्", "अष्टन्", "नवन्", "दशन्")

    /** A nominative member in its own number: एक is singular, द्वि dual, the rest plural. */
    private fun member(value: Int) = numerals[value - 1] + when (value) { 1 -> " + सुँ"; 2 -> " + औ"; else -> " + जस्" }

    @Test fun `an empty list stays empty in both backends`() {
        val source = example.lines().filterNot { it.endsWith(" इति") }.joinToString("\n")
        interpreted(source)
        assertEquals(emptyList(), numbers(compiled(source, "InsertionSortEmpty")["दत्तसूची"]))
    }

    @Test fun `longer and random lists match a reference sort in both backends`() {
        val random = kotlin.random.Random(48)
        val lists = listOf(listOf(9, 3, 10, 1, 7, 2, 8, 2, 6, 4, 10, 5, 3, 1, 9)) +
            List(3) { List(8) { random.nextInt(1, 11) } }
        lists.forEachIndexed { index, values ->
            val source = withList(values.joinToString(" ") { member(it) })
            assertEquals(values.sorted().joinToString(", ", "[", "]") { numerals[it - 1] },
                assertIs<ExecutionResult.Success>(interpreted(source).last()).value, values.toString())
            assertEquals(values.sorted().map(Int::toLong), numbers(compiled(source, "InsertionSortRandom$index")["दत्तसूची"]), values.toString())
        }
    }
}
