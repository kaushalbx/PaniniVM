package dev.panini.ashtadhyayi.adhyaya3.pada3

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
 * 3.3.18: भावे / 3.3.121: हलश्च.
 * Introduces the suffix 'घञ्' (ghañ) after a verbal root for action / abstract nouns.
 */
object BhaveGhanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.18",
    text = "भावे",
    hindiExplanation = "भाव तथा अकर्ता कारक अर्थ में धातु से परे घञ् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330018,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isGhanRequested = context.samjnas.any { it.samjna == Samjna.GHAN }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isGhanRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ghanTerm = DerivationTerm(
            id = "ghan_pratyaya",
            surface = "घञ्",
            kind = TermKind.PRATYAYA,
            upadesha = "घञ्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + ghanTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.3.18 introduces suffix घञ् (अ)."
        )
    }
}
