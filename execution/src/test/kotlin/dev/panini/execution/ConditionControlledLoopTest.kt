package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConditionControlledLoopTest {
    @Test
    fun `bounded yavat loop runs until its Sanskrit upper bound`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            वारः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            त्रि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(3, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" }, results.toString())
        assertEquals(1, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "समाप्तम्" }, results.toString())
        val completion = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.loopOutcome != null }
        assertEquals(ExecutionResult.LoopOutcome.SAMAPTI, completion.loopOutcome)
        assertEquals(3, completion.iterationCount)
        assertTrue(results.any { it is ExecutionResult.Success && it.value == "समाप्ति" })
    }

    @Test
    fun `true body result terminates a negated phala loop immediately`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            वारः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            द्वि + अम् एक + अम् च विद् + लोट् + सिप् ॥
            पञ्च + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(1, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" }, results.toString())
        assertTrue(results.none { it is ExecutionResult.Success && it.value == "समाप्तम्" }, results.toString())
        val completion = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.loopOutcome != null }
        assertEquals(ExecutionResult.LoopOutcome.VIJAYA, completion.loopOutcome)
        assertEquals(1, completion.iterationCount)
        assertTrue(results.any { it is ExecutionResult.Success && it.value == "विजय" })
        assertTrue(results.none { it is ExecutionResult.Failure })
    }

    @Test
    fun `loop publishes a structured outcome for genitive access without assignment`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परिणाम + मतुप् + ङस् अवस्था + अम् ।
            परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् ।
            """.trimIndent(),
        )

        val values = results.filterIsInstance<ExecutionResult.Success>().map { it.value }
        assertEquals(listOf("समाप्ति", "द्वि"), values.takeLast(2), results.toString())
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }
}
