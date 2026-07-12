package dev.sanskrit.ashtadhyayi.adhyaya1.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
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
 * 1.3.1: bhūvādayo dhātavaḥ.
 * Elements starting with 'bhū' are called 'dhātu' (verbal roots).
 */
object BhuvadayoDhatavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.1",
    text = "भूवादयो धातवः",
    hindiExplanation = "भू आदि क्रियावाचक शब्दों की धातु संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130001,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.DHATU && 
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.DHATU }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.kind == TermKind.DHATU }
            .map { SamjnaAssignment(it.id, Samjna.DHATU) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.3.1 assigns धातु संज्ञा to verbal roots."
        )
    }
}
