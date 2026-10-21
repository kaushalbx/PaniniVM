package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.divadi.JanDhatu
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSutra

// 1.3: दॄसनिजनिचरिचटिभ्यो ञुण्
object DrSanJanCarCatibhyoNyunSutra : UnadiSutra(
    number = "1.3",
    text = "दॄसनिजनिचरिचटिभ्यो ञुण्",
    roots = setOf(DrDhatu(), JanDhatu()),
    rootReferences = setOf("सन्", "चर्", "चट्"),
    pratyaya = "ञुण्",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NYIT, ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "दॄ" to Samjna.Rudhi("दारु"),
        "सन्" to Samjna.Rudhi("सानु"),
        "जन्" to Samjna.Rudhi("जानु"),
        "चर्" to Samjna.Rudhi("चारु"),
        "चट्" to Samjna.Rudhi("चाटु"),
    ),
    hindiExplanation = "दॄ, सन्, जन्, चर् और चट् धातुओं के बाद ञुण् प्रत्यय होता है। ञ् और ण् इत्संज्ञक होकर लुप्त होते हैं तथा उ शेष रहता है। " +
        "इससे दारु, सानु, जानु, चारु और चाटु शब्द सिद्ध होते हैं।",
)
