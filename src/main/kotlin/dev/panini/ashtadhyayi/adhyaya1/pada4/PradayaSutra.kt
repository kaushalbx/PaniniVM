package dev.panini.ashtadhyayi.adhyaya1.pada4

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

/** 1.4.58: प्रादयः; असत्त्वे का अधिकार 1.4.57 से आता है। */
object PradayaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.58",
    text = "प्रादयः",
    hindiExplanation = "असत्त्व के अर्थ में प्रादि शब्दों की निपात संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140058,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        isAsattva(context) &&
            context.terms.any { term -> GanaPatha.isEligibleMember(4, term.surface, term.lexicalUses) && SamjnaAssignment(
                term.id,
                Samjna.NIPATA
            ) !in context.samjnas }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = if (isAsattva(context)) {
            context.terms.filter { GanaPatha.isEligibleMember(4, it.surface, it.lexicalUses) }
                .map { SamjnaAssignment(it.id, Samjna.NIPATA) }.toSet() - context.samjnas
        } else emptySet()
        return DerivationChange(
            context.withSamjnas(assignments),
            "1.4.58 assigns निपात संज्ञा to प्रादि terms in असत्त्व."
        )
    }
}
