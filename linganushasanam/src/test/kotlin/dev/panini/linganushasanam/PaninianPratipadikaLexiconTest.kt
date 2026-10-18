package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaninianPratipadikaLexiconTest {
    @Test
    fun `gets gender from linganushasanam with provenance`() {
        val entry = requireNotNull(PaninianPratipadikaLexicon.findPratipadika("मनस्"))
        assertEquals(setOf(Linga.NAPUMSAKA), entry.linga)
        assertEquals(setOf("3.3"), entry.lingaRuleIds)
    }

    @Test
    fun `gets gana membership from ganapatha`() {
        val entry = requireNotNull(PaninianPratipadikaLexicon.findPratipadika("तद्"))
        assertTrue("सर्वादिः" in entry.ganaNames)
    }

    @Test
    fun `preserves base lexical semantics while adding grammatical metadata`() {
        val entry = requireNotNull(PaninianPratipadikaLexicon.findPratipadika("गुण"))
        assertTrue(NominalCategory.TECHNICAL_SAMJNA in entry.categories)
        assertTrue(entry.ganaNames.isNotEmpty())
    }

    @Test
    fun `attested corpus stems receive gender only in linganushasanam layer`() {
        for ((text, rule) in mapOf(
            "घट" to "64",
            "पुत्र" to "158",
            "दोष" to "93",
            "गुरु" to "51",
            "हेतु" to "51",
        )) {
            val entry = requireNotNull(PaninianPratipadikaLexicon.findPratipadika(text))
            assertEquals(setOf(Linga.PUMS), entry.linga)
            assertEquals(setOf(rule), entry.lingaRuleIds)
        }
    }

    @Test
    fun `vijaya receives masculine gender from authentic rule 37 provenance`() {
        val entry = requireNotNull(PaninianPratipadikaLexicon.findPratipadika("विजय"))
        assertEquals(setOf(Linga.PUMS), entry.linga)
        assertEquals("अच्", entry.lingaPratyaya)
        assertEquals(setOf("37"), entry.lingaRuleIds)
    }
}
