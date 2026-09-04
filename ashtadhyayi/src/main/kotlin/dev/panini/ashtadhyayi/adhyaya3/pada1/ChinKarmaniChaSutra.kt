package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.60 चिण् कर्मणि.
 * Prescribes ciṇ vikaraṇa in Luṅ passive (karmaṇi).
 */
object ChinKarmaniChaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.60", text = "चिण् कर्मणि",
    hindiExplanation = "कर्म तथा भाव में लुङ् लकार रहने पर धातु से 'चिण्' (इ) विकरण होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310060,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LUNG &&
        context.effectiveContext.rupa.prayoga in setOf(Prayoga.KARMANI, Prayoga.BHAVE) &&
        context.allEffectiveTerms.none { it.upadesha == "चिण्" }

    override fun apply(context: DerivationState): DerivationChange {
        val chin = DerivationTerm("chin", "चिण्", TermKind.PRATYAYA, upadesha = "चिण्", createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(chin),
            explanation = "3.1.60 prescribes चिण् vikaraṇa in Luṅ karmaṇi.",
        )
    }
}
