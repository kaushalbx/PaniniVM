package dev.panini.dhatupatha

import dev.panini.actions.comparison.CopularOrderAction
import dev.panini.actions.comparison.CopularEqualityAction
import dev.panini.actions.collection.ListContainsAction
import dev.panini.core.Karaka
import dev.panini.dhatupatha.adadi.AsDhatu
import dev.panini.execution.OperationValueType
import dev.panini.execution.NominalOperandRole
import dev.panini.execution.SanskritValue
import kotlin.test.*

class CopularOrderContractTest {
    @Test fun `equality signature requires one participant per role without restricting payload structure`() {
        val signature = AsDhatu().operations.single { it.action == CopularEqualityAction }.signature
        assertEquals(setOf(Karaka.KARTR),
            signature.requirements.map { it.karaka }.toSet())
        val standard = signature.nominalRequirements.single()
        assertEquals(NominalOperandRole.COMPARISON_STANDARD, standard.role)
        assertNull(standard.valueType)
        for (requirement in signature.requirements) {
            assertEquals(1, requirement.minimumMembers)
            assertEquals(1, requirement.maximumMembers)
            assertNull(requirement.valueType)
        }
    }

    @Test fun `membership requires an entity and collection locus without a legacy object alias`() {
        val signature = AsDhatu().operations.single { it.action == ListContainsAction }.signature
        assertEquals(setOf(Karaka.KARTR, Karaka.ADHIKARANA), signature.requirements.map { it.karaka }.toSet())
        assertTrue(signature.nominalRequirements.isEmpty())
        assertEquals(OperationValueType.COLLECTION,
            signature.requirements.single { it.karaka == Karaka.ADHIKARANA }.valueType)
        assertFailsWith<IllegalArgumentException> {
            dev.panini.execution.OperationSignature(signature.requirements,
                forbiddenKarakas = setOf(Karaka.KARTR))
        }
    }

    @Test fun `ordering requires one scalar subject and one scalar standard`() {
        val operation = AsDhatu().operations.single { it.action == CopularOrderAction }
        val requirement = operation.signature.requirements.single()
        assertEquals(Karaka.KARTR, requirement.karaka)
        assertEquals(1, requirement.minimumMembers)
        assertEquals(1, requirement.maximumMembers)
        val standard = operation.signature.nominalRequirements.single()
        assertEquals(NominalOperandRole.COMPARISON_STANDARD, standard.role)
        for (type in listOf(requirement.valueType, standard.valueType)) {
            assertEquals(OperationValueType.NUMBER, type)
            val number = SanskritValue.Sankhya(1L, "एक")
            assertTrue(type!!.accepts(number))
            assertFalse(type.accepts(SanskritValue.Suchi(listOf(number))))
            assertFalse(type.accepts(SanskritValue.Gana(listOf(number))))
        }
    }
}
