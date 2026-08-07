package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApavadaOverrideTest {

    private val vm = PaniniVM()

    @Test
    fun testDirectEvalSingleHeaderLine() {
        val result = vm.eval("गाणित + मतुप् + ङस् गुण् + ल्युट् + सुँ इति अप + वद् + घञ् + सुँ इति संज्ञा + सुँ ।")
        assertTrue(result is ExecutionResult.Success, "Expected successful execution result for standalone header line: $result")
    }

    @Test
    fun testApavadaSutraOverrideWithPureMorphemes() {
        val script = """
            # Parent class definition (गणित - General Utsarga method: Addition via युज् + णिच् + लोट् + सिप्)
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।
            गणित + ङस् गुण् + ल्युट् + सुँ इति संज्ञा + सुँ ।
            प्रथ् + अमच् + अम् द्वि + तीय + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Child class override definition (गाणित - Pure Morpheme Apavāda method: इति अप + वद् + घञ् + सुँ, Subtraction via वि + युज् + णिच् + लोट् + सिप्)
            # Sūtra 1.4.2 विप्रतिषेधे परम् -> अनेन उत्सर्गस्य बाधः क्रियते
            गाणित + मतुप् + ङस् गुण् + ल्युट् + सुँ इति अप + वद् + घञ् + सुँ इति संज्ञा + सुँ ।
            प्रथ् + अमच् + अम् द्वि + तीय + अम् च वि + युज् + लोट् + सिप् ॥

            # Execute Apavāda method on child instance 'गाणित': 5 - 2 = 3 (त्रीणि) instead of 5 + 2 = 7 (सप्त)
            पञ्च + अम् द्वि + अम् च गाणित + मतुप् + ङस् गुण् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful execution result")
        assertEquals("त्रीणि", success.last().value, "Expected 5 - 2 = 3 (त्रीणि) via Apavāda exception method override")
    }
}
