package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada3.DvyashtanahSankhyayamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.VibhashaChatvarimshatPrabhritauSarveshamSutra
import dev.panini.core.SamasaType
import dev.panini.shiksha.*
import kotlin.test.*

class CompoundNumeralVarnaTest {
    private fun state(left: String, right: String, type: SamasaType? = null) = DerivationState(
        listOf(DerivationTerm("left", left, TermKind.PRATIPADIKA), DerivationTerm("right", right, TermKind.PRATIPADIKA)),
        samjnas = setOf(SamjnaAssignment("right", Samjna.SANKHYA)),
        context = DerivationalContext(samasaType = type))

    @Test fun `mandatory and optional numeral domains are disjoint and exclude bahuvrihi`() {
        for (left in listOf("द्वि", "अष्टन्")) for (right in listOf("दश", "विंशति", "त्रिंशत्")) {
            assertTrue(DvyashtanahSankhyayamSutra.matches(state(left, right)))
            assertFalse(VibhashaChatvarimshatPrabhritauSarveshamSutra.matches(state(left, right)))
            assertFalse(DvyashtanahSankhyayamSutra.matches(state(left, right, SamasaType.BAHUVRIHI)))
        }
        for (left in listOf("द्वि", "अष्टन्", "त्रि")) for (right in listOf("चत्वारिंशत्", "पञ्चाशत्", "षष्टि", "सप्तति", "नवति")) {
            assertTrue(VibhashaChatvarimshatPrabhritauSarveshamSutra.matches(state(left, right)))
            assertFalse(DvyashtanahSankhyayamSutra.matches(state(left, right)))
            assertFalse(VibhashaChatvarimshatPrabhritauSarveshamSutra.matches(state(left, right, SamasaType.BAHUVRIHI)))
        }
        for (right in listOf("अशीति", "शत", "सहस्र", "मातृ")) {
            assertFalse(DvyashtanahSankhyayamSutra.matches(state("द्वि", right)))
            assertFalse(VibhashaChatvarimshatPrabhritauSarveshamSutra.matches(state("द्वि", right)))
        }
    }

    @Test fun `numeral replacements preserve annotated surviving prefixes and exact positions`() {
        for ((word, expected, index) in listOf(
            Triple("द्विँऽ", "द्वाँऽ", 2), Triple("अष्टँन्ऽ", "अष्टाँऽ", 3), Triple("त्रिँऽ", "त्रँयस्ऽ", 2))) {
            val original = state(word, "चत्वारिंशत्")
            val rule = VibhashaChatvarimshatPrabhritauSarveshamSutra
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            assertEquals(expected, result.terms.first().surface)
            assertEquals(original.terms.first().upadesha, result.terms.first().upadesha)
            assertEquals(original.terms.last(), result.terms.last())
            assertEquals(index, result.substitutions.single().sourceVarnaIndex)
            assertFalse(rule.matches(result))
        }
    }

    @Test fun `optional numeral substitution retains both branches without reapplying`() {
        val rule = VibhashaChatvarimshatPrabhritauSarveshamSutra
        val engine = DerivationEngine(listOf(rule))
        val initial = state("द्वि", "चत्वारिंशत्")
        val outcomes = engine.deriveAll(initial)
        assertEquals(setOf("द्वि", "द्वा"), outcomes.map { it.final.terms.first().surface }.toSet())
        assertTrue(outcomes.all { it.applications.count { step -> step.sutra == rule.sutra } <= 1 })
        assertEquals(2, outcomes.size)
    }
}
