package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdirAntyenaSahetaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SasthiSthaneYogaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TatiSankhyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasmatItyUttarasyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasminNirdistePurvasyaSutra
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class InterpretativeSamjnaTest {

    @Test
    fun testTatiSankhyaSutra() {
        assertTrue(TatiSankhyaSutra.matches("कति"))
        assertEquals("सङ्ख्या", TatiSankhyaSutra.apply("कति"))
    }

    @Test
    fun testTasminNirdistePurvasyaSutra() {
        assertTrue(TasminNirdistePurvasyaSutra.matches("अचि"))
        assertTrue(TasminNirdistePurvasyaSutra.apply("अचि"))
    }

    @Test
    fun testTasmatItyUttarasyamSutra() {
        assertTrue(TasmatItyUttarasyamSutra.matches("तस्मात्"))
        assertTrue(TasmatItyUttarasyamSutra.apply("तस्मात्"))
    }

    @Test
    fun testSasthiSthaneYogaSutra() {
        assertTrue(SasthiSthaneYogaSutra.matches("इकः"))
        assertTrue(SasthiSthaneYogaSutra.apply("इकः"))
    }

    @Test
    fun testAdirAntyenaSahetaSutra() {
        assertTrue(AdirAntyenaSahetaSutra.matches("अच्"))
        assertEquals("प्रत्याहार", AdirAntyenaSahetaSutra.apply("अच्"))
    }
}
