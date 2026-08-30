package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.133: ण्वुल्तृचौ.
 * Introduces the agent suffixes 'ण्वुल्' (ṇvul) and 'तृच्' (tṛc) after a verbal root.
 */
object NvultrcauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.133",
    text = "ण्वुल्तृचौ",
    hindiExplanation = "कर्ता अर्थ में धातु से परे ण्वुल् तथा तृच् प्रत्यय होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310133,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isAgentRequested = context.samjnas.any { it.samjna == Samjna.NVUL || it.samjna == Samjna.TRC }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isAgentRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val isNvul = context.samjnas.any { it.samjna == Samjna.NVUL }
        val pratyayaTerm = if (isNvul) {
            DerivationTerm(
                id = "nvul_pratyaya",
                surface = "ण्वुल्",
                kind = TermKind.PRATYAYA,
                upadesha = "ण्वुल्",
                createdBySutra = sutra,
                itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            )
        } else {
            DerivationTerm(
                id = "trc_pratyaya",
                surface = "तृच्",
                kind = TermKind.PRATYAYA,
                upadesha = "तृच्",
                createdBySutra = sutra,
                itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            )
        }

        return DerivationChange(
            state = context.copy(
                terms = context.terms + pratyayaTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.133 introduces agent suffix ${pratyayaTerm.upadesha}."
        )
    }
}
