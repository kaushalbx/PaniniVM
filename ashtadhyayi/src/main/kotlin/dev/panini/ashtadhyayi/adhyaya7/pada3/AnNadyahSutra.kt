package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.ItMarker
import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 7.3.112 prescribes āṭ for the following ṅit affix, not a whole-word ending. */
object AnNadyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.112", text = "आण्नद्याः",
    hindiExplanation = "नदीसंज्ञक अङ्ग के बाद ङित् प्रत्यय को आट् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false, kramaValue = 730112,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA, dependencies = setOf("6.4.1"),
), DerivationSutra {
    private fun target(state: DerivationState): DerivationTerm? {
        if ("6.4.1" !in state.activeAdhikaras) return null
        return state.terms.zipWithNext().firstOrNull { (stem, suffix) ->
            stem.kind == TermKind.PRATIPADIKA &&
                state.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.NADI } &&
                suffix.kind == TermKind.PRATYAYA && suffix.hasEffectiveMarker(ItMarker.NGIT) &&
                suffix.itProcessingPhase == ItProcessingPhase.PROCESSED &&
                suffix.itDesignations.isEmpty() && suffix.deferredItDesignations.isEmpty() &&
                // Let the locative substitution establish its operative ām first.
                (suffix.sourceSupAffix != SupAffix.NGI || suffix.establishedBySutras.any { it in setOf("7.3.116", "7.3.117") }) &&
                state.allEffectiveTerms.none { it.kind == TermKind.AGAMA && it.createdBySutra == number &&
                    it.augmentTargetId == suffix.id }
        }?.second
    }

    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val suffix = requireNotNull(target(context))
        val index = context.terms.indexOfFirst { it.id == suffix.id }
        val augment = DerivationTerm("aat:${suffix.id}", "आट्", TermKind.AGAMA, upadesha = "आट्",
            createdBySutra = number, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = suffix.id, mergeIntoAugmentTarget = false)
        return DerivationChange(context.copy(terms = context.terms.take(index) + augment + context.terms.drop(index),
            stage = DerivationStage.IT_PROCESSED), "7.3.112 introduces raw āṭ for the ṅit affix after a nadī stem.")
    }
}
