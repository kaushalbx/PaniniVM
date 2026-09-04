package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.*

/**
 * 1.1.47: midaco'ntyāt paraḥ.
 * An augment marked with 'm' is placed after the last vowel of the term.
 */
object MidacoAntyatParahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.47",
    text = "मिदचोऽन्त्यात् परः",
    hindiExplanation = "मकार-इत् आगम अन्त्य अच् (स्वर) के बाद बैठता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110047,
    role = SutraRole.Paribhasha(targetScope = ParibhashaScope.AUGMENT_PLACEMENT),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
    // Placement depends on the exact 1.3.3 designation and therefore must
    // precede 1.3.9, which consumes that designation.
    stage = SutraStage.IT_PROCESSING,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any { term ->
        term.kind == TermKind.PRATYAYA && term.augmentTargetId != null &&
            sutra !in term.establishedBySutras && terminalMitDesignation(term) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val insertion = context.terms.first { term ->
            term.kind == TermKind.PRATYAYA && term.augmentTargetId != null &&
                sutra !in term.establishedBySutras && terminalMitDesignation(term) != null
        }
        val targetIndex = context.terms.indexOfFirst { it.id == insertion.augmentTargetId }
        require(targetIndex >= 0) { "1.1.47 cannot find target ${insertion.augmentTargetId}." }
        val target = context.terms[targetIndex]
        val insertionIndex = requireNotNull(finalVowelEnd(target.surface)) {
            "1.1.47 requires a vowel in ${target.surface}."
        }
        val merged = target.copy(
            surface = target.surface.substring(0, insertionIndex) + insertion.surface + target.surface.substring(insertionIndex),
            itDesignations = target.itDesignations + insertion.itDesignations.shiftedBy(insertionIndex),
            deferredItDesignations = target.deferredItDesignations + insertion.deferredItDesignations.shiftedBy(insertionIndex),
            itProcessingPhase = ItProcessingPhase.DESIGNATED,
            establishedBySutras = target.establishedBySutras + sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms.mapNotNull { term -> when (term.id) {
                    target.id -> merged
                    insertion.id -> null
                    else -> term
                } },
                droppedTerms = context.droppedTerms + insertion.copy(surface = "", establishedBySutras = insertion.establishedBySutras + sutra),
                // The target's own final consonant was never part of the raw
                // augment upadeśa. Only the remapped augment designations are
                // eligible for this IT-processing cycle.
                halantyamExemptTermIds = context.halantyamExemptTermIds + target.id,
            ),
            explanation = "1.1.47 places the mit श्नम् after the last vowel of ${target.surface}, preserving its exact designations.",
        )
    }

    private fun terminalMitDesignation(term: dev.panini.derivation.DerivationTerm): ItDesignation? =
        (term.itDesignations + term.deferredItDesignations).singleOrNull {
            it.sutra == "1.3.3" && it.endExclusive == term.surface.length && it.designatedText == "म्"
        }

    private fun finalVowelEnd(surface: String): Int? {
        val vowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')
        return surface.indexOfLast { it in vowels }.takeIf { it >= 0 }?.plus(1)
    }

    private fun List<ItDesignation>.shiftedBy(offset: Int): List<ItDesignation> = map {
        it.copy(start = it.start + offset, endExclusive = it.endExclusive + offset)
    }
}
