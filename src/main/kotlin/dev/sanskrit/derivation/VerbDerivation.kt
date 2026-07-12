package dev.sanskrit.derivation


data class TingantaDerivationRequest(val dhatu: String, val vacana: Vacana = Vacana.EKAVACANA) {
    init { require(dhatu == "भू") { "Only भू is executable in the initial tiṅ path." } }
    fun initialState() = DerivationState(listOf(DerivationTerm("dhatu", dhatu, TermKind.DHATU)), semanticFeatures = setOf(SemanticFeature.KARTARI, SemanticFeature.VARTAMANA, SemanticFeature.PRATHAMA_PURUSHA, vacana.semanticFeature))
}

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
