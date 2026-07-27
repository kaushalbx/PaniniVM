package dev.panini.unadipatha.adhyaya3

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.DrshDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 3.60: दृशेः कः
object DrsehKsSutra : UnadiSutra(
    number = "3.60",
    text = "दृशेः कः",
    roots = setOf(DrshDhatu()),
    pratyaya = "कः",
    pratyayaSurface = "अ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("दृश्" to Samjna.Rudhi("दृश")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "दृश् धातु से कः प्रत्यय होता है।"
)
