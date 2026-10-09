package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DiceOppositeFaceTest {
    private val source = File("examples/algorithms/dice_opposite_face.pvm").readText()
    private val interactiveSource = File("projects/dice-opposite-face/dice_opposite_face.pvm").readText()

    private fun executeInteractive(input: String): List<ExecutionResult> {
        val vm = PaniniVM()
        vm.registerExternalCapability(ExecutionEffect.READ_RESOURCE) { payload, _ ->
            val request = assertNotNull(InputRequest.decode(payload))
            assertEquals(InputValueType.NUMBER, request.type)
            assertEquals(1L, request.minimum)
            assertEquals(6L, request.maximum)
            input
        }
        return vm.evalScript(interactiveSource)
    }

    @Test
    fun `interactive project handles every face with bounded numeric input`() {
        for (face in 1L..6L) {
            val results = executeInteractive(face.toString())
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals(renderSankhyaResult(7L - face), assertIs<ExecutionResult.Success>(results.last()).value)
        }
    }

    @Test
    fun `interactive project rejects invalid faces without printing an answer`() {
        for (input in listOf("0", "7", "-1", "1.5", "face")) {
            val results = executeInteractive(input)
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            assertTrue(results.none { it is ExecutionResult.Success && it.outputKind == OutputKind.CONSOLE }, results.toString())
        }
    }

    @Test
    fun `all six faces give the standard opposite face`() {
        val faces = listOf("एक + अम्", "द्वि + औट्", "त्रि + शस्", "चतुर् + शस्", "पञ्चन् + शस्", "षष् + शस्")
        for ((index, face) in faces.withIndex()) {
            val results = PaniniVM().evalScript("सप्तन् + शस् $face च वि + युज् + णिच् + ल्यप् " +
                "फल + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals(renderSankhyaResult(6L - index), assertIs<ExecutionResult.Success>(results.last()).value)
        }
    }

    @Test
    fun `random example prints a face and its opposite without requesting input`() {
        repeat(20) {
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            val successes = results.filterIsInstance<ExecutionResult.Success>()
            val printed = successes.filter { it.outputKind == OutputKind.CONSOLE }
            assertEquals(2, printed.size, results.toString())
            val face = (1L..6L).single { renderSankhyaResult(it) == printed.first().value }
            assertEquals(listOf(renderSankhyaResult(face), renderSankhyaResult(7L - face)), printed.map { it.value })
        }
    }
}
