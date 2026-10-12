package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class NaturalCollectionParameterParityTest {
    @Test
    fun `member sum depends on grammatical relation not parameter spelling`() {
        for ((index, name) in listOf("मान", "समवाय", "सङ्ग्रह").withIndex()) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
                $name + सुँ सूची + सुँ इति मान + सुँ ।
                $name + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ॥
                क्रम + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent()
            val initial = mapOf("क्रम" to SanskritValue.Suchi(listOf(1L, 2L, 3L).map {
                SanskritValue.Sankhya(it, "host") }))
            val results = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            assertEquals(6L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "NaturalCollectionParameter$index")
            assertEquals(result.typedValue,
                (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
        }
    }
}
