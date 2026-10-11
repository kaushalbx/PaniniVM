package dev.panini.execution

import java.nio.file.Path
import org.junit.jupiter.api.io.TempDir
import kotlin.test.Test
import kotlin.test.assertFalse

class PvmPrakriyaConditionalPipelineTest {
    @TempDir lateinit var projectDirectory: Path
    @Test
    fun `bare conditional value pipes to an ordinary action inside a prakriya`() {
        val source = """
            निर्णय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् तर्हि लघु अन्यथा गुरु ततः मुद्र् + णिच् + लोट् + सिप् ॥
            पञ्चन् + कृत्वसुच् यावत् फल + सुँ न तावत् निर्णय + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val file = projectDirectory.resolve("main.pvm").toFile()
        try {
            file.writeText(source)
            val results = PaniniVM().evalFile(file)
            assertFalse(results.any { it is ExecutionResult.Failure }, results.joinToString())
        } finally {
            file.delete()
        }
    }
}
