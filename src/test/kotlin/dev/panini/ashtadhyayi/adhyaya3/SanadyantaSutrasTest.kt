package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatohKarmanahSamanakartrkadIcchayamSanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatorEkayacoHaladerKriyasamabhihareYangSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.GupTijKitsadbhyahSanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.HetumatiCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SanaadyantaDhatavahSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SanadyantaSutrasTest {

    @Test
    fun `test 3 1 5 GupTijKitsadbhyahSanSutra`() {
        assertTrue(GupTijKitsadbhyahSanSutra.matches("गुप्"))
        assertEquals("सन्", GupTijKitsadbhyahSanSutra.apply("गुप्"))
    }

    @Test
    fun `test 3 1 7 DhatohKarmanahSamanakartrkadIcchayamSanSutra`() {
        assertTrue(DhatohKarmanahSamanakartrkadIcchayamSanSutra.matches("भू"))
        assertEquals("सन्", DhatohKarmanahSamanakartrkadIcchayamSanSutra.apply("भू"))
    }

    @Test
    fun `test 3 1 22 DhatorEkayacoHaladerKriyasamabhihareYangSutra`() {
        assertTrue(DhatorEkayacoHaladerKriyasamabhihareYangSutra.matches("पच्"))
        assertEquals("यङ्", DhatorEkayacoHaladerKriyasamabhihareYangSutra.apply("पच्"))
    }

    @Test
    fun `test 3 1 26 HetumatiCaSutra`() {
        assertTrue(HetumatiCaSutra.matches("कृ"))
        assertEquals("णिच्", HetumatiCaSutra.apply("कृ"))
    }

    @Test
    fun `test 3 1 32 SanaadyantaDhatavahSutra`() {
        assertTrue(SanaadyantaDhatavahSutra.matches("बुभूषा"))
        assertTrue(SanaadyantaDhatavahSutra.matches("पापच्य"))
        assertTrue(SanaadyantaDhatavahSutra.matches("कारिणिच्"))
        assertEquals("बुभूषा", SanaadyantaDhatavahSutra.apply("बुभूषा"))
    }
}
