package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 1.2: ताभ्यामन्यत्रापि दृश्यकर्तर्युण्
object TabhyamAnyatrapiDrshyaKartaryUnSutra : UnadiSutra(
    number = "1.2",
    text = "ताभ्यामन्यत्रापि दृश्यकर्तर्युण्",
    roots = setOf(DrDhatu(), ShrDhatu()),
    pratyaya = "उण्",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "दृ" to Samjna.Rudhi("दारु"),
        "शॄ" to Samjna.Rudhi("शारु")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "अन्य धातुओं से भी कर्ता अर्थ में उण् प्रत्यय देखा जाता है।"
)
