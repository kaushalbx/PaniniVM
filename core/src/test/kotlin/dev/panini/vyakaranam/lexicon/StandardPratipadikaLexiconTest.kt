package dev.panini.vyakaranam.lexicon

import dev.panini.core.NominalCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StandardPratipadikaLexiconTest {
    @Test fun `participant type nouns retain grammatical gender`() {
        assertEquals(setOf(dev.panini.core.Linga.STRI), StandardPratipadikaLexicon.findPratipadika("सङ्ख्या")?.linga)
        assertEquals(setOf(dev.panini.core.Linga.STRI), StandardPratipadikaLexicon.findPratipadika("सूची")?.linga)
        for (text in listOf("शब्द", "घट", "पुत्र", "दोष", "गुरु", "हेतु", "विजय",
            "ऋषि", "राशि", "ध्वनि", "कपि", "गज", "दूत", "अन्त", "कफ",
            "मठ", "मणि", "गन्ध", "अमित्र", "छात्र", "मन्त्र", "उष्ट्र")) {
            assertEquals(emptySet(), StandardPratipadikaLexicon.findPratipadika(text)?.linga)
        }
        assertEquals("अच्", StandardPratipadikaLexicon.findPratipadika("विजय")?.lingaPratyaya)
    }
    @Test
    fun `ordinal numeric identity does not erase feminine stem gender`() {
        assertEquals(setOf(dev.panini.core.Linga.STRI),
            StandardPratipadikaLexicon.findPratipadika("प्रथमा")?.linga)
        assertEquals(setOf(dev.panini.core.Linga.PUMS, dev.panini.core.Linga.NAPUMSAKA),
            StandardPratipadikaLexicon.findPratipadika("प्रथम")?.linga)
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

    @Test
    fun `does not duplicate grammatical gender data`() {
        assertNull(StandardPratipadikaLexicon.findPratipadika("मनस्"))
    }
}
