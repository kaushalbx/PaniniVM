package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada4.AdAjadinamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.LunglanglrngksvaduDattaSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

/** Visibility mechanics fixtures, not claimed grammatical derivations of these strings. */
class RutvaVisibilityTest {
    private val origin = RutvaOccurrence(1, "8.2.66")
    private fun initial() = DerivationState(listOf(DerivationTerm("word", "अर्", TermKind.PRATIPADIKA,
        rutvaOccurrences = listOf(origin))))
    private val rules: Map<String, DerivationSutra> =
        mapOf("6.4.71" to LunglanglrngksvaduDattaSutra, "6.4.72" to AdAjadinamSutra)

    @Test fun `rollback restores ru consumed by an invisible substitution`() {
        val changed = initial().replaceTermVarna("word", 1, listOf(Vyanjana.YA), "6.4.71")
        assertTrue(changed.terms.single().rutvaOccurrences.isEmpty())
        assertEquals(listOf(origin), changed.substitutions.single().originalRutvaOccurrences)
        assertEquals(listOf(origin), changed.substitutions.single().copy().originalRutvaOccurrences)
        val visible = RuleVisibility.view(AdAjadinamSutra, changed, rules)
        assertEquals(initial().terms.single().varnas, visible.terms.single().varnas)
        assertEquals(listOf(origin), visible.terms.single().rutvaOccurrences)
        assertTrue(changed.terms.single().rutvaOccurrences.isEmpty())
    }

    @Test fun `multiple invisible changes restore paired position snapshots in reverse order`() {
        val changed = initial().insertTermVarnas("word", 0, listOf(Svara.I), "6.4.71")
            .replaceTermVarna("word", 2, listOf(Vyanjana.YA), "6.4.71")
        val visible = RuleVisibility.view(AdAjadinamSutra, changed, rules)
        assertEquals(initial().terms.single().varnas, visible.terms.single().varnas)
        assertEquals(listOf(origin), visible.terms.single().rutvaOccurrences)
    }

    @Test fun `legacy summary cannot guess occurrence mapping`() {
        val changed = initial().addVarnaSubstitution("word", Svara.A, listOf(Svara.I), "6.4.71")
        assertFailsWith<IllegalArgumentException> { RuleVisibility.view(AdAjadinamSutra, changed, rules) }
    }
}
