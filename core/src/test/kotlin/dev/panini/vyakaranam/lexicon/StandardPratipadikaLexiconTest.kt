package dev.panini.vyakaranam.lexicon

import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StandardPratipadikaLexiconTest {
    @Test
    fun `provides shared neuter pratipadika metadata`() {
        listOf("हविस्", "मनस्", "पयस्", "उरस्", "चक्षुस्").forEach { text ->
            assertEquals(setOf(Linga.NAPUMSAKA), StandardPratipadikaLexicon.findPratipadika(text)?.linga)
        }
        assertNull(StandardPratipadikaLexicon.findPratipadika("राम"))
    }

    @Test
    fun `classifies canonical technical samjna identities`() {
        listOf("सङ्ख्या", "गुण", "वृद्धि", "लोप", "साधकतमम्", "कर्म", "करणम्").forEach { text ->
            assertEquals(
                setOf(NominalCategory.TECHNICAL_SAMJNA),
                StandardPratipadikaLexicon.findPratipadika(text)?.categories,
            )
        }
    }
}
