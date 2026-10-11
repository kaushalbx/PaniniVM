package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.shiksha.Samjna
import kotlin.test.*

class OperationPayloadTypeTest {
    private fun join(left: SanskritValue, right: SanskritValue): OperationResolution = OperationResolver.resolve(
        DhatuInvocation("join", YujirDhatu(), mapOf(
            Karaka.KARMAN to ExecutionExpression.Reference("left"),
            Karaka.KARTR to ExecutionExpression.Reference("right"),
        ), selectedOperation = "सूचीसंयोगः"),
        mapOf("left" to left, "right" to right),
    )

    @Test fun `natural joining requires collection payloads not inherited tags`() {
        val number = SanskritValue.Sankhya(1, "एक")
        val list = SanskritValue.Suchi(listOf(number))
        assertIs<OperationResolution.Resolved>(join(list, SanskritValue.Suchi(emptyList())))
        assertIs<OperationResolution.Resolved>(join(list, SanskritValue.Gana(emptyList())))
        for (invalid in listOf(
            number,
            SanskritValue.Range(number, number),
            SanskritValue.Shabda("सूची", setOf(Samjna.GANA, Samjna.SANKHYA)),
        )) {
            assertIs<OperationResolution.Invalid>(join(list, invalid))
            assertIs<OperationResolution.Invalid>(join(invalid, list))
        }
    }

    @Test fun `number payload constraint does not flatten or trust a numeric collection`() {
        val number = SanskritValue.Sankhya(1, "एक")
        assertTrue(OperationValueType.NUMBER.accepts(number))
        assertTrue(OperationValueType.NUMBER.accepts(SanskritValue.Rational(1, 2, "अर्ध")))
        assertFalse(OperationValueType.NUMBER.accepts(SanskritValue.Suchi(listOf(number))))
        assertFalse(OperationValueType.NUMBER.accepts(SanskritValue.Shabda("एक", setOf(Samjna.SANKHYA))))
    }
}
