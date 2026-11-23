package dev.panini.sankhya

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationPipeline
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.sutra.SutraStage

/** Executes numeral-compound operations in their grammatical dependency order. */
class SankhyaDerivationEngine {
    private val stemStages = listOf(SutraStage.PRATYAYA_SELECTION, SutraStage.IT_PROCESSING, SutraStage.ANGAKARYA, SutraStage.PADA_FORMATION)
    private val tripadiStages = SutraStage.sandhiPhases
        .filterNot { it == SutraStage.THUK_PHONOLOGY }
        .flatMap { stage ->
            if (stage == SutraStage.RUTVA) listOf(stage, SutraStage.IT_PROCESSING) else listOf(stage)
        }
    private val pipeline = pipeline(stemStages + tripadiStages)
    // Accent belongs to the completed word, so the stem pass leaves it to the word pass.
    private val stemPipeline = pipeline(stemStages, computeSvara = false, withTripadi = false)
    private val wordPipeline = pipeline(listOf(SutraStage.PADA_FORMATION) + tripadiStages)

    private fun pipeline(stages: List<SutraStage>, computeSvara: Boolean = true, withTripadi: Boolean = true) = DerivationPipeline(
        stages = stages,
        computeSvaraAtCompletion = computeSvara,
        prepareStage = { _, state -> state.copy(stage = DerivationStage.PADA_FORMED) },
        isStageEnabled = { stage, initial, current ->
            stage == SutraStage.PRATYAYA_SELECTION || stage == SutraStage.IT_PROCESSING ||
                stage == SutraStage.ANGAKARYA || initial.terms.size > 1 || current.terms.size > 1
        },
        finalizeState = { state -> state.copy(stage = DerivationStage.FINAL) },
        // 8.2.1 पूर्वत्रासिद्धम्: the Tripādī (8.2–8.4) follows the sup and its luk, so the stem pass stops before it.
        sutrasForStage = { stage ->
            Ashtadhyayi.sankhyaSutrasAt(stage).filter { withTripadi || !isTripadi(it) }
        },
    )

    private fun isTripadi(sutra: dev.panini.derivation.DerivationSutra): Boolean =
        (sutra as? dev.panini.sutra.Sutra<*, *>)?.let { it.chapter == 8 && it.pada >= 2 } == true

    fun derive(initial: DerivationState): DerivationResult {
        return pipeline.derive(initial)
    }

    /** The taddhitānta up to its pada designations, before any sup or Tripādī operation. */
    fun deriveStem(initial: DerivationState): DerivationResult = stemPipeline.derive(initial)

    /** Completes a word whose sup has been appended: 2.4.82 and the other pada rules, then the Tripādī. */
    fun deriveWord(withSup: DerivationState): DerivationResult = wordPipeline.derive(withSup)

    /** Returns both application and non-application branches of optional numeral rules. */
    fun deriveAll(initial: DerivationState): List<DerivationResult> {
        return pipeline.deriveAll(initial, setOf(SutraStage.ANGAKARYA))
    }
}
