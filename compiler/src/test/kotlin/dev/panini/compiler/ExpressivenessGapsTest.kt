package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** Compound cardinals, ततः chains inside a branch, and top-level तद् behave alike in both backends. */
class ExpressivenessGapsTest {
    private fun interpreted(source: String): String =
        assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last()).value

    @Suppress("UNCHECKED_CAST")
    private fun compiled(source: String, name: String): Map<String, SanskritValue> =
        BytecodeCompiler.compileAndLoad(source, name).getMethod("execute").invoke(null) as Map<String, SanskritValue>

    @Test fun `segmented compound cardinals are numbers`() {
        val source = "त्रि + दशन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।\nअङ्क + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        assertEquals(14L, assertIs<SanskritValue.Sankhya>(compiled(source, "GapSegmented")["LastResult"]).value)
        assertEquals("चतुर्दश", interpreted(source))
    }

    @Test fun `a stored segmented number keeps the stem of its written construction`() {
        for ((written, stem, name) in listOf(Triple("षष् + दशन् + शस्", "षोडशन्", "GapSixteen"),
                Triple("एक + ऊन + विंशति + अम्", "एकोनविंशति", "GapNineteen"))) {
            val source = "$written अङ्क + ङि स्था + णिच् + लोट् + सिप् ।\nअङ्क + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertEquals(stem, interpreted(source), written)
            assertEquals(stem, assertIs<SanskritValue.Sankhya>(compiled(source, name)["अङ्क"]).word, written)
        }
    }

    @Test fun `a one-word compound number is refused with its segmented spelling`() {
        val source = "त्रयोदशन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।"
        val failure = PaniniVM().evalScript(source).last()
        assertTrue(failure is ExecutionResult.Failure && "त्रि + दशन्" in failure.message, failure.toString())
        assertFails { compiled(source, "GapOneWordRefused") }
        val diagnostic = dev.panini.execution.PrakriyaScriptValidator.validate(source).single { it.code == "SEGMENTED_NUMERAL" }
        assertEquals("त्रि + दशन्", diagnostic.replacement)
        assertEquals("त्रयोदशन्", source.substring(diagnostic.offset, diagnostic.offset + diagnostic.length))
    }

    @Test fun `eleven to nineteen must be plural in both backends`() {
        val source = "एक + दशन् + अम् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।"
        assertFails { PaniniVM().evalScript(source).forEach { check(it is ExecutionResult.Success) { it } } }
        assertFails { compiled(source, "GapSegSingular") }
    }

    @Test fun `a segmented numeral takes the number of its head`() {
        // एकोनविंशति is headed by विंशति, which is singular, although its value is nineteen.
        val source = "एक + ऊन + विंशति + अम् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।"
        assertEquals(19L, assertIs<SanskritValue.Sankhya>(compiled(source, "GapUnaHead")["अङ्क"]).value)
        assertTrue(PaniniVM().evalScript(source).all { it is ExecutionResult.Success })
    }

    @Test fun `a tatah chain inside the consequent before anyatha`() {
        fun program(start: String) = "$start अङ्क + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "यदि अङ्क + सुँ दशन् + भ्यस् न्यून + सुँ असँ + लट् + तिप् तर्हि अङ्क + अम् एक + अम् च युज् + णिच् + लोट् + सिप् " +
            "ततः फल + अम् द्वि + औट् च गुण + णिच् + लोट् + सिप् ततः फल + अम् अङ्क + ङि स्था + णिच् + लोट् + सिप् " +
            "अन्यथा शून्य + अम् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।\nअङ्क + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        for ((start, expected, name) in listOf(Triple("पञ्चन् + शस्", 12L, "GapChainThen"), Triple("एक + दशन् + शस्", 0L, "GapChainElse"))) {
            val source = program(start)
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(compiled(source, name)["अङ्क"]).value, start)
            assertEquals(if (expected == 0L) "शून्य" else "द्वादश", interpreted(source), start)
        }
        val spoken = dev.panini.execution.PvmUktiSadhaka().sadhayaScript(program("पञ्चन् + शस्"))
        assertTrue("तर्हि अङ्कम् एकं च योजय ततः फलं द्वे च गुणय ततः फलम् अङ्के स्थापय अन्यथा" in spoken, spoken)
    }

    @Test fun `a top-level pronoun updates the same variable in both backends`() {
        val source = "पञ्चन् + शस् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "तद् + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् तद् + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(compiled(source, "GapPronoun")["सङ्ख्या"]).value)
        assertEquals("षट्", interpreted(source))
    }
}
