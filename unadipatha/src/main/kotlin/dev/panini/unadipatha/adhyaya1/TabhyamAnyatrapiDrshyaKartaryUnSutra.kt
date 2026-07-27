package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra

// 1.2: ताभ्यामन्यत्रापि दृश्यकर्तर्युण्
object TabhyamAnyatrapiDrshyaKartaryUnSutra : UnadiSutra(
    number = "1.2",
    text = "ताभ्यामन्यत्रापि दृश्यकर्तर्युण्",
    hindiExplanation = "अन्य धातुओं से भी कर्ता अर्थ में उण् प्रत्यय देखा जाता है।",
    suffix = "उण्",
    roots = setOf(DrDhatu(), ShrDhatu())
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
