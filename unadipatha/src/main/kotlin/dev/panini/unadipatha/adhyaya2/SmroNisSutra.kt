package dev.panini.unadipatha.adhyaya2

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.DrshDhatu
import dev.panini.dhatupatha.bhvadi.SmrDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 2.115: स्मृदृशोः असुन्
object SmroNisSutra : UnadiSutra(
    number = "2.115",
    text = "स्मृदृशोः असुन्",
    roots = setOf(SmrDhatu(), DrshDhatu()),
    pratyaya = "असुन्",
    pratyayaSurface = "अस्",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "स्मृ" to Samjna.Rudhi("स्मर"),
        "स्मृँ" to Samjna.Rudhi("स्मर"),
        "स्मॄ" to Samjna.Rudhi("स्मर"),
        "दृश्" to Samjna.Rudhi("दृश")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "स्मृ तथा दृश् धातुओं से असुन् प्रत्यय होता है।"
)
