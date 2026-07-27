package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.rudhadi.VidDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 4.218: विधेश्च
object VidheshcaSutra : UnadiSutra(
    number = "4.218",
    text = "विधेश्च",
    roots = setOf(VidDhatu()),
    pratyaya = "असुन्",
    pratyayaSurface = "अस्",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("विद्" to Samjna.Rudhi("वेधस्")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "विद् धातु से असुन् प्रत्यय होकर वेधस् शब्द सिद्ध होता है।"
)
