package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

// 4.138: कवेर्बुन्
object KaverBunSutra : UnadiSutra(
    number = "4.138",
    text = "कवेर्बुन्",
    roots = setOf(KruDhatu()),
    pratyaya = "बुन्",
    pratyayaSurface = "इ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("कृ" to Samjna.Rudhi("कवि")),
    meaning = Artha.Context.SHILPA,
    hindiExplanation = "कृ धातु से बुन् (इ) प्रत्यय होकर कवि शब्द सिद्ध होता है।"
)
