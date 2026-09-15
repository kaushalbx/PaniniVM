package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya2.pada4.Adhyaya2Pada4
import kotlin.test.Test
import kotlin.test.assertEquals

class SamasaPada4CanonicalIdentityTest {
    @Test
    fun `implemented samasa sutras in 2 4 retain canonical identities`() {
        val expected = linkedMapOf(
            "2.4.2" to "द्वन्द्वश्च प्राणितूर्यसेनाङ्गानाम्",
            "2.4.6" to "जातिरप्राणिनाम्",
        )
        val actual = Adhyaya2Pada4.sutras
            .filterIsInstance<SamasaSutra>()
            .associate { sutra ->
                val identity = sutra as Sutra<*, *>
                identity.number to identity.text
            }

        assertEquals(expected, actual)
        assertEquals(expected.size, actual.size)
    }
}
