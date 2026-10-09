package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class PrefixBoundarySandhiTest {
    @Test
    fun `homogeneous prefix vowels coalesce without reopening verbal phonology`() {
        val engine = SandhiEngine()
        assertEquals("नीहि", engine.joinPrefix("नि", "इहि"))
        assertEquals("सूपविश", engine.joinPrefix("सु", "उपविश"))
        assertEquals("उपागच्छ", engine.joinPrefix("उप", "आगच्छ"))
        assertEquals("प्रक्षिप", engine.joinPrefix("प्र", "क्षिप"))
    }

    @Test
    fun `prefix nasal assimilation distinguishes varga from semivowels`() {
        val engine = SandhiEngine()
        assertEquals("सङ्करोति", engine.joinPrefix("सम्", "करोति"))
        assertEquals("सञ्चरति", engine.joinPrefix("सम्", "चरति"))
        assertEquals("सन्तरति", engine.joinPrefix("सम्", "तरति"))
        assertEquals("सम्पतति", engine.joinPrefix("सम्", "पतति"))
        assertEquals("संयाति", engine.joinPrefix("सम्", "याति"))
        assertEquals("संवदति", engine.joinPrefix("सम्", "वदति"))
    }

    @Test
    fun `sentence boundary jash stays restricted to final dental t`() {
        val engine = SandhiEngine()
        assertEquals("तद् अस्ति", engine.joinPadas("तत्", "अस्ति"))
        assertEquals("तद् भवति", engine.joinPadas("तत्", "भवति"))
        assertEquals("तत् च", engine.joinPadas("तत्", "च"))
        assertEquals("तत अस्ति", engine.joinPadas("तत", "अस्ति"))
        assertEquals("वाक् अस्ति", engine.joinPadas("वाक्", "अस्ति"))
        assertEquals("अहं करोमि", engine.joinPadas("अहम्", "करोमि"))
    }
}
