package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.6: कृके वचः कश्च
object KrkeVacahKashCaSutra : UnadiSutra(
    number = "1.6",
    text = "कृके वचः कश्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0058" }),
    pratyaya = "ञुण्",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NYIT, ItMarker.NIT),
    rootSamjnaMap = mapOf("वचँ" to Samjna.Rudhi("कृकवाकु")),
    upapadas = setOf(listOf(Vyanjana.KA, Svara.R, Vyanjana.KA, Svara.A)),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Vyanjana.CA, replacement = Vyanjana.KA),
    ),
    baseSamjnas = setOf(
        Samjna.Affix.KRT,
        Samjna.Unit.PRATIPADIKA,
        Samjna.Karaka.KARTA,
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "कृक उपपद के साथ वचँ धातु के बाद पूर्वसूत्र से प्राप्त ञुण् प्रत्यय होता है। ञ् और ण् इत्संज्ञक होकर लुप्त होते हैं तथा उ शेष रहता है। " +
        "सूत्र के ‘कश्च’ पद से वच् के च् के स्थान पर क् होता है। इस प्रकार ‘कृक अर्थात् गले से बोलने वाला’ इस कर्ता-अर्थ में कृकवाकु शब्द सिद्ध होता है।",
)
