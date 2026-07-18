package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.ganapatha.GanaPatha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
