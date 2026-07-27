package dev.panini.unadipatha.adhyaya2

import dev.panini.dhatupatha.adadi.PraDhatu
import dev.panini.dhatupatha.bhvadi.ShruDhatu
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 2.8: श्रुवृदृप्रावृभ्यः सिः
object ShruVrDrPraVrbhyahSihSutra : UnadiSutra(
    number = "2.8",
    text = "श्रुवृदृप्रावृभ्यः सिः",
    roots = setOf(ShruDhatu(), VrDhatu(), DrDhatu(), PraDhatu()),
    pratyaya = "सिः",
    pratyayaSurface = "सि",
    itMarkers = emptySet(),
    baseSamjnas = setOf(
        Samjna.Affix.KRT,
        Samjna.Unit.PRATIPADIKA,
        Samjna.Karaka.KARTA
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "श्रु, वृ, दृ, प्रा, वृ धातुओं से सिः प्रत्यय होता है।"
)
