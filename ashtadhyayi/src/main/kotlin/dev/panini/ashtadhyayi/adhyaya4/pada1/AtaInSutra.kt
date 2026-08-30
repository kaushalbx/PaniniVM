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

/** 4.1.95: अत इञ्. Introduces इञ् affix after short-a ending nominal stem for Apatya. */
object AtaInSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.95",
    text = "अत इञ्",
    hindiExplanation = "अदन्त (ह्रस्व अ-कारान्त) प्रातिपदिक से अपत्य अर्थ में इञ् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410095,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("4.1.92"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL) return false
        val meaning = context.context.requestedMeaning ?: return false
        if (meaning != DerivationalMeaning.APATYA && meaning != DerivationalMeaning.ANANTARA_APATYA) return false
        val stem = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        if (!stem.surface.endsWith('अ') && !stem.surface.endsWith('थ')) return false
        if (GargadibhyoYanySutra.matches(context)) return false
        return context.terms.none { it.kind == TermKind.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val inPratyaya = DerivationTerm(
            id = "in-apatya",
            surface = "इञ्",
            kind = TermKind.PRATYAYA,
            upadesha = "इञ्",
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + inPratyaya,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "4.1.95 introduces इञ् for short-a ending stem in apatya.",
        )
    }
}
