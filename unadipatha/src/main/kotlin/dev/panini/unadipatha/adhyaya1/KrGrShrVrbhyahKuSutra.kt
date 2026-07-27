package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.dhatupatha.tudadi.GrDhatu
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra

// 1.5: कृगृशॄवृञ्भ्यः कुः
object KrGrShrVrbhyahKuSutra : UnadiSutra(
    number = "1.5",
    text = "कृगृशॄवृञ्भ्यः कुः",
    hindiExplanation = "कृ, गॄ, शॄ, वृञ् धातुओं से कुः (उ) प्रत्यय होता है।",
    suffix = "कुः",
    roots = setOf(KruDhatu(), GrDhatu(), ShrDhatu(), VrDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        val root = when (context.root) {
            "कृ" -> "कुर्"
            "गॄ" -> "गुर्"
            "शॄ" -> "शुर्"
            "वृ" -> "वुर्"
            else -> context.root
        }
        return UnadiChange(
            state = context.copy(
                root = root,
                suffix = suffix,
                surface = root + "उ",
                itMarkers = setOf(ItMarker.KIT),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix (उ) with क्-it marker."
            ),
            explanation = "$number: Applied suffix $suffix (उ) with क्-it marker."
        )
    }
}
