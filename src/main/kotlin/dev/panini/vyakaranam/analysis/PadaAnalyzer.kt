package dev.panini.vyakaranam.analysis

import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.DhatuEntry
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.VyakaranamLexicon

data class SubantaAnalysis(
    val pada: SubantaPada,
    val lexicalEntry: PratipadikaEntry?,
    val sup: SupMetadata,
    val linga: Set<Linga>,
)

data class TingantaAnalysis(
    val pada: TingantaPada,
    val lexicalEntry: DhatuEntry?,
    val ting: TingMetadata,
    val lakara: Lakara,
)

sealed interface PadaAnalysis {
    val pada: Pada
}

data class AnalyzedSubanta(
    override val pada: SubantaPada,
    val analysis: SubantaAnalysis,
) : PadaAnalysis

data class AnalyzedTinganta(
    override val pada: TingantaPada,
    val analysis: TingantaAnalysis,
) : PadaAnalysis

data class AnalyzedAvyaya(
    override val pada: AvyayaPada,
) : PadaAnalysis

data class AnalyzedSamuccita(
    override val pada: SamuccitaSubanta,
    val members: List<SubantaAnalysis>,
) : PadaAnalysis

class PadaAnalyzer(
    private val lexicon: VyakaranamLexicon,
) {

    fun analyze(pada: Pada): PadaAnalysis =
        when (pada) {
            is SubantaPada ->
                AnalyzedSubanta(
                    pada = pada,
                    analysis = analyzeSubanta(pada),
                )

            is TingantaPada ->
                AnalyzedTinganta(
                    pada = pada,
                    analysis = analyzeTinganta(pada),
                )

            is AvyayaPada ->
                AnalyzedAvyaya(pada)

            is SamuccitaSubanta ->
                AnalyzedSamuccita(
                    pada = pada,
                    members = pada.members.map(::analyzeSubanta),
                )
        }

    fun analyzeSubanta(
        pada: SubantaPada,
    ): SubantaAnalysis {
        val lexicalText = lexicalPratipadikaText(pada.pratipadika)
        val lexicalEntry = lexicalText?.let(lexicon::findPratipadika)

        return SubantaAnalysis(
            pada = pada,
            lexicalEntry = lexicalEntry,
            sup = PratyayaMetadata.requireSup(pada.sup.text),
            linga = lexicalEntry?.linga ?: setOf(Linga.ANIRDHARITA),
        )
    }

    fun analyzeTinganta(
        pada: TingantaPada,
    ): TingantaAnalysis {
        val dhatu = lexicon.findDhatu(pada.dhatu.mulaDhatu)
            ?: throw IllegalArgumentException(
                "धातुपाठे धातुः न प्राप्तः: ${pada.dhatu.mulaDhatu}",
            )

        val tingMetadata =
            PratyayaMetadata.requireTing(pada.ting.text)

        require(tingMetadata.pada in dhatu.pada) {
            buildString {
                append("धातोः पदविरोधः: ")
                append(dhatu.upadesha)
                append(" धातुः ")
                append(dhatu.pada)
                append(" स्वीकरोति, किन्तु ")
                append(pada.ting.text)
                append(" ")
                append(tingMetadata.pada)
                append(" सूचयति।")
            }
        }

        return TingantaAnalysis(
            pada = pada,
            lexicalEntry = dhatu,
            ting = tingMetadata,
            lakara = pada.lakara,
        )
    }

    private fun lexicalPratipadikaText(
        pratipadika: Pratipadika,
    ): String? =
        when (pratipadika) {
            is MulaPratipadika -> pratipadika.text
            is KridantaPratipadika -> null
            is UnadyantaPratipadika -> null
            is SamasaPratipadika -> null
        }
}
