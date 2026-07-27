package dev.panini.unadipatha.adhyaya5

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.divadi.DivDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 5.10: दिवेः ऊन्
object DivaerUunSutra : UnadiSutra(
    number = "5.10",
    text = "दिवेः ऊन्",
    roots = setOf(DivDhatu()),
    pratyaya = "ऊन्",
    pratyayaSurface = "ऊ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("दिव्" to Samjna.Rudhi("दिव्य")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "दिव् धातु से ऊन् प्रत्यय होता है।"
)
