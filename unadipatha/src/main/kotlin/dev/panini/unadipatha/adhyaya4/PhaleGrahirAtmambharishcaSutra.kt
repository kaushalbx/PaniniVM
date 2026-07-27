package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.GrahDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 4.135: फले ग्रहिरात्मभरिश्च
object PhaleGrahirAtmambharishcaSutra : UnadiSutra(
    number = "4.135",
    text = "फले ग्रहिरात्मभरिश्च",
    roots = setOf(GrahDhatu()),
    pratyaya = "इन्",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("ग्रह्" to Samjna.Rudhi("फलग्रहि")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "फल उपपद में ग्रह् धातु से इन् प्रत्यय होता है।"
)
