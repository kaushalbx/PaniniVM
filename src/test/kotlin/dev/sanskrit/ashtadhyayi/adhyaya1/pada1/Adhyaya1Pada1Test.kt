package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.sutra.SutraType
import kotlin.test.Test
import kotlin.test.assertEquals

class Adhyaya1Pada1Test {
    @Test
    fun `patha retains opening sutra objects`() {
        assertEquals(22, Adhyaya1Pada1.rules.size)
    }

    @Test
    fun `catalogs opening samjna sutras`() {
        assertEquals(
            listOf("1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5", "1.1.6", "1.1.7", "1.1.8", "1.1.9", "1.1.10"),
            Adhyaya1Pada1.sutras.take(10).map { it.number },
        )
        assertEquals("वृद्धिरादैच्", Adhyaya1Pada1.sutras[0].text)
        assertEquals("अदेङ्गुणः", Adhyaya1Pada1.sutras[1].text)
        assertEquals("इको गुणवृद्धी", Adhyaya1Pada1.sutras[2].text)
        assertEquals("नाज्झलौ", Adhyaya1Pada1.sutras[9].text)
        assertEquals(SutraType.SAMJNA, Adhyaya1Pada1.sutras[0].type)
        assertEquals(SutraType.PARIBHASHA, Adhyaya1Pada1.sutras[2].type)
        assertEquals(SutraType.NISHEDHA, Adhyaya1Pada1.sutras[9].type)
    }
}
