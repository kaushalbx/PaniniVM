package dev.panini.unadipatha.adhyaya2

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.PaaDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 2.95: पापाभ्यां तृन्
object PaPaBhyamTrnSutra : UnadiSutra(
    number = "2.95",
    text = "पापाभ्यां तृन्",
    roots = setOf(PaaDhatu()),
    pratyaya = "तृन्",
    pratyayaSurface = "तृ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "पा" to Samjna.Rudhi("पितृ"),
        "पाँ" to Samjna.Rudhi("पितृ")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "पा धातु से कर्ता अर्थ में तृन् प्रत्यय होता है और इ-कार आदेश होता है।"
)
