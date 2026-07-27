package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.dhatupatha.adadi.VaaDhatu
import dev.panini.dhatupatha.bhvadi.PaaDhatu
import dev.panini.dhatupatha.bhvadi.JiDhatu
import dev.panini.dhatupatha.svadi.MiDhatu
import dev.panini.dhatupatha.bhvadi.SvadDhatu
import dev.panini.dhatupatha.svadi.SadhDhatu
import dev.panini.dhatupatha.svadi.AshDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra

// 1.1: कृवापाजिमिस्वदिसाध्यशूभ्य उण्
object KrvaPajiMiSvadisadhyashubhyoUnSutra : UnadiSutra(
    number = "1.1",
    text = "कृवापाजिमिस्वदिसाध्यशूभ्य उण्",
    hindiExplanation = "कृ, वा, पा, जि, मि, स्वद्, साध्, अश् धातुओं से उण् प्रत्यय होता है।",
    suffix = "उण्",
    roots = setOf(
        KruDhatu(), VaaDhatu(), PaaDhatu(), JiDhatu(),
        MiDhatu(), SvadDhatu(), SadhDhatu(), AshDhatu()
    )
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
