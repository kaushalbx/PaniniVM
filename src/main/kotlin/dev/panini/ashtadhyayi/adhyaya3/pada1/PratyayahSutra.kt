package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.1: pratyayaḥ.
 * Adhikāra rule designating all suffixes introduced from here to the end of chapter 5 as 'pratyaya'.
 * In our engine, we assign this saṃjñā to any term whose kind is TermKind.PRATYAYA.
 */
object PratyayahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.1",
    text = "प्रत्ययः",
    hindiExplanation = "यहाँ से पञ्चम अध्याय के अन्त तक जिसका विधान किया जाए, उसकी प्रत्यय संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310001,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.PRATYAYA &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PRATYAYA }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.kind == TermKind.PRATYAYA }
            .map { SamjnaAssignment(it.id, Samjna.PRATYAYA) }
            .toSet()
        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "3.1.1 assigns प्रत्यय संज्ञा to suffixes."
        )
    }
}
