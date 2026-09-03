package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.105: गर्गादिभ्यो यञ्. */
object GargadibhyoYanySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.105",
    text = "गर्गादिभ्यो यञ्",
    hindiExplanation = "अपत्य के अर्थ में गर्गादि शब्दों से यञ् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410105,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val meaning = context.context.requestedMeaning ?: return false
        if (meaning != DerivationalMeaning.APATYA && meaning != DerivationalMeaning.GOTRA) return false
        return context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
                (GanaPatha.isEligibleMember(59, term.surface, term.lexicalUses) || term.surface == "गर्ग")
        } && context.allEffectiveTerms.none { it.upadesha == "यञ्" }
    }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.addTerm(
            DerivationTerm(
                "yany-suffix", "यञ्", TermKind.PRATYAYA, upadesha = "यञ्",
                createdBySutra = sutra,
                itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            ),
        ).copy(stage = DerivationStage.PRATYAYA_SELECTED),
        explanation = "4.1.105 introduces यञ् in the अपत्य sense after an eligible गर्गादि term.",
    )
}
