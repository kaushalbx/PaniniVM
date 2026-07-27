package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.RiDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 4.119: ऋषेः कित्
object RishehKitSutra : UnadiSutra(
    number = "4.119",
    text = "ऋषेः कित्",
    roots = setOf(RiDhatu()),
    pratyaya = "इन्",
    pratyayaSurface = "इ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("ऋ" to Samjna.Rudhi("ऋषि")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "ऋ धातु से कित् इन् प्रत्यय होकर ऋषि शब्द सिद्ध होता है।"
)
