package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.core.SupAffix
import dev.panini.shiksha.Svara

/** Established formation evidence; an arbitrary long ā is not proof of āp. */
fun DerivationTerm.hasApFormation(): Boolean =
    upadesha in apUpadeshas || "4.1.4" in establishedBySutras ||
        sourceSuffixUpadeshas.any { it in apUpadeshas }

private val apUpadeshas = setOf("टाप्", "डाप्", "चाप्")

/** Shared grammatical input for yāṭ/syāṭ, before either augment is introduced. */
fun DerivationState.apNgitJunctions(): List<Pair<DerivationTerm, DerivationTerm>> {
    if ("6.4.1" !in activeAdhikaras) return emptyList()
    return terms.zipWithNext().filter { (stem, suffix) ->
        stem.hasApFormation() && stem.varnas.lastOrNull() == Svara.AA &&
            suffix.kind == TermKind.PRATYAYA && suffix.sourceSupAffix != null &&
            suffix.hasEffectiveMarker(ItMarker.NGIT) &&
            suffix.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            suffix.itDesignations.isEmpty() && suffix.deferredItDesignations.isEmpty() &&
            suffix.establishedBySutras.none { it in setOf("7.3.113", "7.3.114", "7.3.115") } &&
            (suffix.sourceSupAffix != SupAffix.NGI || "7.3.116" in suffix.establishedBySutras) &&
            allEffectiveTerms.none { it.kind == TermKind.AGAMA &&
                it.createdBySutra in setOf("7.3.113", "7.3.114", "7.3.115") && it.augmentTargetId == suffix.id }
    }
}

/** Common prescribed operation, leaving it processing and vowel coalescence independent. */
fun DerivationState.introduceApSyat(stemId: String, suffixId: String, sutra: String): DerivationState {
    val (stem, suffix) = requireNotNull(apNgitJunctions().firstOrNull { (stem, suffix) ->
        stem.id == stemId && suffix.id == suffixId
    })
    val shortened = replaceTermVarna(stem.id, stem.varnas.lastIndex, listOf(Svara.A), sutra)
    val index = shortened.terms.indexOfFirst { it.id == suffix.id }
    val augment = DerivationTerm("syaat:${suffix.id}", "स्याट्", TermKind.AGAMA,
        upadesha = "स्याट्", createdBySutra = sutra,
        itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        augmentTargetId = suffix.id, mergeIntoAugmentTarget = true)
    return shortened.copy(terms = shortened.terms.take(index) + augment + shortened.terms.drop(index),
        stage = DerivationStage.IT_PROCESSED)
}
