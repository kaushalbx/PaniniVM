package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSequenceLopa

// 1.30: ऊर्णोतेर्नुलोपश्च
object UrnoterNuLopashCaSutra : UnadiSutra(
    number = "1.30",
    text = "ऊर्णोतेर्नुलोपश्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0034" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("ऊर्णुञ्" to Samjna.Rudhi("ऊरु")),
    rootMeaningMap = mapOf("ऊर्णुञ्" to Artha.Rudhi("ऊरु", "thigh")),
    varnaSequenceLopas = listOf(
        UnadiVarnaSequenceLopa(listOf(Vyanjana.NNA, Svara.U)),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("ऊरु", "thigh"),
    hindiExplanation = "आच्छादनार्थक ऊर्णुञ् धातु के बाद अनुवृत्त कु प्रत्यय होता है। क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। ‘नुलोपश्च’ धातु के ण् तथा उसके बाद के उ—पूरे णु वर्ण-समूह—का एक साथ लोप करता है; फलतः ऊर् शेष रहता है और प्रत्यय के उ से ऊरु रूप सिद्ध होता है। " +
        "स्रोत में ऊरु का अर्थ सक्थि अर्थात् जाँघ दिया गया है।",
)
