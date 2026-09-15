package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse

class PvmProcedureConditionalPipelineTest {
    @Test
    fun `bare conditional value pipes to an ordinary action inside a procedure`() {
        val source = """
            निर्णय + ल्युट् + सुँ ।
            यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् तर्हि लघु अन्यथा गुरु ततः मुद्र् + णिच् + लोट् + सिप् ॥
            पञ्चन् + कृत्वसुच् यावत् फल + सुँ न तावत् निर्णय + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val file = File.createTempFile("pvm-procedure-conditional-", ".pvm")
        try {
            file.writeText(source)
            val results = PaniniVM().evalFile(file)
            assertFalse(results.any { it is ExecutionResult.Failure }, results.joinToString())
        } finally {
            file.delete()
        }
    }
}
