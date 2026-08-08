package dev.panini.vyakaranam.lexicon

import dev.panini.core.Linga
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
}
