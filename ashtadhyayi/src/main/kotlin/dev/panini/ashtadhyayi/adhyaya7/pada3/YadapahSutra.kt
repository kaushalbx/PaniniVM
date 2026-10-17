package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.*
import dev.panini.sutra.*

/** 7.3.113 introduces yāṭ; placement, it-lopa and vowel sandhi are independent rules. */
object YadapahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.113", text = "याडापः",
    hindiExplanation = "आप् के बाद ङित् विभक्ति को याट् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false, kramaValue = 730113,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA, dependencies = setOf("6.4.1"),
), DerivationSutra {
    private fun target(state: DerivationState): DerivationTerm? {
        return state.apNgitJunctions().firstOrNull()?.second
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val suffix = requireNotNull(target(context))
        val index = context.terms.indexOfFirst { it.id == suffix.id }
        val augment = DerivationTerm("yaat:${suffix.id}", "याट्", TermKind.AGAMA,
            upadesha = "याट्", createdBySutra = number,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = suffix.id, mergeIntoAugmentTarget = true)
        return DerivationChange(context.copy(
            terms = context.terms.take(index) + augment + context.terms.drop(index),
            stage = DerivationStage.IT_PROCESSED),
            "7.3.113 introduces raw yāṭ for the ṅit case ending after an established āp formation.")
    }
}
