package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.IjashChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KasPratyayadAmAmantreLitSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KrnChanuprayujyateLitSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.UshavidajabhyashChaSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AmantaLitSutrasTest {

    @Test
    fun `test 3 1 35 KasPratyayadAmAmantreLitSutra`() {
        assertTrue(KasPratyayadAmAmantreLitSutra.matches("कास्"))
        assertEquals("आम", KasPratyayadAmAmantreLitSutra.apply("कास्"))
    }

    @Test
    fun `test 3 1 36 IjashChaSutra`() {
        assertTrue(IjashChaSutra.matches("ईक्ष्"))
        assertEquals("आम", IjashChaSutra.apply("ईक्ष्"))
    }

    @Test
    fun `test 3 1 38 UshavidajabhyashChaSutra`() {
        assertTrue(UshavidajabhyashChaSutra.matches("विद्"))
        assertEquals("आम", UshavidajabhyashChaSutra.apply("विद्"))
    }

    @Test
    fun `test 3 1 40 KrnChanuprayujyateLitSutra`() {
        assertTrue(KrnChanuprayujyateLitSutra.matches("ईक्षामाम्"))
        assertEquals("ईक्षामाम् कृञ्", KrnChanuprayujyateLitSutra.apply("ईक्षामाम्"))
    }
}
