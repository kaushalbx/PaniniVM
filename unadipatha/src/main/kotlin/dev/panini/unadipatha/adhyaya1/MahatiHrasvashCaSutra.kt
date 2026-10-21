package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSequenceLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.31: महति ह्रस्वश्च
object MahatiHrasvashCaSutra : UnadiSutra(
    number = "1.31",
    text = "महति ह्रस्वश्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0034" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("ऊर्णुञ्" to Samjna.Rudhi("उरु")),
    rootMeaningMap = mapOf("ऊर्णुञ्" to Artha.Rudhi("उरु", "great or wide")),
    applicationMeaning = Artha.Rudhi("महत्", "great"),
    varnaSequenceLopas = listOf(
        UnadiVarnaSequenceLopa(listOf(Vyanjana.NNA, Svara.U)),
    ),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(Svara.UU, Svara.U),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("उरु", "great or wide"),
    hindiExplanation = "जब ऊर्णुञ् धातु से बनने वाला शब्द महत् अर्थात् बड़ा या विस्तृत अर्थ व्यक्त करे, तब अनुवृत्त कु प्रत्यय होता है। पिछले सूत्र से णु वर्ण-समूह का लोप जारी रहता है और ‘ह्रस्वश्च’ से आरम्भिक ऊ का ह्रस्व उ होता है। " +
        "इस प्रकार ऊर्णु से णु हटाकर ऊर्, फिर ऊ→उ तथा प्रत्यय के उ के योग से उरु रूप सिद्ध होता है। अर्थ-शर्त को निष्पन्न शब्द के रूढ़ार्थ से अलग applicationMeaning में रखा गया है।",
)
