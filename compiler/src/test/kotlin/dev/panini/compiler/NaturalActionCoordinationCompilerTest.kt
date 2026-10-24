package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NaturalActionCoordinationCompilerTest {
    @Test
    fun `unprefixed stha outside a loop compiles as an ordinary action`() {
        val generated = BytecodeCompiler.compileAndLoad("स्था + लोट् + सिप् ।", "OrdinaryStha")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertTrue(values.getValue("LastResult") is SanskritValue.Shabda)
    }

    @Test
    fun `natural loop stop and prior action have interpreter compiler parity`() {
        val source = """
            त्रि + कृत्वसुच् एक + अम् मुद्र् + णिच् + क्त्वा स्था + लोट् + सिप् ।
            द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        assertEquals(listOf("एक", "द्वि"), interpreted.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })

        val generated = BytecodeCompiler.compileAndLoad(source, "NaturalLoopStopParity")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals("द्वि", values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `cha coordinated actions compile structurally`() {
        val source = """
            यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप्
            तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप्
            च द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "FiniteActionCha")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals("द्वि", values.getValue("LastResult").toDisplayText())
    }
}
