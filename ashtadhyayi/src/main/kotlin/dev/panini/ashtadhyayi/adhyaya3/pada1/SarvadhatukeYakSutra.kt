package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Prayoga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.ItProcessingPhase
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.67 सार्वधातुके यक्.
 * Prescribes yak vikaraṇa pratyaya in passive (karmaṇi) and bhāve before Sārvadhātuka affixes.
 */
object SarvadhatukeYakSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.67", text = "सार्वधातुके यक्",
    hindiExplanation = "भाव और कर्म में सार्वधातुक प्रत्यय परे रहते धातु से 'यक्' विकरण होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310067,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.prayoga in setOf(Prayoga.KARMANI, Prayoga.BHAVE) &&
        context.allEffectiveTerms.none { it.upadesha == "यक्" }

    override fun apply(context: DerivationState): DerivationChange {
        val yak = DerivationTerm(
            "yak", "यक्", TermKind.PRATYAYA, upadesha = "यक्",
            createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.addTerm(yak),
            explanation = "3.1.67 prescribes यक् in karmaṇi/bhāve Sārvadhātuka.",
        )
    }
}
