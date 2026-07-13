package dev.sanskrit.derivation

class TingantaEngine(private val engine: DerivationEngine = DerivationEngine()) {
    fun derive(request: TingantaDerivationRequest): DerivationResult = engine.derive(request.initialState()).also { result ->
        val expected = when (request.vacana) {
            Vacana.EKAVACANA -> "भवति"
            Vacana.DVIVACANA -> "भवतः"
            Vacana.BAHUVACANA -> "भवन्ति"
        }
        require(result.final.surface == expected) { "Incomplete tiṅ derivation: ${result.final.surface}" }
    }
}
