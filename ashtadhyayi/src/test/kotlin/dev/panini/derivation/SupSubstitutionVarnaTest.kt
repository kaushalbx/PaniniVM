package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada1.*
import dev.panini.core.SupAffix
import dev.panini.shiksha.Samjna
import kotlin.test.*

class SupSubstitutionVarnaTest {
    private fun state(surface: String, upadesha: String, source: SupAffix? = null) = DerivationState(
        listOf(DerivationTerm("stem", "सर्व", TermKind.PRATIPADIKA),
            DerivationTerm("arbitrary-affix-id", surface, TermKind.PRATYAYA, upadesha = upadesha, sourceSupAffix = source)),
        activeAdhikaras = setOf("6.4.1"), samjnas = setOf(SamjnaAssignment("stem", Samjna.SARVANAMA)))

    @Test
    fun `ordered ablative and locative replacements use sup origin not ids`() {
        for ((source, expected) in listOf(SupAffix.NGASI to "स्मात्", SupAffix.NGI to "स्मिन्")) {
            val original = state("अ", "substitute", source)
            assertTrue(NasinyohSmatsminauSutra.matches(original))
            val result = NasinyohSmatsminauSutra.apply(original).state
            assertEquals(expected, result.terms.last().surface)
            assertEquals(source, result.terms.last().sourceSupAffix)
            assertEquals(original.terms.last().id, result.terms.last().id)
            assertEquals(original.terms.first(), result.terms.first())
            assertFalse(NasinyohSmatsminauSutra.matches(result.replaceTerm(result.terms.last().id, result.terms.last().copy(surface = expected + "ऽ"))))
        }
        val unrelated = state("अ", "substitute")
        assertFalse(NasinyohSmatsminauSutra.matches(unrelated.replaceTerm(unrelated.terms.last().id, unrelated.terms.last().copy(id = "sup-ngasi"))))
    }

    @Test
    fun `general ordered substitutions preserve final vowels and pronoun exclusions`() {
        for ((source, expected) in listOf(SupAffix.TA to "इन", SupAffix.NGASI to "आत्", SupAffix.NGAS to "स्य")) {
            val original = state(source.upadesha, source.upadesha)
            assertTrue(TangasingsamInatsyahSutra.matches(original))
            val result = TangasingsamInatsyahSutra.apply(original).state
            assertEquals(expected, result.terms.last().surface)
            assertFalse(TangasingsamInatsyahSutra.matches(result.replaceTerm(result.terms.last().id, result.terms.last().copy(surface = expected + "ऽ"))))
            for (completedPronoun in listOf("स्मात्ऽ", "स्मिन्ऽ", "स्मैँऽ")) {
                assertFalse(TangasingsamInatsyahSutra.matches(original.replaceTerm(original.terms.last().id, original.terms.last().copy(surface = completedPronoun))))
            }
        }
    }

    @Test
    fun `dative substitutions inspect canonical varnas and typed sup origin`() {
        for (surface in listOf("ङेँऽ", "एँऽ")) {
            val original = state(surface, SupAffix.NGE.upadesha)
            assertTrue(NgeryahSutra.matches(original))
            val result = NgeryahSutra.apply(original).state
            assertEquals("य", result.terms.last().surface)
            assertEquals(original.terms.first(), result.terms.first())
            assertFalse(NgeryahSutra.matches(result))
        }
        val original = state("एँऽ", "substitute", SupAffix.NGE)
        assertTrue(SarvanamnasSmaiSutra.matches(original))
        val result = SarvanamnasSmaiSutra.apply(original).state
        assertEquals("स्मै", result.terms.last().surface)
        assertEquals(SupAffix.NGE, result.terms.last().sourceSupAffix)
        assertEquals(original.terms.last().id, result.terms.last().id)
        assertFalse(SarvanamnasSmaiSutra.matches(result.replaceTerm(result.terms.last().id, result.terms.last().copy(surface = "स्मैँऽ"))))
        val unrelated = state("ए", "substitute")
        assertFalse(SarvanamnasSmaiSutra.matches(unrelated.replaceTerm(unrelated.terms.last().id, unrelated.terms.last().copy(id = "sup-nge"))))
    }

    @Test
    fun `shi uses sup origin rather than term id and retains fresh it lifecycle`() {
        val original = state("अस्", "substitute", SupAffix.JAS)
        assertTrue(JasahShiSutra.matches(original))
        val result = JasahShiSutra.apply(original).state
        val ending = result.terms.last()
        assertEquals("शी", ending.surface)
        assertEquals("शी", ending.upadesha)
        assertEquals(SupAffix.JAS, ending.sourceSupAffix)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, ending.itProcessingPhase)
        assertEquals(original.terms.first(), result.terms.first())
        assertFalse(JasahShiSutra.matches(result))
        assertFalse(JasahShiSutra.matches(original.replaceTerm(original.terms.last().id, original.terms.last().copy(surface = "ईँऽ"))))
        val unrelated = state("अस्", "substitute")
        assertFalse(JasahShiSutra.matches(unrelated.replaceTerm(unrelated.terms.last().id, unrelated.terms.last().copy(id = "sup-jas"))))
    }
}
