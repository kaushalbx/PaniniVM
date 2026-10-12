package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class CausativeVikaranaTest {
    @Test fun `explicit causative shap preserves arithmetic semantics across backends`() {
        for ((index, affix) in listOf("", "शप् + ").withIndex()) {
            val source = "एक + अम् द्वि + अम् च युज् + णिच् + ${affix}लोट् + सिप् ।"
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
            assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "CausativeShap$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }
}
