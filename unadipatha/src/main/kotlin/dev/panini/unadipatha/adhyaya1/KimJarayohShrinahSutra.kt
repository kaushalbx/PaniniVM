package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSutra

// 1.4: किञ्जरयोः श्रिणः
object KimJarayohShrinahSutra : UnadiSutra(
    number = "1.4",
    text = "किञ्जरयोः श्रिणः",
    roots = setOf(ShrDhatu()),
    pratyaya = "ञुण्",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NYIT, ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "शॄ" to Samjna.Rudhi("किंशारु"),
        "जॄ" to Samjna.Rudhi("जरायु"),
    ),
    rootReferences = setOf("जॄ"),
    hindiExplanation = "‘किम्’ उपपद के साथ शॄ धातु तथा जॄ धातु के बाद ञुण् प्रत्यय होता है। ञ् और ण् इत्संज्ञक होकर लुप्त होते हैं तथा उ शेष रहता है। " +
        "शॄ से किंशारु और जॄ से जरायु शब्द सिद्ध होते हैं; जरायु का अर्थ गर्भाशय को ढकने वाली झिल्ली है।",
)
