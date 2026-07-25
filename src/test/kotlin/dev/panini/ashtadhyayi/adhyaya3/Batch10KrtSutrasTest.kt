package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.AchoYatSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.GeheKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.NvultrchauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.RhalorNyatSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SilpiniShvunSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.AnyaebhyopiDrshyateSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.AtoAnupasargeKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LaksanaghetvohKriyahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LatahSatriShanacauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SanashamsabhikshuchSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Batch10KrtSutrasTest {

    @Test
    fun `test 3 1 97 AchoYatSutra`() {
        assertTrue(AchoYatSutra.matches("जि"))
        assertEquals("यत्", AchoYatSutra.apply("जि"))
    }

    @Test
    fun `test 3 1 124 RhalorNyatSutra`() {
        assertTrue(RhalorNyatSutra.matches("कृ"))
        assertEquals("ण्यत्", RhalorNyatSutra.apply("कृ"))
    }

    @Test
    fun `test 3 1 133 NvultrchauSutra`() {
        assertTrue(NvultrchauSutra.matches("कृ"))
        assertEquals("तृच्", NvultrchauSutra.apply("कृ"))
    }

    @Test
    fun `test 3 1 144 GeheKahSutra`() {
        assertTrue(GeheKahSutra.matches("ग्रह्"))
        assertEquals("क", GeheKahSutra.apply("ग्रह्"))
    }

    @Test
    fun `test 3 1 145 SilpiniShvunSutra`() {
        assertTrue(SilpiniShvunSutra.matches("नृत्"))
        assertEquals("ष्वुन्", SilpiniShvunSutra.apply("नृत्"))
    }

    @Test
    fun `test 3 2 3 AtoAnupasargeKahSutra`() {
        assertTrue(AtoAnupasargeKahSutra.matches("दा"))
        assertEquals("क", AtoAnupasargeKahSutra.apply("दा"))
    }

    @Test
    fun `test 3 2 124 LatahSatriShanacauSutra`() {
        assertTrue(LatahSatriShanacauSutra.matches("लट्"))
        assertEquals("शतृ", LatahSatriShanacauSutra.apply("लट्"))
    }

    @Test
    fun `test 3 2 126 LaksanaghetvohKriyahSutra`() {
        assertTrue(LaksanaghetvohKriyahSutra.matches("लक्षण"))
        assertEquals("शतृ", LaksanaghetvohKriyahSutra.apply("लक्षण"))
    }

    @Test
    fun `test 3 2 168 SanashamsabhikshuchSutra`() {
        assertTrue(SanashamsabhikshuchSutra.matches("भिक्ष्"))
        assertEquals("उच्", SanashamsabhikshuchSutra.apply("भिक्ष्"))
    }

    @Test
    fun `test 3 2 178 AnyaebhyopiDrshyateSutra`() {
        assertTrue(AnyaebhyopiDrshyateSutra.matches("दृश्"))
        assertEquals("क्विप्", AnyaebhyopiDrshyateSutra.apply("दृश्"))
    }
}
