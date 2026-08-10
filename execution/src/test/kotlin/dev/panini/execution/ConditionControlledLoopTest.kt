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
            त्रि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(3, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" }, results.toString())
        assertEquals(1, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "समाप्तम्" }, results.toString())
    }

    @Test
    fun `true body result terminates a negated phala loop immediately`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            वारः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            द्वि + अम् एक + अम् च विद् + लोट् + सिप् ॥
            पञ्च + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(1, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" }, results.toString())
        assertTrue(results.none { it is ExecutionResult.Success && it.value == "समाप्तम्" }, results.toString())
        assertTrue(results.none { it is ExecutionResult.Failure })
    }
}
