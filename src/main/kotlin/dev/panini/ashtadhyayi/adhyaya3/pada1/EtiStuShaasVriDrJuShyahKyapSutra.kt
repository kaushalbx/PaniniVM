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
 * Sūtra 3.1.109 एतिस्तुशास्वदृजुषःक्यप्.
 * Prescribes kyap kṛtya affix after i, stu, śās, vṛ, dṛ, juṣ roots.
 */
object EtiStuShaasVriDrJuShyahKyapSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.109", text = "एतिस्तुशास्वदृजुषःक्यप्",
    hindiExplanation = "इ, स्तु, शास्, वृ, दृ तथा जुष् धातुओं से 'क्यप्' कृत्य प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310109,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "क्यप्" }

    override fun apply(context: DerivationState): DerivationChange {
        val kyap = DerivationTerm("kyap", "य", TermKind.PRATYAYA, upadesha = "क्यप्")
        return DerivationChange(
            state = context.addTerm(kyap),
            explanation = "3.1.109 prescribes क्यप् kṛtya affix after i, stu, śās, etc.",
        )
    }
}
