package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.56: गुपादिभ्यः कित्
object GupadibhyahKitSutra : UnadiSutra(
    number = "1.56",
    text = "गुपादिभ्यः कित्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0461" },
        BhvadiDhatus.all.single { it.id == "01.1126" },
        BhvadiDhatus.all.single { it.id == "01.1043" },
    ),
    pratyaya = "इलच्",
    pratyayaSurface = "इल",
    itMarkers = setOf(ItMarker.CIT, ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "गुपूँ" to Samjna.Rudhi("गुपिल"),
        "तिजँ" to Samjna.Rudhi("तिजिल"),
        "गुहूँ" to Samjna.Rudhi("गुहिल"),
    ),
    rootMeaningMap = mapOf(
        "गुपूँ" to Artha.Explanation("राजा"),
        "तिजँ" to Artha.Explanation("चन्द्रमा"),
        "गुहूँ" to Artha.Explanation("वन"),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("गुपिल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("राजा"), "1.56"),
        UnadiLexicalAttestation("तिजिल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("चन्द्रमा"), "1.56"),
        UnadiLexicalAttestation("गुहिल".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("वन"), "1.56"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("गुपिल राजा, तिजिल चन्द्रमा और गुहिल वन का वाचक है।"),
    hindiExplanation = "रक्षणार्थक गुपूँ, निशान/तीक्ष्णता-अर्थक तिजँ और संवरणार्थक गुहूँ—इन धातुओं के बाद पिछले सूत्र से अनुवृत्त इलच् प्रत्यय होता है। इलच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है और इल शेष रहता है। इस सूत्र का ‘कित्’ विधान प्रत्यय को कित्संज्ञा देता है। कित्त्व के कारण गुप् और गुह् की लघु उपधा उ का गुण नहीं होता; इसलिए गोपिल या गोहिल के स्थान पर गुपिल और गुहिल सिद्ध होते हैं। तिज् + इल से तिजिल बनता है। वृत्ति के अनुसार गुपिल पुल्लिङ्ग में राजा, तिजिल पुल्लिङ्ग में चन्द्रमा और गुहिल नपुंसकलिङ्ग में वन का वाचक है।",
)
