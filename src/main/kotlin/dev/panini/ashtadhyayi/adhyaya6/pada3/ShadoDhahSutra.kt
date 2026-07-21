package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.3.49: षडो ढः */
object ShadoDhahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.49",
    text = "षडो ढः",
    hindiExplanation = "षष् के स्थान पर ढ तथा दशन् के उत्तरपद रहते षोडश निष्पन्न होता है।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630049,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val purvapada = context.terms.firstOrNull() ?: return false
        val uttarapada = context.terms.getOrNull(1) ?: return false

        return purvapada.surface == "षष्" && (uttarapada.surface == "दशन्" || uttarapada.surface == "दश")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val purvapada = context.terms.first()
        val updatedTerm = purvapada.copy(surface = "षोडश")
        val newTerms = listOf(updatedTerm)
        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "$text: षष् + दशन् -> षोडश"
        )
    }
}
