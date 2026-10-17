package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.yogavibhaga.EkaDasaAtvaYogavibhaga
import dev.panini.sutra.runtime.SutraSource
import kotlin.test.*

class EkaDasaYogavibhagaTest {
    private val rule = EkaDasaAtvaYogavibhaga
    private fun state(right: String = "दश", domain: TermCompositionDomain? = TermCompositionDomain.SANKHYA) =
        DerivationState(listOf(
            DerivationTerm("eka", "एक", TermKind.PRATIPADIKA, compositionDomain = domain),
            DerivationTerm("dasa", right, TermKind.PRATIPADIKA, compositionDomain = domain)))

    @Test fun `attested operation requires numeral construction and exact lexical members`() {
        assertFalse(rule.matches(state(domain = null)))
        assertFalse(rule.matches(state("विंशति")))
        assertFalse(rule.matches(state("दशक")))
        val initial = state()
        assertTrue(rule.matches(initial))
        val changed = rule.apply(initial).state
        assertEquals("एका", changed.terms.first().surface)
        assertEquals(initial.terms.first().upadesha, changed.terms.first().upadesha)
        assertEquals(initial.terms.last(), changed.terms.last())
        assertEquals(2, changed.substitutions.single().sourceVarnaIndex)
        assertFalse(rule.matches(changed))
    }

    @Test fun `annotated occurrence and source survive a single application`() {
        val original = state()
        val annotated = original.copy(terms = listOf(
            original.terms.first().copy(surface = "एकँऽ"), original.terms.last()))
        val result = DerivationEngine(listOf(rule)).derive(annotated)
        assertEquals("एकाँऽ", result.final.terms.first().surface)
        assertEquals("एक", result.final.terms.first().upadesha)
        assertEquals(1, result.applications.size)
        assertEquals(rule.source, result.applications.single().source)
    }

    @Test fun `commentarial source is registered separately from canonical patha`() {
        assertIs<SutraSource.Yogavibhaga>(rule.source)
        assertEquals("6.3.46", rule.source.parentSutra)
        assertEquals(1, Ashtadhyayi.registry.sutras.count { it.number == rule.number })
        assertFalse(Ashtadhyayi.cataloguedSutras.any { it.number == rule.number })
        assertFalse(Ashtadhyayi.registry.sutras.any { it.number == "6.3.76" })
        assertTrue(Ashtadhyayi.catalogIssues.none { it.sutra == rule.sutra }, Ashtadhyayi.catalogIssues.toString())
    }
}
