package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.sutra.*

/** Short-i/u branch of 1.4.6. The separate iyaṅ/uvaṅ eligibility domain
 * requires its own lexical/derivational evidence; it is not guessed here. */
object NgitiHrasvashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.6", text = "ङिति ह्रस्वश्च",
    hindiExplanation = "ङित् प्रत्यय परे स्त्रीलिङ्ग ह्रस्व इ/उकारान्त को विकल्प से नदी संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = true, kramaValue = 140006,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SAMJNA, priority = SutraPriority.APAVADA,
), DerivationSutra {
    private fun target(state: DerivationState): DerivationTerm? = state.terms.zipWithNext().firstOrNull { (stem, suffix) ->
        stem.kind == TermKind.PRATIPADIKA && stem.varnas.lastOrNull() in setOf(Svara.I, Svara.U) &&
            (stem.formedPadaRupa?.linga ?: state.effectiveContext.rupa.linga) == Linga.STRI &&
            state.samjnas.none { it.targetId == stem.id && it.samjna == Samjna.NADI } &&
            suffix.kind == TermKind.PRATYAYA && suffix.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            suffix.hasEffectiveMarker(ItMarker.NGIT)
    }?.first

    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(target(context))
        val samjnas = context.samjnas.filterNot { it.targetId == stem.id && it.samjna == Samjna.GHI }.toSet() +
            SamjnaAssignment(stem.id, Samjna.NADI)
        return DerivationChange(context.copy(samjnas = samjnas),
            "1.4.6 optionally assigns nadī before ṅit; the chosen designation replaces residual Ghi.")
    }
}
