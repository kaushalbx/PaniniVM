package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.*

internal fun pastAugmentEnvironment(state: DerivationState): Boolean =
    state.stage !in setOf(DerivationStage.INITIAL, DerivationStage.PRATYAYA_SELECTED) &&
        state.terms.lastOrNull()?.let { last ->
            listOf(Lakara.LANG, Lakara.LUNG, Lakara.LRNG).any { last.matchesUpadesha(it.upadesha) }
        } == true

internal fun introducePastAugment(state: DerivationState, upadesha: String, sutra: String): DerivationChange {
    val index = state.terms.indexOfFirst { it.kind == TermKind.DHATU }.let { if (it < 0) 0 else it }
    val target = state.terms[index]
    val augment = DerivationTerm("at-agama", upadesha, TermKind.AGAMA, upadesha = upadesha,
        createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        augmentTargetId = target.id, mergeIntoAugmentTarget = false,
        grammaticalAccents = listOf(GrammaticalAccent(0, AccentType.UDATTA, sutra)))
    return DerivationChange(state.copy(terms = state.terms.take(index) + augment + state.terms.drop(index),
        stage = DerivationStage.IT_PROCESSED), "$sutra introduces raw $upadesha with prescribed udātta and target-linked placement.")
}
