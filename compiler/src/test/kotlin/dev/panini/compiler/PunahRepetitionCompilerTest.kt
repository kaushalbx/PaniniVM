package dev.panini.compiler

import dev.panini.execution.SanskritValue
import dev.panini.execution.ExecutionError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PunahRepetitionCompilerTest {
    @Test
    fun `compiled punah performs a compatible connected action again once`() {
        val source = """
            शून्य + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
            गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
            पुनः गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledPunahRepetition")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(2L, (values.getValue("गणना") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `compiled punah requires a compatible completed antecedent`() {
        val generated = BytecodeCompiler.compileAndLoad(
            "पुनः एक + अम् मुद्र् + णिच् + लोट् + सिप् ।", "CompiledPunahMissing",
        )
        val wrapped = assertFailsWith<java.lang.reflect.InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }
        assertEquals(ExecutionError.INVALID_VALUE, (wrapped.cause as CompiledPaniniExecutionException).error)
    }

    @Test
    fun `compiled punah remains inside its selected conditional branch`() {
        fun execute(right: String, suffix: String): Map<String, SanskritValue> {
            val source = """
                शून्य + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
                यदि एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
                तर्हि पुनः एक + अम् गणना + ङि स्था + णिच् + लोट् + सिप्
                अन्यथा नवन् + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent()
            val generated = BytecodeCompiler.compileAndLoad(source, "CompiledPunahBranch$suffix")
            @Suppress("UNCHECKED_CAST")
            return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        }

        assertEquals(1L, (execute("एक", "True").getValue("गणना") as SanskritValue.Sankhya).value)
        assertEquals(9L, (execute("द्वि", "False").getValue("गणना") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `compiled punah does not multiply a bounded loop count`() {
        val prefix = """
            शून्य + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
            गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
            द्वि + कृत्वसुच् यावत् एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप्
            तावत् पुनः गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val third = BytecodeCompiler.compileAndLoad(
            prefix + "\nयुज् + ल्युट् + ङस् तृतीय + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            "CompiledPunahLoopThird",
        )
        @Suppress("UNCHECKED_CAST")
        val values = third.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals("एकम्", values.getValue("LastResult").toDisplayText())

        val fourth = BytecodeCompiler.compileAndLoad(
            prefix + "\nयुज् + ल्युट् + ङस् चतुर् + डट् + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            "CompiledPunahLoopFourth",
        )
        val wrapped = assertFailsWith<java.lang.reflect.InvocationTargetException> {
            fourth.getMethod("execute").invoke(null)
        }
        assertEquals(ExecutionError.INVALID_VALUE, (wrapped.cause as CompiledPaniniExecutionException).error)
    }
}
