package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SubantaKarakaParserTest {

    @Test
    fun `extracts karma from parsed case metadata`() {
        assertEquals(
            listOf("द्वि", "त्रि"),
            SubantaKarakaParser.extractKarmaTerms("द्वि+अम्   त्रि + अम् च"),
        )
        assertEquals(
            emptyList(),
            SubantaKarakaParser.extractKarmaTerms("द्वि + अम् +"),
            "An invalid fragment must not be partially interpreted by a regex.",
        )
    }

    @Test
    fun `recognizes instrumental from parsed case metadata`() {
        assertTrue(SubantaKarakaParser.hasTritiyaInstrumental("वृध् + ल्युट् + टा कृ + लोट् + सिप्"))
        assertFalse(SubantaKarakaParser.hasTritiyaInstrumental("वृध् + ल्युट् + अम् कृ + लोट् + सिप्"))
        assertFalse(SubantaKarakaParser.hasTritiyaInstrumental("अपरिचित + टा पाठ"))
    }
}
