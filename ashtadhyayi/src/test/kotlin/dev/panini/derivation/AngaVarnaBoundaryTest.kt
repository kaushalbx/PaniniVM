package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.AccaGhehSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.AngiCapahSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AngaVarnaBoundaryTest {
    private fun state(stem: String, affix: SupAffix, ghi: Boolean = false): DerivationState =
        DerivationState(
            terms = listOf(DerivationTerm("stem", stem, TermKind.PRATIPADIKA), affix.term()),
            activeAdhikaras = setOf("6.4.1"),
            samjnas = if (ghi) setOf(SamjnaAssignment("stem", Samjna.GHI)) else emptySet(),
        )

    @Test
    fun `ghi locative applicability retains exact sup domain`() {
        for (affix in SupAffix.entries) {
            assertEquals(affix == SupAffix.NGI, AccaGhehSutra.matches(state("हरि", affix, ghi = true)), affix.name)
            assertEquals(false, AccaGhehSutra.matches(state("हरि", affix)), affix.name)
        }
    }

    @Test
    fun `ap applicability includes both os slots without admitting other endings`() {
        for (affix in SupAffix.entries) {
            val expected = affix in setOf(SupAffix.TA, SupAffix.OS_6, SupAffix.OS_7)
            assertEquals(expected, AngiCapahSutra.matches(state("लता", affix)), affix.name)
            assertEquals(false, AngiCapahSutra.matches(state("हरि", affix)), affix.name)
        }
    }

    @Test
    fun `ghi substitution retains stem identity and consumes old affix designations`() {
        for (stem in listOf("हरि", "गुरु")) {
            val original = state(stem, SupAffix.NGI, ghi = true)
            val result = AccaGhehSutra.apply(original).state
            assertEquals(original.terms.first().varnas.dropLast(1) + Svara.A, result.terms.first().varnas)
            assertEquals(listOf(Svara.AU), result.terms.last().varnas)
            assertEquals("औ", result.terms.last().upadesha)
            assertEquals(original.terms.map { it.id }, result.terms.map { it.id })
            assertEquals(original.samjnas, result.samjnas)
            assertTrue(result.terms.last().itMarkers.isEmpty())
            assertEquals(DerivationStage.ANGAKARYA, result.stage)
            assertEquals(false, AccaGhehSutra.matches(result), "Consumed ṅi must not reapply through sthānin identity")
        }
    }

    @Test
    fun `ap replacement is an exact final vowel substitution`() {
        val original = state("लता", SupAffix.TA)
        val result = AngiCapahSutra.apply(original).state
        assertEquals(listOf(Vyanjana.LA, Svara.A, Vyanjana.TA, Svara.E), result.terms.first().varnas)
        assertEquals("लते", result.terms.first().surface)
        assertEquals(original.terms.last(), result.terms.last())
        assertEquals(original.terms.first().upadesha, result.terms.first().upadesha)
        assertEquals(original.terms.first().id, result.terms.first().id)
    }
}
