package dev.panini.unadipatha.adhyaya2

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.GrahDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 2.135: ग्रहेर्णिन्
object GraherInSutra : UnadiSutra(
    number = "2.135",
    text = "ग्रहेर्णिन्",
    roots = setOf(GrahDhatu()),
    pratyaya = "णिन्",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("ग्रह्" to Samjna.Rudhi("ग्राहिन्")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "ग्रह् धातु से णिन् प्रत्यय होता है।"
)
