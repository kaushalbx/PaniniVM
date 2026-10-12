package dev.panini.dhatupatha

import dev.panini.core.Karaka
import dev.panini.dhatupatha.adadi.AsDhatu
import dev.panini.execution.OperationValueType
import dev.panini.execution.SanskritValue
import kotlin.test.*

class MembershipContractTest {
    @Test fun `natural membership requires one entity and one collection whole`() {
        val signature = AsDhatu().operations.single {
            it.signature.requirements.any { requirement -> requirement.karaka == Karaka.ADHIKARANA }
        }.signature
        val subject = signature.requirements.single { it.karaka == Karaka.KARTR }
        assertEquals(1, subject.maximumMembers)
        assertNull(subject.valueType, "The member may itself be a structured value")
        val whole = signature.requirements.single { it.karaka == Karaka.ADHIKARANA }
        assertEquals(1, whole.maximumMembers)
        assertEquals(OperationValueType.COLLECTION, whole.valueType)
        val number = SanskritValue.Sankhya(1L, "एक")
        assertFalse(whole.valueType!!.accepts(number))
        assertTrue(whole.valueType!!.accepts(SanskritValue.Suchi(listOf(number))))
        assertTrue(whole.valueType!!.accepts(SanskritValue.Gana(listOf(number))))
    }
}
