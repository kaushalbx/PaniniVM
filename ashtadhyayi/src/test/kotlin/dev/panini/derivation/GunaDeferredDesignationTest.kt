package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.core.ItMarker
import dev.panini.shiksha.*
import kotlin.test.*

class GunaDeferredDesignationTest {
    @Test fun `guna transfers surviving deferred marker for exact subsequent lopa`() {
        val stem = DerivationTerm("stem", "अँऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture")
        val raw = DerivationTerm("suffix", "इकँऽट्", TermKind.PRATYAYA)
        val designation = raw.designateVarnaIt(raw.varnas.lastIndex, ItMarker.T, "1.3.3")
        val suffix = raw.copy(deferredItDesignations = listOf(designation))
        val before = DerivationState(listOf(stem, suffix), stage = DerivationStage.PADA_FORMED)
        assertTrue(AdGunaSutra.matches(before))
        val composed = AdGunaSutra.apply(before).state
        val term = composed.terms.single()
        assertEquals(listOf(Svara.E) + suffix.varnas.drop(1), term.varnas)
        assertEquals(stem.id, term.id)
        assertEquals(stem.createdBySutra, term.createdBySutra)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertTrue(term.phonologicalText.effectiveVarnas[2].nasalized)
        assertEquals(setOf(term.varnas.lastIndex), term.deferredItDesignations.single().varnaIndices)
        assertEquals(suffix.surface, composed.droppedTerms.single().originalSurfaceBeforeDrop)
        assertTrue(TasyaLopahSutra.matches(composed))
        val completed = TasyaLopahSutra.apply(composed).state.terms.single()
        assertEquals(term.varnas.dropLast(1), completed.varnas)
        assertTrue(completed.deferredItDesignations.isEmpty())
        assertTrue(completed.itMarkerProvenance.any { it.marker == designation.marker && it.designationSutra == designation.sutra })
    }
}
