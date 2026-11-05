package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KarakaCanonicalMetadataTest {
    @Test
    fun `source block inherits apadana and bhu also retains the birth agent reference`() {
        for (index in 25..31) {
            val rule = Ashtadhyayi.registry.sutras.single { it.number == "1.4.$index" }
            assertEquals(if (index == 31) setOf("1.4.24", "1.4.30") else setOf("1.4.24"), rule.anuvrtti)
            assertEquals(setOf("1.4.23"), rule.adhikara)
        }
    }

    @Test
    fun `locus and causative block separates inherited terms from governing scope`() {
        val inherited = mapOf(
            "1.4.46" to setOf("1.4.45"),
            "1.4.47" to setOf("1.4.44", "1.4.45", "1.4.46"),
            "1.4.48" to setOf("1.4.45", "1.4.46"),
            "1.4.51" to setOf("1.4.49"), "1.4.52" to setOf("1.4.49"),
            "1.4.55" to setOf("1.4.54"),
        )
        inherited.forEach { (number, sources) ->
            val rule = Ashtadhyayi.registry.sutras.single { it.number == number }
            assertEquals(sources, rule.anuvrtti, number)
            assertEquals(setOf("1.4.23"), rule.adhikara, number)
        }
        assertEquals(setOf("1.4.42"), DivahKarmaCaSutra.anuvrtti)
        assertEquals(setOf("1.4.42"), ParikrayaneSampradanamAnyatarasyamSutra.anuvrtti)
        assertTrue(AbhinivishasCaSutra.optional)
        assertTrue(!AdhishingsthasamKarmaSutra.optional && !UpanvadhyangvasahSutra.optional)
    }

    @Test
    fun `recipient block declares inherited designation and construction terms separately`() {
        val inherited = mapOf(
            "1.4.33" to setOf("1.4.32"), "1.4.34" to setOf("1.4.32"),
            "1.4.35" to setOf("1.4.32"), "1.4.36" to setOf("1.4.32"),
            "1.4.37" to setOf("1.4.32"), "1.4.38" to setOf("1.4.32", "1.4.37"),
            "1.4.39" to setOf("1.4.32"), "1.4.40" to setOf("1.4.32"),
            "1.4.41" to setOf("1.4.32", "1.4.40"),
        )
        inherited.forEach { (number, sources) ->
            val rule = Ashtadhyayi.registry.sutras.single { it.number == number }
            assertEquals(sources, rule.anuvrtti, number)
            assertTrue(sources.none { it in rule.adhikara }, number)
        }
    }

    // Selected Kāśikā heading forms; comparison ignores editorial word spacing only.
    // https://vishvasa.github.io/sanskrit/vyAkaraNam/pANinIyam/mUlAni/kAshikA/puShpA-pratiH/1/04/
    private val texts = listOf(
        "कारके", "ध्रुवमपायेऽपादानम्", "भीत्रार्थानां भयहेतुः", "पराजेरसोढः",
        "वारणार्थानामीप्सितः", "अन्तर्धौ येनादर्शनमिच्छति", "आख्यातोपयोगे",
        "जनिकर्तुः प्रकृतिः", "भुवः प्रभवः", "कर्मणा यमभिप्रैति स सम्प्रदानम्",
        "रुच्यर्थानां प्रीयमाणः", "श्लाघह्नुङ्स्थाशपां ज्ञीप्स्यमानः", "धारेरुत्तमर्णः",
        "स्पृहेरीप्सितः", "क्रुधद्रुहेर्ष्यासूयार्थानां यं प्रति कोपः",
        "क्रुधद्रुहोरुपसृष्टयोः कर्म", "राधीक्ष्योर्यस्य विप्रश्नः",
        "प्रत्याङ्भ्यां श्रुवः पूर्वस्य कर्ता", "अनुप्रतिगृणश्च", "साधकतमं करणम्",
        "दिवः कर्म च", "परिक्रयणे सम्प्रदानमन्यतरस्याम्", "आधारोऽधिकरणम्",
        "अधिशीङ्स्थासां कर्म", "अभिनिविशश्च", "उपान्वध्याङ्वसः",
        "कर्तुरीप्सिततमं कर्म", "तथायुक्तं चानीप्सितम्", "अकथितं च",
        "गतिबुद्धिप्रत्यवसानार्थशब्दकर्माकर्मकाणामणि कर्ता स णौ",
        "हृक्रोरन्यतरस्याम्", "स्वतन्त्रः कर्ता", "तत्प्रयोजको हेतुश्च",
    )

    @Test
    fun `canonical karaka block has unique numbers checked text and governing metadata`() {
        assertEquals(33, texts.size)
        val rules = Ashtadhyayi.registry.sutras.groupBy { it.number }
        fun normalized(text: String) = text.filterNot(Char::isWhitespace)
        texts.forEachIndexed { index, expected ->
            val number = "1.4.${index + 23}"
            val matching = rules[number].orEmpty()
            assertEquals(1, matching.size, number)
            val rule = matching.single()
            assertEquals(normalized(expected), normalized(rule.text), number)
            if (index == 0) {
                assertEquals(SutraAction.ADHIKARA, rule.action)
                assertTrue(rule.role is SutraRole.Adhikara)
            } else {
                assertEquals(SutraAction.SAMJNA, rule.action, number)
                assertTrue("1.4.23" in rule.adhikara, number)
            }
        }
    }
}
