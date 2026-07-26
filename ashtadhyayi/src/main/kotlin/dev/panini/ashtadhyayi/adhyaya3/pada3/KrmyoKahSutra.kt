package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationChange
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
 * Sūtra 3.3.14 कॄम्यः कः.
 * Prescribes ka affix after kṛ and mī roots.
 */
object KrmyoKahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.14", text = "कॄम्यः कः",
    hindiExplanation = "कृ तथा मी धातुओं से 'क' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330014,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.none { it.upadesha == "क" }

    override fun apply(context: DerivationState): DerivationChange {
        val ka = DerivationTerm("ka", "अ", TermKind.PRATYAYA, upadesha = "क")
        return DerivationChange(
            state = context.addTerm(ka),
            explanation = "3.3.14 prescribes क affix after kṛ and mī.",
        )
    }
}
