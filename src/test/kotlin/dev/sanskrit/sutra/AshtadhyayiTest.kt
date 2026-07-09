package dev.sanskrit.sutra

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AshtadhyayiTest {
    @Test
    fun `tracks current patha coverage truthfully`() {
        assertEquals(3959, Ashtadhyayi.expectedSutraCount)
        assertEquals(16, Ashtadhyayi.pathitaCount)
        assertEquals(13, Ashtadhyayi.kriyavatCount)
        assertTrue(Ashtadhyayi.remainingCount > 3900)
    }

    @Test
    fun `exposes executable sandhi sutras by sutra number`() {
        val metadata = Ashtadhyayi.patha.get("6.1.101")

        assertNotNull(metadata)
        assertEquals("अकः सवर्णे दीर्घः", metadata.sutraText)
        assertEquals(
            "अक् प्रत्याहार के स्वर के बाद उसी सवर्ण का स्वर आए तो दोनों के स्थान पर दीर्घ स्वर होता है।",
            metadata.hindiVyakhya,
        )
        assertEquals(SutraAvastha.KRIYAVAT, metadata.avastha)
    }
}
