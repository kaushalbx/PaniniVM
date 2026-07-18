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

/** 1.4.74: साक्षात्प्रभृतीनि च; गत्यधिकार में साक्षात्प्रभृति को गति संज्ञा। */
object SakshatPrabhrtiniCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.74",
    text = "साक्षात्प्रभृतीनि च",
    hindiExplanation = "क्रियायोग में साक्षात्प्रभृति शब्दों की गति संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140074,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        hasKriyayoga(context) &&
            context.terms.any { term -> GanaPatha.isEligibleMember(6, term.surface, term.lexicalUses) && SamjnaAssignment(
                term.id,
                Samjna.GATI
            ) !in context.samjnas }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = if (hasKriyayoga(context)) {
            context.terms.filter { GanaPatha.isEligibleMember(6, it.surface, it.lexicalUses) }
                .map { SamjnaAssignment(it.id, Samjna.GATI) }.toSet() - context.samjnas
        } else emptySet()
        return DerivationChange(
            context.withSamjnas(assignments),
            "1.4.74 assigns गति संज्ञा to साक्षात्प्रभृति terms in क्रियायोग."
        )
    }
}
