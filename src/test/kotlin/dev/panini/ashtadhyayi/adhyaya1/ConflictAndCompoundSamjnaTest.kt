package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.PratyayasyaLupSlulopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada2.PrathamanirdistamSamasaUpasarjanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada2.UpasarjanamPurvamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.VipratisedheParamKaryamSutra
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ConflictAndCompoundSamjnaTest {

    @Test
    fun testVipratisedheParamKaryamSutra() {
        assertTrue(VipratisedheParamKaryamSutra.matches(100 to 200))
        assertEquals(200, VipratisedheParamKaryamSutra.apply(100 to 200))
    }

    @Test
    fun testPratyayasyaLupSlulopahSutra() {
        assertTrue(PratyayasyaLupSlulopahSutra.matches("लुप्"))
        assertTrue(PratyayasyaLupSlulopahSutra.matches("श्लु"))
        assertTrue(PratyayasyaLupSlulopahSutra.matches("लोप"))
        assertEquals("श्लु", PratyayasyaLupSlulopahSutra.apply("श्लु"))
    }

    @Test
    fun testUpasarjanamPurvamSutra() {
        val pair = "राज" to "पुरुष"
        assertTrue(UpasarjanamPurvamSutra.matches(pair))
        assertEquals(pair, UpasarjanamPurvamSutra.apply(pair))
    }

    @Test
    fun testPrathamanirdistamSamasaUpasarjanamSutra() {
        assertTrue(PrathamanirdistamSamasaUpasarjanamSutra.matches("राज"))
        assertEquals("उपसर्जनम्", PrathamanirdistamSamasaUpasarjanamSutra.apply("राज"))
    }
}
