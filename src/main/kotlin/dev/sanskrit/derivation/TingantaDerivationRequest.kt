package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.Dhatu
import dev.sanskrit.dhatupatha.PadaType

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
    fun initialState() = DerivationState(
        listOf(DerivationTerm("dhatu", dhatu, TermKind.DHATU)),
        context = DerivationalContext(
            kala = when (lakara) {
                Lakara.LAT -> Kala.VARTAMANA
                Lakara.LRT -> Kala.BHAVISYAT
                Lakara.LRNG -> Kala.BHAVISYAT
                Lakara.LUT -> Kala.BHAVISYAT
                Lakara.LANG -> Kala.BHUTA
                Lakara.LIT -> Kala.BHUTA
                Lakara.LUNG -> Kala.BHUTA
                else -> Kala.VARTAMANA
            },
            rupa = Rupa(purusha = purusha, prayoga = Prayoga.KARTARI, vacana = vacana, lakara = lakara, pada = pada),
            letAugment = letAugment,
            letFormation = letFormation,
            letEOption = letEOption,
            environments = when (lakara) {
                Lakara.LRT, Lakara.LIT, Lakara.LRNG, Lakara.LUT -> setOf(DerivationalEnvironment.ARDHADHATUKA)
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
            Lakara.LRNG -> Kala.BHAVISYAT
            Lakara.LUT -> Kala.BHAVISYAT
            Lakara.LANG -> Kala.BHUTA
            Lakara.LIT -> Kala.BHUTA
            Lakara.LUNG -> Kala.BHUTA
            else -> Kala.VARTAMANA
        },
        rupa = Rupa(purusha = purusha, prayoga = Prayoga.KARTARI, vacana = vacana, lakara = lakara, pada = pada),
        letAugment = letAugment,
        letFormation = letFormation,
        letEOption = letEOption,
        environments = when (lakara) {
            Lakara.LRT, Lakara.LIT, Lakara.LRNG, Lakara.LUT -> setOf(DerivationalEnvironment.ARDHADHATUKA)
            else -> emptySet()
        }
    ),
)
