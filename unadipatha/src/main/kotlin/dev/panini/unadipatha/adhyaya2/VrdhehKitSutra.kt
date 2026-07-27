package dev.panini.unadipatha.adhyaya2

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.VridhDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 2.41: वृधेः कित्
object VrdhehKitSutra : UnadiSutra(
    number = "2.41",
    text = "वृधेः कित्",
    roots = setOf(VridhDhatu()),
    pratyaya = "अन्",
    pratyayaSurface = "अन्",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("वृध्" to Samjna.Rudhi("वर्धमान")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "वृध् धातु से अन् प्रत्यय कित् होता है।"
)
