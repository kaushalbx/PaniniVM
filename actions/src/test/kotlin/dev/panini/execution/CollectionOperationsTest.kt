package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.shiksha.Samjna
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

    @Test
    fun `VrjDhatu executes SanskritListFilterAction to filter list elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrj = DhatuPatha.all.first { it.upadesha == "वृजीँ" }
        val filterOp = vrj.operations.first { it.name == "सूचीशोधनम्" || it.action.name == "सूचीशोधनम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि"),
                    ExecutionExpression.sankhya(4L, "चतुर्")
                ),
                Karaka.KARANA to ExecutionExpression.Pada("युग्मत्वम्") // predicate check even
            )
        )

        // Setup mock renderer
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                else -> value.toString()
            }
        }

        val result = filterOp.action.execute(context, filterOp)
        assertIs<ExecutionResult.Success>(result)
        
        // Filtered list (even numbers only): [2, 4] -> "द्वि चतुर्"
        assertEquals("[द्वि, चतुर्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(2, typed.items.size)
        assertEquals(2L, (typed.items[0] as SanskritValue.Sankhya).value)
        assertEquals(4L, (typed.items[1] as SanskritValue.Sankhya).value)
    }

    @Test
    fun `SrjDhatu executes SanskritListConcatAction to concatenate two lists`() {
        DhatuPathaRegistration.ensureRegistered()
        val srj = DhatuPatha.all.first { it.id == "06.0150" }
        val concatOp = srj.operations.first { it.name == "सूचीसंयोगः" || it.action.name == "सूचीसंयोगः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि")
                ),
                Karaka.SAMPRADANA to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(3L, "त्रि"),
                    ExecutionExpression.sankhya(4L, "चतुर्")
                )
            )
        )

        // Setup mock renderer
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                else -> value.toString()
            }
        }

        val result = concatOp.action.execute(context, concatOp)
        assertIs<ExecutionResult.Success>(result)

        // Combined: [1, 2, 3, 4] -> "एक द्वि त्रि चतुर्"
        assertEquals("[एक, द्वि, त्रि, चतुर्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(4, typed.items.size)
        assertEquals(1L, (typed.items[0] as SanskritValue.Sankhya).value)
        assertEquals(2L, (typed.items[1] as SanskritValue.Sankhya).value)
        assertEquals(3L, (typed.items[2] as SanskritValue.Sankhya).value)
        assertEquals(4L, (typed.items[3] as SanskritValue.Sankhya).value)
    }

    @Test
    fun `SthaDhatu executes ListIndexAction to retrieve element by index`() {
        DhatuPathaRegistration.ensureRegistered()
        val stha = DhatuPatha.all.first { it.id == "01.9901" }
        val indexOp = stha.operations.first { it.name == "सूचीस्थानम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10L, "दश"),
                    ExecutionExpression.sankhya(20L, "विंशति"),
                    ExecutionExpression.sankhya(30L, "त्रिंशत्")
                ),
                Karaka.KARANA to ExecutionExpression.sankhya(2L, "द्वि")
            )
        )

        val result = indexOp.action.execute(context, indexOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("विंशति", result.value)
        assertIs<SanskritValue.Sankhya>(result.typedValue)
        assertEquals(20L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `BhajDhatu executes ListSliceAction to slice elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val bhaj = DhatuPatha.all.first { it.id == "01.1153" }
        val sliceOp = bhaj.operations.first { it.name == "सूचीविभागः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10L, "दश"),
                    ExecutionExpression.sankhya(20L, "विंशति"),
                    ExecutionExpression.sankhya(30L, "त्रिंशत्"),
                    ExecutionExpression.sankhya(40L, "चत्वारिंशत्")
                ),
                Karaka.KARANA to ExecutionExpression.sankhya(2L, "द्वि"),
                Karaka.SAMPRADANA to ExecutionExpression.sankhya(3L, "त्रि")
            )
        )

        val result = sliceOp.action.execute(context, sliceOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("[विंशति, त्रिंशत्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(2, typed.items.size)
    }

    @Test
    fun `VrtDhatu executes ListReverseAction to reverse elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrt = DhatuPatha.all.first { it.id == "01.9910" }
        val reverseOp = vrt.operations.first { it.name == "सूचीविलोमः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि")
                )
            )
        )

        val result = reverseOp.action.execute(context, reverseOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("[द्वि, एक]", result.value)
    }

    @Test
    fun `KshipDhatu executes ListFoldAction to aggregate elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val kship = DhatuPatha.all.first { it.id == "06.0005" }
        val foldOp = kship.operations.first { it.name == "सूचीसङ्क्षेपः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि")
                ),
                Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्यागुणनम्"),
                Karaka.SAMPRADANA to ExecutionExpression.sankhya(5L, "पञ्च")
            )
        )

        val result = foldOp.action.execute(context, foldOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals(30L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `JnaDhatu executes IfAction to branch execution`() {
        DhatuPathaRegistration.ensureRegistered()
        val jna = DhatuPatha.all.first { it.id == "09.0043" }
        val ifOp = jna.operations.first { it.name == "निर्णयः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.APADANA to ExecutionExpression.Pada("सत्यम्", setOf(Samjna.SHABDA), SanskritValue.Satya(true)),
                Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्यायोजनम्"), // true: add
                Karaka.SAMPRADANA to ExecutionExpression.Pada("सङ्ख्यावियोगः"), // false: sub
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(5L, "पञ्च"),
                    ExecutionExpression.sankhya(10L, "दश")
                )
            )
        )

        val result = ifOp.action.execute(context, ifOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals(15L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `VrtDhatu executes WhileAction conditional loop`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrt = DhatuPatha.all.first { it.id == "01.9910" }
        val whileOp = vrt.operations.first { it.name == "यावदवृत्तिः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("युग्मत्वम्"), // condition predicate
                Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्यावियोगः"), // body action
                Karaka.SAMPRADANA to ExecutionExpression.sankhya(10L, "दश"), // initial state
                Karaka.KARTR to ExecutionExpression.sankhya(2L, "द्वि") // subtract 2 in each step
            )
        )

        val resultImmediate = whileOp.action.execute(context.copy(bindings = context.bindings + (Karaka.SAMPRADANA to ExecutionExpression.sankhya(5L, "पञ्च"))), whileOp)
        assertIs<ExecutionResult.Success>(resultImmediate)
        assertEquals(5L, (resultImmediate.typedValue as SanskritValue.Sankhya).value)
    }
}
