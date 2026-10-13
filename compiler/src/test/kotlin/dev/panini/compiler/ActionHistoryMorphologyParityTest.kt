package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ActionHistoryMorphologyParityTest {
    private fun <T> permutations(values: List<T>): List<List<T>> =
        if (values.isEmpty()) listOf(emptyList()) else values.indices.flatMap { index ->
            permutations(values.filterIndexed { other, _ -> other != index }).map { listOf(values[index]) + it }
        }

    @Test fun `history result ordering and ordering-named destinations retain distinct roles`() {
        val phrases = permutations(listOf("युज् + ल्युट् + ङस्", "प्रथम + अम्", "फल + अम्", "पूर्व + ङे"))
        assertEquals(24, phrases.size)
        for ((index, words) in phrases.withIndex()) {
            val phrase = words.joinToString(" ")
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । " +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् । " +
                "$phrase दा + लोट् + सिप् । " +
                "पूर्व + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last(), phrase)
            assertEquals("त्रीणि", result.value)
            val generated = BytecodeCompiler.compileAndLoad(source, "HistoryResultDestination$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(3L, assertIs<SanskritValue.Sankhya>(values["पूर्व"]).value)
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `postposed ordinal selects the same participant history in both backends`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । " +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् । " +
            "युज् + ल्युट् + ङस् कर्मन् + अम् प्रथम + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        val preposed = source.replace("कर्मन् + अम् प्रथम + अम्", "प्रथम + अम् कर्मन् + अम्")
        assertEquals(assertIs<ExecutionResult.Success>(PaniniVM().evalScript(preposed).last()).typedValue,
            result.typedValue)
        val latest = source.replace("कर्मन् + अम् प्रथम + अम्", "कर्मन् + अम्")
        assertNotEquals(assertIs<ExecutionResult.Success>(PaniniVM().evalScript(latest).last()).typedValue,
            result.typedValue)
        val generated = BytecodeCompiler.compileAndLoad(source, "PostposedParticipantOrdinal")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
    }

    @Test fun `participant history assignment is invariant across unambiguous nominal word orders`() {
        val phrases = permutations(listOf("युज् + ल्युट् + ङस्", "प्रथम + अम्", "कर्मन् + अम्", "पूर्व + ङे"))
        for ((index, words) in phrases.withIndex()) {
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । " +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् । " +
                "${words.joinToString(" ")} दा + लोट् + सिप् ।"
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last(), words.toString())
            val generated = BytecodeCompiler.compileAndLoad(source, "HistoryParticipantDestination$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            val destination = assertIs<SanskritValue.Suchi>(values["पूर्व"])
            assertEquals(listOf(1L, 2L), destination.items.map { assertIs<SanskritValue.Sankhya>(it).value })
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `unrelated krt meanings cannot read an available completed actions result or participants`() {
        for ((index, ending) in listOf("क्त", "क्तवतुँ", "तृच्", "ण्वुल्").withIndex()) {
            for (referent in listOf("फल", "कर्मन्")) {
                val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । " +
                    "युज् + $ending + ङस् $referent + अम् मुद्र् + णिच् + लोट् + सिप् ।"
                val results = PaniniVM().evalScript(source)
                assertIs<ExecutionResult.Failure>(results.last(), "$ending $referent: $results")
                assertFails("$ending $referent must not alias completed action history") {
                    BytecodeCompiler.compileAndLoad(source, "InvalidHistoryKrt${index}${if (referent == "फल") "Result" else "Participant"}")
                }
            }
        }
    }
}
