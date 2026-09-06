package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.4.82: गुणो यङि.
 * Prescribes guṇa substitution for the abhyāsa (reduplicated syllable) when Yaṅ affix follows.
 */
object GunoYangiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.82",
    text = "गुणो यङि",
    hindiExplanation = "यङ् प्रत्यय परे रहते अभ्यास (द्वित्व) के इक् वर्ण को गुण आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 4,
    optional = false,
    kramaValue = 740082,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val hasYang = context.samjnas.any { it.samjna == Samjna.YANG } ||
            context.allEffectiveTerms.any { it.upadesha == "यङ्" }
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return hasYang && abhyasa.surface.any { Varnamala.getGuna(it) != null }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val vowelIndex = abhyasa.surface.indexOfLast { Varnamala.getGuna(it) != null }
        require(vowelIndex >= 0) { "7.4.82 requires an ik vowel in the abhyāsa." }
        val source = abhyasa.surface[vowelIndex]
        val replacement = requireNotNull(Varnamala.getGuna(source))
        val surface = abhyasa.surface.replaceRange(vowelIndex, vowelIndex + 1, replacement)
        return DerivationChange(
            state = context.substituteTermSurface(abhyasa.id, surface, source, replacement, sutra),
            explanation = "7.4.82 substitutes guṇa $replacement for $source in the abhyāsa before Yaṅ."
        )
    }
}
