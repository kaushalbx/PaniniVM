package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 1.3: कृसृभ्यामुण्
object KrSrbhyamUnSutra : UnadiSutra(
    number = "1.3",
    text = "कृसृभ्यामुण्",
    roots = setOf(KruDhatu(), SrDhatu()),
    pratyaya = "उण्",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    samjnas = setOf(
        Samjna.Technical.KRT,
        Samjna.Technical.PRATIPADIKA,
        Samjna.Karaka.KARTA,
        Samjna.Rudhi("कारु"),
        Samjna.Rudhi("सरु")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "कृ (कृ विलेखने) और सृ धातु से उण् प्रत्यय होता है।"
)
