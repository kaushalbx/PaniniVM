package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class ApDerivationTest {
    private val engine = DerivationEngine()

    private fun deriveForm(stem: String, vibhakti: Vibhakti, vacana: Vacana, stemClass: SubantaStemClass): String {
        val request = SubantaDerivationRequest(stem, vibhakti, vacana, stemClass)
        val result = engine.derive(request.initialState())
        return result.final.surface
    }

    @Test
    fun `derive full feminine a-stem paradigm for rama`() {
        val cases = listOf(
            // Prathama
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "रमा"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "रमे"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "रमाः"),
            // Dvitiya
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "रमाम्"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "रमे"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "रमाः"),
            // Trtiya
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "रमया"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "रमाभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "रमाभिः"),
            // Chaturthi
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "रमायै"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "रमाभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "रमाभ्यः"),
            // Panchami
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "रमायाः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "रमाभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "रमाभ्यः"),
            // Sasthi
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "रमायाः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "रमयोः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "रमाणाम्"),
            // Saptami
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "रमायाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "रमयोः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "रमासु")
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = deriveForm("रमा", vibhakti, vacana, SubantaStemClass.A_STEM_FEMININE)
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }
}
