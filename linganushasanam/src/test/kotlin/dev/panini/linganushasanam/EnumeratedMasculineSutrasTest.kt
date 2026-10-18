package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.linganushasanam.adhyaya2.DhvajaGajaMunjaPunjaSutra
import dev.panini.linganushasanam.adhyaya2.HastaKuntaAntaVataVrataDutaSutra
import dev.panini.linganushasanam.adhyaya2.HradaKandaKundaBudbudaSabdaSutra
import dev.panini.linganushasanam.adhyaya2.PallavaPalvalaKapharephaSutra
import dev.panini.linganushasanam.adhyaya2.RsiRasiDrtiGranthiKrimiDhvaniSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class EnumeratedMasculineSutrasTest {
    @Test
    fun `canonical enumerations assign masculine with exact rule provenance`() {
        val expected = mapOf(
            "ऋषि" to "109", "राशि" to "109", "दृति" to "109", "ग्रन्थि" to "109",
            "क्रिमि" to "109", "ध्वनि" to "109", "बालि" to "109", "कौलि" to "109",
            "मौलि" to "109", "रवि" to "109", "कवि" to "109", "कपि" to "109", "मुनि" to "109",
            "ध्वज" to "110", "गज" to "110", "मुञ्ज" to "110", "पुञ्ज" to "110",
            "हस्त" to "111", "कुन्त" to "111", "अन्त" to "111", "वात" to "111",
            "व्रात" to "111", "दूत" to "111", "धूर्त" to "111", "सूत" to "111",
            "चूत" to "111", "मुहूर्त" to "111",
            "ह्रद" to "114", "कन्द" to "114", "कुन्द" to "114", "बुद्बुद" to "114", "शब्द" to "114",
            "पल्लव" to "116", "पल्वल" to "116", "कफ" to "116", "रेफ" to "116",
            "कटाह" to "116", "निर्व्यूह" to "116", "मठ" to "116", "मणि" to "116",
            "तरङ्ग" to "116", "तुरङ्ग" to "116", "गन्ध" to "116", "स्कन्ध" to "116",
            "मृदङ्ग" to "116", "सङ्ग" to "116", "समुद्ग" to "116", "पुङ्ख" to "116",
            "भृत्र" to "158", "अमित्र" to "158", "छात्र" to "158", "पुत्र" to "158",
            "मन्त्र" to "158", "वृत्र" to "158", "मेढ्र" to "158", "उष्ट्र" to "158",
        )
        for ((stem, rule) in expected) {
            val entry = PaninianPratipadikaLexicon.findPratipadika(stem)!!
            assertEquals(setOf(Linga.PUMS), entry.linga, stem)
            assertEquals(setOf(rule), entry.lingaRuleIds, stem)
        }
    }

    @Test
    fun `enumerated rules reject similar but unlisted stems`() {
        val rules = listOf(
            RsiRasiDrtiGranthiKrimiDhvaniSutra,
            DhvajaGajaMunjaPunjaSutra,
            HastaKuntaAntaVataVrataDutaSutra,
            HradaKandaKundaBudbudaSabdaSutra,
            PallavaPalvalaKapharephaSutra,
        )
        for (stem in listOf("गजक", "कविता", "शब्दिन्", "सङ्गत")) {
            assertFalse(rules.any { it.matches(LingaRuleContext(stem)) }, stem)
        }
    }
}
