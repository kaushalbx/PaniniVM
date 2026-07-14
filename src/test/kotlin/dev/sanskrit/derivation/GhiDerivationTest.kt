package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class GhiDerivationTest {
    private val engine = DerivationEngine()

    private fun deriveForm(stem: String, vibhakti: Vibhakti, vacana: Vacana, stemClass: SubantaStemClass): String {
        val request = SubantaDerivationRequest(stem, vibhakti, vacana, stemClass)
        val result = engine.derive(request.initialState())
        return result.final.surface
    }

    @Test
    fun `derive full masculine i-stem paradigm for kavi`() {
        val cases = listOf(
            // Prathama
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "कविः"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "कवी"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "कवयः"),
            // Dvitiya
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "कविम्"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "कवी"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "कवीन्"),
            // Trtiya
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "कविना"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "कविभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "कविभिः"),
            // Chaturthi
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "कवये"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "कविभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "कविभ्यः"),
            // Panchami
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "कवेऽः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "कविभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "कविभ्यः"),
            // Sasthi
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "कवेऽः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "कव्योः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "कवीनाम्"),
            // Saptami
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "कवौ"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "कव्योः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "कविषु")
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = deriveForm("कवि", vibhakti, vacana, SubantaStemClass.I_STEM_MASCULINE)
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }

    @Test
    fun `derive full masculine u-stem paradigm for bhanu`() {
        val cases = listOf(
            // Prathama
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "भानुः"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "भानू"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "भानवः"),
            // Dvitiya
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "भानुम्"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "भानू"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "भानून्"),
            // Trtiya
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "भानुना"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "भानुभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "भानुभिः"),
            // Chaturthi
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "भानवे"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "भानुभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "भानुभ्यः"),
            // Panchami
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "भानोऽः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "भानुभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "भानुभ्यः"),
            // Sasthi
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "भानोऽः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "भान्वोः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "भानूनाम्"),
            // Saptami
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "भानौ"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "भान्वोः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "भानुषु")
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = deriveForm("भानु", vibhakti, vacana, SubantaStemClass.U_STEM_MASCULINE)
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }
}
