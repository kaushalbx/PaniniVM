package dev.panini.unadipatha.adhyaya3

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.ShruDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 3.80: श्रुवश्च
object ShruvaKitSutra : UnadiSutra(
    number = "3.80",
    text = "श्रुवश्च",
    roots = setOf(ShruDhatu()),
    pratyaya = "कि",
    pratyayaSurface = "ति",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("श्रु" to Samjna.Rudhi("श्रुति")),
    meaning = Artha.Karaka.BHAVA,
    hindiExplanation = "श्रु धातु से कि प्रत्यय होता है।"
)
