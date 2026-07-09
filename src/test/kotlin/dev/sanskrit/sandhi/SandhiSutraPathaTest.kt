package dev.sanskrit.sandhi

import dev.sanskrit.patha.adhyaya6.pada1.Adhyaya6Pada1
import dev.sanskrit.patha.adhyaya8.pada3.Adhyaya8Pada3
import kotlin.test.Test
import kotlin.test.assertEquals

class SandhiSutraPathaTest {
    @Test
    fun `arranges sandhi sutras by adhyaya and pada`() {
        assertEquals(
            listOf("6.1.77", "6.1.78", "6.1.87", "6.1.101"),
            Adhyaya6Pada1.sutras.map { it.sutra },
        )
        assertEquals(
            listOf("8.3.15", "8.3.34"),
            Adhyaya8Pada3.sutras.map { it.sutra },
        )
    }

    @Test
    fun `collects patha in Ashtadhyayi order`() {
        assertEquals(
            listOf("6.1.77", "6.1.78", "6.1.87", "6.1.101", "8.3.15", "8.3.34"),
            SandhiSutraPatha.sutras.map { it.sutra },
        )
    }
}
