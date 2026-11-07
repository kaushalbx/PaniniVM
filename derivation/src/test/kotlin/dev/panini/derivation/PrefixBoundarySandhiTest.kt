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
    fun `unlike prefix vowels follow yan, guna and the upasarga rules 6_1_91 and 6_1_94`() {
        val engine = SandhiEngine()
        assertEquals("अन्विच्छ", engine.joinPrefix("अनु", "इच्छ"))
        assertEquals("अध्यस्ति", engine.joinPrefix("अधि", "अस्ति"))
        assertEquals("प्रेच्छ", engine.joinPrefix("प्र", "इच्छ"))
        assertEquals("उपैक्षत", engine.joinPrefix("उप", "ऐक्षत"))
        assertEquals("उपेजते", engine.joinPrefix("उप", "एजते"))
        assertEquals("प्रार्च्छति", engine.joinPrefix("प्र", "ऋच्छति"))
    }

    @Test
    fun `the s of nis and dus follows the sutras, with 8_3_41 before ku and pu`() {
        val engine = SandhiEngine()
        for ((right, form) in listOf("गच्छ" to "निर्गच्छ", "क्राम" to "निष्क्राम", "पत" to "निष्पत", "तर" to "निस्तर",
            "सर" to "निःसर", "रस" to "नीरस", "चल" to "निश्चल")) assertEquals(form, engine.joinPrefix("निस्", right), right)
        assertEquals("दुष्कृत", engine.joinPrefix("दुस्", "कृत"))
        assertEquals("दुराचार", engine.joinPrefix("दुस्", "आचार"))
        // The visarga of a case ending is a pratyaya's: 8.3.41 leaves it to 8.3.37.
        assertEquals(false, engine.join("अग्निः", "करोति").applications.any { it.sutra == "8.3.41" })
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
