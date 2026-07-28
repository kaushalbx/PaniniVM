package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.SankhyaResultRenderer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class LoopOperationTest {
    @Test
    fun `VrtDhatu is registered in DhatuPatha`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrt = DhatuPatha.all.firstOrNull { it.upadesha == "वृताँ" }
        assertNotNull(vrt, "VrtDhatu must be registered in DhatuPatha.")
        assertEquals("वर्तने", vrt.artha)
        assertEquals("to turn, to exist, to repeat/loop", vrt.arthaEnglish)
    }

    @Test
    fun `VrtDhatu executes LoopAction to perform repeated addition`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrt = DhatuPatha.all.first { it.upadesha == "वृताँ" }
        val loopOp = vrt.operations.first()

        // We want to loop 5 times, running the addition operation "सङ्ख्यायोजनम्"
        // In each iteration, we add: loop_result + loop_index
        // i=1: 0 + 1 = 1
        // i=2: 1 + 2 = 3
        // i=3: 3 + 3 = 6
        // i=4: 6 + 4 = 10
        // i=5: 10 + 5 = 15
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.sankhya(5L, "पञ्च"), // loop count
                Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्यायोजनम्"), // target operation
                Karaka.SAMPRADANA to ExecutionExpression.Coordination( // target input mapping to target's KARMAN
                    ExecutionExpression.Reference("loop_result"),
                    ExecutionExpression.Reference("loop_index")
                )
            )
        )

        // Setup a mock renderer for Sanskrit numbers for the test
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                5L -> "पञ्च"
                10L -> "दश"
                15L -> "पञ्चदश"
                else -> value.toString()
            }
        }

        val result = loopOp.action.execute(context, loopOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("पञ्चदश", result.value) // 15 in Sanskrit

        val typed = result.typedValue
        assertIs<SanskritValue.Sankhya>(typed)
        assertEquals(15L, typed.value)
    }
}
