package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.NashcapadantasyaSutra
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnusvaraVarnaTest {
    @Test
    fun `restoring internal nasal after advancing tripadi is not a cycle`() {
        val result = SandhiEngine().join("साम्प्रतम्", "धैर्यम्")
        assertEquals(DerivationStage.FINAL, result.final.stage)
        val rules = result.applications.map { it.sutra }
        assertTrue("8.3.24" in rules)
        assertTrue("8.4.58" in rules)
        assertTrue(rules.indexOf("8.3.24") < rules.indexOf("8.4.58"))
        assertEquals(1, rules.count { it == "8.3.24" })
        assertEquals("साम्प्रतन्धैर्यम्", result.final.surface)
    }

    @Test
    fun `a vowel after an earlier nasal does not conceal a later eligible nasal`() {
        val state = DerivationState(listOf(DerivationTerm("word", "नम्स", TermKind.PRATIPADIKA)))
        assertTrue(NashcapadantasyaSutra.matches(state))
        assertEquals("नंस", NashcapadantasyaSutra.apply(state).state.surface)
    }

    @Test
    fun `word final n is outside the non final nasal rule`() {
        val left = DerivationTerm("left", "तान्", TermKind.PRATIPADIKA)
        val state = DerivationState(
            listOf(left, DerivationTerm("right", "स", TermKind.PRATIPADIKA)),
            samjnas = setOf(SamjnaAssignment(left.id, Samjna.PADA)),
        )
        assertFalse(NashcapadantasyaSutra.matches(state))
    }
}
