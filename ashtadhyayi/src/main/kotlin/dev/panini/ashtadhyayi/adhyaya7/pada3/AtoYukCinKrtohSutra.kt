package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.ItMarker
import dev.panini.core.KitAugment
import dev.panini.core.SanadiAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.sutra.*

/** 7.3.33: ā-final aṅga takes yuk before ciṇ or ñit/ṇit kṛt, not arbitrary u. */
object AtoYukCinKrtohSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.33", text = "आतो युक् चिण्कृतोः",
    hindiExplanation = "आकारान्त अङ्ग को चिण् अथवा ञित्-णित् कृत् प्रत्यय परे युक् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false, kramaValue = 730033,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA, dependencies = setOf("6.4.1"),
), DerivationSutra {
    private fun target(state: DerivationState): DerivationTerm? {
        if ("6.4.1" !in state.activeAdhikaras) return null
        for ((index, root) in state.terms.withIndex()) {
            if (root.kind != TermKind.DHATU || root.varnas.lastOrNull() != Svara.AA || root.itProcessingPending) continue
            val suffix = state.terms.drop(index + 1).firstOrNull { it.kind == TermKind.PRATYAYA } ?: continue
            if (suffix.itProcessingPending || suffix.itDesignations.isNotEmpty() || suffix.deferredItDesignations.isNotEmpty()) continue
            val isKrt = state.samjnas.any { it.targetId == suffix.id && it.samjna == Samjna.KRT }
            val excluded = suffix.sourceSupAffix != null || suffix.sourceTingAffix != null ||
                SanadiAffix.fromUpadesha(suffix.upadesha) != null ||
                suffix.sourceTaddhitaAffix != null || state.samjnas.any {
                    it.targetId == suffix.id && it.samjna == Samjna.TADDHITA
                }
            val eligible = suffix.upadesha == "चिण्" || (!excluded && isKrt &&
                (suffix.hasEffectiveMarker(ItMarker.NYIT) || suffix.hasEffectiveMarker(ItMarker.NIT)))
            if (eligible && state.allEffectiveTerms.none {
                it.kind == TermKind.AGAMA && it.createdBySutra == number && it.augmentTargetId == root.id
            }) return root
        }
        return null
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(target(context))
        val augment = KitAugment.YUK.rawTerm("yuk:${root.id}", number).copy(
            augmentTargetId = root.id, mergeIntoAugmentTarget = true)
        return DerivationChange(context.addTerm(augment).copy(stage = DerivationStage.IT_PROCESSED),
            "7.3.33 introduces raw yuk, with pronunciation-only u, for kit placement at the end of the ā-final aṅga.")
    }
}
