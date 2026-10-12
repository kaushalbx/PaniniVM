package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class SequenceResultPayloadTest {
    @Test fun `an empty stage does not forward a stale earlier value to the following stage`() {
        val invocation = assertIs<Invocation>(PaniniParser().parse("मुद्र् + णिच् + लोट् + सिप् ।").body)
        val sequence = Sequence("", listOf(invocation.copy(implicitValue = "एक"), invocation, invocation),
            listOf("ततः", "ततः"))
        var ordinaryCalls = 0
        val received = mutableListOf<SanskritValue?>()
        PvmSequenceExecutor().execute(sequence, ExecutionScope(), PrakriyaRegistry(), null,
            evaluateWhole = { error("Expected staged execution") },
            executeNode = {
                ordinaryCalls++
                listOf(ExecutionResult.Success(operation = "ordinary", value = "एक",
                    typedValue = SanskritValue.Sankhya(1, "एक")))
            },
            executePipedInvocation = { _, _, binding ->
                received += binding.value
                emptyList()
            })
        assertEquals(1, received.size)
        assertEquals(2, ordinaryCalls)
    }

    @Test fun `turn conversion does not attach an earlier typed payload or truth to a later display result`() {
        val phala = Phala.Siddha(
            values = linkedMapOf("first" to "सत्य", "last" to "राम"),
            typedValues = mapOf("first" to SanskritValue.Satya(true)),
            samjnas = emptyMap(), trace = emptyList(),
        )
        val result = assertIs<ExecutionResult.Success>(phala.toExecutionResult("test"))
        assertEquals("राम", result.value)
        assertNull(result.typedValue)
        assertNull(result.conditionValue)
    }

    @Test fun `turn conversion selects typed payload by final result identity not typed map order`() {
        val finalValue = SanskritValue.Sankhya(7, "सप्तन्")
        val phala = Phala.Siddha(
            values = linkedMapOf("first" to "सत्य", "last" to "सप्तन्"),
            typedValues = linkedMapOf("last" to finalValue, "first" to SanskritValue.Satya(true)),
            samjnas = emptyMap(), trace = emptyList(),
        )
        val result = assertIs<ExecutionResult.Success>(phala.toExecutionResult("test"))
        assertEquals(finalValue, result.typedValue)
        assertNull(result.conditionValue)
    }

    @Test fun `a display-only success replaces the preceding pipeline value`() {
        val invocation = assertIs<Invocation>(PaniniParser().parse("मुद्र् + णिच् + लोट् + सिप् ।").body)
        val sequence = Sequence("", listOf(invocation.copy(implicitValue = "एक"), invocation, invocation),
            listOf("ततः", "ततः"))
        val received = mutableListOf<SanskritValue?>()
        PvmSequenceExecutor().execute(sequence, ExecutionScope(), PrakriyaRegistry(), null,
            evaluateWhole = { error("Expected staged execution") },
            executeNode = { listOf(ExecutionResult.Success(operation = "first", value = "एक",
                typedValue = SanskritValue.Sankhya(1L, "एक"))) },
            executePipedInvocation = { _, _, binding ->
                received += binding.value
                listOf(ExecutionResult.Success(operation = "stage", value = "राम"))
            })
        assertEquals(listOf<SanskritValue?>(SanskritValue.Sankhya(1L, "एक"), SanskritValue.Shabda("राम")), received)
    }
}
