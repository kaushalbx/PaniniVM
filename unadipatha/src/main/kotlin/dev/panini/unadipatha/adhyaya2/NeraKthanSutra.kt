package dev.panini.unadipatha.adhyaya2

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.NiDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 2.2: णीञः कित्
object NeraKthanSutra : UnadiSutra(
    number = "2.2",
    text = "णीञः कित्",
    roots = setOf(NiDhatu()),
    pratyaya = "क्थन्",
    pratyayaSurface = "ति",
    itMarkers = setOf(ItMarker.KIT, ItMarker.NIT),
    rootSamjnaMap = mapOf("णीञ्" to Samjna.Rudhi("नीति")),
    meaning = Artha.Karaka.KARANA,
    hindiExplanation = "णीञ् धातु से क्थन् प्रत्यय होता है।"
)
