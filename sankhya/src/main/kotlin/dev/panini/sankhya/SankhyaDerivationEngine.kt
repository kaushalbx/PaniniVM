package dev.panini.sankhya

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationPipeline
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.sutra.SutraStage

/** Executes numeral-compound operations in their grammatical dependency order. */
class SankhyaDerivationEngine {
    private val stages = listOf(SutraStage.PRATYAYA_SELECTION, SutraStage.ANGAKARYA, SutraStage.PADA_FORMATION) +
        SutraStage.sandhiPhases.filterNot { it == SutraStage.THUK_PHONOLOGY || it == SutraStage.SANDHI }
    private val pipeline = DerivationPipeline(
        stages = stages,
        prepareStage = { _, state -> state.copy(stage = DerivationStage.PADA_FORMED) },
        isStageEnabled = { stage, initial, _ ->
            stage == SutraStage.PRATYAYA_SELECTION || stage == SutraStage.ANGAKARYA || initial.terms.size > 1
        },
        finalizeState = { state -> state.copy(stage = DerivationStage.FINAL) },
        sutrasForStage = Ashtadhyayi::sankhyaSutrasAt,
    )

    fun derive(initial: DerivationState): DerivationResult {
        return pipeline.derive(initial)
    }

    /** Returns both application and non-application branches of optional numeral rules. */
    fun deriveAll(initial: DerivationState): List<DerivationResult> {
        return pipeline.deriveAll(initial, setOf(SutraStage.ANGAKARYA))
    }
}
