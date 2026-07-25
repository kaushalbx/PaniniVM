package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.26 हेतुमति च.
 * Prescribes ṇic causative affix in prompter agent sense.
 */
object HetumatiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.26", text = "हेतुमति च",
    hindiExplanation = "प्रयोजक व्यापार (प्रेरणा) अर्थ में धातु से 'णिच्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310026,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "णिच्" }

    override fun apply(context: DerivationState): DerivationChange {
        val nic = DerivationTerm("nic", "इ", TermKind.PRATYAYA, upadesha = "णिच्")
        return DerivationChange(
            state = context.addTerm(nic),
            explanation = "3.1.26 prescribes णिच् causative affix.",
        )
    }
}
