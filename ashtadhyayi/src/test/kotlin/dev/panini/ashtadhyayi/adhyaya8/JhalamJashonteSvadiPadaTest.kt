package dev.panini.ashtadhyayi.adhyaya8

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 8.2.39 झलां जशोऽन्ते at the pada made by 1.4.17 स्वादिष्वसर्वनामस्थाने, before 8.2.41 षढोः कः सि. */
class JhalamJashonteSvadiPadaTest {
    private fun derive(stem: String, vibhakti: Vibhakti) =
        SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, Vacana.BAHUVACANA, Linga.PUMS))

    @Test fun `a pada final sa before su is voiced then devoiced, not replaced by ka`() {
        for ((stem, expected) in listOf("षष्" to "षट्सु", "द्विष्" to "द्विट्सु")) {
            val result = derive(stem, Vibhakti.SAPTAMI)
            assertEquals(expected, result.final.surface)
            assertTrue("8.2.39" in result.final.appliedSutras && "8.4.55" in result.final.appliedSutras)
            assertTrue("8.2.41" !in result.final.appliedSutras)
        }
    }

    @Test fun `other pada finals before su and bhis keep their forms`() {
        assertEquals("वाक्षु", derive("वाच्", Vibhakti.SAPTAMI).final.surface)
        assertEquals("मरुत्सु", derive("मरुत्", Vibhakti.SAPTAMI).final.surface)
        assertEquals("षड्भिः", derive("षष्", Vibhakti.TRTIYA).final.surface)
    }
}
