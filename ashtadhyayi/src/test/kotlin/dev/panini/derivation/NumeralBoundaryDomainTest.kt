package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.IkoYanAciSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class NumeralBoundaryDomainTest {
    @Test
    fun `mixed constructions do not acquire numeral internal boundary selection`() {
        for ((left, right, matches) in listOf(
            Triple("त्रि", "अ", IkoYanAciSutra::matches),
            Triple("अ", "अ", SavarnaDirghaSutra::matches),
        )) {
            val original = state(left, right, true)
            val mixed = original.copy(terms = original.terms.dropLast(1) + original.terms.last().copy(compositionDomain = null))
            assertFalse(matches(mixed))
        }
    }

    private fun state(left: String, right: String, typed: Boolean, misleadingIds: Boolean = false) = DerivationState(
        terms = listOf(left, right, "क").mapIndexed { index, text ->
            DerivationTerm(if (misleadingIds) "sankhya_$index" else "member-$index", text, TermKind.PRATIPADIKA,
                compositionDomain = if (typed) TermCompositionDomain.SANKHYA else null)
        },
        stage = DerivationStage.IT_PROCESSED,
    )

    @Test
    fun `yan selects the first eligible internal boundary independent of identifiers`() {
        val typed = state("त्रि", "अ", true)
        assertTrue(IkoYanAciSutra.matches(typed))
        val result = IkoYanAciSutra.apply(typed).state
        assertEquals("त्र्य", result.terms.first().surface)
        assertEquals("क", result.terms.last().surface)
        assertTrue(result.terms.all { it.compositionDomain == TermCompositionDomain.SANKHYA })
        assertFalse(IkoYanAciSutra.matches(state("त्रि", "अ", false, misleadingIds = true)))
    }

    @Test
    fun `dirgha selects the first eligible internal boundary independent of identifiers`() {
        val typed = state("अ", "अ", true)
        assertTrue(SavarnaDirghaSutra.matches(typed))
        val result = SavarnaDirghaSutra.apply(typed).state
        assertEquals("आ", result.terms.first().surface)
        assertEquals("क", result.terms.last().surface)
        assertFalse(SavarnaDirghaSutra.matches(state("अ", "अ", false, misleadingIds = true)))
    }
}
