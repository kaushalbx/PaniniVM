package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.*
import dev.panini.derivation.*
import dev.panini.shiksha.*
import kotlin.test.*

class ConsumedAffixSignProjectionTest {
    // Core composition fixtures, not grammatical rules or derivation shortcuts.
    private fun compose(before: DerivationState, replacement: List<Varna>, vowelIndex: Int): DerivationState {
        val affix = before.terms.last()
        return before.substituteFinalVarnaAndConsumeFollowingTerm("stem", "affix", replacement, "fixture",
            incorporatedAffixVowelIndex = vowelIndex,
            annotationDonor = affix.phonologicalText.effectiveVarnas.firstOrNull { it.varna is Svara },
            consumedSignProjection = affix.consumedVowelAffixSignProjection(vowelIndex, replacement.size))
    }
    private fun state(sup: SupAffix, surface: String, stem: String = "मनस्") = DerivationState(
        listOf(DerivationTerm("stem", stem, TermKind.PRATIPADIKA),
            sup.term("affix").copy(surface = surface, itProcessingPhase = ItProcessingPhase.PROCESSED)),
        context = DerivationalContext(rupa = Rupa(linga = Linga.NAPUMSAKA,
            vibhakti = sup.vibhakti, vacana = sup.vacana)))

    @Test fun `affix terminal avagraha survives without becoming a varna`() {
        val before = state(SupAffix.NGE, "एँऽ")
        val after = compose(before, listOf(Vyanjana.SA, Svara.E), 1)
        assertEquals("मनसेँऽ", after.surface)
        assertEquals("मनसे".toVarnas(), after.terms.single().varnas)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 6)), after.terms.single().orthographicSigns)
        assertEquals("एँऽ", after.droppedTerms.single().originalSurfaceBeforeDrop)
        assertEquals(4, after.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `internal vowel boundary and terminal boundary remain distinct`() {
        val after = compose(state(SupAffix.OS_6, "ओऽस्ऽ"), listOf(Vyanjana.SA, Svara.O, Ayogavaha.VISARGA), 1)
        assertEquals("मनसोऽःऽ", after.surface)
        assertEquals(listOf(6, 7), after.terms.single().orthographicSigns.map { it.afterVarnaCount })
        assertEquals("मनसोः".toVarnas(), after.terms.single().varnas)
    }

    @Test fun `signs from two consumed operands are not deduplicated against each other`() {
        val after = compose(state(SupAffix.NGE, "एऽ", "मनस्ऽ"), listOf(Vyanjana.SA, Svara.E), 1)
        assertEquals("मनसेऽऽ", after.surface)
        assertEquals(2, after.terms.single().orthographicSigns.size)
    }

    @Test fun `glyph qualifier on replaced s is explicitly consumed not applied to visarga`() {
        val before = state(SupAffix.OS_6, "ओस्\u200D")
        val after = compose(before, listOf(Vyanjana.SA, Svara.O, Ayogavaha.VISARGA), 1)
        assertEquals("मनसोः", after.surface)
        assertTrue(after.terms.single().orthographicSigns.isEmpty())
        assertEquals(before.terms.last().surface, after.droppedTerms.single().originalSurfaceBeforeDrop)
    }

    @Test fun `missing or out of range projection cannot silently erase signs`() {
        val before = state(SupAffix.NGE, "एऽ")
        assertFailsWith<IllegalArgumentException> {
            before.substituteFinalVarnaAndConsumeFollowingTerm("stem", "affix",
                listOf(Vyanjana.SA, Svara.E), "fixture", incorporatedAffixVowelIndex = 1)
        }
        val source = OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 1)
        assertFailsWith<IllegalArgumentException> {
            before.substituteFinalVarnaAndConsumeFollowingTerm("stem", "affix",
                listOf(Vyanjana.SA, Svara.E), "fixture", incorporatedAffixVowelIndex = 1,
                consumedSignProjection = mapOf(source to source.copy(afterVarnaCount = 3)))
        }
    }

    @Test fun `consonant vowel replacement fixtures use the same explicit sign policy`() {
        val before = state(SupAffix.NGE, "एऽ", "मति").copy(
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI,
                vibhakti = Vibhakti.CHATURTHI, vacana = Vacana.EKAVACANA)),
            samjnas = setOf(SamjnaAssignment("stem", Samjna.GHI)))
        val after = compose(before, listOf(Vyanjana.YA, Svara.AI), 1)
        assertEquals("मत्यैऽ", after.surface)
        assertEquals("मत्यै".toVarnas(), after.terms.single().varnas)
    }
}
