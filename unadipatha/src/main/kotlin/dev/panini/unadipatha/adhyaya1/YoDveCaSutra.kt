package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSanvatTreatment
import dev.panini.unadipatha.UnadiSutra

// 1.21: यो द्वे च
object YoDveCaSutra : UnadiSutra(
    number = "1.21",
    text = "यो द्वे च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0044" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    rootSamjnaMap = mapOf("या" to Samjna.Rudhi("ययु")),
    rootMeaningMap = mapOf("या" to Artha.Rudhi("ययु", "horse intended for the Ashvamedha sacrifice")),
    sanvatTreatment = UnadiSanvatTreatment(explicitDve = true, blocksItva = true),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("ययु", "horse intended for the Ashvamedha sacrifice"),
    hindiExplanation = "प्रापणार्थक या धातु के बाद उ प्रत्यय होता है। पूर्वसूत्र से सन्वत्-भाव की अनुवृत्ति है और ‘द्वे’ धातु के द्विरुक्त रूप का स्पष्ट विधान करता है। टीका के अनुसार द्वे का पुनः ग्रहण सन्वत् से मिलने वाले इत्त्व को रोकने के लिए है; इसलिए इयु न होकर ययु रूप बनता है। " +
        "ययु उस अश्व का रूढ़ नाम है जो अश्वमेध यज्ञ के लिए नियत किया गया हो।",
)
