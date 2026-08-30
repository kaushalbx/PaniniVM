package dev.panini.derivation

import dev.panini.core.Kala
import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Prayoga
import dev.panini.core.Purusha
import dev.panini.core.Vacana
import dev.panini.dhatupatha.Dhatu

data class TingantaDerivationRequest(
    val dhatu: String,
    val vacana: Vacana = Vacana.EKAVACANA,
    val purusha: Purusha = Purusha.PRATHAMA,
    val lakara: Lakara = Lakara.LAT,
    val letAugment: LetAugment = LetAugment.AAT,
    val letFormation: LetFormation = LetFormation.PRESENT_STEM,
    val letEOption: LetEOption = LetEOption.E,
    val pada: PadaType? = null,
    val sanadiPratyayas: List<String> = emptyList(),
) {
    fun initialState(): DerivationState = initialState(
        DerivationTerm("dhatu", dhatu, TermKind.DHATU),
    )

    /** Builds a state from the authoritative Dhātupāṭha entry, including iṭ-status. */
    fun initialState(dhatu: Dhatu): DerivationState = initialState(
        DerivationTerm.fromDhatu(dhatu),
    )

    private fun initialState(dhatuTerm: DerivationTerm) = DerivationState(
        listOf(dhatuTerm) + sanadiPratyayas.mapIndexed { index, pratyaya ->
            require(pratyaya == "णिच्") { "Unsupported sanādi pratyaya: $pratyaya" }
            DerivationTerm(
                id = "sanadi_$index",
                // Effective अय् reflects it-lopa and 6.1.77 before the following शप् vowel.
                surface = "अय्",
                kind = TermKind.PRATYAYA,
                itMarkers = setOf(ItMarker.NIT, ItMarker.GENERIC),
                upadesha = pratyaya,
            )
        },
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
