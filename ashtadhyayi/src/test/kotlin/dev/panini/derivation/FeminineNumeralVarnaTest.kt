package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada2.TisrCatasruSutra
import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.shiksha.*
import kotlin.test.*

class FeminineNumeralVarnaTest {
    private val rule = TisrCatasruSutra
    private fun state(word: String, affix: DerivationTerm = SupAffix.JAS.term("renamed"),
        linga: Linga = Linga.STRI) = DerivationState(
        listOf(DerivationTerm("stem", word, TermKind.PRATIPADIKA), affix),
        context = DerivationalContext(rupa = Rupa(linga = linga)))

    @Test fun `feminine allomorphs preserve prefix annotations and exact correspondence`() {
        for ((word, expected, index) in listOf(
            Triple("त्रिँऽ", "तिँसृऽ", 1), Triple("चँतुँर्ऽ", "चँतँसृऽ", 3))) {
            val original = state(word)
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            val stem = result.terms.first()
            assertEquals(expected, stem.surface)
            assertEquals(original.terms.first().id, stem.id)
            assertEquals(original.terms.first().upadesha, stem.upadesha)
            assertEquals(original.terms.last(), result.terms.last())
            assertEquals(index, result.substitutions.single().sourceVarnaIndex)
            assertEquals(word, result.substitutions.single().originalSurface)
            // Written spans belong to each rendering: त् before र् and ति
            // have different spans despite the same surviving t occurrence.
            assertEquals(original.terms.first().phonologicalText.effectiveVarnas.take(index).map { it.copy(sourceSpan = null) },
                stem.phonologicalText.effectiveVarnas.take(index).map { it.copy(sourceSpan = null) })
            assertFalse(rule.matches(result))
        }
    }

    @Test fun `source sup identity is required independently of display names`() {
        val fake = DerivationTerm("sup", "जस्", TermKind.PRATYAYA)
        assertFalse(rule.matches(state("त्रि", fake)))
        val dropped = consumeAffixForDrop(SupAffix.JAS.term("old"), "test-drop")
        assertTrue(rule.matches(state("त्रि", fake).copy(droppedTerms = listOf(dropped))))
        for (linga in listOf(Linga.PUMS, Linga.NAPUMSAKA)) assertFalse(rule.matches(state("त्रि", linga = linga)))
        for (word in listOf("त्रयस्", "तिसृ", "चतसृ", "चत्वार्")) assertFalse(rule.matches(state(word)))
    }
}
