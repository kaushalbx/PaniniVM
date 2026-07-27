package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.GamDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 1.156: गमेरिनिः
object GamerIniSutra : UnadiSutra(
    number = "1.156",
    text = "गमेरिनिः",
    roots = setOf(GamDhatu()),
    pratyaya = "इनिः",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("गम्" to Samjna.Rudhi("गामिन्")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "गम् धातु से इनिः प्रत्यय होता है।"
)
