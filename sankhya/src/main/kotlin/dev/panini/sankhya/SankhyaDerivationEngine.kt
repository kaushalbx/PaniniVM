package dev.panini.sankhya

import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.HashiCaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.IkoYanAciSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.DvyashtanahSankhyayamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.EkadishCaikasyaCadukSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.TreStrayahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.VibhashaChatvarimshatPrabhritauSarveshamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.JhalamJashonteSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.NaloPratipadikantasyaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.SasajusoRuhSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.KharavasanayorVisarjaniyahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.VisarjaniyasyaSahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.StosShcunaShcuhSutra
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationEvent
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState

/** Executes numeral-compound operations in their grammatical dependency order. */
class SankhyaDerivationEngine {
    private val compoundAnga = DerivationEngine(listOf(EkadishCaikasyaCadukSutra, DvyashtanahSankhyayamSutra, TreStrayahSutra, VibhashaChatvarimshatPrabhritauSarveshamSutra))
    private val pratipadikaLopa = DerivationEngine(listOf(NaloPratipadikantasyaSutra))
    private val vowelSandhi = DerivationEngine(listOf(SavarnaDirghaSutra, IkoYanAciSutra))
    private val rutva = DerivationEngine(listOf(SasajusoRuhSutra))
    private val postRutva = DerivationEngine(listOf(HashiCaSutra, AdGunaSutra))
    private val visarjaniya = DerivationEngine(listOf(KharavasanayorVisarjaniyahSutra))
    private val sibilantSandhi = DerivationEngine(listOf(VisarjaniyasyaSahSutra, StosShcunaShcuhSutra))
    private val finalConsonantSandhi = DerivationEngine(listOf(JhalamJashonteSutra))

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
            add(pratipadikaLopa)
            add(vowelSandhi)
            add(rutva)
            add(postRutva)
            add(finalConsonantSandhi)
            add(visarjaniya)
            add(sibilantSandhi)
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
