package dev.panini.execution

import dev.panini.core.Karaka
import kotlin.test.*

class NumericContractTest {
    @Test fun `fraction display preserves lexical names without repeating numeric labels`() {
        assertEquals("3/2", SanskritValue.Rational(3, 2, "3/2").toDisplayText())
        assertEquals("1/2 (अर्ध)", SanskritValue.Rational(1, 2, "अर्ध").toDisplayText())
    }

    @Test fun `rational member sums reduce exactly and reject unrepresentable results`() {
        fun sum(vararg values: SanskritValue) = sumNumericCollection(SanskritValue.Suchi(values.toList())) { "number-$it" }
        val large = SanskritValue.Rational(Long.MAX_VALUE, 2, "large")
        assertEquals(Long.MAX_VALUE, assertIs<SanskritValue.Sankhya>(sum(large, large)).value)
        assertEquals(SanskritValue.Rational(-1, 2, "-1/2"), sum(SanskritValue.Rational(1, -2, "negative")))
        assertNull(sum(large, large, large))
        assertNull(sum(SanskritValue.Rational(1, 0, "undefined")))
        assertNull(sum(SanskritValue.Suchi(emptyList())))
    }

    @Test fun `number eligibility rejects undefined fractions without banning negative denominators`() {
        assertFalse(OperationValueType.NUMBER.accepts(SanskritValue.Rational(1, 0, "undefined")))
        assertFalse(ListMemberType.NUMBER.accepts(SanskritValue.Rational(1, 0, "undefined")))
        assertTrue(ListMemberType.NUMBER.accepts(SanskritValue.Rational(1, -2, "negative")))
        assertTrue(OperationValueType.NUMBER.accepts(SanskritValue.Rational(1, -2, "negative")))
        assertFalse(OperationValueType.INTEGER.accepts(SanskritValue.Rational(2, 2, "integral fraction")))
    }

    @Test fun `numeric equality agrees with exact ordering across representations`() {
        val integer = SanskritValue.Sankhya(1, "एक")
        val fraction = SanskritValue.Rational(2, 2, "fraction")
        assertTrue(integer.semanticallyEquals(fraction))
        assertTrue(fraction.semanticallyEquals(integer))
        assertTrue(SanskritValue.Suchi(listOf(integer)).semanticallyEquals(SanskritValue.Suchi(listOf(fraction))))
        assertFalse(SanskritValue.Gana(listOf(integer)).semanticallyEquals(SanskritValue.Suchi(listOf(fraction))))
        assertFalse(integer.semanticallyEquals(SanskritValue.Rational(1, 0, "undefined")))
        assertFalse(integer.semanticallyEquals(SanskritValue.Shabda("1")))
    }

    @Test fun `scalar ordering compares fractions exactly without overflow`() {
        fun rational(n: Long, d: Long) = SanskritValue.Rational(n, d, "fraction")
        assertEquals(-1, compareNumericValues(rational(1, 2), rational(2, 3)))
        assertEquals(0, compareNumericValues(rational(2, 2), SanskritValue.Sankhya(1, "एक")))
        assertEquals(-1, compareNumericValues(rational(1, -2), rational(1, 2)))
        assertEquals(1, compareNumericValues(rational(Long.MAX_VALUE, 2), rational(Long.MAX_VALUE, 3)))
        assertEquals(0, compareNumericValues(rational(Long.MIN_VALUE, Long.MIN_VALUE), rational(1, 1)))
        assertNull(compareNumericValues(rational(1, 0), rational(1, 2)))
        assertNull(compareNumericValues(SanskritValue.Suchi(emptyList()), rational(1, 2)))
    }

    @Test fun `numeric signatures require number payloads rather than inherited annotations alone`() {
        val action = object : DhatuAction("test-numeric", "signature-only fixture") {
            override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
                error("Signature inspection must not execute the action")
        }
        val requirement = action.numericOp().signature.requirements.single { it.karaka == Karaka.KARMAN }
        assertEquals(OperationValueType.NUMBER, requirement.valueType)
        val number = SanskritValue.Sankhya(1L, "एक")
        assertTrue(requirement.valueType!!.accepts(number))
        assertFalse(requirement.valueType!!.accepts(SanskritValue.Suchi(listOf(number))))
        assertFalse(requirement.valueType!!.accepts(SanskritValue.Gana(listOf(number))))
    }

    @Test fun `context renderer fallback reaches default helper without recursive receiver resolution`() {
        val context = ExecutionContext(sankhyaRenderer = SankhyaResultRenderer { null })
        assertEquals(renderSankhyaResult(1L), context.renderSankhyaResult(1L))
        assertNull(context.renderSankhyaResult(-1L))
        val custom = ExecutionContext(sankhyaRenderer = SankhyaResultRenderer { "custom-$it" })
        assertEquals("custom-1", custom.renderSankhyaResult(1L))
    }
}
