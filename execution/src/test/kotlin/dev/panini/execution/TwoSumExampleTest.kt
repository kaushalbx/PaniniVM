package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TwoSumExampleTest {
    private fun printed(source: String): List<String> {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        return results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE && it.value != "विजयः" }
            .map { it.value }
    }

    @Test
    fun `two sum example derives and prints distinct one-based positions`() {
        assertEquals(
            listOf("एकम् चत्वारि"),
            printed(File("examples/algorithms/two_sum.pvm").readText()),
        )
    }

    @Test
    fun `two sum uses two distinct positions for duplicate values`() {
        val source = File("examples/algorithms/two_sum.pvm").readText()
            .replace(
                "एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च",
                "त्रि + ङस् त्रि + ओस् चतुर् + आम् पञ्चन् + आम् च",
            )
            .replace(
                "पञ्चन् + अम् लक्ष्य + ङि स्था + णिच् + लोट् + सिप्",
                "षष् + अम् लक्ष्य + ङि स्था + णिच् + लोट् + सिप्",
            )

        assertEquals(listOf("एकम् द्वे"), printed(source))
    }
}
