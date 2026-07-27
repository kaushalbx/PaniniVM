package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.VaaDhatu
import dev.panini.dhatupatha.bhvadi.JiDhatu
import dev.panini.dhatupatha.bhvadi.PaaDhatu
import dev.panini.dhatupatha.bhvadi.SvadDhatu
import dev.panini.dhatupatha.svadi.AshDhatu
import dev.panini.dhatupatha.svadi.MiDhatu
import dev.panini.dhatupatha.svadi.SadhDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 1.1: कृवापाजिमिस्वदिसाध्यशूभ्य उण्
object KrvaPajiMiSvadisadhyashubhyoUnSutra : UnadiSutra(
    number = "1.1",
    text = "कृवापाजिमिस्वदिसाध्यशूभ्य उण्",
    roots = setOf(
        KruDhatu(), VaaDhatu(), PaaDhatu(), JiDhatu(),
        MiDhatu(), SvadDhatu(), SadhDhatu(), AshDhatu()
    ),
    pratyaya = "उण्",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    samjnas = setOf(
        Samjna.Technical.KRT,
        Samjna.Technical.PRATIPADIKA,
        Samjna.Karaka.KARTA,
        Samjna.Rudhi("कारु"),
        Samjna.Rudhi("वायु"),
        Samjna.Rudhi("पायु"),
        Samjna.Rudhi("स्वादु"),
        Samjna.Rudhi("साधु"),
        Samjna.Rudhi("आशु")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "कृ, वा, पा, जि, मि, स्वद्, साध्, अश् धातुओं से उण् प्रत्यय होता है।"
)
