package dev.panini.dhatupatha

import dev.panini.actions.comparison.CopularOrderAction
import dev.panini.actions.comparison.CopularEqualityAction
import dev.panini.core.Karaka
import dev.panini.dhatupatha.adadi.AsDhatu
import dev.panini.execution.OperationValueType
import dev.panini.execution.SanskritValue
import kotlin.test.*

class CopularOrderContractTest {
    @Test fun `equality signature requires one participant per role without restricting payload structure`() {
        val signature = AsDhatu().operations.single { it.action == CopularEqualityAction }.signature
        assertEquals(setOf(Karaka.KARTR, Karaka.KARANA, Karaka.KARMAN),
            signature.requirements.map { it.karaka }.toSet())
        for (requirement in signature.requirements) {
            assertEquals(1, requirement.minimumMembers)
            assertEquals(1, requirement.maximumMembers)
            assertNull(requirement.valueType)
        }
    }

    @Test fun `legacy membership cannot consume comparison or locative frames`() {
        val legacy = AsDhatu().operations.single {
            it.signature.requirements.map { requirement -> requirement.karaka } == listOf(Karaka.KARMAN)
        }
        assertEquals(setOf(Karaka.KARTR, Karaka.APADANA, Karaka.ADHIKARANA),
            legacy.signature.forbiddenKarakas)
        assertFailsWith<IllegalArgumentException> {
            dev.panini.execution.OperationSignature(legacy.signature.requirements,
                forbiddenKarakas = setOf(Karaka.KARMAN))
        }
    }

    @Test fun `ordering requires one scalar subject and one scalar standard`() {
        val operation = AsDhatu().operations.single { it.action == CopularOrderAction }
        for (role in listOf(Karaka.KARTR, Karaka.APADANA)) {
            val requirement = operation.signature.requirements.single { it.karaka == role }
            assertEquals(1, requirement.minimumMembers)
            assertEquals(1, requirement.maximumMembers)
            assertEquals(OperationValueType.NUMBER, requirement.valueType)
            val number = SanskritValue.Sankhya(1L, "एक")
            assertTrue(requirement.valueType!!.accepts(number))
            assertFalse(requirement.valueType!!.accepts(SanskritValue.Suchi(listOf(number))))
            assertFalse(requirement.valueType!!.accepts(SanskritValue.Gana(listOf(number))))
        }
    }
}
