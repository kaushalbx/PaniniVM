package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class DerivedResultReferenceTest {
    @Test fun `derived phala does not print the preceding number`() {
        val source = """
            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
            फल + त्व + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertIs<SanskritValue.Shabda>(interpreted.typedValue)
        val generated = BytecodeCompiler.compileAndLoad(source, "DerivedPhalaIdentity")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(interpreted.typedValue, values["LastResult"])
    }
}
