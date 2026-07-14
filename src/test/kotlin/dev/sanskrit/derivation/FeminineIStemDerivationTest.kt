package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class FeminineIStemDerivationTest {
    private val engine = DerivationEngine()

    @Test
    fun `derive full feminine i-stem paradigm for mati`() {
        val cases = listOf(
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "मतिः"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "मती"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "मतयः"),
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "मतिम्"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "मती"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "मतीः"),
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "मत्या"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "मतिभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "मतिभिः"),
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "मत्यै"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "मतिभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "मतिभ्यः"),
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "मत्याः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "मतिभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "मतिभ्यः"),
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "मत्याः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "मत्योः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "मतीनाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "मत्याम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "मत्योः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "मतिषु"),
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = engine.derive(
                SubantaDerivationRequest("मति", vibhakti, vacana, SubantaStemClass.I_STEM_FEMININE).initialState(),
            ).final.surface
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }
}
