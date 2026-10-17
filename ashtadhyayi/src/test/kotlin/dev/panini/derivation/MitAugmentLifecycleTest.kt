package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada1.MidacoAntyatParahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.NaVibhaktauTusmahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.CaturanuduhorAmSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.SavAnaduhahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.IkoYanAciSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

class MitAugmentLifecycleTest {
    @Test fun `num retains pronunciation-only u and independently deletes m after placement`() {
        val stem = DerivationTerm("stem", "अनडुआह्", TermKind.PRATIPADIKA, upadesha = "अनडुह्")
        val initial = DerivationState(listOf(stem, SupAffix.SU.term()),
            activeAdhikaras = setOf("6.4.1"), stage = DerivationStage.ANGAKARYA)
        assertFalse(SavAnaduhahSutra.matches(initial.copy(terms = listOf(stem, SupAffix.JAS.term()))))
        assertFalse(SavAnaduhahSutra.matches(initial.replaceTerm(stem.id, stem.copy(surface = "अनडुह्"))))
        var state = SavAnaduhahSutra.apply(initial).state
        val raw = state.terms.last()
        assertEquals("नुम्", raw.upadesha)
        assertEquals(listOf(Vyanjana.NA, Vyanjana.MA), raw.varnas)
        assertEquals("ु", raw.nonOperativeUpadeshaSegments.single().text)
        state = HalantyamSutra.apply(state).state
        state = MidacoAntyatParahSutra.apply(state).state
        state = TasyaLopahSutra.apply(state).state
        assertEquals(listOf(Svara.A, Vyanjana.NA, Svara.A, Vyanjana.DDA,
            Svara.U, Svara.AA, Vyanjana.NA, Vyanjana.HA), state.terms.first().varnas)
        assertFalse(SavAnaduhahSutra.matches(state))
        val archived = state.droppedTerms.single()
        assertEquals(stem.id, archived.mergedIntoTermId)
        assertEquals(raw.nonOperativeUpadeshaSegments, archived.nonOperativeUpadeshaSegments)
    }

    @Test fun `am producer leaves stem intact and accent survives independent placement and yan`() {
        val stem = DerivationTerm("stem", "चतुर्", TermKind.PRATIPADIKA)
        var state = DerivationState(listOf(stem, SupAffix.JAS.term()),
            activeAdhikaras = setOf("6.4.1"), stage = DerivationStage.ANGAKARYA)
        state = CaturanuduhorAmSutra.apply(state).state
        assertEquals(stem, state.terms.first())
        val augment = state.terms.last()
        assertEquals(TermKind.AGAMA, augment.kind)
        assertEquals(listOf(Svara.AA, Vyanjana.MA), augment.varnas)
        assertEquals(listOf(GrammaticalAccent(0, AccentType.UDATTA, "7.1.98")), augment.grammaticalAccents)
        assertFalse(NaVibhaktauTusmahSutra.matches(state))
        assertFalse(CaturanuduhorAmSutra.matches(state))
        state = HalantyamSutra.apply(state).state
        state = MidacoAntyatParahSutra.apply(state).state
        state = TasyaLopahSutra.apply(state).state
        assertEquals(listOf(Svara.U, Svara.AA), state.terms.first().varnas.subList(3, 5))
        assertTrue(IkoYanAciSutra.matches(state))
        state = IkoYanAciSutra.apply(state).state
        assertEquals(listOf(Vyanjana.CA, Svara.A, Vyanjana.TA, Vyanjana.VA,
            Svara.AA, Vyanjana.RA), state.terms.first().varnas)
        assertEquals(listOf(GrammaticalAccent(4, AccentType.UDATTA, "7.1.98")), state.terms.first().grammaticalAccents)
        assertFalse(CaturanuduhorAmSutra.matches(state))
    }

    @Test fun `agama m designation is placed and consumed without deleting target consonant`() {
        val stem = DerivationTerm("stem", "अनडुह्", TermKind.PRATIPADIKA)
        val augment = DerivationTerm("am", "आम्", TermKind.AGAMA,
            createdBySutra = "7.1.98", augmentTargetId = stem.id,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        var state = DerivationState(listOf(stem, augment))
        assertFalse(MidacoAntyatParahSutra.matches(state))
        state = HalantyamSutra.apply(state).state
        assertTrue(MidacoAntyatParahSutra.matches(state))
        state = MidacoAntyatParahSutra.apply(state).state
        assertEquals(listOf(Svara.A, Vyanjana.NA, Svara.A, Vyanjana.DDA,
            Svara.U, Svara.AA, Vyanjana.MA, Vyanjana.HA), state.terms.single().varnas)
        assertEquals(setOf(6), state.terms.single().itDesignations.single().varnaIndices)
        val archived = state.droppedTerms.single()
        assertEquals(stem.id, archived.mergedIntoTermId)
        assertEquals("7.1.98", archived.createdBySutra)
        assertEquals(augment.id, archived.id)
        assertFalse(MidacoAntyatParahSutra.matches(state))
        state = TasyaLopahSutra.apply(state).state
        assertEquals(listOf(Svara.A, Vyanjana.NA, Svara.A, Vyanjana.DDA,
            Svara.U, Svara.AA, Vyanjana.HA), state.terms.single().varnas)
        state.requireCompleteItProcessing()
    }

    @Test fun `ordinary affix final m without an augment target is not placed`() {
        val affix = DerivationTerm("am", "अम्", TermKind.PRATYAYA,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        val state = HalantyamSutra.apply(DerivationState(listOf(affix))).state
        assertFalse(MidacoAntyatParahSutra.matches(state))
    }
}
