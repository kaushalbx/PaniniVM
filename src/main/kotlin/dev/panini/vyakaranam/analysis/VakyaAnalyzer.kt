package dev.panini.vyakaranam.analysis

import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.vyakaranam.ast.*

data class KarakaAssignment(
    val pada: SubantaPada,
    val karaka: Karaka,
    val confidence: Double,
    val reason: String,
)

data class VakyaAnalysis(
    val vakya: Vakya,
    val padaAnalyses: List<PadaAnalysis>,
    val prayoga: Prayoga,
    val karakas: List<KarakaAssignment>,
    val warnings: List<String>,
)

class VakyaAnalyzer(
    private val padaAnalyzer: PadaAnalyzer,
) {

    fun analyze(vakya: Vakya): VakyaAnalysis =
        when (vakya) {
            is AkhyataVakya -> analyzeAkhyataVakya(vakya)
            is NamaVakya -> analyzeNamaVakya(vakya)
        }

    private fun analyzeAkhyataVakya(
        vakya: AkhyataVakya,
    ): VakyaAnalysis {
        val padaAnalyses = vakya.padas.map(padaAnalyzer::analyze)

        val tingantaAnalysis =
            padaAnalyses
                .filterIsInstance<AnalyzedTinganta>()
                .single()
                .analysis

        val prayoga = inferPrayoga(tingantaAnalysis)

        val subantas = padaAnalyses.flatMap { analysis ->
            when (analysis) {
                is AnalyzedSubanta -> listOf(analysis.analysis)
                is AnalyzedSamuccita -> analysis.members
                else -> emptyList()
            }
        }

        val karakas = assignKarakas(
            subantas = subantas,
            tinganta = tingantaAnalysis,
            prayoga = prayoga,
        )

        return VakyaAnalysis(
            vakya = vakya,
            padaAnalyses = padaAnalyses,
            prayoga = prayoga,
            karakas = karakas,
            warnings = agreementWarnings(
                subantas = subantas,
                tinganta = tingantaAnalysis,
                karakas = karakas,
            ),
        )
    }

    private fun analyzeNamaVakya(
        vakya: NamaVakya,
    ): VakyaAnalysis {
        val analyses = vakya.padas.map(padaAnalyzer::analyze)

        return VakyaAnalysis(
            vakya = vakya,
            padaAnalyses = analyses,
            prayoga = Prayoga.ANIRDHARITA,
            karakas = emptyList(),
            warnings = listOf(
                "नामवाक्ये अध्याहृतक्रियायाः पृथक् विश्लेषणम् अपेक्षितम्।",
            ),
        )
    }

    private fun inferPrayoga(
        tinganta: TingantaAnalysis,
    ): Prayoga {
        /*
         * Ātmanepada does not by itself mean karmani-prayoga.
         *
         * The real decision must be made from:
         * - lakāra
         * - yak/cin or other derivational markers
         * - dhātu properties
         * - derivation trace
         *
         * Therefore the basic segmented input cannot always determine
         * prayoga conclusively.
         */
        return Prayoga.KARTARI
    }

    private fun assignKarakas(
        subantas: List<SubantaAnalysis>,
        tinganta: TingantaAnalysis,
        prayoga: Prayoga,
    ): List<KarakaAssignment> =
        subantas.mapNotNull { subanta ->
            when (prayoga) {
                Prayoga.KARTARI ->
                    assignKartariKaraka(subanta, tinganta)

                Prayoga.KARMANI ->
                    assignKarmaniKaraka(subanta)

                Prayoga.BHAVE,
                Prayoga.ANIRDHARITA,
                    -> null
            }
        }

    private fun assignKartariKaraka(
        subanta: SubantaAnalysis,
        tinganta: TingantaAnalysis,
    ): KarakaAssignment? =
        when (KarakaInference.infer(subanta.sup.vibhakti, Prayoga.KARTARI, tinganta.lexicalEntry?.sakarmaka == true)) {
            Karaka.KARTR ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.KARTR,
                    confidence = 0.90,
                    reason = "कर्तरिप्रयोगे प्रथमा तथा तिङन्तसामञ्जस्यम्।",
                )

            Karaka.KARMAN -> KarakaAssignment(
                pada = subanta.pada,
                karaka = Karaka.KARMAN,
                confidence = 0.85,
                reason = "सकर्मकधातोः द्वितीयान्तपदम्।",
            )

            Karaka.KARANA ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.KARANA,
                    confidence = 0.65,
                    reason = "तृतीयाविभक्तेः सामान्यं करणसम्बन्धम्।",
                )

            Karaka.SAMPRADANA ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.SAMPRADANA,
                    confidence = 0.70,
                    reason = "चतुर्थीविभक्तेः सामान्यं सम्प्रदानसम्बन्धम्।",
                )

            Karaka.APADANA ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.APADANA,
                    confidence = 0.70,
                    reason = "पञ्चमीविभक्तेः सामान्यं अपादानसम्बन्धम्।",
                )

            Karaka.SAMBANDHA ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.SAMBANDHA,
                    confidence = 0.90,
                    reason = "षष्ठी सामान्यतः सम्बन्धं सूचयति, कारकं न।",
                )

            Karaka.ADHIKARANA ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.ADHIKARANA,
                    confidence = 0.70,
                    reason = "सप्तमीविभक्तेः सामान्यं अधिकरणसम्बन्धम्।",
                )

            else -> null
        }

    private fun assignKarmaniKaraka(
        subanta: SubantaAnalysis,
    ): KarakaAssignment? =
        when (KarakaInference.infer(subanta.sup.vibhakti, Prayoga.KARMANI)) {
            Karaka.KARMAN ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.KARMAN,
                    confidence = 0.80,
                    reason = "कर्मणिप्रयोगे अभिहितं कर्म प्रथमान्तं भवति।",
                )

            Karaka.KARTR ->
                KarakaAssignment(
                    pada = subanta.pada,
                    karaka = Karaka.KARTR,
                    confidence = 0.80,
                    reason = "कर्मणिप्रयोगे अनभिहितः कर्ता तृतीयान्तः भवति।",
                )

            else -> null
        }

    private fun agreementWarnings(
        subantas: List<SubantaAnalysis>,
        tinganta: TingantaAnalysis,
        karakas: List<KarakaAssignment>,
    ): List<String> {
        val karta = karakas.firstOrNull {
            it.karaka == Karaka.KARTR
        } ?: return emptyList()

        val kartaAnalysis =
            subantas.firstOrNull {
                it.pada == karta.pada
            } ?: return emptyList()

        if (kartaAnalysis.sup.vacana != tinganta.ting.vacana) {
            return listOf(
                buildString {
                    append("कर्तृक्रियावचनयोः विरोधः: कर्ता ")
                    append(kartaAnalysis.sup.vacana)
                    append(", क्रिया ")
                    append(tinganta.ting.vacana)
                    append('।')
                },
            )
        }

        return emptyList()
    }
}
