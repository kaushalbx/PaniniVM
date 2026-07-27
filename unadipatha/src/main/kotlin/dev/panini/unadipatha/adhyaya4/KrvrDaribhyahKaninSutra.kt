package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra

// 4.1: कृवृदारिभ्यः कनिन्
object KrvrDaribhyahKaninSutra : UnadiSutra(
    number = "4.1",
    text = "कृवृदारिभ्यः कनिन्",
    hindiExplanation = "कृ, वृ, दॄ धातुओं से कनिन् प्रत्यय होता है।",
    suffix = "कनिन्",
    roots = setOf(KruDhatu(), VrDhatu(), DrDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        val root = when (context.root) {
            "कृ" -> "कर्म्"
            "वृ" -> "वर्म्"
            "दृ" -> "धर्म्"
            else -> context.root
        }
        return UnadiChange(
            state = context.copy(
                root = root,
                suffix = suffix,
                surface = root + "अन्",
                itMarkers = setOf(ItMarker.KIT),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix (अन्) with क्-it marker and mut-āgama."
            ),
            explanation = "$number: Applied suffix $suffix (अन्) with क्-it marker and mut-āgama."
        )
    }
}
