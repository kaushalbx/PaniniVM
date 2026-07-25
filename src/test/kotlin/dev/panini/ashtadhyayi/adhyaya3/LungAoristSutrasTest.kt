package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.ChinKarmaniChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.NishriDruSruBhyahKarthariChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.PusAdiDyutAdyLdtahParasmaipadesuSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LungAoristSutrasTest {

    @Test
    fun `test 3 1 48 NishriDruSruBhyahKarthariChaSutra`() {
        assertTrue(NishriDruSruBhyahKarthariChaSutra.matches("श्रि"))
        assertEquals("चङ्", NishriDruSruBhyahKarthariChaSutra.apply("श्रि"))
    }

    @Test
    fun `test 3 1 55 PusAdiDyutAdyLdtahParasmaipadesuSutra`() {
        assertTrue(PusAdiDyutAdyLdtahParasmaipadesuSutra.matches("पुष्"))
        assertEquals("अङ्", PusAdiDyutAdyLdtahParasmaipadesuSutra.apply("पुष्"))
    }

    @Test
    fun `test 3 1 60 ChinKarmaniChaSutra`() {
        assertTrue(ChinKarmaniChaSutra.matches("कर्मणि"))
        assertEquals("चिण्", ChinKarmaniChaSutra.apply("कर्मणि"))
    }
}
