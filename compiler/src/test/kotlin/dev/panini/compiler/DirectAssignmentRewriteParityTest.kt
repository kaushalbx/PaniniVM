package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class DirectAssignmentRewriteParityTest {
    @Test fun `compiler rejects further derived action noun instead of borrowing base history`() {
        for ((index, affix) in listOf("मतुप्", "तरप्").withIndex()) {
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः " +
                "युज् + ल्युट् + $affix + ङस् फल + अम् उत्तर + ङे दा + लोट् + सिप् ।"
            assertFailsWith<IllegalArgumentException> {
                BytecodeCompiler.compileAndLoad(source, "DerivedActionHistory$index")
            }
        }
    }

    @Test fun `compiler rejects competing genitive actions for one result`() {
        val source = "युज् + ल्युट् + ङस् भज् + ल्युट् + ङस् फल + अम् उत्तर + ङे दा + लोट् + सिप् ।"
        assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileAndLoad(source, "AmbiguousNamedActionResult")
        }
    }

    @Test fun `case based assignment rewrite preserves interpreter and compiled results`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः " +
            "फल + अम् उत्तर + ङे युज् + ल्युट् + ङस् दा + लोट् + सिप् ।"
        val suggestion = DirectResultAssignment.suggestions(source).single()
        val rewritten = source.replaceRange(suggestion.offset, suggestion.offset + suggestion.length, suggestion.replacement)
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        val expected = assertIs<ExecutionResult.Success>(interpreted.last()).typedValue
        for ((index, program) in listOf(source, rewritten).withIndex()) {
            val generated = BytecodeCompiler.compileAndLoad(program, "AssignmentRewriteParity$index")
            val values = generated.getMethod("execute", Map::class.java).invoke(null, emptyMap<String, SanskritValue>()) as Map<*, *>
            assertEquals(expected, values["LastResult"])
            assertEquals(expected, values["उत्तर"])
        }
    }
}
