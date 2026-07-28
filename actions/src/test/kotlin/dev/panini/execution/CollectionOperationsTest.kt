package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.DhatuPathaRegistration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class CollectionOperationsTest {
    @Test
    fun `HrDhatu executes SanskritListPopAction to pop last element`() {
        DhatuPathaRegistration.ensureRegistered()
        val hr = DhatuPatha.all.first { it.upadesha == "हृञ्" }
        // Find list pop operation
        val popOp = hr.operations.first { it.name == "सूच्युद्धरणम्" || it.action.name == "सूच्युद्धरणम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि")
                )
            )
        )

        val result = popOp.action.execute(context, popOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("त्रि", result.value) // Popped last element
        assertIs<SanskritValue.Sankhya>(result.typedValue)
        assertEquals(3L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `GanDhatu executes SanskritListLengthAction to count size`() {
        DhatuPathaRegistration.ensureRegistered()
        val gan = DhatuPatha.all.first { it.upadesha == "गण" }
        // Find list length operation
        val lengthOp = gan.operations.first { it.name == "सूच्याकारः" || it.action.name == "सूच्याकारः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10L, "दश"),
                    ExecutionExpression.sankhya(20L, "विंशति")
                )
            )
        )

        // Setup mock renderer for 2
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            if (value == 2L) "द्वि" else value.toString()
        }

        val result = lengthOp.action.execute(context, lengthOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("द्वि", result.value) // Count is 2
        assertIs<SanskritValue.Sankhya>(result.typedValue)
        assertEquals(2L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `YuDhatu executes SanskritListMapAction to map list elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val yu = DhatuPatha.all.first { it.upadesha == "यु" }
        // Find list map operation
        val mapOp = yu.operations.first { it.name == "सूचीसंयोजनम्" || it.action.name == "सूचीसंयोजनम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि")
                ),
                Karaka.KARANA to ExecutionExpression.Pada("वर्धनम्") // target operation
            )
        )

        // Setup mock renderer
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                6L -> "षट्"
                else -> value.toString()
            }
        }

        val result = mapOp.action.execute(context, mapOp)
        assertIs<ExecutionResult.Success>(result)
        
        // Mapped list: [1*2, 2*2, 3*2] -> [2, 4, 6] -> "द्वि चतुर् षट्"
        assertEquals("[द्वि, चतुर्, षट्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(3, typed.items.size)
        assertEquals(2L, (typed.items[0] as SanskritValue.Sankhya).value)
        assertEquals(4L, (typed.items[1] as SanskritValue.Sankhya).value)
        assertEquals(6L, (typed.items[2] as SanskritValue.Sankhya).value)
    }
}
