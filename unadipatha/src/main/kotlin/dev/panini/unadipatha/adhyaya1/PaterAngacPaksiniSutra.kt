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

// 1.116: पतेरङ्गच् पक्षिणि
object PaterAngacPaksiniSutra : UnadiSutra(
    number = "1.116",
    text = "पतेरङ्गच् पक्षिणि",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0979" }),
    pratyaya = "अङ्गच्",
    pratyayaSurface = "अङ्ग",
    itMarkers = setOf(ItMarker.C),
    rootSamjnaMap = mapOf("पतॢँ" to Samjna.Rudhi("पतङ्ग")),
    applicationMeaning = Artha.Explanation("पक्षी के अर्थ में"),
    rootMeaningMap = mapOf("पतॢँ" to Artha.Explanation("पक्षी")),
    rootMorphology = mapOf("पतॢँ" to UnadiRootMorphology(conditionedStem = "पत्".toVarnas())),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("पतङ्ग".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पक्षी"), "1.116"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("पतङ्ग पक्षी-अर्थ में अङ्गच्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "पतॢँ धातु से पक्षी के अर्थ में अङ्गच् प्रत्यय होता है। प्रत्यय का अन्तिम च् इत्संज्ञक होकर लुप्त होता है; इसलिए उसका श्रव्य Varna-रूप अङ्ग है। धातु के इत्-वर्णों के लोप के बाद पत् आधार रहता है और पत् + अङ्ग से पतङ्ग प्रातिपदिक बनता है। ‘पक्षिणि’ केवल उदाहरण का अर्थ नहीं, सूत्र में कही हुई application restriction है; इसलिए उसे पृथक् applicationMeaning के रूप में भी सुरक्षित किया गया है। वृत्ति ‘पतङ्गः’ कहती है, अतः रूप पुल्लिङ्ग है और अर्थ पक्षी है। इससे आगे पतङ्ग के अन्य कोशार्थ इस स्रोतांश में नहीं दिए गए, इसलिए वे नहीं जोड़े गए।",
)
