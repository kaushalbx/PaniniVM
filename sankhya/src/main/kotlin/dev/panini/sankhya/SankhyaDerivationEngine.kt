package dev.panini.sankhya

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationEvent
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.sutra.SutraStage

/** Executes numeral-compound operations in their grammatical dependency order. */
class SankhyaDerivationEngine {
    private val compoundAnga = DerivationEngine(Ashtadhyayi.executableSutrasAt(SutraStage.ANGAKARYA))
    private val padaFormation = DerivationEngine(Ashtadhyayi.executableSutrasAt(SutraStage.PADA_FORMATION))
    private val sandhiPhases = SutraStage.sandhiPhases
        .filterNot { it == SutraStage.THUK_PHONOLOGY || it == SutraStage.SANDHI }
        .map { stage -> DerivationEngine(Ashtadhyayi.executableSutrasAt(stage)) }

    fun derive(initial: DerivationState): DerivationResult {
        val start = initial.copy(stage = DerivationStage.PADA_FORMED)
        return complete(initial, compoundAnga.derive(start))
    }

    /** Returns both application and non-application branches of optional numeral rules. */
    fun deriveAll(initial: DerivationState): List<DerivationResult> {
        val start = initial.copy(stage = DerivationStage.PADA_FORMED)
        return compoundAnga.deriveAll(start)
            .map { branch -> complete(initial, branch) }
            .distinctBy { result -> result.final.surface to result.applications.map { it.sutra } }
    }

    private fun complete(initial: DerivationState, compoundResult: DerivationResult): DerivationResult {
        val stages = if (initial.terms.size == 1) emptyList() else buildList {
            add(padaFormation)
            addAll(sandhiPhases)
        }
        var state = compoundResult.final
        val applications = compoundResult.applications.toMutableList()
        val events = compoundResult.events.filterNot { it is DerivationEvent.Completed }.toMutableList()
        stages.forEach { engine ->
            val result = engine.derive(state.copy(stage = DerivationStage.PADA_FORMED))
            state = result.final
            applications += result.applications
            events += result.events.filterNot { it is DerivationEvent.Completed }
        }
        return DerivationResult(
            initial = initial,
            final = state.copy(stage = DerivationStage.FINAL),
            applications = applications,
            events = events + DerivationEvent.Completed(state, applications.size),
        )
    }
}
