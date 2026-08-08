package dev.panini.execution

import dev.panini.vyakaranam.ast.PipelineStage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PurvaparaPipelineTest {

    private val vm = PaniniVM()

    @Test
    fun testPurvaparaPipelineExecution() {
        val script = """
            # Domain declaration
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।

            # Kriyā 1 (योगः / Addition): 5 + 2 = 7 (सप्त)
            गणित + ङस् गुण् + ल्युट् + सुँ इति संज्ञा + सुँ ।
            पञ्च + अम् पञ्च + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Kriyā 2 (अन्तरम् / Subtraction): 10 - 2 = 8 (अष्ट)
            गणित + ङस् रन्ध्र + ल्युट् + सुँ इति संज्ञा + सुँ ।
            दश + अम् द्वि + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            # Compound Pipeline (6.1.84 एकः पूर्वपरयोः):
            # Stage 1 (पूर्व): गुण् (5 + 2 = 7)
            # Stage 2 (पर): रन्ध्र (7 - 2 = 5)
            पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + ङस् गणित + ङस् रन्ध्र + ल्युट् + ङस् पूर्व + पर + ङस् एका + सुँ कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful execution of Purvapara pipeline, but got: $results")
        assertEquals("अष्ट", success.last().value, "Expected pipeline result 8 (अष्ट) after Stage 1 (5 * 2 = 10) and Stage 2 (10 - 2 = 8)")
    }

    @Test
    fun `pipeline directive compiles to structured stages`() {
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + ङस् गणित + ङस् रन्ध्र + ल्युट् + ङस् पूर्व + पर + ङस् एका + सुँ कृ + लोट् + सिप् ।"
        val plan = requireNotNull(PurvaparaPipelineCompiler.compile(source))

        assertEquals(listOf("पञ्च", "द्वि"), plan.arguments)
        assertEquals(
            listOf(
                PipelineStage("गणित + ङस् गुण् + ल्युट्", "गणित", "गुण् + ल्युट्"),
                PipelineStage("गणित + ङस् रन्ध्र + ल्युट्", "गणित", "रन्ध्र + ल्युट्"),
            ),
            plan.stages,
        )
    }
}
