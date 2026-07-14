package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class FeminineUStemDerivationTest {
    private val engine = DerivationEngine()

    @Test
    fun `derive full feminine u-stem paradigm for dhenu`() {
        val cases = listOf(
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "धेनुः"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "धेनू"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "धेनवः"),
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "धेनुम्"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "धेनू"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "धेनूः"),
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "धेन्वा"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "धेनुभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "धेनुभिः"),
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "धेन्वै"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "धेनुभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "धेनुभ्यः"),
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "धेन्वाः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "धेनुभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "धेनुभ्यः"),
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "धेन्वाः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "धेन्वोः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "धेनूनाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "धेन्वाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "धेन्वोः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "धेनुषु"),
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = engine.derive(
                SubantaDerivationRequest("धेनु", vibhakti, vacana, SubantaStemClass.U_STEM_FEMININE).initialState(),
            ).final.surface
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }
}
