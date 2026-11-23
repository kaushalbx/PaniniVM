package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.OutputKind
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals

/** A position held in a name (अन्तस्थान) places a member just as a written ordinal does. */
class NamedPositionInsertionTest {
    private val source = """
अन्तनिक्षेप + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
सूचि + सुँ नाम मान + सुँ सूची + सुँ असँ + लट् + तिप् ।
अङ्क + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
परिणाम + सुँ सूची + सुँ असँ + लट् + तिप् ।
सूचि + अम् गण् + णिच् + लोट् + सिप्
    ततः गण् + ल्युट् + ङस् फल + अम् एक + अम् च युज् + णिच् + लोट् + सिप्
    ततः युज् + ल्युट् + ङस् फल + अम् अन्तस्थान + ङि स्था + णिच् + लोट् + सिप् ।
सूचि + ङस् अन्तस्थान + ङि अङ्क + अम् नि + क्षिप् + लोट् + सिप् ॥
पञ्चन् + जस् एक + सुँ त्रि + जस् इति दत्त + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
दत्त + अम् नवन् + शस् च अन्तनिक्षेप + टा डुकृञ् + उ + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
""".trimIndent()

    @Test fun `a procedure inserts at a position it computed, in both backends`() {
        val shown = PaniniVM().evalScript(source).filterIsInstance<ExecutionResult.Success>()
            .last { it.outputKind == OutputKind.CONSOLE }.value
        assertEquals("[पञ्चन्, एक, त्रि, नवन्]", shown)
        @Suppress("UNCHECKED_CAST")
        val values = BytecodeCompiler.compileAndLoad(source, "NamedPositionInsertion").getMethod("execute")
            .invoke(null) as Map<String, SanskritValue>
        assertEquals(shown, values.getValue("LastResult").toDisplayText())
    }
}
