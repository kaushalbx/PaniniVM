package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada2.AaneMukSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.*
import dev.panini.core.ItMarker
import dev.panini.shiksha.*
import kotlin.test.*

class MukAugmentLocusTest {
    private fun initial(anga: String = "पच", ana: String = "आन") = DerivationState(listOf(
        DerivationTerm("anga", anga, TermKind.DHATU),
        DerivationTerm("sanac", ana, TermKind.PRATYAYA, upadesha = "शानच्",
            createdBySutra = "3.2.124", itProcessingPhase = ItProcessingPhase.PROCESSED)))

    @Test fun `muk has kit proof and attaches to the preceding final a not to ana`() {
        val original = initial("पचँऽ")
        val introduced = AaneMukSutra.apply(original).state
        val raw = introduced.terms.last()
        assertEquals("मुक्", raw.upadesha)
        assertEquals(listOf(Vyanjana.MA, Vyanjana.KA), raw.varnas)
        assertEquals("anga", raw.augmentTargetId)
        assertEquals(3, raw.augmentTargetVarnaIndex)
        assertEquals(Svara.A, raw.augmentTargetVarna)
        assertTrue(raw.nonOperativeUpadeshaSegments.isNotEmpty())
        assertEquals(original.terms.first(), introduced.terms.first())
        val designated = HalantyamSutra.apply(introduced).state
        assertTrue(AdyantauTakitauSutra.matches(designated))
        val placed = AdyantauTakitauSutra.apply(designated).state
        assertEquals(listOf("anga", raw.id, "sanac"), placed.terms.map { it.id })
        val result = TasyaLopahSutra.apply(placed).state
        val muk = result.terms[1]
        assertEquals(listOf(Vyanjana.MA), muk.varnas)
        assertTrue(muk.itMarkerProvenance.any { it.marker == ItMarker.KIT })
        assertFalse(muk.itMarkerProvenance.any { it.marker == ItMarker.T })
        assertEquals(original.terms.first(), result.terms.first())
        assertEquals(original.terms.last(), result.terms.last())
        assertFalse(AaneMukSutra.matches(result))
        result.requireCompleteItProcessing()
    }

    @Test fun `non a final angas and imperative shanac are excluded`() {
        for (anga in listOf("भू", "ग्रह्", "पा")) assertFalse(AaneMukSutra.matches(initial(anga)))
        assertFalse(AaneMukSutra.matches(initial(ana = "आनक")))
        val state = initial()
        val imperative = state.copy(terms = listOf(state.terms.first(),
            state.terms.last().copy(createdBySutra = "3.1.83")))
        assertFalse(AaneMukSutra.matches(imperative))
    }

    @Test fun `placement refuses changed final vowel proof`() {
        val introduced = AaneMukSutra.apply(initial()).state
        val designated = HalantyamSutra.apply(introduced).state
        val changed = designated.replaceTerm("anga", designated.terms.first().copy(surface = "पचा"))
        assertFailsWith<IllegalArgumentException> { AdyantauTakitauSutra.apply(changed) }
    }
}
