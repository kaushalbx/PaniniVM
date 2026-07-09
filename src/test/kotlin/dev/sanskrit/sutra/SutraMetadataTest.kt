package dev.sanskrit.sutra

import kotlin.test.Test
import kotlin.test.assertEquals

class SutraMetadataTest {
    @Test
    fun `metadata identity is the sutra number`() {
        val first = SutraMetadata(
            "6.1.101",
            "अकः सवर्णे दीर्घः",
            "अक् प्रत्याहार के स्वर के बाद उसी सवर्ण का स्वर आए तो दोनों के स्थान पर दीर्घ स्वर होता है।",
            SutraType.NITYA,
            6,
            1,
            false,
            1,
        )
        val second = SutraMetadata(
            "6.1.101",
            "भिन्न सूत्र-पाठ",
            "भिन्न हिन्दी व्याख्या",
            SutraType.APAVADA,
            6,
            1,
            true,
            99,
        )

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }
}
