package dev.sanskrit.ashtadhyayi.adhyaya1.pada4

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

/** 1.4.60: गतिश्च. */
object GatishCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.60",
    text = "गतिश्च",
    hindiExplanation = "क्रियायोग में प्रादि शब्दों की गति संज्ञा भी होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140060,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        hasKriyayoga(context) && context.terms.any { term ->
            GanaPatha.isEligibleMember(4, term.surface, term.lexicalUses) &&
                SamjnaAssignment(term.id, Samjna.GATI) !in context.samjnas
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = if (hasKriyayoga(context)) {
            context.terms.filter { GanaPatha.isEligibleMember(4, it.surface, it.lexicalUses) }
                .map { SamjnaAssignment(it.id, Samjna.GATI) }.toSet() - context.samjnas
        } else emptySet()
        return DerivationChange(context.withSamjnas(assignments), "1.4.60 assigns गति संज्ञा to प्रादि terms in क्रियायोग.")
    }
}
