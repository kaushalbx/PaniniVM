package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class OrdinalMemberExtractionTest {
    private val declaration = "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।"
    private fun compiled(source: String, name: String): Map<String, SanskritValue> {
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
    }

    @Test fun `substantival ordinal selects one based members independently of word order`() {
        for ((ordinal, expected) in listOf("प्रथम" to 1L, "द्वितीय" to 2L, "तृतीय" to 3L, "द्वि + तीय" to 2L)) {
            for (phrase in listOf("सूची + ङस् $ordinal + अम्", "$ordinal + अम् सूची + ङस्")) {
                val source = "$declaration\n$phrase उद् + हृ + लोट् + सिप् ।"
                val results = PaniniVM().evalScript(source)
                val interpreted = assertIs<ExecutionResult.Success>(results.last(), "$ordinal $results")
                assertEquals(expected, assertIs<SanskritValue.Sankhya>(interpreted.typedValue).value)
                val values = compiled(source, "Member${ordinal.hashCode().toUInt()}${phrase.hashCode().toUInt()}")
                assertEquals(interpreted.typedValue, values["LastResult"])
                assertEquals(3, assertIs<SanskritValue.Suchi>(values["सूची"]).items.size)
            }
        }
    }

    @Test fun `ordinal selection survives prior action lowering`() {
        val source = "$declaration\nसूची + ङस् द्वितीय + अम् उद् + हृ + ल्यप् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(results.last(), results.toString()).value)
        assertEquals("द्वि", compiled(source, "PriorOrdinalMember").getValue("LastResult").toDisplayText())
    }

    @Test fun `out of bounds and ambiguous selectors fail in both backends`() {
        for (selector in listOf("चतुर् + डट् + अम्", "द्वितीय + अम् अन्तिम + अम्", "द्वितीय + शस्",
            "द्वितीय + तरप् + अम्")) {
            val source = "$declaration\nसूची + ङस् $selector उद् + हृ + लोट् + सिप् ।"
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), selector)
            assertFails(selector) { compiled(source, "BadOrdinal${selector.hashCode().toUInt()}") }
        }
    }
}
