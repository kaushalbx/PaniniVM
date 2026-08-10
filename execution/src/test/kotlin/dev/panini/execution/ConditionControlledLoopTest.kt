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
            वारः + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            त्रि + कृत्वः यावत् विजय + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(3, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" }, results.toString())
    }

    @Test
    fun `loop break makes a negated victory condition false immediately`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            वि + स्था + लोट् + सिप् ॥
            पञ्च + कृत्वः यावत् विजय + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )
        val breaks = results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.controlSignal == ExecutionControlSignal.BREAK_LOOP }

        assertEquals(1, breaks.size, results.toString())
        assertTrue(results.none { it is ExecutionResult.Failure })
    }
}
