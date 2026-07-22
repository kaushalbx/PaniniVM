package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.SamjnaAssignment
import dev.panini.ganapatha.GanaPatha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

fun isAsattva(state: DerivationState): Boolean =
    HasDerivationalEnvironment(DerivationalEnvironment.ASATTVA).matches(state)

 fun hasKriyayoga(state: DerivationState): Boolean =
    HasDerivationalEnvironment(DerivationalEnvironment.KRIYAYOGA).matches(state)

/** 1.4.57: चादयोऽसत्त्वे. */
object ChadayoAsattveSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.57",
    text = "चादयोऽसत्त्वे",
    hindiExplanation = "असत्त्व के अर्थ में चादि शब्दों की निपात संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140057,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        isAsattva(context) &&
            context.terms.any { term -> GanaPatha.isEligibleMember(3, term.surface, term.lexicalUses) && SamjnaAssignment(term.id, Samjna.NIPATA) !in context.samjnas }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = if (isAsattva(context)) {
            context.terms.filter { GanaPatha.isEligibleMember(3, it.surface, it.lexicalUses) }
                .map { SamjnaAssignment(it.id, Samjna.NIPATA) }.toSet() - context.samjnas
        } else emptySet()
        return DerivationChange(context.withSamjnas(assignments), "1.4.57 assigns निपात संज्ञा to चादि terms in असत्त्व.")
    }
}
