package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.KrdAticSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SarvadhatukeYakSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.TavyattavyanIyarahSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PassiveAndKrtSutrasTest {

    @Test
    fun `test 3 1 67 SarvadhatukeYakSutra`() {
        assertTrue(SarvadhatukeYakSutra.matches("कर्मणि"))
        assertEquals("यक्", SarvadhatukeYakSutra.apply("कर्मणि"))
    }

    @Test
    fun `test 3 1 93 KrdAticSutra`() {
        assertTrue(KrdAticSutra.matches("घञ्"))
        assertEquals("घञ्", KrdAticSutra.apply("घञ्"))
    }

    @Test
    fun `test 3 1 96 TavyattavyanIyarahSutra`() {
        assertTrue(TavyattavyanIyarahSutra.matches("कृ"))
        assertEquals("तव्यत्", TavyattavyanIyarahSutra.apply("कृ"))
    }
}
