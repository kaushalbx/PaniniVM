package dev.panini.parser

import dev.panini.parser.ast.*
import dev.panini.vyakaranam.ast.*

/** Compatibility projection from the canonical vyākaraṇa AST to execution AST. */
class VakyaAstBuilder {

    fun build(ukti: Ukti): ParsedUtterance =
        ParsedUtterance(
            sambodhana = ukti.sambodhana?.let {
                ParsedSambodhana(
                    particle = it.suchaka ?: "हे",
                    pada = it.subanta.toParsedSubanta(),
                )
            },
            statements = ukti.vakyas.map(::buildVakya),
            connectives = ukti.sambandhas,
            hasDanda = '।' in ukti.sourceText || '॥' in ukti.sourceText,
        )

    private fun buildVakya(vakya: Vakya): ParsedVakya =
        ParsedVakya(
            padas = vakya.padas.mapNotNull(::buildPada),
            tinganta = (vakya as? AkhyataVakya)?.tinganta?.let(::buildTinganta),
        )

    private fun buildPada(pada: Pada): ParsedPada? =
        when (pada) {
            is SubantaPada -> ParsedPada.Subanta(pada.toParsedSubanta())
            is SamuccitaSubanta -> ParsedPada.Coordination(
                pada.members.map { it.toParsedSubanta() },
            )
            is AvyayaPada -> buildAvyaya(pada)
            is TingantaPada -> null
        }

    private fun buildAvyaya(pada: AvyayaPada): ParsedPada =
        when (val derivation = pada.derivation) {
            is AvyayaKridantaDerivation -> ParsedPada.AvyayaKridanta(
                upasargas = derivation.upasargas,
                dhatu = derivation.dhatu.mulaDhatu,
                vikarana = null,
                pratyaya = derivation.pratyaya,
            )
            else -> ParsedPada.Avyaya(pada.form)
        }

    private fun SubantaPada.toParsedSubanta(): ParsedSubanta =
        ParsedSubanta(
            base = pratipadika.toParsedBase(),
            supPratyaya = sup.text,
        )

    private fun Pratipadika.toParsedBase(): ParsedNominalBase {
        val base = when (this) {
            is MulaPratipadika -> ParsedNominalBase.Simple(text, classify(text))
            is KridantaPratipadika -> ParsedNominalBase.Kridanta(
                upasargas = upasargas,
                dhatu = dhatu.mulaDhatu,
                vikarana = null,
                pratyaya = krtPratyaya,
            )
            is UnadyantaPratipadika -> ParsedNominalBase.Simple(
                sourceText,
                SimpleNominalKind.IDENTIFIER,
            )
            is SamasaPratipadika -> ParsedNominalBase.Samasa(
                members = angas.map { it.pratipadika.toParsedBase() },
                separator = "-",
            )
        }

        return vikaras.fold(base) { prakriti, vikara ->
            when (vikara) {
                is TaddhitaVikara -> ParsedNominalBase.Taddhita(prakriti, vikara.pratyaya)
                is StriVikara -> ParsedNominalBase.Stri(prakriti, vikara.pratyaya)
            }
        }
    }

    private val Pratipadika.vikaras: List<PratipadikaVikara>
        get() = when (this) {
            is MulaPratipadika -> vikaras
            is KridantaPratipadika -> vikaras
            is UnadyantaPratipadika -> vikaras
            is SamasaPratipadika -> vikaras
        }

    private fun buildTinganta(pada: TingantaPada): ParsedTinganta =
        ParsedTinganta(
            upasargas = pada.upasargas,
            dhatu = pada.dhatu.mulaDhatu,
            sanadiPratyayas = pada.dhatu.sanadiPratyayas,
            vikarana = null,
            lakara = pada.lakara.upadesha,
            tingPratyaya = pada.ting.text,
            unresolvedIdentifier = null,
        )

    private fun classify(text: String): SimpleNominalKind =
        when (text) {
            in numerals -> SimpleNominalKind.NUMERAL
            "पूर्वफल", "फल", "फले", "फलानि" -> SimpleNominalKind.RESULT_REFERENCE
            else -> SimpleNominalKind.IDENTIFIER
        }

    private companion object {
        val numerals = setOf(
            "शून्य", "एक", "द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश",
            "विंशति", "त्रिंशत्", "चत्वारिंशत्", "पञ्चाशत्", "षष्टि", "सप्तति", "अशीति", "नवति",
            "शत", "सहस्र", "अयुत", "लक्ष", "प्रयुत", "कोटि",
        )
    }
}
