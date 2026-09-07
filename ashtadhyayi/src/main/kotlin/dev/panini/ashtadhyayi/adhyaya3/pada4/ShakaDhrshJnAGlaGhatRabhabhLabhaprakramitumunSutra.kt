package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.65 शकधृषज्ञाग्लाघटरभलभक्रमसहार्हाणां तुमुन्.
 * Prescribes tumun infinitive affix after śak, dhṛṣ, jñā, glā, ghaṭ, rabh, labh, kram, sah, arh.
 */
object ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.65", text = "शकधृषज्ञाग्लाघटरभलभक्रमसहार्हाणां तुमुन्",
    hindiExplanation = "शक्, धृष्, ज्ञा, ग्ला, घट्, रभ्, लभ्, क्रम्, सह्, अर्ह् उपपद रहते 'तुमुन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340065,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVISYAT &&
        context.allEffectiveTerms.none { it.upadesha == "तुमुँन्" }

    override fun apply(context: DerivationState): DerivationChange {
        val tumun = DerivationTerm("tumun", "तुमुँन्", TermKind.PRATYAYA, upadesha = "तुमुँन्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(tumun),
            explanation = "3.4.65 prescribes तुमुन् infinitive affix.",
        )
    }
}
