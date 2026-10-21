package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.58: पतिकठिकुठिगडिगुडिदंशिभ्य एरक्
object PatiKathiKuthiGadiGudiDansibhyahErakSutra : UnadiSutra(
    number = "1.58",
    text = "पतिकठिकुठिगडिगुडिदंशिभ्य एरक्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0979" },
        BhvadiDhatus.all.single { it.id == "01.0385" },
        BhvadiDhatus.all.single { it.id == "01.0886" },
        TudadiDhatus.all.single { it.id == "06.0097" },
        BhvadiDhatus.all.single { it.id == "01.1144" },
    ),
    rootReferences = setOf("कुठ"),
    pratyaya = "एरक्",
    pratyayaSurface = "एर",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "पतॢँ" to Samjna.Rudhi("पतेर"),
        "कठँ" to Samjna.Rudhi("कठेर"),
        "कुठ" to Samjna.Rudhi("कुठेर"),
        "गडँ" to Samjna.Rudhi("गडेर"),
        "गुडँ" to Samjna.Rudhi("गुडेर"),
        "दन्शँ" to Samjna.Rudhi("दंशेर"),
    ),
    rootMeaningMap = mapOf(
        "पतॢँ" to Artha.Explanation("पक्षी अथवा गन्ता"),
        "कठँ" to Artha.Explanation("कष्ट से जीवन बिताने वाला"),
        "कुठ" to Artha.Explanation("पत्ते खाने वाला"),
        "गडँ" to Artha.Explanation("मेघ"),
        "गुडँ" to Artha.Explanation("गुड़क"),
        "दन्शँ" to Artha.Explanation("हिंस्र"),
    ),
    rootMorphology = mapOf(
        "कुठ" to UnadiRootMorphology(conditionedStem = "कुठ्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("पतेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पक्षी अथवा गन्ता"), "1.58"),
        UnadiLexicalAttestation("कठेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कष्ट से जीवन बिताने वाला"), "1.58"),
        UnadiLexicalAttestation("कुठेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पत्ते खाने वाला"), "1.58"),
        UnadiLexicalAttestation("गडेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("मेघ"), "1.58"),
        UnadiLexicalAttestation("गुडेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("गुड़क"), "1.58"),
        UnadiLexicalAttestation("दंशेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("हिंस्र"), "1.58"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("पतेर आदि छह एरक्-प्रत्ययान्त शब्द अपने-अपने वृत्तिप्रदत्त रूढ़ अर्थों में प्रयुक्त होते हैं।"),
    hindiExplanation = "सूत्र में पति, कठि, कुठि, गडि, गुडि और दंशि के बाद एरक् प्रत्यय होता है। एरक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है और एर शेष रहता है। गत्यर्थक पतॢँ के पत् आधार से पतेर, कृच्छ्रजीवनार्थक कठँ से कठेर, सेचनार्थक गडँ से गडेर, रक्षणार्थक गुडँ से गुडेर और दंशार्थक दन्शँ से दंशेर बनते हैं। वृत्ति से कुठि का प्रभावी आधार कुठ् मिलता है; उपलब्ध Dhātupāṭha प्रविष्टियों से उसके इस प्रयोग की सुरक्षित पहचान नहीं होती, इसलिए उसे source-only root reference रखा गया है। ‘बाहुलकान्नुम् न’ के अनुसार इस रूप में नुम् नहीं आता और कुठ् + एर से कुठेर बनता है। स्रोत-पाठ के ‘छठेरः’ को अन्य सिद्धान्तकौमुदी प्रतियों और कठँ कृच्छ्रजीवने धातु के प्रमाण पर OCR-दोष मानकर कठेर रखा गया है। वृत्ति के अनुसार पतेर पक्षी अथवा गन्ता, कठेर कृच्छ्रजीवी, कुठेर पर्णाश, गडेर मेघ, गुडेर गुड़क और दंशेर हिंस्र का वाचक है।",
)
