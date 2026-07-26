package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi

/** Main entry point for performing secondary nominal (Taddhita) derivations. */
class TaddhitaEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
) {
    fun derive(request: TaddhitaDerivationRequest): DerivationResult =
        derivationEngine.derive(request.initialState())

    fun derive(pratipadika: String, meaning: DerivationalMeaning): DerivationResult =
        derive(TaddhitaDerivationRequest(pratipadika, meaning))
}
