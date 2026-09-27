package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Svara
import dev.panini.shiksha.replaceVarna
import dev.panini.shiksha.toDevanagari
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
        context.allEffectiveTerms.any { it.upadesha == "घञ्" } &&
        "3.3.56" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange {
        val root = context.allEffectiveTerms.firstOrNull { it.kind == TermKind.DHATU }
        val newState = if (root != null && root.varnas.lastOrNull() == Svara.AI) {
            val newSurface = root.varnas.replaceVarna(root.varnas.lastIndex, listOf(Svara.I)).toDevanagari()
            context.substituteTermSurface(root.id, newSurface, Svara.AI, listOf(Svara.I), sutra)
        } else {
            context.activateAdhikara("3.3.56")
        }
        return DerivationChange(
            state = newState,
            explanation = "3.3.56 substitutes ik for ec vowel before ghañ.",
        )
    }
}
