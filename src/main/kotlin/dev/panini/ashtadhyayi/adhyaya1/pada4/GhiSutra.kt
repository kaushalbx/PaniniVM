package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Samjna
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.4.7: śeṣo ghy-asākhi.
 * Short 'i' and 'u' vowels that remain (after excluding 'nadi' stems)
 * are called 'ghi', except for the word 'sakhi'.
 */
object GhiSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.7",
    text = "शेषो घ्यसखि",
    hindiExplanation = "नदी संज्ञा से भिन्न ह्रस्व इकारान्त और उकारान्त शब्दों की घि संज्ञा होती है, 'सखि' शब्द को छोड़कर।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140007,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            isEligibleGhiTerm(context, term)
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { isEligibleGhiTerm(context, it) }
            .map { SamjnaAssignment(it.id, Samjna.GHI) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.4.7 assigns घि संज्ञा to short i/u stems."
        )
    }

    /**
     * "śeṣa" restricts घि to the short i/u stems left after the नदी
     * designation has been taken into account; सखि is explicitly excepted.
     */
    private fun isEligibleGhiTerm(context: DerivationState, term: DerivationTerm): Boolean =
        term.kind == TermKind.PRATIPADIKA &&
            term.surface != "सखि" &&
            term.surface.lastOrNull() in setOf('इ', 'ि', 'उ', 'ु') &&
            context.samjnas.none { it.targetId == term.id && it.samjna in setOf(Samjna.NADI, Samjna.GHI) }
}
