package dev.panini.unadipatha.adhyaya3

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.PumsDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 3.1: पुंसोऽसुन्
object PunsoAsunSutra : UnadiSutra(
    number = "3.1",
    text = "पुंसोऽसुन्",
    roots = setOf(PumsDhatu()),
    pratyaya = "असुन्",
    pratyayaSurface = "अस्",
    itMarkers = setOf(ItMarker.NIT),
    samjnas = setOf(
        Samjna.Technical.KRT,
        Samjna.Technical.PRATIPADIKA,
        Samjna.Karaka.KARTA,
        Samjna.Rudhi("पुमः")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "पुम् धातु से असुन् प्रत्यय होता है।"
)
