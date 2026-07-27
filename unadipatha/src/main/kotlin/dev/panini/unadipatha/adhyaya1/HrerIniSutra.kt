package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.HrDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 1.157: हरेरिनिः
object HrerIniSutra : UnadiSutra(
    number = "1.157",
    text = "हरेरिनिः",
    roots = setOf(HrDhatu()),
    pratyaya = "इनिः",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("हृ" to Samjna.Rudhi("हारिन्")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "हृ धातु से इनिः प्रत्यय होता है।"
)
