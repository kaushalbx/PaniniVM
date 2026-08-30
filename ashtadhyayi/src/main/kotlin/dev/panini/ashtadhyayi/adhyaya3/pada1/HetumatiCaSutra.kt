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
 * 3.1.26: हेतुमति च.
 * Introduces the causative suffix 'णिच्' (ṇic) after a verbal root when prompting another to act.
 */
object HetumatiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.26",
    text = "हेतुमति च",
    hindiExplanation = "प्रयोजक व्यापार (प्रेरणा) अर्थ में धातु से परे णिच् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310026,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isNicRequested = context.samjnas.any { it.samjna == Samjna.NIC } || context.context.requestedMeaning == dev.panini.derivation.DerivationalMeaning.BHAVA
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isNicRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nicTerm = DerivationTerm(
            id = "nic_pratyaya",
            surface = "णिच्",
            kind = TermKind.PRATYAYA,
            upadesha = "णिच्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + nicTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.26 introduces causative suffix णिच् (इ)."
        )
    }
}
