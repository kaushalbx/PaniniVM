package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.DrshDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 4.2: भृमृदृशियजिपनिभ्यः कन्
object BhrMrDrshiYajiPanibhyahKanSutra : UnadiSutra(
    number = "4.2",
    text = "भृमृदृशियजिपनिभ्यः कन्",
    roots = setOf(DrshDhatu()),
    pratyaya = "कन्",
    pratyayaSurface = "अक",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("दृश्" to Samjna.Rudhi("दृशक")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "भृ, मृ, दृश्, यज्, पन् धातुओं से कन् प्रत्यय होता है।"
)
