package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 3.2.102 निष्ठा — introduces the requested क्त or क्तवतु suffix after a verbal root. */
object NisthaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.102",
    text = "निष्ठा",
    hindiExplanation = "भूतकाल के अर्थ में धातु से निष्ठा-संज्ञक क्त अथवा क्तवतु प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320102,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val requested = context.samjnas.any { it.samjna == Samjna.KTA || it.samjna == Samjna.KTAVATU }
        val alreadyPresent = context.terms.any { it.upadesha == "क्त" || it.upadesha == "क्तवतुँ" }
        return requested && !alreadyPresent
    }

    override fun apply(context: DerivationState): DerivationChange {
        val upadesha = if (context.samjnas.any { it.samjna == Samjna.KTAVATU }) "क्तवतुँ" else "क्त"
        val term = DerivationTerm(
            id = if (upadesha == "क्तवतुँ") "ktavatu_pratyaya" else "kta_pratyaya",
            surface = upadesha,
            kind = TermKind.PRATYAYA,
            upadesha = upadesha,
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + term, stage = DerivationStage.PRATYAYA_SELECTED),
            explanation = "3.2.102 introduces the niṣṭhā suffix $upadesha.",
        )
    }
}
