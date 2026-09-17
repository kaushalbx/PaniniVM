package dev.panini.compiler

import dev.panini.execution.SanskritValue
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class NumberGuessingCompilerTest {
    @Test
    fun `number guessing example compiles and executes through native control flow`() {
        val source = File("projects/number-guessing-game/number_guessing_game.pvm").readText()
        val bytes = BytecodeCompiler.compile(source, "CompiledNumberGuessingGame")
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledNumberGuessingGame", bytes)
        val originalInput = System.`in`

        try {
            System.setIn(ByteArrayInputStream("1\n2\n3\n4\n5\n".toByteArray()))
            @Suppress("UNCHECKED_CAST")
            val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
            val secret = values.getValue("रहस्य") as SanskritValue.Sankhya
            assertTrue(secret.value in 1L..10L)
        } finally {
            System.setIn(originalInput)
        }
    }
}
