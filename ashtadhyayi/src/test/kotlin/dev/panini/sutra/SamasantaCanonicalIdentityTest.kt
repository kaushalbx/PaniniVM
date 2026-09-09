package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya5.pada4.Adhyaya5Pada4
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class SamasantaCanonicalIdentityTest {
    @Test
    fun `registered samasanta rules have canonical identities and executable implementations`() {
        val expected = linkedMapOf(
            "5.4.69" to "न पूजनात्", "5.4.70" to "किमः क्षेपे", "5.4.71" to "नञस्तत्पुरुषात्",
            "5.4.72" to "पथो विभाषा", "5.4.73" to "बहुव्रीहौ संख्येये डजबहुगणात्",
            "5.4.74" to "ऋक्पूरप्धूःपथामानक्षे", "5.4.75" to "अच् प्रत्यन्ववपूर्वात् सामलोम्नः",
            "5.4.76" to "अक्ष्णोऽदर्शनात्", "5.4.85" to "उपसर्गादध्वनः",
            "5.4.86" to "तत्पुरुषस्याङ्गुलेः संख्याऽव्ययादेः", "5.4.88" to "अह्नोऽह्न एतेभ्यः",
            "5.4.89" to "न संख्याऽऽदेः समाहारे", "5.4.90" to "उत्तमैकाभ्यां च",
            "5.4.91" to "राजाऽहस्सखिभ्यष्टच्", "5.4.100" to "अर्धाच्च", "5.4.101" to "खार्याः प्राचाम्",
            "5.4.102" to "द्वित्रीभ्यामञ्जलेः", "5.4.113" to "बहुव्रीहौ सक्थ्यक्ष्णोः स्वाङ्गात् षच्",
            "5.4.150" to "सुहृद्दुर्हृदौ मित्रामित्रयोः", "5.4.151" to "उरःप्रभृतिभ्यः कप्",
            "5.4.153" to "नद्यृतश्च", "5.4.17" to "संख्यायाः क्रियाअभ्यावृत्तिगणने कृत्वसुच्",
            "5.4.18" to "द्वित्रिचतुर्भ्यः सुच्",
        )
        val actual = Adhyaya5Pada4.sutras.filter { it.number.startsWith("5.4.") }.associate { it.number to it.text }
        assertEquals(expected, actual)
        assertFalse(Adhyaya5Pada4.sutras.any { it.javaClass.simpleName.contains("Inactive") })
    }
}
