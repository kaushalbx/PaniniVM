package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra

// 1.3: कृसृभ्यामुण्
object KrSrbhyamUnSutra : UnadiSutra(
    number = "1.3",
    text = "कृसृभ्यामुण्",
    hindiExplanation = "कृ (कृ विलेखने) और सृ धातु से उण् प्रत्यय होता है।",
    suffix = "उण्",
    roots = setOf(KruDhatu(), SrDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        return UnadiChange(
            state = context.copy(
                suffix = suffix,
                surface = context.root + "उ",
                itMarkers = setOf(ItMarker.NIT),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix (उ) with ण्-it marker."
            ),
            explanation = "$number: Applied suffix $suffix (उ) with ण्-it marker."
        )
    }
}
