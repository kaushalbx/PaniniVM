package dev.panini.unadipatha.adhyaya2

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.juhotyadi.DhaDhatu
import dev.panini.dhatupatha.juhotyadi.HuDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 2.85: हुधाभ्यां ञ्नुः
object HuDhabhyamJnuhSutra : UnadiSutra(
    number = "2.85",
    text = "हुधाभ्यां ञ्नुः",
    roots = setOf(HuDhatu(), DhaDhatu()),
    pratyaya = "ञ्नुः",
    pratyayaSurface = "नु",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "हु" to Samjna.Rudhi("हुताशन"),
        "धा" to Samjna.Rudhi("धातु")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "हु तथा धा धातुओं से ञ्नुः प्रत्यय होता है।"
)
