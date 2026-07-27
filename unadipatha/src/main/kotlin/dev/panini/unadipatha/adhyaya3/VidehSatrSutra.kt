package dev.panini.unadipatha.adhyaya3

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.rudhadi.VidDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 3.50: विदो लटो वा
object VidehSatrSutra : UnadiSutra(
    number = "3.50",
    text = "विदो लटो वा",
    roots = setOf(VidDhatu()),
    pratyaya = "शतृ",
    pratyayaSurface = "अत्",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("विद्" to Samjna.Rudhi("विद्वान्")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "विद् धातु से लट् के स्थान में वा शतृ आदेश होता है।"
)
