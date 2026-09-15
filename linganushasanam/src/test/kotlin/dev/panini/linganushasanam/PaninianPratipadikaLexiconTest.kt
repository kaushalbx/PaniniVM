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
}
