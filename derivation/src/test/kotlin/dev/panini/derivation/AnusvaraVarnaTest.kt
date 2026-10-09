package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.NashcapadantasyaSutra
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnusvaraVarnaTest {
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
