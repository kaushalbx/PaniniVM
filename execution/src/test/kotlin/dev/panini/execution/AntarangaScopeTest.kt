package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AntarangaScopeTest {

    private val vm = PaniniVM()

    @Test
    fun testAntarangaDetection() {
        val line = "पञ्च + अम् द्वि + अम् च गुण + मतुप् + ङस् अन्तर + अम् अङ्ग + ङसिँ वृध् + ल्युट् + टा कृ + लोट् + सिप् ।"
        assertTrue(AntarangaScopeEngine.detectAntaranga(line))
        assertEquals(
            "पञ्च + अम् द्वि + अम् च गुण + मतुप् + ङस् वृध् + ल्युट् + टा कृ + लोट् + सिप् ।",
            AntarangaScopeEngine.stripAntarangaDirective(line)
        )
    }

    @Test
    fun testAntarangaDynamicScopeExecution() {
        val script = """
            # Define parent class method
            गणित + सुँ इति अधिकार + सुँ ।
            गणित + ङस् वृध् + ल्युट् + सुँ ।
            प्रथ् + अमच् + अम् द्वि + तीय + अम् च युज् + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् फल + अम् ॥

            # Invoke with Antaranga dynamic scope directive: अन्तर + अम् अङ्ग + ङसिँ
            पञ्च + अम् द्वि + अम् च गणित + ङस् अन्तर + अम् अङ्ग + ङसिँ वृध् + ल्युट् + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् फल + अम् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful execution result")
        assertEquals("सप्त", success.last().value, "Expected 5 + 2 = 7 (सप्त)")
    }
}
