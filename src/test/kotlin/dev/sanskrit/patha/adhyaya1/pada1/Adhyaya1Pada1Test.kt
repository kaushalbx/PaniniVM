package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraType
import kotlin.test.Test
import kotlin.test.assertEquals

class Adhyaya1Pada1Test {
    @Test
    fun `catalogs opening samjna sutras`() {
        assertEquals(
            listOf("1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5", "1.1.6", "1.1.7", "1.1.8", "1.1.9", "1.1.10"),
            Adhyaya1Pada1.sutras.map { it.sutraNumber },
        )
        assertEquals("वृद्धिरादैच्", Adhyaya1Pada1.sutras[0].sutraText)
        assertEquals("अदेङ् गुणः", Adhyaya1Pada1.sutras[1].sutraText)
        assertEquals("इको गुणवृद्धी", Adhyaya1Pada1.sutras[2].sutraText)
        assertEquals("नाज्झलौ", Adhyaya1Pada1.sutras[9].sutraText)
        assertEquals(SutraType.SAMJNA, Adhyaya1Pada1.sutras[0].type)
        assertEquals(SutraType.PARIBHASHA, Adhyaya1Pada1.sutras[2].type)
        assertEquals(SutraType.NISHEDHA, Adhyaya1Pada1.sutras[9].type)
    }
}
