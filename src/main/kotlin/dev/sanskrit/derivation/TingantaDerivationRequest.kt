package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.Dhatu


data class TingantaDerivationRequest(val dhatu: String, val vacana: Vacana = Vacana.EKAVACANA) {
    init { require(dhatu == "भू") { "Only भू is executable in the initial tiṅ path." } }
    fun initialState() = DerivationState(
        listOf(DerivationTerm("dhatu", dhatu, TermKind.DHATU)),
        context = DerivationalContext(kala = Kala.VARTAMANA, rupa = Rupa(purusha = Purusha.PRATHAMA, prayoga = Prayoga.KARTARI, vacana = vacana)),
    )
}

/** Builds a derivation state from the authoritative Dhātupāṭha entry, including iṭ-status. */
fun TingantaDerivationRequest.initialState(dhatu: Dhatu) = DerivationState(
    listOf(DerivationTerm.fromDhatu(dhatu)),
    context = DerivationalContext(kala = Kala.VARTAMANA, rupa = Rupa(purusha = Purusha.PRATHAMA, prayoga = Prayoga.KARTARI, vacana = vacana)),
)
