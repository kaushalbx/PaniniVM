package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.56 एच इग्घ्रस्यादेशे.
 * Replaces ec vowel with short ik before ghañ.
 */
object EchaIgGhanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.56", text = "एच इग्घ्रस्यादेशे",
    hindiExplanation = "घञ् प्रत्यय परे रहते एजन्त (ए, ओ, ऐ, औ) के स्थान पर ह्रस्व इक् (इ, उ, ऋ) आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330056,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha == "घञ्" }

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context,
            explanation = "3.3.56 substitutes ik for ec vowel before ghañ.",
        )
}
