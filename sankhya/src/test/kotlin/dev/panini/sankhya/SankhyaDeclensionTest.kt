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
    fun `declension preserves an una expression rather than canonicalizing its value`() {
        val expression = SankhyaExpression.Una(
            SankhyaExpression.Primitive(PrimitiveSankhya.EKA),
            SankhyaExpression.Primitive(PrimitiveSankhya.VIMSHATI),
        )
        assertEquals(Linga.STRI, SankhyaGenerator().intrinsicLinga(expression))
        assertEquals(null, SankhyaGenerator().intrinsicLinga(expression.value))
        assertEquals("एकोनविंशतिम्", SankhyaGenerator().decline(expression, Vibhakti.DVITIYA, Vacana.EKAVACANA))
    }
    @Test
    fun `compound cardinal declension follows its expression head`() {
        val generator = SankhyaGenerator()
        assertEquals(Linga.STRI, generator.intrinsicLinga(23))
        assertEquals(Linga.STRI, generator.intrinsicLinga(35))
        assertEquals(Linga.NAPUMSAKA, generator.intrinsicLinga(200))
        assertEquals("त्रयोविंशतिम्", generator.decline(23, Vibhakti.DVITIYA, Vacana.EKAVACANA))
        assertEquals("पञ्चत्रिंशतम्", generator.decline(35, Vibhakti.DVITIYA, Vacana.EKAVACANA))
        assertEquals("द्विशतम्", generator.decline(200, Vibhakti.DVITIYA, Vacana.EKAVACANA))
    }
    @Test
    fun `nominal quantities keep lexical gender across requested referent genders`() {
        val generator = SankhyaGenerator()
        for (linga in Linga.entries) {
            assertEquals("विंशतिम्", generator.decline(20, Vibhakti.DVITIYA, Vacana.EKAVACANA, linga))
            assertEquals("त्रिंशतम्", generator.decline(30, Vibhakti.DVITIYA, Vacana.EKAVACANA, linga))
            assertEquals("पञ्चाशत्", generator.decline(50, Vibhakti.PRATHAMA, Vacana.EKAVACANA, linga))
            assertEquals("शतम्", generator.decline(100, Vibhakti.PRATHAMA, Vacana.EKAVACANA, linga))
        }
    }
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

    @Test
    fun `shat numerals derive nominative and accusative plural forms`() {
        val generator = SankhyaGenerator()
        val expected = mapOf(
            5L to ("पञ्चन्" to "पञ्च"),
            6L to ("षष्" to "षट्"),
            7L to ("सप्तन्" to "सप्त"),
            8L to ("अष्टन्" to "अष्टौ"),
            9L to ("नवन्" to "नव"),
            10L to ("दशन्" to "दश"),
        )

        expected.forEach { (value, forms) ->
            val (pratipadika, surface) = forms
            Linga.entries.forEach { linga ->
                val derivation = SubantaEngine().derive(
                    SubantaDerivationRequest(pratipadika, Vibhakti.PRATHAMA, Vacana.BAHUVACANA, linga),
                )
                assertEquals(
                    surface,
                    derivation.final.surface,
                    derivation.applications.joinToString("\n") { "${it.sutra}: ${it.after.surface}" },
                )
                val accusative = SubantaEngine().derive(
                    SubantaDerivationRequest(pratipadika, Vibhakti.DVITIYA, Vacana.BAHUVACANA, linga),
                )
                assertEquals(
                    surface,
                    accusative.final.surface,
                    accusative.applications.joinToString("\n") { "${it.sutra}: ${it.after.surface}" },
                )
                assertEquals(surface, generator.decline(value, Vibhakti.PRATHAMA, Vacana.BAHUVACANA, linga))
                assertEquals(surface, generator.decline(value, Vibhakti.DVITIYA, Vacana.BAHUVACANA, linga))
            }
        }
    }
}
