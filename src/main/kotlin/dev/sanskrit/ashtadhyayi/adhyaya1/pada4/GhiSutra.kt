package dev.sanskrit.ashtadhyayi.adhyaya1.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
            term.kind == TermKind.PRATIPADIKA && 
            term.surface != "सखि" &&
            (term.surface.endsWith('इ') || term.surface.endsWith('ि') || 
             term.surface.endsWith('उ') || term.surface.endsWith('ु')) &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.GHI }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.kind == TermKind.PRATIPADIKA && it.surface != "सखि" &&
                      (it.surface.endsWith('इ') || it.surface.endsWith('ि') || 
                       it.surface.endsWith('उ') || it.surface.endsWith('ु')) }
            .map { SamjnaAssignment(it.id, Samjna.GHI) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.4.7 assigns घि संज्ञा to short i/u stems."
        )
    }
}
