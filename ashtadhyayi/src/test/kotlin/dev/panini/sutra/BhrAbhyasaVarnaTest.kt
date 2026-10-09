package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya7.pada4.BhrnamItSutra
import dev.panini.derivation.*
import dev.panini.shiksha.*
import kotlin.test.*

class BhrAbhyasaVarnaTest {
    private fun state(upadesha: String) = DerivationState(
        terms = listOf(
            DerivationTerm("abhyasa", "भृ", TermKind.DHATU, upadesha="भृ"),
            DerivationTerm("root", "भृ", TermKind.DHATU, upadesha=upadesha),
        ),
        samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
    )

    @Test fun `bhr n marker belongs to root identity not its current spelling`() {
        val input=state("डुभृञ्")
        assertTrue(BhrnamItSutra.matches(input))
        assertFalse(BhrnamItSutra.matches(state("भृ")))
        val result=BhrnamItSutra.apply(input).state
        assertEquals(listOf(Vyanjana.BHA, Svara.I), result.terms.first().varnas)
        assertEquals(input.terms.first().id, result.terms.first().id)
        assertEquals(input.terms.first().upadesha, result.terms.first().upadesha)
        assertEquals(input.terms.last(), result.terms.last())
        assertEquals("7.4.76", result.substitutions.last().sutra)
    }
}
