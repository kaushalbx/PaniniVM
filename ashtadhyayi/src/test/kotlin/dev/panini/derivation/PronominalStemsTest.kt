package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class PronominalStemsTest {

    private val engine = SubantaEngine()

    @Test
    fun `derives tad pronominal paradigm forms`() {
        val sah = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("सः", sah.final.surface)
        kotlin.test.assertTrue(sah.applications.any { it.sutra == "7.2.102" })
        kotlin.test.assertTrue(sah.applications.any { it.sutra == "7.2.106" })

        val tau = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("तौ", tau.final.surface)
        kotlin.test.assertTrue(tau.applications.any { it.sutra == "7.2.102" })

        val te = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("ते", te.final.surface)

        val tasmai = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.CHATURTHI, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("तस्मै", tasmai.final.surface)
        kotlin.test.assertTrue(tasmai.applications.any { it.sutra == "7.1.14" })

        val tasmat = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PANCHAMI, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("तस्मात्", tasmat.final.surface)
        kotlin.test.assertTrue(tasmat.applications.any { it.sutra == "7.1.15" })

        val tasmin = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.SAPTAMI, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("तस्मिन्", tasmin.final.surface)
        kotlin.test.assertTrue(tasmin.applications.any { it.sutra == "7.1.15" })
    }

    @Test
    fun `derives yad kim and idam pronominal forms`() {
        val yah = engine.derive(SubantaDerivationRequest("यद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("यः", yah.final.surface)
        kotlin.test.assertTrue(yah.applications.any { it.sutra == "7.2.102" })

        val kah = engine.derive(SubantaDerivationRequest("किम्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("कः", kah.final.surface)
        kotlin.test.assertTrue(kah.applications.any { it.sutra == "7.2.102" })

        val ayam = engine.derive(SubantaDerivationRequest("इदम्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("अयम्", ayam.final.surface)
        kotlin.test.assertTrue(ayam.applications.any { it.sutra == "7.2.111" })

        val imau = engine.derive(SubantaDerivationRequest("इदम्", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("इमौ", imau.final.surface)
        kotlin.test.assertTrue(imau.applications.any { it.sutra == "7.2.102" })

        val ime = engine.derive(SubantaDerivationRequest("इदम्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("इमे", ime.final.surface)
        kotlin.test.assertTrue(ime.applications.any { it.sutra == "7.1.17" })
    }
}
