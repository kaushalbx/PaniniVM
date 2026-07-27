package dev.panini.unadipatha.adhyaya3

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna

// 3.17: तनूकृभ्यः तन्
object TanuKrbhyahTanSutra : UnadiSutra(
    number = "3.17",
    text = "तनूकृभ्यः तन्",
    roots = setOf(KruDhatu()),
    pratyaya = "तन्",
    pratyayaSurface = "त",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("कृ" to Samjna.Rudhi("क्रतु")),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "तन् तथा कृ धातुओं से तन् प्रत्यय होता है।"
)
