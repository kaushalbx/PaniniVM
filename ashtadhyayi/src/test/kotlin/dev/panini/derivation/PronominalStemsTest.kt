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

        val imam = engine.derive(SubantaDerivationRequest("इदम्", Vibhakti.DVITIYA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("इमम्", imam.final.surface)
        kotlin.test.assertTrue(imam.applications.any { it.sutra == "7.2.102" })

        val iman = engine.derive(SubantaDerivationRequest("इदम्", Vibhakti.DVITIYA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("इमान्", iman.final.surface)
        kotlin.test.assertTrue(iman.applications.any { it.sutra == "7.2.102" })

        val trayah = engine.derive(SubantaDerivationRequest("त्रि", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("त्रयः", trayah.final.surface)
        kotlin.test.assertTrue(trayah.applications.any { it.sutra == "7.3.109" })

        val trini = engine.derive(SubantaDerivationRequest("त्रि", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.NAPUMSAKA))
        assertEquals("त्रीणि", trini.final.surface)

        val tisrah = engine.derive(SubantaDerivationRequest("त्रि", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.STRI))
        assertEquals("तिस्रः", tisrah.final.surface)
        kotlin.test.assertTrue(tisrah.applications.any { it.sutra == "7.2.99" })

        val trayanam = engine.derive(SubantaDerivationRequest("त्रि", Vibhakti.SASTHI, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("त्रयाणाम्", trayanam.final.surface)
        kotlin.test.assertTrue(trayanam.applications.any { it.sutra == "7.1.53" })

        val catvarah = engine.derive(SubantaDerivationRequest("चतुर्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("चत्वारः", catvarah.final.surface)
        kotlin.test.assertTrue(catvarah.applications.any { it.sutra == "7.1.98" })

        val catvari = engine.derive(SubantaDerivationRequest("चतुर्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.NAPUMSAKA))
        assertEquals("चत्वारि", catvari.final.surface)
        kotlin.test.assertTrue(catvari.applications.any { it.sutra == "7.1.98" })

        val catasrah = engine.derive(SubantaDerivationRequest("चतुर्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.STRI))
        assertEquals("चतस्रः", catasrah.final.surface)
        kotlin.test.assertTrue(catasrah.applications.any { it.sutra == "7.2.99" })

        val caturnam = engine.derive(SubantaDerivationRequest("चतुर्", Vibhakti.SASTHI, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("चतुर्णाम्", caturnam.final.surface)
        kotlin.test.assertTrue(caturnam.applications.any { it.sutra == "7.1.55" })

        val panca = engine.derive(SubantaDerivationRequest("पञ्चन्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("पञ्च", panca.final.surface)
        kotlin.test.assertTrue(panca.applications.any { it.sutra == "7.1.22" })

        val pancanam = engine.derive(SubantaDerivationRequest("पञ्चन्", Vibhakti.SASTHI, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("पञ्चानाम्", pancanam.final.surface)
        kotlin.test.assertTrue(pancanam.applications.any { it.sutra == "7.1.55" })

        val sat = engine.derive(SubantaDerivationRequest("षट्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("षट्", sat.final.surface)
        kotlin.test.assertTrue(sat.applications.any { it.sutra == "7.1.22" })

        val sapta = engine.derive(SubantaDerivationRequest("सप्तन्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("सप्त", sapta.final.surface)
        kotlin.test.assertTrue(sapta.applications.any { it.sutra == "7.1.22" })

        val dasa = engine.derive(SubantaDerivationRequest("दशन्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("दश", dasa.final.surface)
        kotlin.test.assertTrue(dasa.applications.any { it.sutra == "7.1.22" })

        val asta = engine.derive(SubantaDerivationRequest("अष्टन्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("अष्टौ", asta.final.surface)
        kotlin.test.assertTrue(asta.applications.any { it.sutra == "7.1.21" })

        val astanam = engine.derive(SubantaDerivationRequest("अष्टन्", Vibhakti.SASTHI, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("अष्टानाम्", astanam.final.surface)
        kotlin.test.assertTrue(astanam.applications.any { it.sutra == "7.1.55" })

        val asau = engine.derive(SubantaDerivationRequest("अदस्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("असौ", asau.final.surface)
        kotlin.test.assertTrue(asau.applications.any { it.sutra == "7.2.107" })

        val amu = engine.derive(SubantaDerivationRequest("अदस्", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("अमू", amu.final.surface)
        kotlin.test.assertTrue(amu.applications.any { it.sutra == "8.2.80" })

        val ami = engine.derive(SubantaDerivationRequest("अदस्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("अमी", ami.final.surface)
        kotlin.test.assertTrue(ami.applications.any { it.sutra == "8.2.80" })

        val tvam = engine.derive(SubantaDerivationRequest("युष्मद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("त्वम्", tvam.final.surface)
        kotlin.test.assertTrue(tvam.applications.any { it.sutra == "7.2.86" })

        val aham = engine.derive(SubantaDerivationRequest("अस्मद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("अहम्", aham.final.surface)
        kotlin.test.assertTrue(aham.applications.any { it.sutra == "7.2.86" })

        val yuyam = engine.derive(SubantaDerivationRequest("युष्मद्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("यूयम्", yuyam.final.surface)
        kotlin.test.assertTrue(yuyam.applications.any { it.sutra == "7.2.86" })

        val vayam = engine.derive(SubantaDerivationRequest("अस्मद्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, Linga.PUMS))
        assertEquals("वयम्", vayam.final.surface)
        kotlin.test.assertTrue(vayam.applications.any { it.sutra == "7.2.86" })

        val dvau = engine.derive(SubantaDerivationRequest("द्वि", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("द्वौ", dvau.final.surface)
        kotlin.test.assertTrue(dvau.applications.any { it.sutra == "7.2.102" })

        val dveF = engine.derive(SubantaDerivationRequest("द्वि", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.STRI))
        assertEquals("द्वे", dveF.final.surface)
        kotlin.test.assertTrue(dveF.applications.any { it.sutra == "7.1.18" })

        val dveN = engine.derive(SubantaDerivationRequest("द्वि", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.NAPUMSAKA))
        assertEquals("द्वे", dveN.final.surface)
        kotlin.test.assertTrue(dveN.applications.any { it.sutra == "7.1.19" })
    }
}
