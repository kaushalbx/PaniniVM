package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 1.1.46: ādyantau ṭakitau.
 * An augment (āgama) marked with 'ṭ' is placed at the beginning of the term.
 * An augment marked with 'k' is placed at the end of the term.
 */
object AdyantauTakitauSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.46",
    text = "आद्यन्तौ टकितौ",
    hindiExplanation = "टकार-इत् आगम आदि में और ककार-इत् आगम अन्त में जुड़ता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110046,
    role = SutraRole.Paribhasha(targetScope = ParibhashaScope.AUGMENT_PLACEMENT),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
    // Placement consumes the exact 1.3.3 designation as evidence, so it must
    // run after designation and before 1.3.9 removes the designated segment.
    stage = SutraStage.IT_PROCESSING,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val terms = context.terms
        for (term in terms) {
            if (term.kind == TermKind.AGAMA) {
                if (term.augmentTargetId == null) continue
                if (placementFromDesignation(term) != null) return true
            }
        }
        return false
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.toMutableList()
        for (i in terms.indices) {
            val term = terms[i]
            if (term.kind == TermKind.AGAMA) {
                if (term.augmentTargetId == null) continue
                val placement = placementFromDesignation(term) ?: continue
                val isTit = placement == AugmentPlacement.BEGINNING

                term.augmentTargetId?.let { targetId ->
                    val targetIndex = terms.indexOfFirst { it.id == targetId }
                    if (targetIndex >= 0) {
                        val target = terms[targetIndex]
                        val targetOffset = if (isTit) term.surface.length else 0
                        val augmentOffset = if (isTit) 0 else target.surface.length
                        val merged = target.copy(
                            surface = if (isTit) term.surface + target.surface else target.surface + term.surface,
                            itMarkers = target.itMarkers + term.itMarkers,
                            itDesignations = target.itDesignations.shiftedBy(targetOffset) +
                                term.itDesignations.shiftedBy(augmentOffset),
                            deferredItDesignations = target.deferredItDesignations.shiftedBy(targetOffset) +
                                term.deferredItDesignations.shiftedBy(augmentOffset),
                            itProcessingPhase = when {
                                target.itDesignations.isNotEmpty() || term.itDesignations.isNotEmpty() ->
                                    ItProcessingPhase.DESIGNATED
                                target.itProcessingPending || term.itProcessingPending ->
                                    ItProcessingPhase.RAW_UPADESHA
                                target.itProcessingPhase == ItProcessingPhase.DEFERRED_SUBSTITUTION ||
                                    term.itProcessingPhase == ItProcessingPhase.DEFERRED_SUBSTITUTION ->
                                    ItProcessingPhase.DEFERRED_SUBSTITUTION
                                else -> ItProcessingPhase.PROCESSED
                            },
                        )
                        terms[targetIndex] = merged
                        terms.removeAt(terms.indexOfFirst { it.id == term.id })
                        return DerivationChange(
                            state = context.copy(terms = terms),
                            explanation = "1.1.46 places the ${if (isTit) "ṭit" else "kit"} augment ${term.upadesha} ${if (isTit) "at the beginning" else "at the end"} of ${target.upadesha}.",
                        )
                    }
                }

            }
        }
        return DerivationChange(context, "No positioning change needed.")
    }

    private fun placementFromDesignation(agama: DerivationTerm): AugmentPlacement? {
        val terminalDesignation = (agama.itDesignations + agama.deferredItDesignations).singleOrNull {
            it.sutra == "1.3.3" &&
                it.endExclusive == agama.surface.length &&
                it.designatedText in setOf("ट्", "क्")
        } ?: return null
        return if (terminalDesignation.designatedText == "ट्") {
            AugmentPlacement.BEGINNING
        } else {
            AugmentPlacement.END
        }
    }

    private fun List<ItDesignation>.shiftedBy(offset: Int): List<ItDesignation> = map {
        it.copy(start = it.start + offset, endExclusive = it.endExclusive + offset)
    }

    private enum class AugmentPlacement { BEGINNING, END }
}
