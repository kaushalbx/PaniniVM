package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.SthaDhatu
import dev.panini.dhatupatha.juhotyadi.DaDhatu
import dev.panini.dhatupatha.juhotyadi.DhaDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 1.28: स्थाघ्वोरिच्च
object SthaghvorIccaSutra : UnadiSutra(
    number = "1.28",
    text = "स्थाघ्वोरिच्च",
    roots = setOf(SthaDhatu(), DaDhatu(), DhaDhatu()),
    pratyaya = "कि",
    pratyayaSurface = "ति",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "स्था" to Samjna.Rudhi("स्थिति"),
        "दा" to Samjna.Rudhi("दिति"),
        "धा" to Samjna.Rudhi("धृति")
    ),
    meaning = Artha.Karaka.BHAVA,
    hindiExplanation = "स्था तथा घु-संज्ञक (दा, धा) धातुओं से कि प्रत्यय होता है और इ-कार आदेश होता है।"
)
