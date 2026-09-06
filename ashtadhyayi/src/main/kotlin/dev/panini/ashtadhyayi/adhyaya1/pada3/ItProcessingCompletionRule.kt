package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItProcessingPhase
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Administrative lifecycle boundary after 1.3.2–1.3.8.
 *
 * A raw upadeśa on which none of the designation rules found an इत् segment
 * is complete without deletion. This is deliberately separate from 1.3.9.
 */
object ItProcessingCompletionRule : Sutra<DerivationState, DerivationChange>(
    number = "IT-COMPLETE",
    text = "इत्-प्रक्रिया-समाप्तिः",
    hindiExplanation = "जिस उपदेश में कोई इत् वर्ण निर्दिष्ट नहीं हुआ, उसकी इत्-प्रक्रिया बिना लोप पूर्ण होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130008,
    role = SutraRole.Vidhi,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    private val selectedEndingUpadeshas = SupAffix.entries.mapTo(mutableSetOf()) { it.upadesha } +
        TingAffix.entries.map { it.upadesha }

    override fun matches(context: DerivationState): Boolean {
        val hasDeletionPending = context.terms.any {
            it.itDesignations.isNotEmpty() ||
                (it.deferredItDesignations.isNotEmpty() && it.itProcessingPhase != ItProcessingPhase.DEFERRED_SUBSTITUTION)
        }
        if (hasDeletionPending) return false
        return (context.stage == DerivationStage.PRATYAYA_SELECTED &&
            context.terms.any { term ->
                (term.id.startsWith("ting-") || selectedEndingUpadeshas.any(term::matchesUpadesha)) && !term.itProcessingPending
            }) ||
            context.terms.any {
                it.itProcessingPhase == ItProcessingPhase.RAW_UPADESHA &&
                    it.itMarkers.isEmpty() &&
                    it.itDesignations.isEmpty() &&
                    it.deferredItDesignations.isEmpty()
            }
    }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.copy(
            terms = context.terms.map { term ->
                if (term.itProcessingPhase == ItProcessingPhase.RAW_UPADESHA &&
                    term.itMarkers.isEmpty() &&
                    term.itDesignations.isEmpty() &&
                    term.deferredItDesignations.isEmpty()
                ) term.copy(itProcessingPhase = ItProcessingPhase.PROCESSED) else term
            },
            stage = if (context.stage == DerivationStage.PRATYAYA_SELECTED) DerivationStage.IT_PROCESSED else context.stage,
        ),
        explanation = "Completed इत् processing for raw upadeśas on which 1.3.2–1.3.8 designated no segment.",
    )
}
