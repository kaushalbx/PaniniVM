package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.GupDhoopVichchhiPaniPanibhyOyaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KamerNingSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.RtIyIyAnehKyanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LanSakatayanasyaIvaSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoreTingMorphologyTest {

    @Test
    fun `test 3 1 28 GupDhoopVichchhiPaniPanibhyOyaSutra`() {
        assertTrue(GupDhoopVichchhiPaniPanibhyOyaSutra.matches("गुप्"))
        assertTrue(GupDhoopVichchhiPaniPanibhyOyaSutra.matches("धूप"))
        assertEquals("आय", GupDhoopVichchhiPaniPanibhyOyaSutra.apply("गुप्"))
    }

    @Test
    fun `test 3 1 29 RtIyIyAnehKyanSutra`() {
        assertTrue(RtIyIyAnehKyanSutra.matches("ऋतीय"))
        assertEquals("क्यङ्", RtIyIyAnehKyanSutra.apply("ऋतीय"))
    }

    @Test
    fun `test 3 1 30 KamerNingSutra`() {
        assertTrue(KamerNingSutra.matches("कम्"))
        assertEquals("णिङ्", KamerNingSutra.apply("कम्"))
    }

    @Test
    fun `test 3 4 111 LanSakatayanasyaIvaSutra`() {
        assertTrue(LanSakatayanasyaIvaSutra.matches("झि"))
        assertEquals("जुस्", LanSakatayanasyaIvaSutra.apply("झि"))
    }
}
