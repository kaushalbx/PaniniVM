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

    @Test
    fun `implemented sutras in 2 2 11 through 38 retain canonical identities`() {
        val expected = linkedMapOf(
            "2.2.12" to "क्तेन च पूजायाम्",
            "2.2.13" to "अधिकरणवाचिना च",
            "2.2.14" to "कर्मणि च",
            "2.2.15" to "तृजकाभ्यां कर्तरि",
            "2.2.16" to "कर्तरि च",
            "2.2.17" to "नित्यं क्रीडाजीविकयोः",
            "2.2.18" to "कुगतिप्रादयः",
            "2.2.19" to "उपपदमतिङ्",
            "2.2.20" to "अमैवाव्ययेन",
            "2.2.21" to "तृतीयाप्रभृतीन्यन्यतरस्याम्",
            "2.2.24" to "अनेकमन्यपदार्थे",
            "2.2.25" to "संख्ययाऽव्ययासन्नादूराधिकसंख्याः संख्येये",
            "2.2.27" to "तत्र तेनेदमिति सरूपे",
            "2.2.28" to "तेन सहेति तुल्ययोगे",
            "2.2.29" to "चार्थे द्वन्द्वः",
            "2.2.30" to "उपसर्जनं पूर्वम्",
            "2.2.31" to "राजदन्तादिषु परम्",
            "2.2.33" to "अजाद्यदन्तम्",
            "2.2.34" to "अल्पाच्तरम्",
            "2.2.35" to "सप्तमीविशेषणे बहुव्रीहौ",
            "2.2.36" to "निष्ठा",
            "2.2.37" to "वाऽऽहिताग्न्यादिषु",
            "2.2.38" to "कडाराः कर्मधारये",
        )
        val actual = Adhyaya2Pada2.sutras
            .filter { it.number in expected }
            .associate { it.number to it.text }

        assertEquals(expected, actual)
        assertEquals(expected.keys, actual.keys)
    }

    @Test
    fun `2 2 registry contains only canonical implemented identities without duplicates`() {
        val registrations = Adhyaya2Pada2.sutras.groupingBy { it.number }.eachCount()

        assertEquals(Adhyaya2Pada2.sutras.size, registrations.size)
        assertEquals(emptyList(), Adhyaya2Pada2.sutras.filterNot { it.number.startsWith("2.2.") })
    }
}
