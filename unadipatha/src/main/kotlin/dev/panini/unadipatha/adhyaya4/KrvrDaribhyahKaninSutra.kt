package dev.panini.unadipatha.adhyaya4

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind

// 4.1: कृवृदारिभ्यः कनिन्
object KrvrDaribhyahKaninSutra : UnadiSutra(
    number = "4.1",
    text = "कृवृदारिभ्यः कनिन्",
    hindiExplanation = "कृ, वृ, दॄ धातुओं से कनिन् प्रत्यय होता है।",
    suffix = "कनिन्",
    roots = setOf(KruDhatu(), VrDhatu(), DrDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        return UnadiChange(
            state = context.copy(
                suffix = suffix,
                surface = context.root + "अन्",
                itMarkers = setOf(ItMarker.KIT),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix (अन्) with क्-it marker."
            ),
            explanation = "$number: Applied suffix $suffix (अन्) with क्-it marker."
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
        val newRootSurface = when (oldRoot.surface) {
            "कृ" -> "कर्"
            "वृ" -> "वर्"
            "दृ" -> "धर्"
            else -> oldRoot.surface
        }
        val newRoot = oldRoot.copy(surface = newRootSurface)
        val mutAgama = DerivationTerm("mut-agama", "म्", TermKind.AGAMA, upadesha = "मुट्")
        
        val newTerms = context.terms.take(rootIndex) + newRoot + mutAgama + context.terms.drop(rootIndex + 1)
        return DerivationChange(
            context.copy(terms = newTerms),
            "$number: Applied root guṇa to ${oldRoot.surface} -> $newRootSurface and added mut-āgama (म्)."
        )
    }
}
