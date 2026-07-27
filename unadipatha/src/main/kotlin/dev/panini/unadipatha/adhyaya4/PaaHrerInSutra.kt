package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.HrDhatu
import dev.panini.dhatupatha.bhvadi.PaaDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 4.50: पाहेः कित्
object PaaHrerInSutra : UnadiSutra(
    number = "4.50",
    text = "पाहेः कित्",
    roots = setOf(PaaDhatu(), HrDhatu()),
    pratyaya = "इन्",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "पा" to Samjna.Rudhi("पाही"),
        "हृ" to Samjna.Rudhi("हारी")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "पा तथा हृ धातुओं से इन् प्रत्यय कित् होता है।"
)
