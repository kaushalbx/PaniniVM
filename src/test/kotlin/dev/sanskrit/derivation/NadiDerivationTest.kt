package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class NadiDerivationTest {
    private val engine = DerivationEngine()

    @Test
    fun `derive full feminine i-stem paradigm for nadi`() {
        val cases = listOf(
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "नदी"), Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "नद्यौ"), Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "नद्यः"),
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "नदीम्"), Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "नद्यौ"), Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "नदीः"),
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "नद्या"), Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "नदीभ्याम्"), Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "नदीभिः"),
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "नद्यै"), Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "नदीभ्याम्"), Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "नदीभ्यः"),
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "नद्याः"), Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "नदीभ्याम्"), Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "नदीभ्यः"),
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "नद्याः"), Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "नद्योः"), Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "नदीनाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "नद्याम्"), Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "नद्योः"), Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "नदीषु"),
        )

        for ((vibhakti, vacana, expected) in cases) {
            val request = SubantaDerivationRequest("नदी", vibhakti, vacana, SubantaStemClass.II_STEM_FEMININE)
            val result = runCatching { engine.derive(request.initialState()) }
            assertEquals(null, result.exceptionOrNull(), "Derivation failed for $vibhakti $vacana")
            assertEquals(expected, result.getOrThrow().final.surface, "Failed for $vibhakti $vacana")
        }
    }
}
