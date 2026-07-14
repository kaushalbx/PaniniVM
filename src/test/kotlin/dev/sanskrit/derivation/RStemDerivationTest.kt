package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class RStemDerivationTest {
    private val engine = DerivationEngine()

    @Test
    fun `derive full masculine r-stem paradigm for pitr`() {
        val cases = listOf(
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "पिता"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "पितरौ"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "पितरः"),
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "पितरम्"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "पितरौ"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "पितॄन्"),
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "पित्रा"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "पितृभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "पितृभिः"),
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "पित्रे"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "पितृभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "पितृभ्यः"),
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "पितुः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "पितृभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "पितृभ्यः"),
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "पितुः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "पित्रोः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "पितॄणाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "पितरि"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "पित्रोः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "पितृषु"),
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = engine.derive(
                SubantaDerivationRequest("पितृ", vibhakti, vacana, SubantaStemClass.R_STEM_MASCULINE).initialState(),
            ).final.surface
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }
}
