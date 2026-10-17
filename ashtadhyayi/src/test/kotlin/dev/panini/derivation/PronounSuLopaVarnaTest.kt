package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.EtattadohSulopoKoAnanjparoHaliSutra
import dev.panini.shiksha.*
import kotlin.test.*

class PronounSuLopaVarnaTest {
    private val rule = EtattadohSulopoKoAnanjparoHaliSutra
    private fun state(left: String, right: String) = DerivationState(listOf(
        DerivationTerm("left", left, TermKind.PRATIPADIKA),
        DerivationTerm("right", right, TermKind.PRATIPADIKA)))

    @Test fun `su deletion preserves nasalization signs identity and exact trace`() {
        for (surface in listOf("सँऽः", "एषँऽः", "सँऽस्", "एषँऽस्")) {
            val original = state(surface, "ददाति")
            assertTrue(rule.matches(original))
            val term = original.terms.first()
            val result = rule.apply(original).state
            val left = result.terms.first()
            assertEquals(term.varnas.dropLast(1), left.varnas)
            assertEquals(term.phonologicalText.effectiveVarnas.dropLast(1), left.phonologicalText.effectiveVarnas)
            assertEquals(term.phonologicalText.sourceOrthographicSigns, left.orthographicSigns)
            assertEquals(term.id, left.id)
            assertEquals(term.upadesha, left.upadesha)
            assertEquals(original.terms.last(), result.terms.last())
            assertEquals(term.varnas.lastIndex, result.substitutions.single().sourceVarnaIndex)
            assertEquals(rule.sutra, result.substitutions.single().sutra)
            assertFalse(rule.matches(result))
        }
    }

    @Test fun `vowels other pronouns kaka and negative compound spellings are excluded`() {
        for ((left, right) in listOf("सः" to "अत्र", "एषः" to "अत्र", "यः" to "ददाति",
            "सकः" to "ददाति", "एषकः" to "ददाति", "असः" to "ददाति", "अनेषः" to "ददाति")) {
            assertFalse(rule.matches(state(left, right)), "$left + $right")
        }
    }
}
