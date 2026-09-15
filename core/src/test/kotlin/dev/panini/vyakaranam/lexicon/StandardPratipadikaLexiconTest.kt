package dev.panini.vyakaranam.lexicon

import dev.panini.core.NominalCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StandardPratipadikaLexiconTest {
    @Test
    fun `classifies canonical technical samjna identities`() {
        listOf("सङ्ख्या", "गुण", "वृद्धि", "लोप", "साधकतमम्", "कर्म", "करणम्").forEach { text ->
            assertEquals(
                setOf(NominalCategory.TECHNICAL_SAMJNA),
                StandardPratipadikaLexicon.findPratipadika(text)?.categories,
            )
        }
    }

    @Test
    fun `does not duplicate grammatical gender data`() {
        assertNull(StandardPratipadikaLexicon.findPratipadika("मनस्"))
    }
}
