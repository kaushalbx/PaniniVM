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

/** 1.4.59: उपसर्गाः क्रियायोगे. */
object UpasargahKriyayogeSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.59",
    text = "उपसर्गाः क्रियायोगे",
    hindiExplanation = "क्रियायोग में प्रादि शब्दों की उपसर्ग संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140059,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        hasKriyayoga(context) && context.terms.any { term ->
            GanaPatha.isEligibleMember(4, term.surface, term.lexicalUses) &&
                SamjnaAssignment(term.id, Samjna.UPASARGA) !in context.samjnas
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = if (hasKriyayoga(context)) {
            context.terms.filter { GanaPatha.isEligibleMember(4, it.surface, it.lexicalUses) }
                .map { SamjnaAssignment(it.id, Samjna.UPASARGA) }.toSet() - context.samjnas
        } else emptySet()
        return DerivationChange(context.withSamjnas(assignments), "1.4.59 assigns उपसर्ग संज्ञा to प्रादि terms in क्रियायोग.")
    }
}
