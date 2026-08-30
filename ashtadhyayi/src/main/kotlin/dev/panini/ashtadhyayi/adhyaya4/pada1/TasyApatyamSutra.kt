package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.92: तस्यापत्यम्. Introduces default Taddhita affix अण् for offspring (Apatya). */
object TasyApatyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.92",
    text = "तस्यापत्यम्",
    hindiExplanation = "षष्ठ्यन्त समर्थ प्रातिपदिक से 'उसका अपत्य (सन्तान)' इस अर्थ में तद्धित अण् प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410092,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL) return false
        val meaning = context.context.requestedMeaning ?: return false
        if (meaning != DerivationalMeaning.APATYA && meaning != DerivationalMeaning.ANANTARA_APATYA) return false
        if (AtaInSutra.matches(context) || GargadibhyoYanySutra.matches(context)) return false
        return context.terms.none { it.kind == TermKind.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val anPratyaya = DerivationTerm(
            id = "an-apatya",
            surface = "अण्",
            kind = TermKind.PRATYAYA,
            upadesha = "अण्",
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + anPratyaya,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "4.1.92 introduces general Taddhita affix अण् for apatya.",
        )
    }
}
