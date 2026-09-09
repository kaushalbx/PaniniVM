package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya6.pada3.Adhyaya6Pada3
import kotlin.test.Test
import kotlin.test.assertEquals

class SamasaPada3CanonicalIdentityTest {
    @Test
    fun `audited opening samasa sutras in 6 3 retain canonical identities`() {
        val expected = linkedMapOf(
            "6.3.1" to "अलुगुत्तरपदे",
            "6.3.2" to "पञ्चम्याः स्तोकादिभ्यः",
            "6.3.4" to "मनसः संज्ञायाम्",
            "6.3.6" to "आत्मनश्च पूरणे",
            "6.3.14" to "तत्पुरुषे कृति बहुलम्",
            "6.3.22" to "पुत्रेऽन्यतरस्याम्",
        )
        val actual = Adhyaya6Pada3.sutras
            .filterIsInstance<SamasaSutra>()
            .map { it as Sutra<*, *> }
            .filter { it.number in expected }
            .associate { it.number to it.text }

        assertEquals(expected, actual)
    }
}
