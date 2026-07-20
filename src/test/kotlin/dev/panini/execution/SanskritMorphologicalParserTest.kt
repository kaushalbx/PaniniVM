package dev.panini.execution

import dev.panini.derivation.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SanskritMorphologicalParserTest {
    @Test
    fun `parses canonical numbers into karman karaka`() {
        val token = SanskritMorphologicalParser.parseToken("दश")
        assertEquals("दश", token.stem)
        assertEquals(Karaka.KARMAN, token.inferredKaraka)
        assertTrue(ExecutionSamjna.SANKHYA in token.samjnas)
    }

    @Test
    fun `parses accusative case endings into karman karaka`() {
        val token = SanskritMorphologicalParser.parseToken("रामम्")
        assertEquals("राम", token.stem)
        assertEquals(Vibhakti.DVITIYA, token.vibhakti)
        assertEquals(Karaka.KARMAN, token.inferredKaraka)
    }

    @Test
    fun `parses instrumental case endings into kartr karaka`() {
        val token = SanskritMorphologicalParser.parseToken("रामेण")
        assertEquals("राम", token.stem)
        assertEquals(Vibhakti.TRTIYA, token.vibhakti)
        assertEquals(Karaka.KARTR, token.inferredKaraka)
    }

    @Test
    fun `parses dative case endings into sampradana karaka`() {
        val token = SanskritMorphologicalParser.parseToken("यन्त्राय")
        assertEquals("यन्त्र", token.stem)
        assertEquals(Vibhakti.CHATURTHI, token.vibhakti)
        assertEquals(Karaka.SAMPRADANA, token.inferredKaraka)
    }

    @Test
    fun `groups tokens into structured karaka map`() {
        val tokens = listOf(
            SanskritMorphologicalParser.parseToken("दश"),
            SanskritMorphologicalParser.parseToken("द्वि"),
        )
        val map = SanskritMorphologicalParser.groupKarakas(
            tokens,
            setOf("पूर्वफलं"),
            null,
            { "योग-$it" },
            0,
        )

        assertTrue(Karaka.KARMAN in map)
    }
}
