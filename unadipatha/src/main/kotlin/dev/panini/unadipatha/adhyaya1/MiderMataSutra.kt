package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.svadi.MiDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 1.53: मिदेर्माता
object MiderMataSutra : UnadiSutra(
    number = "1.53",
    text = "मिदेर्माता",
    roots = setOf(MiDhatu()),
    pratyaya = "क्त्र",
    pratyayaSurface = "त्र",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("मि" to Samjna.Rudhi("मित्र")),
    meaning = Artha.Karaka.KARANA,
    hindiExplanation = "मिद् (मि) धातु से क्त्र प्रत्यय होता है और गुण-निषेध होकर मित्र शब्द सिद्ध होता है।"
)
