package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.dhatupatha.tudadi.GrDhatu
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.TermKind

// 1.5: कृगृशॄवृञ्भ्यः कुः
object KrGrShrVrbhyahKuSutra : UnadiSutra(
    number = "1.5",
    text = "कृगृशॄवृञ्भ्यः कुः",
    hindiExplanation = "कृ, गॄ, शॄ, वृञ् धातुओं से कुः (उ) प्रत्यय होता है।",
    suffix = "कुः",
    roots = setOf(KruDhatu(), GrDhatu(), ShrDhatu(), VrDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        return UnadiChange(
            state = context.copy(
                suffix = suffix,
                surface = context.root + "उ",
                itMarkers = setOf(ItMarker.KIT),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix (उ) with क्-it marker."
            ),
            explanation = "$number: Applied suffix $suffix (उ) with क्-it marker."
        )
    }

    override fun matches(context: DerivationState): Boolean {
        if (context.substitutions.any { it.sutra == this.sutra }) return false
        val suffixTerm = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return suffixTerm.id == "unadi_$number"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val oldRoot = context.terms[rootIndex]
        val newSurface = when (oldRoot.surface) {
            "कृ" -> "कुर्"
            "गॄ" -> "गुर्"
            "शॄ" -> "शुर्"
            "वृ" -> "वुर्"
            else -> oldRoot.surface
        }
        return DerivationChange(
            context.replaceTerm(oldRoot.id, oldRoot.copy(surface = newSurface)),
            "$number: Applied root vowel replacement to ${oldRoot.surface} -> $newSurface."
        )
    }
}
