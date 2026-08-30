package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val terms = context.terms
        for (i in terms.indices) {
            val term = terms[i]
            if (term.kind == TermKind.AGAMA) {
                if (term.augmentTargetId == null) continue
                if (term.itProcessingPending && term.itDesignations.isEmpty()) continue
                val isTit = term.itMarkers.contains(ItMarker.T) || term.upadesha?.endsWith("ट्") == true
                val isKit = term.itMarkers.contains(ItMarker.KIT) || term.upadesha?.endsWith("क्") == true

                if (term.augmentTargetId != null && (isTit || isKit)) return true

                if (isTit) {
                    val targetIndex = findTargetIndex(term, terms)
                    if (targetIndex != -1 && i > targetIndex) {
                        return true
                    }
                } else if (isKit) {
                    val targetIndex = findTargetIndex(term, terms)
                    if (targetIndex != -1 && i < targetIndex) {
                        return true
                    }
                }
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
                if (term.itProcessingPending && term.itDesignations.isEmpty()) continue
                val isTit = term.itMarkers.contains(ItMarker.T) || term.upadesha?.endsWith("ट्") == true
                val isKit = term.itMarkers.contains(ItMarker.KIT) || term.upadesha?.endsWith("क्") == true

                term.augmentTargetId?.let { targetId ->
                    val targetIndex = terms.indexOfFirst { it.id == targetId }
                    if (targetIndex >= 0 && (isTit || isKit)) {
                        val target = terms[targetIndex]
                        val designationOffset = if (isTit) 0 else target.surface.length
                        val merged = target.copy(
                            surface = if (isTit) term.surface + target.surface else target.surface + term.surface,
                            itMarkers = target.itMarkers + term.itMarkers,
                            itDesignations = target.itDesignations + term.itDesignations.map {
                                it.copy(start = it.start + designationOffset, endExclusive = it.endExclusive + designationOffset)
                            },
                            itProcessingPending = target.itProcessingPending || term.itProcessingPending,
                        )
                        terms[targetIndex] = merged
                        terms.removeAt(terms.indexOfFirst { it.id == term.id })
                        return DerivationChange(
                            state = context.copy(terms = terms),
                            explanation = "1.1.46 places the ${if (isTit) "ṭit" else "kit"} augment ${term.upadesha} ${if (isTit) "at the beginning" else "at the end"} of ${target.upadesha}.",
                        )
                    }
                }

                if (isTit) {
                    val targetIndex = findTargetIndex(term, terms)
                    if (targetIndex != -1 && i > targetIndex) {
                        val item = terms.removeAt(i)
                        val newTargetIndex = terms.indexOfFirst { it.id == terms[targetIndex].id }
                        terms.add(newTargetIndex, item)
                        return DerivationChange(
                            state = context.copy(terms = terms),
                            explanation = "1.1.46 (ādyantau ṭakitau) places the ṭit augment ${term.surface} before its target."
                        )
                    }
                } else if (isKit) {
                    val targetIndex = findTargetIndex(term, terms)
                    if (targetIndex != -1 && i < targetIndex) {
                        val item = terms.removeAt(i)
                        val newTargetIndex = terms.indexOfFirst { it.id == terms[targetIndex - 1].id }
                        terms.add(newTargetIndex + 1, item)
                        return DerivationChange(
                            state = context.copy(terms = terms),
                            explanation = "1.1.46 (ādyantau ṭakitau) places the kit augment ${term.surface} after its target."
                        )
                    }
                }
            }
        }
        return DerivationChange(context, "No positioning change needed.")
    }

    private fun findTargetIndex(agama: DerivationTerm, terms: List<DerivationTerm>): Int {
        agama.augmentTargetId?.let { targetId ->
            return terms.indexOfFirst { it.id == targetId }
        }
        val isTit = agama.itMarkers.contains(ItMarker.T) || agama.upadesha?.endsWith("ट्") == true
        return if (isTit) {
            terms.indexOfFirst { it.kind != TermKind.AGAMA && it.id != "abhyasa" }
        } else {
            terms.indexOfLast { it.kind != TermKind.AGAMA && it.id != "abhyasa" }
        }
    }
}
