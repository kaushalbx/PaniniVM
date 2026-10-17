package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AtoGuneSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EngiPararupamSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class PararupaCompositionMigrationTest {
    private fun state(left: String, right: String) = DerivationState(listOf(
        DerivationTerm("left", left, TermKind.DHATU, upadesha = left),
        DerivationTerm("right", right, TermKind.PRATYAYA, upadesha = right)
    ), stage = DerivationStage.ANGAKARYA)

    @Test fun `affix vowel locus refers to coalesced first vowel not last vowel`() {
        val before = state("भव", "अन्ति")
        assertTrue(AtoGuneSutra.matches(before))
        val after = AtoGuneSutra.apply(before).state
        assertEquals("भवन्ति", after.surface)
        assertEquals("left", after.terms.single().id)
        assertEquals("अन्ति", after.droppedTerms.single().originalSurfaceBeforeDrop)
        assertEquals(1, after.droppedTerms.single().mergedAffixVowelFromEnd)
    }

    @Test fun `following vowel supplies nasality without erasing unrelated annotations`() {
        val after = AtoGuneSutra.apply(state("अँक", "एँत्")).state
        assertEquals("अँकेँत्", after.surface)
        val plainDonor = AtoGuneSutra.apply(state("कँ", "एत्")).state
        assertEquals("केत्", plainDonor.surface)
    }

    @Test fun `identical vowels still consume following term without a fake replacement`() {
        val after = AtoGuneSutra.apply(state("क", "अत्")).state
        assertEquals("कत्", after.surface)
        assertEquals(1, after.terms.size)
        assertTrue(after.substitutions.all { it.targetId == "right" })
    }

    @Test fun `written signs survive at their projected boundary`() {
        val before = state("कऽ", "एत्")
        val after = AtoGuneSutra.apply(before).state
        assertEquals("केत्".toVarnas(), after.terms.single().varnas)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)),
            after.terms.single().orthographicSigns)
    }

    @Test fun `prefix pararupa retains prefix identity and original consumed root`() {
        val before = DerivationState(listOf(
            DerivationTerm("prefix", "प्र", TermKind.PRATIPADIKA, upadesha = "प्र"),
            DerivationTerm("root", "एज्", TermKind.DHATU, upadesha = "एज्")
        ), stage = DerivationStage.ANGAKARYA,
            samjnas = setOf(SamjnaAssignment("prefix", Samjna.UPASARGA)))
        assertTrue(EngiPararupamSutra.matches(before))
        val after = EngiPararupamSutra.apply(before).state
        assertEquals("प्रेज्", after.surface)
        assertEquals("prefix", after.terms.single().id)
        assertEquals("एज्", after.droppedTerms.single().originalSurfaceBeforeDrop)
    }
}
