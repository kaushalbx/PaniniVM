package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya5.pada4.Adhyaya5Pada4
import kotlin.test.Test
import kotlin.test.assertEquals

class SamasantaCanonicalIdentityTest {
    @Test
    fun `5 4 68 through 87 retain canonical identities`() {
        val expected = linkedMapOf(
            "5.4.68" to "समासान्ताः",
            "5.4.69" to "न पूजनात्",
            "5.4.70" to "किमः क्षेपे",
            "5.4.71" to "नञस्तत्पुरुषात्",
            "5.4.72" to "पथो विभाषा",
            "5.4.73" to "बहुव्रीहौ संख्येये डजबहुगणात्",
            "5.4.74" to "ऋक्पूरप्धूःपथामानक्षे",
            "5.4.75" to "अच् प्रत्यन्ववपूर्वात् सामलोम्नः",
            "5.4.76" to "अक्ष्णोऽदर्शनात्",
            "5.4.77" to "अचतुरविचतुरसुचतुरस्त्रीपुंसधेन्वनडुहर्क्सामवाङ्मनसाक्षिभ्रुवदारगवोर्वष्ठीवपदष्ठीवनक्तंदिवरत्रिंदिवाहर्दिवसरजसनिःश्रेयसपुरुषायुषद्व्यायुषत्र्यायुषर्ग्यजुषजातोक्षमहोक्षवृद्धोक्षोपशुनगोष्ठश्वाः",
            "5.4.78" to "ब्रह्महस्तिभ्यां वर्चसः",
            "5.4.79" to "अवसमन्धेभ्यस्तमसः",
            "5.4.80" to "श्वसो वसीयःश्रेयसः",
            "5.4.81" to "अन्ववतप्ताद्रहसः",
            "5.4.82" to "प्रतेरुरसः सप्तमीस्थात्",
            "5.4.83" to "अनुगवमायामे",
            "5.4.84" to "द्विस्तावा त्रिस्तावा वेदिः",
            "5.4.85" to "उपसर्गादध्वनः",
            "5.4.86" to "तत्पुरुषस्याङ्गुलेः संख्याऽव्ययादेः",
            "5.4.87" to "अहस्सर्वैकदेशसंख्यातपुण्याच्च रात्रेः",
        )
        val actual = Adhyaya5Pada4.sutras
            .filter { it.number in expected }
            .associate { it.number to it.text }

        assertEquals(expected, actual)
    }
}
