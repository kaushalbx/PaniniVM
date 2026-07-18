package dev.panini.derivation

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.PadaType

data class TingantaDerivationRequest(
    val dhatu: String,
    val vacana: Vacana = Vacana.EKAVACANA,
    val purusha: Purusha = Purusha.PRATHAMA,
    val lakara: Lakara = Lakara.LAT,
    val letAugment: LetAugment = LetAugment.AAT,
    val letFormation: LetFormation = LetFormation.PRESENT_STEM,
    val letEOption: LetEOption = LetEOption.E,
    val pada: PadaType? = null,
) {
    fun initialState(): DerivationState = initialState(
        DerivationTerm("dhatu", dhatu, TermKind.DHATU),
    )

    /** Builds a state from the authoritative Dhātupāṭha entry, including iṭ-status. */
    fun initialState(dhatu: Dhatu): DerivationState = initialState(
        DerivationTerm.fromDhatu(dhatu),
    )

    private fun initialState(dhatuTerm: DerivationTerm) = DerivationState(
        listOf(dhatuTerm),
        context = DerivationalContext(
            kala = when (lakara) {
                Lakara.LRT, Lakara.LRNG, Lakara.LUT -> Kala.BHAVISYAT
                Lakara.LANG, Lakara.LIT, Lakara.LUNG -> Kala.BHUTA
                else -> Kala.VARTAMANA
            },
            rupa = Rupa(purusha = purusha, prayoga = Prayoga.KARTARI, vacana = vacana, lakara = lakara, pada = pada),
            letAugment = letAugment,
            letFormation = letFormation,
            letEOption = letEOption,
            environments = when (lakara) {
                Lakara.LRT, Lakara.LIT, Lakara.LRNG, Lakara.LUT -> setOf(DerivationalEnvironment.ARDHADHATUKA)
                else -> emptySet()
            },
        ),
    )
}
