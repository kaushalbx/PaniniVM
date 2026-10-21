package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.70: पः किच्च
object PahKicCaSutra : UnadiSutra(
    number = "1.70",
    text = "पः किच्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1074" }),
    pratyaya = "तुन्",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.NIT, ItMarker.KIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf("पा" to Samjna.Rudhi("पितु")),
    rootMeaningMap = mapOf("पा" to Artha.Explanation("वह्नि अथवा दिवाकर")),
    rootMorphology = mapOf(
        "पा" to UnadiRootMorphology(conditionedStem = "पि".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "पितु".toVarnas(),
            setOf(Linga.PUMS),
            Artha.Explanation("वह्नि अथवा दिवाकर"),
            "1.70",
        ),
    ),
    relatedSutraNumbers = setOf("1.69", "7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("पितु तुन्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और वह्नि अथवा दिवाकर का वाचक है।"),
    hindiExplanation = "सूत्र का ‘पः’ और वृत्ति का ‘पिबति’ पानार्थक भ्वादिगणीय पा धातु को निर्दिष्ट करते हैं। पिछले सूत्र से तुन् प्रत्यय आता है; उसका अन्तिम न् इत्संज्ञक होकर लुप्त होता है और तु शेष रहता है। ‘किच्च’ इस प्रत्यय को इस प्रयोग में कित् भी बनाता है और 7.2.9 के अनुसार इट्-आगम नहीं होता। वृत्ति निष्पन्न रूप पितु देती है, अतः इस औणादिक प्रयोग के लिए पा का प्रभावी आधार पि सुरक्षित किया गया है; पि + तु से पितु बनता है। स्रोत पि-आधार की कोई पृथक् सार्वत्रिक वर्ण-क्रिया नहीं बताता, इसलिए अनुमानित आ → इ नियम नहीं गढ़ा गया। वृत्ति के अनुसार पुल्लिङ्ग पितु वह्नि अर्थात् अग्नि और दिवाकर अर्थात् सूर्य का वाचक है।",
)
