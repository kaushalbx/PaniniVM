package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class NStemDerivationTest {
    private val engine = DerivationEngine()

    @Test
    fun `derive full masculine n-stem paradigm for atman`() {
        val cases = listOf(
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "आत्मा"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "आत्मानौ"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "आत्मानः"),
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "आत्मानम्"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "आत्मानौ"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "आत्मनः"),
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "आत्मना"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "आत्मभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "आत्मभिः"),
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "आत्मने"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "आत्मभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "आत्मभ्यः"),
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "आत्मनः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "आत्मभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "आत्मभ्यः"),
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "आत्मनः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "आत्मनोः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "आत्मनाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "आत्मनि"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "आत्मनोः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "आत्मसु"),
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = engine.derive(
                SubantaDerivationRequest("आत्मन्", vibhakti, vacana, SubantaStemClass.N_STEM_MASCULINE).initialState(),
            ).final.surface
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }
}
