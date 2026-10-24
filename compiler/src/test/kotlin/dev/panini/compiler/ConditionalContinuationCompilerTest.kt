package dev.panini.compiler

import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals

class ConditionalContinuationCompilerTest {
    @Test fun `compiled tada sentences share the nearest conditional guard`() {
        fun execute(right: String, suffix: String): Map<String, SanskritValue> {
            val source = """
                शून्य + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
                यदि एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
                तर्हि एक + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
                तदा द्वि + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent()
            val generated = BytecodeCompiler.compileAndLoad(source, "TadaContinuation$suffix")
            @Suppress("UNCHECKED_CAST")
            return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        }

        assertEquals(2L, (execute("एक", "True").getValue("परिणाम") as SanskritValue.Sankhya).value)
        assertEquals(0L, (execute("द्वि", "False").getValue("परिणाम") as SanskritValue.Sankhya).value)
    }

    @Test fun `compiled alternate follows all tada consequents`() {
        fun execute(right: String, suffix: String): Map<String, SanskritValue> {
            val source = """
                शून्य + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
                यदि एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
                तर्हि एक + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
                तदा द्वि + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
                अन्यथा त्रि + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent()
            val generated = BytecodeCompiler.compileAndLoad(source, "TadaAlternate$suffix")
            @Suppress("UNCHECKED_CAST")
            return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        }

        assertEquals(2L, (execute("एक", "True").getValue("परिणाम") as SanskritValue.Sankhya).value)
        assertEquals(3L, (execute("द्वि", "False").getValue("परिणाम") as SanskritValue.Sankhya).value)
    }

    @Test fun `compiled yada tada observes once and runs all temporal continuations`() {
        fun execute(right: String, suffix: String): Map<String, SanskritValue> {
            val source = """
                शून्य + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
                यदा एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
                तदा एक + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
                तदा द्वि + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent()
            val generated = BytecodeCompiler.compileAndLoad(source, "YadaTada$suffix")
            @Suppress("UNCHECKED_CAST")
            return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        }

        assertEquals(2L, (execute("एक", "True").getValue("परिणाम") as SanskritValue.Sankhya).value)
        assertEquals(0L, (execute("द्वि", "False").getValue("परिणाम") as SanskritValue.Sankhya).value)
    }
}
