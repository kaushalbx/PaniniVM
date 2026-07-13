package dev.sanskrit.derivation


data class TingantaDerivationRequest(val dhatu: String, val vacana: Vacana = Vacana.EKAVACANA) {
    init { require(dhatu == "भू") { "Only भू is executable in the initial tiṅ path." } }
    fun initialState() = DerivationState(listOf(DerivationTerm("dhatu", dhatu, TermKind.DHATU)), semanticFeatures = setOf(SemanticFeature.KARTARI, SemanticFeature.VARTAMANA, SemanticFeature.PRATHAMA_PURUSHA, vacana.semanticFeature))
}

