package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.BhoBhagoAghoApurvasyaYoshiSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.BhoBhagoSutra
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

class RuYatvaApplicabilityTest {
    @Test fun `compatibility entry point shares canonical scope and exact mutations`() {
        assertEquals(BhoBhagoAghoApurvasyaYoshiSutra.scope, BhoBhagoSutra.scope)
        assertEquals(BhoBhagoAghoApurvasyaYoshiSutra.stage, BhoBhagoSutra.stage)
        for (left in listOf("देवाः", "भोः", "विभोः", "प्रातर्", "हरिः")) {
            val input = state(left)
            val expected = BhoBhagoAghoApurvasyaYoshiSutra.matches(input)
            assertEquals(expected, BhoBhagoSutra.matches(input), left)
            if (expected) {
                val canonical = BhoBhagoAghoApurvasyaYoshiSutra.apply(input)
                val compatibility = BhoBhagoSutra.apply(input)
                assertEquals(canonical.state.terms, compatibility.state.terms)
                assertEquals(canonical.state.substitutions, compatibility.state.substitutions)
                assertEquals(canonical.explanation, compatibility.explanation)
            }
        }
    }

    private fun state(left: String, right: String = "अत्र") = DerivationState(listOf(
        DerivationTerm("left", left, TermKind.PRATIPADIKA),
        DerivationTerm("right", right, TermKind.PRATIPADIKA),
    ))

    @Test fun `rule rejects other vowels native r and mere lexical suffix resemblance`() {
        for (left in listOf("हरिः", "शम्भुः", "अग्निर्", "वायुर्", "प्रातर्", "पुनर्", "विभोः", "अस")) {
            assertFalse(BhoBhagoAghoApurvasyaYoshiSutra.matches(state(left)), left)
        }
        assertFalse(BhoBhagoAghoApurvasyaYoshiSutra.matches(state("देवः", "करोति")))
    }

    @Test fun `a aa and exact lexical bases receive consonantal y without an extra vowel`() {
        for ((left, expected) in listOf("देवः" to "देवय्", "देवाः" to "देवाय्",
            "भोः" to "भोय्", "भगोः" to "भगोय्", "अघोः" to "अघोय्")) {
            val original = state(left)
            assertTrue(BhoBhagoAghoApurvasyaYoshiSutra.matches(original), left)
            val result = BhoBhagoAghoApurvasyaYoshiSutra.apply(original).state
            assertEquals(expected, result.terms.first().surface)
            assertEquals(Vyanjana.YA, result.terms.first().varnas.last())
            assertEquals(original.terms.first().varnas.size, result.terms.first().varnas.size)
            assertEquals(original.terms.first().varnas.lastIndex, result.substitutions.single().sourceVarnaIndex)
        }
    }

    @Test fun `r from rutva is eligible but native r is not`() {
        val original = state("देवार्")
        assertFalse(BhoBhagoAghoApurvasyaYoshiSutra.matches(original))
        val withRutva = original.addVarnaSubstitution("left", Vyanjana.SA, listOf(Vyanjana.RA), "8.2.66")
        assertTrue(BhoBhagoAghoApurvasyaYoshiSutra.matches(withRutva))
        assertEquals("देवाय्", BhoBhagoAghoApurvasyaYoshiSutra.apply(withRutva).state.terms.first().surface)
    }
}
