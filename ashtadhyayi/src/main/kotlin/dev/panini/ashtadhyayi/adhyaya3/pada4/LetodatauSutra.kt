package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.core.PadaType
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.LetAugment
import dev.panini.derivation.LetEOption
import dev.panini.derivation.LetFormation
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana

/** 3.4.94: लेटोऽडाटौ. Selects either the अट् or आट् branch requested by the caller. */
object LetodatauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.94",
    text = "लेटोऽडाटौ",
    hindiExplanation = "लेट् के तिङ् प्रत्यय के आदि में अट् अथवा आट् आगम होता है; यहाँ आट्-पक्ष लिया गया है।",
    type = SutraType.VIBHASHA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340094,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LET ||
            context.stage in setOf(DerivationStage.INITIAL, DerivationStage.FINAL)
        ) return false
        val ending = context.terms.lastOrNull() ?: return false
        if (context.effectiveContext.letFormation == LetFormation.SIP_AORIST &&
            context.allEffectiveTerms.none { it.id == "sip-aorist" }
        ) return false
        if (ending.kind != TermKind.PRATYAYA || sutra in ending.establishedBySutras) return false
        if (ending.matchesUpadesha("तिप्") && ending.varnas == listOf(Vyanjana.TA, Svara.I, Vyanjana.PA)) return false
        if (ending.matchesUpadesha("सिप्") && ending.varnas == listOf(Vyanjana.SA, Svara.I, Vyanjana.PA)) return false
        if (ending.matchesUpadesha("झि") && ending.varnas.firstOrNull() == Vyanjana.JHA) return false
        if (ending.matchesUpadesha("मिप्") && ending.varnas != listOf(Vyanjana.NA, Svara.I)) return false
        val isAtmanepadaEnding = TingAffix.entries.any { it.pada == PadaType.ATMANEPADA && it.upadesha == ending.upadesha }
        if (isAtmanepadaEnding && ending.varnas == ending.upadeshaVarnas) return false
        if (ending.upadesha in setOf(TingAffix.ATAM.upadesha, TingAffix.ATHAM.upadesha) && ending.varnas !in setOf(
                listOf(Svara.AI, Vyanjana.TA, Svara.E),
                listOf(Svara.AI, Vyanjana.THA, Svara.E),
            )) return false
        if (context.effectiveContext.letEOption == LetEOption.AI &&
            isAtmanepadaEnding && ending.varnas.lastOrNull() == Svara.E) return false
        return ending.sourceTingAffix != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val augmentUpadesha = when (context.effectiveContext.letAugment) {
            LetAugment.AT -> "अट्"
            LetAugment.AAT -> "आट्"
        }
        val augment = DerivationTerm(
            id = "let-at-agama",
            surface = augmentUpadesha,
            kind = TermKind.AGAMA,
            upadesha = augmentUpadesha,
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = ending.id,
            mergeIntoAugmentTarget = false,
        )
        val target = ending.copy(establishedBySutras = ending.establishedBySutras + sutra)
        return DerivationChange(
            context.copy(terms = context.terms.dropLast(1) + augment + target),
            "3.4.94 introduces $augmentUpadesha as a raw augment targeted at the beginning of the LET ending.",
        )
    }
}
