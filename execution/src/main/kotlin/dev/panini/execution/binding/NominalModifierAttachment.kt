package dev.panini.execution.binding

import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SupBearingPada

/** Indices denote discourse occurrences; equal-looking heads must remain distinct candidates. */
data class NominalAttachmentCandidate(val headIndex: Int, val slots: Set<SupAffix>, val knownGenderAgreement: Boolean)

enum class NominalAttachmentDiagnostic { MISSING_SUP, NO_COMPATIBLE_HEAD, AMBIGUOUS_HEADS }

data class NominalAttachmentAnalysis(val candidates: List<NominalAttachmentCandidate>, val diagnostic: NominalAttachmentDiagnostic?) {
    val unique: NominalAttachmentCandidate? get() = candidates.singleOrNull()
}

/** Agreement analysis over a caller-supplied grammatical scope, never a nearest-word guess. */
object NominalModifierAttachment {
    fun analyze(modifier: Pada, heads: List<Pada>): NominalAttachmentAnalysis {
        val modifierSup = (modifier as? SupBearingPada)?.sup
            ?: return NominalAttachmentAnalysis(emptyList(), NominalAttachmentDiagnostic.MISSING_SUP)
        val candidates = heads.mapIndexedNotNull { index, head ->
            val headSup = (head as? SupBearingPada)?.sup ?: return@mapIndexedNotNull null
            val common = modifierSup.affixCandidates.toSet().intersect(headSup.affixCandidates.toSet())
            if (common.isEmpty() || !NominalGenderAgreement.compatible(modifier, head)) return@mapIndexedNotNull null
            NominalAttachmentCandidate(index, common, NominalGenderAgreement.knownGenders(modifier).isNotEmpty() &&
                NominalGenderAgreement.knownGenders(head).isNotEmpty())
        }
        val diagnostic = when (candidates.size) {
            0 -> NominalAttachmentDiagnostic.NO_COMPATIBLE_HEAD
            1 -> null
            else -> NominalAttachmentDiagnostic.AMBIGUOUS_HEADS
        }
        return NominalAttachmentAnalysis(candidates, diagnostic)
    }
}
