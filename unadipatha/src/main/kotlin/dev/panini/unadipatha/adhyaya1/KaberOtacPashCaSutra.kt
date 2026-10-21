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
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.62: कबेरोतच् पश्च
object KaberOtacPashCaSutra : UnadiSutra(
    number = "1.62",
    text = "कबेरोतच् पश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0440" }),
    pratyaya = "ओतच्",
    pratyayaSurface = "ओत",
    itMarkers = setOf(ItMarker.CIT),
    rootSamjnaMap = mapOf("कबृँ" to Samjna.Rudhi("कपोत")),
    rootMeaningMap = mapOf("कबृँ" to Artha.Explanation("पक्षी")),
    rootMorphology = mapOf(
        "कबृँ" to UnadiRootMorphology(
            conditionedStem = "कब्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.BA, Vyanjana.PA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कपोत".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पक्षी"), "1.62"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कपोत ओतच्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और एक पक्षी का वाचक है।"),
    hindiExplanation = "वर्णनार्थक भ्वादिगणीय कबृँ धातु के कब् आधार के बाद ओतच् प्रत्यय होता है। ओतच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है और ओत शेष रहता है। सूत्र का ‘पश्च’ धातु के ब् के स्थान पर प् का स्पष्ट वर्णादेश बताता है; इसलिए कब् आधार कप् बनता है। कप् + ओत के संयोग से कपोत प्रातिपदिक सिद्ध होता है। वृत्ति ‘कपोतः पक्षी’ कहती है, अतः पुल्लिङ्ग और पक्षी-अर्थ सुरक्षित किए गए हैं; किसी विशिष्ट पक्षी-जाति का अतिरिक्त अर्थ नहीं जोड़ा गया।",
)
