package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.integrateShitWholeAdesha
import dev.panini.derivation.processedShitWholeAdeshas
import dev.panini.sutra.*

/**
 * 1.1.55: anekāl śit sarvasya.
 * Interpretative rule: A substitute containing multiple phonemes (anekāl) or marked with 'ś' (śit) replaces the entire term.
 */
object AnekalSitSarvasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.55",
    text = "अनेकाल् शित् सर्वस्य",
    hindiExplanation = "अनेकाल् शित् च आदेशः सर्वस्य स्थानिनः स्यात्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110055,
    role = SutraRole.Paribhasha( targetScope = ParibhashaScope.FULL_TERM_SUBSTITUTION),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.processedShitWholeAdeshas().isNotEmpty()
    override fun apply(context: DerivationState): DerivationChange {
        val carrier = context.processedShitWholeAdeshas().first()
        return DerivationChange(context.integrateShitWholeAdesha(carrier.id, sutra),
            "1.1.55 makes the processed śit substitute ${carrier.upadesha} replace the complete sthānin.")
    }
}
