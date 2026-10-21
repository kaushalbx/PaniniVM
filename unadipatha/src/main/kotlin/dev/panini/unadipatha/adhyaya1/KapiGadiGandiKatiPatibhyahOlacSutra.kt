package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.66: कपिगडिगण्डिकटिपटिभ्य ओलच्
object KapiGadiGandiKatiPatibhyahOlacSutra : UnadiSutra(
    number = "1.66",
    text = "कपिगडिगण्डिकटिपटिभ्य ओलच्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0435" },
        BhvadiDhatus.all.single { it.id == "01.0886" },
        BhvadiDhatus.all.single { it.id == "01.0068" },
        BhvadiDhatus.all.single { it.id == "01.0359" },
        BhvadiDhatus.all.single { it.id == "01.0333" },
    ),
    pratyaya = "ओलच्",
    pratyayaSurface = "ओल",
    itMarkers = setOf(ItMarker.CIT),
    rootSamjnaMap = mapOf(
        "कपिँ" to Samjna.Rudhi("कपोल"),
        "गडँ" to Samjna.Rudhi("गडोल"),
        "गडिँ" to Samjna.Rudhi("गण्डोल"),
        "कटीँ" to Samjna.Rudhi("कटोल"),
        "पटँ" to Samjna.Rudhi("पटोल"),
    ),
    rootMeaningMap = mapOf(
        "गडँ" to Artha.Explanation("गुड़क का पर्याय"),
        "गडिँ" to Artha.Explanation("गुड़क का पर्याय"),
        "कटीँ" to Artha.Explanation("कटु"),
    ),
    rootMorphology = mapOf(
        "कपिँ" to UnadiRootMorphology(
            conditionedStem = "कम्प्".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.MA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कपोल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.66"),
        UnadiLexicalAttestation("गडोल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("गुड़क का पर्याय"), "1.66"),
        UnadiLexicalAttestation("गण्डोल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("गुड़क का पर्याय"), "1.66"),
        UnadiLexicalAttestation("कटोल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कटु"), "1.66"),
        UnadiLexicalAttestation("पटोल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.66"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कपोल आदि पाँच ओलच्-प्रत्ययान्त प्रातिपदिक सूत्र और वृत्ति के अनुसार सिद्ध होते हैं।"),
    hindiExplanation = "सूत्र में पठित कपि, गडि, गण्डि, कटि और पटि के बाद ओलच् प्रत्यय होता है। ओलच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है और ओल शेष रहता है। भ्वादिगणीय कपिँ का धातुपाठीय आधार कम्प् है, पर सूत्र में ‘कपि’ निर्देश होने के कारण वृत्ति नलोप बताती है; यहाँ नुम् से आए नासिक्य प्रभाव के म्-वर्ण का Varna-लोप करके कप् + ओल से कपोल बनाया गया है। सेचनार्थक गडँ के गड् आधार से गडोल और गण्डरोगार्थक गडिँ के गण्ड् आधार से गण्डोल बनते हैं; वृत्ति दोनों को गुड़क के पर्याय बताती है। गत्यर्थक कटीँ के कट् आधार से कटोल बनता है और उसका अर्थ कटु दिया गया है। गत्यर्थक पटँ के पट् आधार से पटोल बनता है। वृत्ति कपोल और पटोल के पृथक् अर्थ नहीं देती, इसलिए उनके परिचित कोशार्थ यहाँ अनुमान से नहीं जोड़े गए हैं।",
)
