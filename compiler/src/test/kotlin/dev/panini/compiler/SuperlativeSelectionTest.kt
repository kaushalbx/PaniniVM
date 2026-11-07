package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** उत्तरेषु न्यूनतमः: a superlative chosen from a locative group gives the same answer in both backends. */
class SuperlativeSelectionTest {
    private val example = java.io.File("examples/algorithms/selection_sort.pvm").readText(Charsets.UTF_8)

    private fun interpreted(source: String): List<ExecutionResult> = PaniniVM().evalScript(source).also { results ->
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
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

    @Test fun `selection sort orders the list in both backends`() {
        assertEquals("[एक, द्वि, त्रि, चतुर्, पञ्चन्, अष्टन्]", assertIs<ExecutionResult.Success>(interpreted(example).last()).value)
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 8L), numbers(compiled(example, "SelectionSort")["दत्तसूची"]))
    }

    @Test fun `equal members, a sorted list and one member stay correct`() {
        for ((declared, expected) in listOf(
            "त्रि + जस् एक + सुँ द्वि + औ एक + सुँ त्रि + जस्" to listOf(1L, 1L, 2L, 3L, 3L),
            "एक + सुँ द्वि + औ त्रि + जस्" to listOf(1L, 2L, 3L),
            "सप्तन् + जस्" to listOf(7L),
        )) {
            val source = withList(declared)
            interpreted(source)
            assertEquals(expected, numbers(compiled(source, "SelectionSortCase${expected.size}")["दत्तसूची"]), declared)
        }
    }

    @Test fun `the exchanged members are the accusative dual and nothing else`() {
        val list = "एक + सुँ द्वि + औ त्रि + जस् इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।"
        fun exchange(obj: String) = "$list\nदत्तसूची + ङस् प्रथम + ङि तृतीय + ङि च $obj वि + नि + मे + लोट् + थास् ।"
        assertEquals("[त्रि, द्वि, एक]", assertIs<ExecutionResult.Success>(interpreted(exchange("सदस्य + औट्")).last()).value)
        for (wrong in listOf("सदस्य + अम्", "सदस्य + शस्", "फल + औट्")) {
            val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(exchange(wrong)).last(), wrong)
            assertTrue("सदस्य + औट्" in failure.message, failure.message)
            assertTrue(runCatching { compiled(exchange(wrong), "WrongExchange") }.isFailure, wrong)
        }
    }

    @Test fun `a result named by its action is that action's, not the latest`() {
        // योजनस्य फलम् after an absolutive chain is the sum, though नव was stored after it.
        val source = "द्वि + अम् त्रि + अम् च युज् + णिच् + क्त्वा नवन् + अम् फल + ङि स्था + णिच् + क्त्वा " +
            "युज् + णिच् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("पञ्च", assertIs<ExecutionResult.Success>(interpreted(source).last()).value)
        assertEquals("पञ्च", compiled(source, "NamedAbsolutiveResult")["LastResult"]?.toDisplayText())
    }

    @Test fun `the place of the greatest is found in both backends`() {
        val source = """
            त्रि + जस् नवन् + जस् पञ्चन् + जस् नवन् + जस् इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङि अधिक + तमप् + ङस् स्थान + अम् अनु + इष् + लोट् + सिप् ।
        """.trimIndent()
        // The first of equal greatest members is chosen.
        assertEquals("द्वे", assertIs<ExecutionResult.Success>(interpreted(source).last()).value)
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(compiled(source, "GreatestPlace")["LastResult"]).value)
    }
}
