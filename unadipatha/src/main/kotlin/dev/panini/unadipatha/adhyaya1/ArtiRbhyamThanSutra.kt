package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.RiDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 1.6: अर्तिऋभ्यां थन्
object ArtiRbhyamThanSutra : UnadiSutra(
    number = "1.6",
    text = "अर्तिऋभ्यां थन्",
    roots = setOf(RiDhatu()),
    pratyaya = "थन्",
    pratyayaSurface = "थ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "ऋ" to Samjna.Rudhi("अर्थ")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "ऋ (अर्ति) धातु से थन् प्रत्यय होता है।"
)
