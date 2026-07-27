package dev.panini.unadipatha.adhyaya2

import dev.panini.dhatupatha.bhvadi.ShruDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.adadi.PraDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.DerivationStage

// 2.8: श्रुवृदृप्रावृभ्यः सिः
object ShruVrDrPraVrbhyahSihSutra : UnadiSutra(
    number = "2.8",
    text = "श्रुवृदृप्रावृभ्यः सिः",
    hindiExplanation = "श्रु, वृ, दृ, प्रा, वृ धातुओं से सिः प्रत्यय होता है।",
    suffix = "सिः",
    roots = setOf(ShruDhatu(), VrDhatu(), DrDhatu(), PraDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        return UnadiChange(
            state = context.copy(
                suffix = suffix,
                surface = context.root + "सि",
                itMarkers = emptySet(),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix (सि)."
            ),
            explanation = "$number: Applied suffix $suffix (सि)."
        )
    }

    override fun matches(context: DerivationState): Boolean {
        if (context.substitutions.any { it.sutra == this.sutra }) return false
        val suffixTerm = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return suffixTerm.id == "unadi_$number" && suffixTerm.surface == "सि"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val suffixTerm = context.terms.last { it.kind == TermKind.PRATYAYA }
        val tutAgama = DerivationTerm("tut-agama", "त्", TermKind.AGAMA, upadesha = "तुक्")
        
        val newTerms = context.terms.filter { it.id != suffixTerm.id }
        val insertedTerms = newTerms.take(rootIndex + 1) + tutAgama + newTerms.drop(rootIndex + 1)
        
        return DerivationChange(
            state = context.copy(
                terms = insertedTerms,
                stage = DerivationStage.PADA_FORMED
            ),
            explanation = "$number: Elided suffix 'सि' and added tut-āgama (त्)."
        )
    }
}
