package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra129Langhi = BhvadiDhatus.all.single { it.id == "01.0113" }
private val sutra129Banhi = BhvadiDhatus.all.single { it.id == "01.0663" }

// 1.29: लङ्घिबंह्योर्नलोपश्च
object LanghiBanhyorNaLopashCaSutra : UnadiSutra(
    number = "1.29",
    text = "लङ्घिबंह्योर्नलोपश्च",
    roots = setOf(sutra129Langhi, sutra129Banhi),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra129Langhi.upadesha to Samjna.Rudhi("लघु"),
        sutra129Banhi.upadesha to Samjna.Rudhi("बहु"),
    ),
    rootMeaningMap = mapOf(
        sutra129Langhi.upadesha to Artha.Rudhi("लघु"),
        sutra129Banhi.upadesha to Artha.Rudhi("बहु"),
    ),
    rootMorphology = mapOf(
        sutra129Langhi.upadesha to UnadiRootMorphology(
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.NGA)),
            optionalVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.LA, Vyanjana.RA)),
        ),
        sutra129Banhi.upadesha to UnadiRootMorphology(
            varnaLopas = listOf(UnadiVarnaLopa(Ayogavaha.ANUSVARA)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(output = "रघु".toVarnas(), meaning = Artha.Rudhi("रघु", "a particular king")),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("लङ्घ् और बंह् से क्रमशः लघु तथा बहु, और वैकल्पिक लत्व-रत्व से रघु बनता है।"),
    hindiExplanation = "गतिऔर भोजननिवृत्ति अर्थ वाले लघिँ (जिसका धातु-रूप लङ्घ् है) तथा वृद्ध्यर्थक बहिँ (बंह्) के बाद कु प्रत्यय होता है; क् इत्संज्ञक होकर उ शेष रहता है। सूत्र के नकार-लोप से लङ्घ् का ङ् हटकर लघु और बंह् का अनुस्वार हटकर बहु बनता है। " +
        "वार्तिक में वाल, मूल, लघु, अलम् और अङ्गुलि के ल् को विकल्प से र् होने का विधान है; उसी विकल्प से लघु का रघु रूप बनता है, जो स्रोत के अनुसार एक विशेष राजा का नाम है। वैकल्पिक ल्→र् को अनिवार्य आदेश से अलग रखा गया है।",
)
