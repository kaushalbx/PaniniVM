package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class SStemNeuterDerivationTest {
    private val engine = DerivationEngine()

    @Test
    fun `derive full neuter s-stem paradigm for manas`() {
        val cases = listOf(
            Triple(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "मनः"),
            Triple(Vibhakti.PRATHAMA, Vacana.DVIVACANA, "मनसी"),
            Triple(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "मनांसि"),
            Triple(Vibhakti.DVITIYA, Vacana.EKAVACANA, "मनः"),
            Triple(Vibhakti.DVITIYA, Vacana.DVIVACANA, "मनसी"),
            Triple(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "मनांसि"),
            Triple(Vibhakti.TRTIYA, Vacana.EKAVACANA, "मनसा"),
            Triple(Vibhakti.TRTIYA, Vacana.DVIVACANA, "मनोभ्याम्"),
            Triple(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "मनोभिः"),
            Triple(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "मनसे"),
            Triple(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "मनोभ्याम्"),
            Triple(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "मनोभ्यः"),
            Triple(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "मनसः"),
            Triple(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "मनोभ्याम्"),
            Triple(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "मनोभ्यः"),
            Triple(Vibhakti.SASTHI, Vacana.EKAVACANA, "मनसः"),
            Triple(Vibhakti.SASTHI, Vacana.DVIVACANA, "मनसोः"),
            Triple(Vibhakti.SASTHI, Vacana.BAHUVACANA, "मनसाम्"),
            Triple(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "मनसि"),
            Triple(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "मनसोः"),
            Triple(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "मनःसु"),
        )

        for ((vibhakti, vacana, expected) in cases) {
            val actual = engine.derive(
                SubantaDerivationRequest("मनस्", vibhakti, vacana, SubantaStemClass.S_STEM_NEUTER).initialState(),
            ).final.surface
            assertEquals(expected, actual, "Failed for $vibhakti $vacana")
        }
    }
}
