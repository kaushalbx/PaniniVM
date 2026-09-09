package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya2.pada2.Adhyaya2Pada2
import kotlin.test.Test
import kotlin.test.assertEquals

class SamasaPada2CanonicalIdentityTest {
    @Test
    fun `implemented sutras in 2 2 1 through 10 retain canonical identities`() {
        val expected = linkedMapOf(
            "2.2.1" to "पूर्वापराधरोत्तरमेकदेशिनैकाधिकरणे",
            "2.2.2" to "अर्धं नपुंसकम्",
            "2.2.4" to "प्राप्तापन्ने च द्वितीयया",
            "2.2.6" to "नञ्",
            "2.2.8" to "षष्ठी",
            "2.2.9" to "याजकादिभिश्च",
            "2.2.10" to "न निर्धारणे",
        )
        val actual = Adhyaya2Pada2.sutras
            .filter { it.number in expected }
            .associate { it.number to it.text }

        assertEquals(expected, actual)
        assertEquals(expected.keys, actual.keys)
    }
}
