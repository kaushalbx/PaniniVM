package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationalMeaning
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
 * Sūtra 3.1.133 ण्वुल्तृचौ.
 * Prescribes ṇvul and tṛc agent affixes after roots.
 */
object NvultrchauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.133", text = "ण्वुल्तृचौ",
    hindiExplanation = "धातु से कर्ता अर्थ में सामान्यतः 'ण्वुल्' तथा 'तृच्' प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310133,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.KARTR_VEDANA &&
        context.allEffectiveTerms.none { it.upadesha in setOf("तृच्", "ण्वुल्") }

    override fun apply(context: DerivationState): DerivationChange {
        val trc = DerivationTerm("trc", "तृ", TermKind.PRATYAYA, upadesha = "तृच्")
        return DerivationChange(
            state = context.addTerm(trc),
            explanation = "3.1.133 prescribes तृच् / ण्वुल् agent affix.",
        )
    }
}
