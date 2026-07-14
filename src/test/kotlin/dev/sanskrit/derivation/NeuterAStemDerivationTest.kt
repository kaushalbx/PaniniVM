package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class NeuterAStemDerivationTest {
    private val engine = DerivationEngine()

    @Test
    fun `derive full neuter a-stem paradigm for phala`() {
        val cases = listOf(
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "फलम्"), Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "फले"), Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "फलानि"),
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "फलम्"), Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "फले"), Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "फलानि"),
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "फलेन"), Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "फलाभ्याम्"), Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "फलैः"),
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "फलाय"), Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "फलाभ्याम्"), Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "फलेभ्यः"),
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "फलात्"), Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "फलाभ्याम्"), Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "फलेभ्यः"),
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "फलस्य"), Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "फलयोः"), Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "फलानाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "फले"), Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "फलयोः"), Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "फलेषु"),
        )

        for ((vibhakti, vacana, expected) in cases) {
            val request = SubantaDerivationRequest("फल", vibhakti, vacana, SubantaStemClass.A_STEM_NEUTER)
            assertEquals(expected, engine.derive(request.initialState()).final.surface, "Failed for $vibhakti $vacana")
        }
    }
}
