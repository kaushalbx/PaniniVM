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

/** 1.4.61: ऊर्यादिच्विडाचश्च; गत्यधिकार में ऊर्यादि को गति संज्ञा। */
object UryadiCvidacashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.61",
    text = "ऊर्यादिच्विडाचश्च",
    hindiExplanation = "क्रियायोग में ऊर्यादि शब्दों की गति संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140061,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        hasKriyayoga(context) &&
            context.terms.any { term -> GanaPatha.isEligibleMember(5, term.surface, term.lexicalUses) && SamjnaAssignment(
                term.id,
                Samjna.GATI
            ) !in context.samjnas }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = if (hasKriyayoga(context)) {
            context.terms.filter { GanaPatha.isEligibleMember(5, it.surface, it.lexicalUses) }
                .map { SamjnaAssignment(it.id, Samjna.GATI) }.toSet() - context.samjnas
        } else emptySet()
        return DerivationChange(
            context.withSamjnas(assignments),
            "1.4.61 assigns गति संज्ञा to ऊर्यादि terms in क्रियायोग."
        )
    }
}
