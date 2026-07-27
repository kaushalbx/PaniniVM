package dev.panini.unadipatha.adhyaya5

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 5.5: शॄवृभ्याम् अनक्
object ShrVrbhyamAnakSutra : UnadiSutra(
    number = "5.5",
    text = "शॄवृभ्याम् अनक्",
    roots = setOf(ShrDhatu(), VrDhatu()),
    pratyaya = "अनक्",
    pratyayaSurface = "अन",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "शॄ" to Samjna.Rudhi("शरण"),
        "शृ" to Samjna.Rudhi("शरण"),
        "वृ" to Samjna.Rudhi("वरुण"),
        "वृञ्" to Samjna.Rudhi("वरुण")
    ),
    meaning = Artha.Karaka.ADHIKARANA,
    hindiExplanation = "शॄ तथा वृ धातुओं से अनक् प्रत्यय होता है।"
)
