package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.5: त्रो रश्च लः
object TroRashCaLahSutra : UnadiSutra(
    number = "1.5",
    text = "त्रो रश्च लः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1124" }),
    pratyaya = "ञुण्",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NYIT, ItMarker.NIT),
    rootSamjnaMap = mapOf("तॄ" to Samjna.Rudhi("तालु")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Vyanjana.RA, replacement = Vyanjana.LA),
    ),
    baseSamjnas = setOf(
        Samjna.Affix.KRT,
        Samjna.Unit.PRATIPADIKA,
        Samjna.Karaka.KARANA,
    ),
    meaning = Artha.Karaka.KARANA,
    hindiExplanation = "तॄ धातु के बाद पूर्वसूत्र से प्राप्त ञुण् प्रत्यय होता है। ञ् और ण् इत्संज्ञक होकर लुप्त होते हैं तथा उ शेष रहता है। " +
        "सूत्र में स्पष्ट रूप से र् के स्थान पर ल् का विधान है। इस प्रकार ‘जिससे वर्ण पार होते हैं’ इस करण-अर्थ में तालु शब्द सिद्ध होता है।",
)
