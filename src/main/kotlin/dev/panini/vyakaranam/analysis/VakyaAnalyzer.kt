package dev.panini.vyakaranam.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Vakya

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

        val karakas = analyzeKarakas(
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

    private fun analyzeKarakas(
        subantas: List<SubantaAnalysis>,
        tinganta: TingantaAnalysis,
        prayoga: Prayoga,
    ): List<KarakaAssignment> {
        val dhatuSurface = tinganta.lexicalEntry?.sourceSurface ?: tinganta.pada.dhatu.mulaDhatu
        val profile = DhatuKarakaProfiles.forSurface(dhatuSurface)
        val allParticipants = subantas.mapIndexed { index, sub ->
            val possibleVibhaktis = sub.supCandidates.mapTo(mutableSetOf()) { it.vibhakti }
            val relations = profile?.relations.orEmpty()
            ParticipantFacts(
                id = "p_$index",
                expression = sub.pada,
                possibleVibhaktis = possibleVibhaktis,
                semanticRelations = relations,
            )
        }
        return subantas.mapIndexedNotNull { index, sub ->
            assignKaraka(sub, tinganta, prayoga, allParticipants[index], allParticipants)
        }
    }

    private fun assignKaraka(
        subanta: SubantaAnalysis,
        tinganta: TingantaAnalysis,
        prayoga: Prayoga,
        participant: ParticipantFacts,
        allParticipants: List<ParticipantFacts>,
    ): KarakaAssignment? {
        if (prayoga == Prayoga.BHAVE || prayoga == Prayoga.ANIRDHARITA) return null
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(
                    surface = tinganta.lexicalEntry?.sourceSurface ?: tinganta.pada.dhatu.mulaDhatu,
                    sakarmaka = tinganta.lexicalEntry?.karmatva != dev.panini.shiksha.Karmatva.AKARMAKA,
                ),
                participant = participant,
                allParticipants = allParticipants,
                prayoga = prayoga,
            ),
        )
        val karaka = resolution.resolved ?: return null
        return KarakaAssignment(
            pada = subanta.pada,
            karaka = karaka,
            confidence = if (resolution.evidence.any { it.sutra.startsWith("1.4.") }) 0.95 else 0.75,
            reason = resolution.evidence.joinToString(" ") { "${it.sutra} ${it.text}: ${it.reason}" }
                .ifEmpty { "एकमात्रं सम्भावितं कारकम्: $karaka" },
        )
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
