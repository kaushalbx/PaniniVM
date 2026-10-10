package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.derivation.*
import kotlin.test.*

class YatYukAnnotationTest {
    @Test fun `yuk merged range retains o nasalization and orthographic sign`() {
        val term = DerivationTerm("stem", "मालओँस्ऽ", TermKind.PRATIPADIKA, upadesha = "माला", createdBySutra = "fixture")
        val state = DerivationState(listOf(term), stage = DerivationStage.ANGAKARYA,
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI)))
        assertTrue(AtoYukSutra.matches(state))
        val result = AtoYukSutra.apply(state).state
        assertEquals("मालयोँःऽ", result.surface)
        assertEquals(term.id, result.terms.single().id)
        assertEquals(term.upadesha, result.terms.single().upadesha)
        assertEquals(term.createdBySutra, result.terms.single().createdBySutra)
        assertEquals(term.varnas.size - 2, result.substitutions.single().sourceVarnaIndex)
        assertFalse(AtoYukSutra.matches(result))
    }

    @Test fun `yat and yuk stem shortening preserves nasalization and signs`() {
        val stem = DerivationTerm("stem", "मालाँऽ", TermKind.PRATIPADIKA, upadesha = "माला")
        for ((affix, apply) in listOf(
            DerivationTerm("os", "ओस्", TermKind.PRATYAYA, upadesha = "ओस्") to AtoYukSutra::apply,
            DerivationTerm("ta", "आ", TermKind.PRATYAYA, upadesha = "टा") to YadapahSutra::apply,
        )) {
            val original = DerivationState(listOf(stem, affix), stage = DerivationStage.ANGAKARYA)
            val result = apply(original).state
            val changed = result.terms.first()
            assertEquals("मालँऽ", changed.surface)
            assertEquals(stem.id, changed.id)
            assertEquals(stem.upadesha, changed.upadesha)
            assertEquals(stem.varnas.lastIndex, result.substitutions.single().sourceVarnaIndex)
            assertEquals(affix.id, result.terms.last().id)
            assertEquals(affix.upadesha, result.terms.last().upadesha)
        }
    }
}
