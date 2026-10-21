package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.39: व्यथेः संप्रसारणं किच्च
object VyathehSamprasaranamKicCaSutra : UnadiSutra(
    number = "1.39",
    text = "व्यथेः संप्रसारणं किच्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0868" }),
    pratyaya = "उरच्",
    pratyayaSurface = "उर",
    itMarkers = setOf(ItMarker.CIT, ItMarker.KIT),
    rootSamjnaMap = mapOf("व्यथँ" to Samjna.Rudhi("विथुर")),
    rootMeaningMap = mapOf("व्यथँ" to Artha.Rudhi("विथुर", "thief or demon")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Vyanjana.YA, replacement = Svara.I),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("विथुर", "thief or demon"),
    hindiExplanation = "भयसञ्चलन अर्थ वाले व्यथँ धातु के बाद पूर्वसूत्र से अनुवृत्त उरच् प्रत्यय होता है। उरच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है तथा उर शेष रहता है; ‘किच्च’ के विधान से यह प्रत्यय कित् भी होता है। " +
        "सूत्र व्यथ् के यकार का संप्रसारण इकार करता है। इस प्रकार व्यथ् में य् के स्थान पर इ होकर विथ् आधार बनता है और उर प्रत्यय के योग से विथुर प्रातिपदिक सिद्ध होता है। वृत्ति का ‘विथुरः’ प्रथमा-एकवचन रूप है और उसे चोर तथा राक्षस—इन दो अर्थों में बताती है; model में विभक्ति हटाकर प्रातिपदिक तथा दोनों प्रमाणित अर्थ रखे गए हैं।",
)
