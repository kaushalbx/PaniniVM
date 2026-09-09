package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya6.pada3.Adhyaya6Pada3
import kotlin.test.Test
import kotlin.test.assertEquals

class SamasaPada3CanonicalIdentityTest {
    @Test
    fun `registered samasa sutras in 6 3 retain canonical identities`() {
        val expected = linkedMapOf(
            "6.3.1" to "अलुगुत्तरपदे",
            "6.3.2" to "पञ्चम्याः स्तोकादिभ्यः",
            "6.3.4" to "मनसः संज्ञायाम्",
            "6.3.6" to "आत्मनश्च पूरणे",
            "6.3.14" to "तत्पुरुषे कृति बहुलम्",
            "6.3.22" to "पुत्रेऽन्यतरस्याम्",
            "6.3.27" to "ईदग्नेः सोमवरुणयोः",
            "6.3.30" to "दिवसश्च पृथिव्याम्",
            "6.3.31" to "उषासोषसः",
            "6.3.32" to "मातरपितरावुदीचाम्",
            "6.3.33" to "पितरामातरा च च्छन्दसि",
            "6.3.46" to "आन्महतः समानाधिकरणजातीययोः",
            "6.3.82" to "वोपसर्जनस्य",
        )
        val actual = Adhyaya6Pada3.sutras
            .filterIsInstance<SamasaSutra>()
            .map { it as Sutra<*, *> }
            .associate { it.number to it.text }

        assertEquals(expected, actual)
        assertEquals(expected.size, actual.size)
    }
}
