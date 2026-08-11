package dev.panini.analysis

import dev.panini.core.Lakara
import dev.panini.core.Linga
import dev.panini.core.PadaType
import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.SanskritValue
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.UnadyantaPratipadika
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.VyakaranamLexicon

data class SubantaAnalysis(
    val pada: SubantaPada,
    val lexicalEntry: PratipadikaEntry?,
    val supCandidates: List<SupAffix>,
    val linga: Set<Linga>,
) {
    /** Compatibility view; semantic analysis must use [supCandidates]. */
    val sup: SupAffix get() = supCandidates.first()
}

data class TingantaAnalysis(
    val pada: TingantaPada,
    val lexicalEntry: Dhatu?,
    val ting: TingAffix,
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

/** Resolves encoded numeral syntax before it becomes a typed numeral prātipadika. */
fun interface SankhyaPadaValueResolver {
    fun resolve(pada: Pada): SanskritValue.Sankhya?
}

private fun encodedSankhyaValue(pada: Pada): SanskritValue.Sankhya? {
    val value = when (pada) {
        is SankhyaPada -> pada.value
        is SankhyaPuranaPada -> pada.value
        is KatapayadiPada -> pada.value
        is AryabhatiyaPada -> pada.value
        is BhutasamkhyaPada -> pada.value
        else -> null
    } ?: return null
    val word = when (pada) {
        is SankhyaPada -> pada.stems.joinToString(" ")
        is SankhyaPuranaPada -> pada.stems.joinToString(" ")
        is KatapayadiPada -> pada.word
        is AryabhatiyaPada -> pada.word
        is BhutasamkhyaPada -> pada.terms.joinToString(" ")
        else -> pada.sourceText
    }
    return SanskritValue.Sankhya(value, word)
}

class PadaAnalyzer(
    private val lexicon: VyakaranamLexicon,
    private val validatePadaCompatibility: Boolean = true,
    private val sankhyaValueResolver: SankhyaPadaValueResolver = SankhyaPadaValueResolver(::encodedSankhyaValue),
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

            is SankhyaPada -> analyzeSankhya(pada, pada.sup)

            is SankhyaPuranaPada -> analyzeSankhya(pada, pada.sup)

            is SankhyaAbhyasaPada ->
                AnalyzedAvyaya(
                    pada = AvyayaPada(pada.sourceText, pada.sourceText),
                )

            is KatapayadiPada -> analyzeSankhya(pada, pada.sup)

            is AryabhatiyaPada -> analyzeSankhya(pada, pada.sup)

            is BhutasamkhyaPada -> analyzeSankhya(pada, pada.sup)
        }

    private fun analyzeSankhya(pada: Pada, sup: dev.panini.vyakaranam.ast.SupPratyaya): AnalyzedSubanta {
        val semanticValue = requireNotNull(sankhyaValueResolver.resolve(pada)) {
            "सङ्ख्यायाः अर्थः न निर्धारितः: ${pada.sourceText}"
        }
        val subanta = SubantaPada(
            pada.sourceText,
            SankhyaPratipadika(pada.sourceText, semanticValue),
            sup,
        )
        return AnalyzedSubanta(
            pada = subanta,
            analysis = SubantaAnalysis(
                pada = subanta,
                lexicalEntry = null,
                supCandidates = SupAffix.candidates(sup.text).takeIf { it.isNotEmpty() }
                    ?: error("सुप्प्रत्ययस्य विवरणं न प्राप्तम्: ${sup.text}"),
                linga = emptySet(),
            ),
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
            supCandidates = SupAffix.candidates(pada.sup.text).takeIf { it.isNotEmpty() } ?: error(
                "सुप्प्रत्ययस्य विवरणं न प्राप्तम्: ${pada.sup.text}"
            ),
            linga = lexicalEntry?.linga ?: setOf(),
        )
    }

    fun analyzeTinganta(
        pada: TingantaPada,
    ): TingantaAnalysis {
        val dhatu = lexicon.findDhatu(pada.dhatu.mulaDhatu)
            ?: throw IllegalArgumentException(
                "धातुपाठे धातुः न प्राप्तः: ${pada.dhatu.mulaDhatu}",
            )

        val tingAffix = requireNotNull(TingAffix.fromUpadesha(pada.ting.text)) {
            "तिङ्प्रत्ययस्य विवरणं न प्राप्तम्: ${pada.ting.text}"
        }

        require(
            !validatePadaCompatibility ||
                pada.dhatu.sanadiPratyayas.isNotEmpty() ||
                dhatu.pada == null ||
                dhatu.pada == PadaType.UBHAYAPADA ||
                dhatu.pada == tingAffix.pada,
        ) {
            buildString {
                append("धातोः पदविरोधः: ")
                append(dhatu.upadesha)
                append(" धातुः ")
                append(dhatu.pada)
                append(" स्वीकरोति, किन्तु ")
                append(pada.ting.text)
                append(" ")
                append(tingAffix.pada)
                append(" सूचयति।")
            }
        }

        return TingantaAnalysis(
            pada = pada,
            lexicalEntry = dhatu,
            ting = tingAffix,
            lakara = pada.lakara,
        )
    }

    private fun lexicalPratipadikaText(
        pratipadika: Pratipadika,
    ): String? =
        when (pratipadika) {
            is MulaPratipadika -> pratipadika.text
            is SankhyaPratipadika -> pratipadika.sourceText
            is KridantaPratipadika -> null
            is UnadyantaPratipadika -> null
            is SamasaPratipadika -> null
        }
}
