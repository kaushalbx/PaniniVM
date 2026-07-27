package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhuDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 1.30: भुवः कित्
object BhuvaKthunSutra : UnadiSutra(
    number = "1.30",
    text = "भुवः कित्",
    roots = setOf(BhuDhatu()),
    pratyaya = "क्थुन्",
    pratyayaSurface = "अन",
    itMarkers = setOf(ItMarker.KIT, ItMarker.NIT),
    rootSamjnaMap = mapOf("भू" to Samjna.Rudhi("भुवन")),
    meaning = Artha.Karaka.ADHIKARANA,
    hindiExplanation = "भू धातु से क्थुन् प्रत्यय होता है।"
)
