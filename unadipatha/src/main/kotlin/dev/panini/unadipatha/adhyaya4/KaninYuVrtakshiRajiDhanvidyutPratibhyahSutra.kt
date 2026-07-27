package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.YuDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 4.18: कनिन् युवृतक्षिराजिधन्विद्युत्प्रतिभ्यः
object KaninYuVrtakshiRajiDhanvidyutPratibhyahSutra : UnadiSutra(
    number = "4.18",
    text = "कनिन् युवृतक्षिराजिधन्विद्युत्प्रतिभ्यः",
    roots = setOf(YuDhatu(), VrDhatu()),
    pratyaya = "कनिन्",
    pratyayaSurface = "इन्",
    itMarkers = setOf(ItMarker.KIT, ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "यु" to Samjna.Rudhi("युवन्"),
        "वृ" to Samjna.Rudhi("वर्णि"),
        "वृञ्" to Samjna.Rudhi("वर्णि")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "यु, वृ आदि धातुओं से कनिन् प्रत्यय होता है।"
)
