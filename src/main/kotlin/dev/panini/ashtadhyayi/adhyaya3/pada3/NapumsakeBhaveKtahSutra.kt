package dev.panini.ashtadhyayi.adhyaya3.pada3

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
 * Sūtra 3.3.114 नपुंसके भावे क्तः.
 * Prescribes kta neuter action affix after roots.
 */
object NapumsakeBhaveKtahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.114", text = "नपुंसके भावे क्तः",
    hindiExplanation = "नपुंसकलिङ्ग भाव (क्रिया सिद्धवस्था) अर्थ में धातु से 'क्त' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330114,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "क्त" }

    override fun apply(context: DerivationState): DerivationChange {
        val kta = DerivationTerm("kta", "त", TermKind.PRATYAYA, upadesha = "क्त")
        return DerivationChange(
            state = context.addTerm(kta),
            explanation = "3.3.114 prescribes क्त neuter action affix.",
        )
    }
}
