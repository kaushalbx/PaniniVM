package dev.panini.derivation

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class SubantaKarakaDerivationTest {

    @Test
    fun `derives correct subanta for recipient`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.SAMPRADANA,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा",
                isSakarmaka = true,
                prayoga = Prayoga.KARTARI
            )
        )
        // Recipient of giving is resolved to Chaturthi. 
        // For a-stem masculine "राम" -> "रामाय"
        assertEquals(Vibhakti.CHATURTHI, result.initial.context.rupa.vibhakti)
        assertEquals("रामाय", result.final.surface)
    }

    @Test
    fun `derives correct subanta for instrument`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "लेखनी",
                karaka = Karaka.KARANA,
                vacana = Vacana.EKAVACANA,
                dhatu = "लिख",
                isSakarmaka = true,
                prayoga = Prayoga.KARTARI
            )
        )
        // Instrument of writing is resolved to Trtiya. 
        // For i-stem feminine "लेखनी" -> "लेखन्या" (via 7.3.116, etc.)
        assertEquals(Vibhakti.TRTIYA, result.initial.context.rupa.vibhakti)
        assertEquals("लेखन्या", result.final.surface)
    }

    @Test
    fun `respects abhihita blocking in active and passive prayogas`() {
        val engine = SubantaEngine()

        // Active voice (kartari): Karman (object) is unexpressed (anabhihita)
        // -> Dvitiya -> "रामम्"
        val activeRes = engine.deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.KARMAN,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा",
                isSakarmaka = true,
                prayoga = Prayoga.KARTARI
            )
        )
        assertEquals(Vibhakti.DVITIYA, activeRes.initial.context.rupa.vibhakti)
        assertEquals("रामम्", activeRes.final.surface)

        // Passive voice (karmani): Karman (object) is expressed (abhihita) by verb
        // -> blocks Dvitiya -> falls back to Prathama -> "रामः"
        val passiveRes = engine.deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.KARMAN,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा",
                isSakarmaka = true,
                prayoga = Prayoga.KARMANI
            )
        )
        assertEquals(Vibhakti.PRATHAMA, passiveRes.initial.context.rupa.vibhakti)
        assertEquals("रामः", passiveRes.final.surface)
    }

    @Test
    fun `attaches karaka resolution to derivation result`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.SAMPRADANA,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा"
            )
        )
        val resolution = result.karakaResolution
        kotlin.test.assertNotNull(resolution)
        assertEquals(Karaka.SAMPRADANA, resolution.resolved)
        assertEquals(Vibhakti.CHATURTHI, resolution.resolvedVibhakti)
        kotlin.test.assertTrue(resolution.evidence.any { it.sutra == "1.4.32" })
        kotlin.test.assertTrue(resolution.evidence.any { it.sutra == "2.3.13" })
    }

    @Test
    fun `supports custom semantic relations overrides`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.SAMPRADANA,
                vacana = Vacana.EKAVACANA,
                dhatu = "पठ्",
                semanticRelations = setOf(dev.panini.vyakaranam.analysis.SemanticRelation.RECIPIENT)
            )
        )
        assertEquals(Vibhakti.CHATURTHI, result.initial.context.rupa.vibhakti)
        assertEquals("रामाय", result.final.surface)
    }
}
