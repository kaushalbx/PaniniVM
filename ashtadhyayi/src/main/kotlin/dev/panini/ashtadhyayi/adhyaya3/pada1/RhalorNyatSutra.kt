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
 * Sūtra 3.1.124 ऋहलोर्ण्यत्.
 * Prescribes ṇyat kṛtya affix after ṛ-ending or consonant-ending roots.
 */
object RhalorNyatSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.124", text = "ऋहलोर्ण्यत्",
    hindiExplanation = "ऋकारान्त तथा हलन्त धातुओं से 'ण्यत्' कृत्य प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310124,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "ण्यत्" }

    override fun apply(context: DerivationState): DerivationChange {
        val nyat = DerivationTerm("nyat", "य", TermKind.PRATYAYA, upadesha = "ण्यत्")
        return DerivationChange(
            state = context.addTerm(nyat),
            explanation = "3.1.124 prescribes ण्यत् kṛtya affix after ṛ/halanta dhātu.",
        )
    }
}
