package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 1.1.37: स्वरादिनिपातमव्ययम्. */
object SvaradiNipatamAvyayamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.37",
    text = "स्वरादिनिपातमव्ययम्",
    hindiExplanation = "स्वरादि गण के शब्दों की अव्यय संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110037,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            GanaPatha.isEligibleMember(2, term.surface, term.lexicalUses) &&
                SamjnaAssignment(term.id, Samjna.AVYAYA) !in context.samjnas
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { GanaPatha.isEligibleMember(2, it.surface, it.lexicalUses) }
            .map { SamjnaAssignment(it.id, Samjna.AVYAYA) }
            .toSet() - context.samjnas
        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.37 assigns अव्यय संज्ञा to स्वरादि terms.",
        )
    }
}
