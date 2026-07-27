package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.dhatupatha.tudadi.GrDhatu
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna

// 1.5: कृगृशॄवृञ्भ्यः कुः
object KrGrShrVrbhyahKuSutra : UnadiSutra(
    number = "1.5",
    text = "कृगृशॄवृञ्भ्यः कुः",
    roots = setOf(KruDhatu(), GrDhatu(), ShrDhatu(), VrDhatu()),
    pratyaya = "कुः",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    samjnas = setOf(
        Samjna.Technical.KRT,
        Samjna.Technical.PRATIPADIKA,
        Samjna.Karaka.KARTA,
        Samjna.Rudhi("कुरु"),
        Samjna.Rudhi("गुरु"),
        Samjna.Rudhi("शुरु"),
        Samjna.Rudhi("वुरु")
    ),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "कृ, गॄ, शॄ, वृञ् धातुओं से कुः (उ) प्रत्यय होता है।"
)
