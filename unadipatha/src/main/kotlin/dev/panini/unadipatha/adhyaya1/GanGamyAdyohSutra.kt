package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.GamDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.120: गन् गम्यद्योः
object GanGamyAdyohSutra : UnadiSutra(
    number = "1.120",
    text = "गन् गम्यद्योः",
    roots = setOf(GamDhatu(), AdadiDhatus.all.single { it.id == "02.0001" }),
    pratyaya = "गन्",
    pratyayaSurface = "ग",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf("गमॢँ" to Samjna.Rudhi("गङ्गा"), "अदँ" to Samjna.Rudhi("अद्ग")),
    rootMeaningMap = mapOf("अदँ" to Artha.Explanation("पुरोडाश")),
    rootMorphology = mapOf(
        "गमॢँ" to UnadiRootMorphology(
            conditionedStem = "गम्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NGA)),
        ),
        "अदँ" to UnadiRootMorphology(conditionedStem = "अद्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("गङ्गा".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.120"),
        UnadiLexicalAttestation("अद्ग".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पुरोडाश"), "1.120"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("गङ्गा और अद्ग गन्-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "गमॢँ और अदँ धातुओं के बाद गन् प्रत्यय होता है। प्रत्यय का अन्तिम न् इत्संज्ञक होकर लुप्त होता है, इसलिए श्रव्य प्रत्यय केवल ग है। गम् के अन्तिम म् के बाद कण्ठ्य ग् आने पर परसवर्ण ङ् का स्पष्ट Varna-आदेश होता है: गम् → गङ्; गङ् + ग से गङ्ग आधार और स्रोत में उद्धृत स्त्रीरूप गङ्गा मिलता है। अदँ के अद् आधार में कोई अतिरिक्त परिवर्तन नहीं है; अद् + ग से अद्ग बनता है। वृत्ति अद्गः को पुरोडाश बताती है और पुल्लिङ्ग में उद्धृत करती है। गङ्गा का स्त्रीलिङ्ग citation सुरक्षित है, पर इस स्थान पर उसका कोई अलग कोशार्थ नहीं दिया गया, इसलिए अर्थ अनुमान से नहीं जोड़ा गया।",
)
