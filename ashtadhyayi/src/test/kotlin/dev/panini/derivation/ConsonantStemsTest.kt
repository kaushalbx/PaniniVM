package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class ConsonantStemsTest {

    @Test
    fun `test specialized declensions for nadi rajan and vac`() {
        val engine = SubantaEngine()

        val nadya = engine.derive(SubantaDerivationRequest("नदी", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.STRI))
        assertEquals("नद्या", nadya.final.surface)
        kotlin.test.assertTrue(nadya.applications.any { it.sutra == "1.4.3" })
        kotlin.test.assertTrue(nadya.applications.any { it.sutra == "6.1.77" })

        val nadibhih = engine.derive(SubantaDerivationRequest("नदी", Vibhakti.TRTIYA, Vacana.BAHUVACANA, Linga.STRI))
        assertEquals("नदीभिः", nadibhih.final.surface)
        kotlin.test.assertTrue(nadibhih.applications.any { it.sutra == "1.4.3" })

        val raja = engine.derive(SubantaDerivationRequest("राजन्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("राजा", raja.final.surface)
        kotlin.test.assertTrue(raja.applications.any { it.sutra == "6.4.1" })

        val rajna = engine.derive(SubantaDerivationRequest("राजन्", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("राज्ञा", rajna.final.surface)
        kotlin.test.assertTrue(rajna.applications.any { it.sutra == "6.4.134" })
        kotlin.test.assertTrue(rajna.applications.any { it.sutra == "8.4.40" })

        val vak = engine.derive(SubantaDerivationRequest("वाच्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.STRI))
        assertEquals("वाक्", vak.final.surface)
        kotlin.test.assertTrue(vak.applications.any { it.sutra == "8.2.30" })

        val vaca = engine.derive(SubantaDerivationRequest("वाच्", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.STRI))
        assertEquals("वाचा", vaca.final.surface)
        kotlin.test.assertTrue(vaca.applications.any { it.sutra == "4.1.2" })

        val vagbhih = engine.derive(SubantaDerivationRequest("वाच्", Vibhakti.TRTIYA, Vacana.BAHUVACANA, Linga.STRI))
        println("VAGBHIH SURFACE: ${vagbhih.final.surface}")
        println("VAGBHIH SUTRAS: ${vagbhih.applications.map { "${it.sutra}: ${it.explanation}" }}")
        assertEquals("वाग्भिः", vagbhih.final.surface)
        kotlin.test.assertTrue(vagbhih.applications.any { it.sutra == "8.2.30" })

        // atman stem test (6.4.137 retains 'a' in an-stem after conjunct tm)
        val atma = engine.derive(SubantaDerivationRequest("आत्मन्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("आत्मा", atma.final.surface)

        val atmana = engine.derive(SubantaDerivationRequest("आत्मन्", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("आत्मना", atmana.final.surface)

        // go stem tests
        val gauh = engine.derive(SubantaDerivationRequest("गो", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("गौः", gauh.final.surface)
        kotlin.test.assertTrue(gauh.applications.any { it.sutra == "7.1.90" })

        val gavau = engine.derive(SubantaDerivationRequest("गो", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("गावौ", gavau.final.surface)
        kotlin.test.assertTrue(gavau.applications.any { it.sutra == "7.1.90" })

        val gam = engine.derive(SubantaDerivationRequest("गो", Vibhakti.DVITIYA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("गाम्", gam.final.surface)
        kotlin.test.assertTrue(gam.applications.any { it.sutra == "6.1.93" })

        val gah = engine.derive(SubantaDerivationRequest("गो", Vibhakti.DVITIYA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("गाः", gah.final.surface)
        kotlin.test.assertTrue(gah.applications.any { it.sutra == "6.1.93" })
    }
}
