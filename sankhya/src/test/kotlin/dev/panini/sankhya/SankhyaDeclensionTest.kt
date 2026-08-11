package dev.panini.sankhya

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
import kotlin.test.Test
import kotlin.test.assertEquals

class SankhyaGeneratorDeclensionTest {
    @Test
    fun `generator declines cardinals through the reusable API`() {
        val generator = SankhyaGenerator()

        assertEquals("द्वे", generator.decline(2, Vibhakti.DVITIYA, Vacana.DVIVACANA))
        assertEquals("द्वौ", generator.decline(2, Vibhakti.DVITIYA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("द्वाभ्याम्", generator.decline(2, Vibhakti.TRTIYA, Vacana.DVIVACANA))
        assertEquals("त्रिभिः", generator.decline(3, Vibhakti.TRTIYA, Vacana.BAHUVACANA))
        assertEquals("चतुर्णाम्", generator.decline(4, Vibhakti.SASTHI, Vacana.BAHUVACANA))
    }

    @Test
    fun `generator propagates gender through numeral paradigms`() {
        val generator = SankhyaGenerator()

        assertEquals("द्वौ", generator.decline(2, Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("द्वे", generator.decline(2, Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.STRI))
        assertEquals("द्वे", generator.decline(2, Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.NAPUMSAKA))
        assertEquals("त्रयः", generator.decline(3, Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("तिस्रः", generator.decline(3, Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.STRI))
        assertEquals("त्रीणि", generator.decline(3, Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.NAPUMSAKA))
        assertEquals("चत्वारः", generator.decline(4, Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("चत्वारि", generator.decline(4, Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.NAPUMSAKA))
    }

    @Test
    fun `feminine three follows the tisr derivation`() {
        val derivation = SubantaEngine().derive(
            SubantaDerivationRequest("त्रि", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.STRI),
        )

        assertEquals(
            "तिस्रः",
            derivation.final.surface,
            derivation.applications.joinToString("\n") { "${it.sutra}: ${it.after.surface}" },
        )
        assertEquals(true, derivation.applications.any { it.sutra == "7.2.100" })

        val four = SubantaEngine().derive(
            SubantaDerivationRequest("चतुर्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.STRI),
        )
        assertEquals("चतस्रः", four.final.surface)
        assertEquals(true, four.applications.any { it.sutra == "7.2.100" })
    }

    @Test
    fun `feminine two follows the tap derivation`() {
        val derivation = SubantaEngine().derive(
            SubantaDerivationRequest("द्वि", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.STRI),
        )
        assertEquals(
            "द्वे",
            derivation.final.surface,
            derivation.applications.joinToString("\n") { "${it.sutra}: ${it.after.surface}" },
        )
    }

    @Test
    fun `neuter three follows the shi and num derivation`() {
        val derivation = SubantaEngine().derive(
            SubantaDerivationRequest("त्रि", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.NAPUMSAKA),
        )

        assertEquals(
            "त्रीणि",
            derivation.final.surface,
            derivation.applications.joinToString("\n") { "${it.sutra}: ${it.after.surface}" },
        )
    }
}
