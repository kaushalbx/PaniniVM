package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada3.*
import dev.panini.ashtadhyayi.adhyaya3.pada1.RterIyangSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SanadyantaDhatavahSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class RterIyangLifecycleTest {
    private val rule = RterIyangSutra
    private fun state(surface: String = "ऋत्", upadesha: String = "ऋत्", kind: TermKind = TermKind.DHATU) =
        DerivationState(listOf(DerivationTerm("root", surface, kind, upadesha = upadesha)))

    @Test fun `actual sautradhatu admits iyang not a fabricated kyan after rtiya`() {
        assertTrue(rule.matches(state()))
        assertTrue(rule.matches(state("ऋँत्ऽ", "source")))
        for (surface in listOf("ऋतीय", "ऋति", "ऋत", "अर्त्")) assertFalse(rule.matches(state(surface, surface)), surface)
        assertFalse(rule.matches(state(kind = TermKind.PRATIPADIKA)))
        val original = state("ऋँत्ऽ")
        val raw = rule.apply(original).state
        assertEquals(original.terms.single(), raw.terms.first())
        val affix = raw.terms.last()
        assertEquals("ईयङ्", affix.surface)
        assertEquals(listOf(Svara.II, Vyanjana.YA, Svara.A, Vyanjana.NGA), affix.varnas)
        assertTrue(affix.hasCurrentAffix(SanadiAffix.IYANG))
        assertTrue(affix.itProcessingPending)
        assertTrue(affix.itMarkerProvenance.isEmpty())
        assertFalse(rule.matches(raw))
    }

    @Test fun `independent it rules retain ngit proof and actual iya operative material`() {
        val original = state()
        val engine = DerivationEngine(listOf(rule, HalantyamSutra, ChutuSutra, LasakvataddhiteSutra,
            UpadesheAjanunasikaItSutra, TasyaLopahSutra, SanadyantaDhatavahSutra))
        val result = engine.derive(original, DerivationConfig(computeSvara = false))
        val ending = result.final.terms.last()
        assertEquals("ईय", ending.surface)
        assertEquals("ऋतीय", result.final.surface)
        assertEquals(original.terms.single(), result.final.terms.first())
        assertEquals("ईयङ्", ending.upadesha)
        assertTrue(ending.itMarkerProvenance.any { it.marker == ItMarker.NGIT &&
            it.designationSutra == "1.3.3" && it.designatedText == "ङ्" })
        assertTrue(ending.hasEffectiveMarker(ItMarker.NGIT))
        assertTrue(result.applications.map { it.sutra }.containsAll(listOf("3.1.29", "1.3.3", "1.3.9", "3.1.32")))
        assertTrue(result.final.samjnas.any { it.targetId == ending.id && it.samjna == Samjna.DHATU })
        result.final.requireCompleteItProcessing()
        assertFalse(rule.matches(result.final))
    }

    @Test fun `registered producer precedes existing ting without relying on bhava request`() {
        assertSame(rule, Ashtadhyayi.registry.require("3.1.29"))
        assertEquals("ऋतेरीयङ्", rule.text)
        val root = state().terms.single()
        val ending = TingAffix.TA.term()
        val original = DerivationState(listOf(root, ending), context = DerivationalContext(rupa = Rupa(lakara = Lakara.LAT)))
        assertTrue(rule.matches(original))
        val prescribed = rule.apply(original).state
        assertEquals(listOf(root.id, "iyang", ending.id), prescribed.terms.map { it.id })
        assertEquals(ending, prescribed.terms.last())
    }
}
