package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 4.1: कृवृदारिभ्यः कनिन्
object KrvrDaribhyahKaninSutra : UnadiSutra(
    number = "4.1",
    text = "कृवृदारिभ्यः कनिन्",
    roots = setOf(KruDhatu(), VrDhatu(), DrDhatu()),
    pratyaya = "कनिन्",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.KIT, ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "कृ" to Samjna.Rudhi("कर्ण"),
        "वृ" to Samjna.Rudhi("वर्णि"),
        "वृञ्" to Samjna.Rudhi("वर्णि"),
        "दॄ" to Samjna.Rudhi("धर्मि"),
        "दृ" to Samjna.Rudhi("धर्मि")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "कृ, वृ, दॄ धातुओं से कनिन् प्रत्यय होता है।"
)
