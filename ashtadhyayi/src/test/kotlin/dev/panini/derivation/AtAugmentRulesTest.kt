package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AtashCaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.AdAjadinamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.LunglanglrngksvaduDattaSutra
import dev.panini.core.Lakara
import dev.panini.shiksha.*
import kotlin.test.*

class AtAugmentRulesTest {
    private fun initial(root: String, lakara: Lakara = Lakara.LANG) = DerivationState(listOf(
        DerivationTerm("root", root, TermKind.DHATU, upadesha = root),
        DerivationTerm("lakara", lakara.upadesha, TermKind.PRATYAYA, upadesha = lakara.upadesha)
    ), stage = DerivationStage.ANGAKARYA)

    @Test fun `aat requires vowel initial past tense base`() {
        for (lakara in listOf(Lakara.LANG, Lakara.LUNG, Lakara.LRNG)) {
            val before = initial("ईक्ष्", lakara)
            assertTrue(AdAjadinamSutra.matches(before))
            val after = AdAjadinamSutra.apply(before).state
            val augment = after.terms.first()
            assertEquals("आट्", augment.upadesha)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
            assertEquals("root", augment.augmentTargetId)
            assertEquals(listOf(GrammaticalAccent(0, AccentType.UDATTA, "6.4.72")), augment.grammaticalAccents)
            assertFalse(AdAjadinamSutra.matches(after))
            assertFalse(LunglanglrngksvaduDattaSutra.matches(after))
        }
        assertFalse(AdAjadinamSutra.matches(initial("भू")))
        assertFalse(AdAjadinamSutra.matches(initial("ईक्ष्", Lakara.LAT)))
    }

    @Test fun `aat coalescence covers every vowel and transfers its grammatical accent`() {
        for (vowel in Svara.entries) {
            val root = DerivationTerm("root", listOf(vowel, Vyanjana.KA).toDevanagari(), TermKind.DHATU)
            val augment = DerivationTerm("at-agama", "आ", TermKind.AGAMA, upadesha = "आट्",
                createdBySutra = "6.4.72", augmentTargetId = root.id, mergeIntoAugmentTarget = false,
                establishedBySutras = setOf("1.1.46"), itProcessingPhase = ItProcessingPhase.PROCESSED,
                grammaticalAccents = listOf(GrammaticalAccent(0, AccentType.UDATTA, "6.4.72")))
            val before = DerivationState(listOf(augment, root), stage = DerivationStage.ANGAKARYA)
            assertTrue(AtashCaSutra.matches(before))
            val after = AtashCaSutra.apply(before).state
            assertEquals(requireNotNull(Varnamala.getVrddhi(vowel)) + Vyanjana.KA, after.terms.single().varnas)
            assertEquals("root", after.terms.single().id)
            assertEquals(augment.grammaticalAccents, after.terms.single().grammaticalAccents)
            assertEquals(augment.grammaticalAccents, after.droppedTerms.single().originalGrammaticalAccentsBeforeDrop)
            assertTrue(after.droppedTerms.single().grammaticalAccents.isEmpty())
            assertFalse(AtashCaSutra.matches(before.copy(terms = listOf(augment.copy(createdBySutra = "fixture"), root))))
            assertFalse(AtashCaSutra.matches(before.copy(terms = listOf(augment.copy(augmentTargetId = "other"), root))))
        }
    }
}
