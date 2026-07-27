package dev.panini.unadipatha.adhyaya3

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.JiDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 3.75: जिञः कित्
object JiaerInSutra : UnadiSutra(
    number = "3.75",
    text = "जिञः कित्",
    roots = setOf(JiDhatu()),
    pratyaya = "इन्",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("जि" to Samjna.Rudhi("जयिन")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "जिञ् धातु से इन् प्रत्यय कित् होता है।"
)
