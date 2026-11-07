package dev.panini.compiler

import dev.panini.execution.SanskritValue
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** The compiled backend takes roles from kārakas too: word order and voice change nothing it does. */
class KarakaCompiledOrderTest {
    private var counter = 0
    private fun run(source: String): Pair<String, Map<String, SanskritValue>> {
        val type = BytecodeCompiler.compileAndLoad(source, "KarakaOrder${counter++}")
        val original = System.out; val captured = ByteArrayOutputStream()
        System.setOut(PrintStream(captured, true, "UTF-8"))
        try {
            @Suppress("UNCHECKED_CAST")
            return captured.toString("UTF-8") to (type.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
                .let { values -> captured.toString("UTF-8") to values }.second
        } finally { System.setOut(original) }
    }

    @Test fun `passive causative and active compile to the same action in any order`() {
        val runs = listOf("पञ्चन् + शस् मुद्र् + णिच् + लोट् + सिप् ।", "मुद्र् + णिच् + लोट् + सिप् पञ्चन् + शस् ।",
            "पञ्चन् + जस् राम + टा मुद्र् + यक् + लोट् + झ ।", "राम + टा पञ्चन् + जस् मुद्र् + यक् + लोट् + झ ।",
            "गुरु + सुँ शिष्य + टा पञ्चन् + शस् मुद्र् + णिच् + लोट् + तिप् ।", "पञ्चन् + शस् शिष्य + टा गुरु + सुँ मुद्र् + णिच् + लोट् + तिप् ।")
            .associateWith { source -> runCatching { run(source).second.values.map { it.toString() } } }
        runs.forEach { (source, result) -> assertTrue(result.isSuccess, source + " " + result.exceptionOrNull()) }
        assertEquals(1, runs.values.map { it.getOrThrow() }.toSet().size, runs.toString())
    }
}
