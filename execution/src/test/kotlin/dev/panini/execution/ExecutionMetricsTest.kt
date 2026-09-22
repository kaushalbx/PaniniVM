package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExecutionMetricsTest {
    @Test
    fun `metrics expose AST-native script work without render reparse activity`() {
        val metrics = ExecutionMetrics()
        val vm = PaniniVM(executionMetrics = metrics)
        vm.evalScript(
            """
            योग + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            योग + अम् डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val snapshot = metrics.snapshot()
        assertEquals(2, snapshot.parsedSentences)
        assertTrue(snapshot.executedAstNodes > 0)
        assertEquals(1, snapshot.prakriyaCalls)
        assertEquals(0, snapshot.renderedOrReparsedSources)
    }

    @Test
    fun `file evaluation records parsed file work`() {
        val directory = kotlin.io.path.createTempDirectory("pvm-metrics-").toFile()
        val file = File(directory, "main.pvm")
        try {
            file.writeText("एक + अम् मुद्र् + लोट् + सिप् ।")
            val metrics = ExecutionMetrics()
            PaniniVM(executionMetrics = metrics).evalFile(file)

            assertEquals(1, metrics.snapshot().parsedFiles)
        } finally {
            file.delete()
            directory.delete()
        }
    }
}
