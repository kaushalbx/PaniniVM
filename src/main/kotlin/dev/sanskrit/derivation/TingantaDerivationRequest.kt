package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.Dhatu

data class TingantaDerivationRequest(
    val dhatu: String,
    val vacana: Vacana = Vacana.EKAVACANA,
    val purusha: Purusha = Purusha.PRATHAMA,
    val lakara: Lakara = Lakara.LAT,
) {
    fun initialState() = DerivationState(
        listOf(DerivationTerm("dhatu", dhatu, TermKind.DHATU)),
        context = DerivationalContext(
            kala = when (lakara) {
                Lakara.LAT -> Kala.VARTAMANA
                Lakara.LRT -> Kala.BHAVISYAT
                Lakara.LANG -> Kala.BHUTA
                else -> Kala.VARTAMANA
            },
            rupa = Rupa(purusha = purusha, prayoga = Prayoga.KARTARI, vacana = vacana, lakara = lakara),
            environments = when (lakara) {
                Lakara.LRT -> setOf(DerivationalEnvironment.ARDHADHATUKA)
                else -> emptySet()
            }
        ),
    )
}

/** Builds a derivation state from the authoritative Dhātupāṭha entry, including iṭ-status. */
fun TingantaDerivationRequest.initialState(dhatu: Dhatu) = DerivationState(
    listOf(DerivationTerm.fromDhatu(dhatu)),
    context = DerivationalContext(
        kala = when (lakara) {
            Lakara.LAT -> Kala.VARTAMANA
            Lakara.LRT -> Kala.BHAVISYAT
            Lakara.LANG -> Kala.BHUTA
            else -> Kala.VARTAMANA
        },
        rupa = Rupa(purusha = purusha, prayoga = Prayoga.KARTARI, vacana = vacana, lakara = lakara),
        environments = when (lakara) {
            Lakara.LRT -> setOf(DerivationalEnvironment.ARDHADHATUKA)
            else -> emptySet()
        }
    ),
)
