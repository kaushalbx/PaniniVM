package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.shiksha.Karmatva
import dev.panini.unadipatha.analysis.UnadiAnalyzer
import dev.panini.unadipatha.analysis.UnadiStemAnalysis
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
    val unadiAnalyses: List<UnadiStemAnalysis> = emptyList(),
    val frames: List<KriyaFrame> = emptyList(),
)

class VakyaAnalyzer(
    private val padaAnalyzer: PadaAnalyzer,
) {

    fun analyze(
        vakya: Vakya,
        frameId: KriyaId = KriyaId("kriya-1"),
    ): VakyaAnalysis =
        when (vakya) {
            is AkhyataVakya -> analyzeAkhyataVakya(vakya, frameId)
            is NamaVakya -> analyzeNamaVakya(vakya)
        }

    private fun analyzeAkhyataVakya(
        vakya: AkhyataVakya,
        frameId: KriyaId,
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

        val relations = analyzeKarakas(
            kriyaId = frameId,
            subantas = subantas,
            tinganta = tingantaAnalysis,
            prayoga = prayoga,
        )
        val karakas = relations.mapNotNull { relation ->
            val resolved = relation.resolution as? FrameKarakaResolution.Resolved ?: return@mapNotNull null
            KarakaAssignment(
                pada = relation.participant.pada,
                karaka = resolved.karaka,
                confidence = if (relation.evidence.any { it.sutra.startsWith("1.4.") }) 0.95 else 0.75,
                reason = relation.evidence.joinToString(" ") { "${it.sutra} ${it.text}: ${it.reason}" }
                    .ifEmpty { "एकमात्रं सम्भावितं कारकम्: ${resolved.karaka}" },
            )
        }
        val warnings = agreementWarnings(
            subantas = subantas,
            tinganta = tingantaAnalysis,
            karakas = karakas,
        )
        val qualifications = analyzeQualifications(frameId, padaAnalyses)
        val diagnostics = buildList {
            if (tingantaAnalysis.lexicalEntry == null) {
                add(
                    FrameDiagnostic(
                        FrameDiagnosticCode.UNKNOWN_DHATU,
                        "The kriyā head could not be linked to a Dhātupāṭha entry.",
                        tingantaAnalysis.pada.sourceText,
                    ),
                )
            }
            relations.forEach { relation ->
                when (val resolution = relation.resolution) {
                    is FrameKarakaResolution.Ambiguous -> add(
                        FrameDiagnostic(
                            FrameDiagnosticCode.AMBIGUOUS_KARAKA,
                            "Participant has multiple kāraka candidates: ${resolution.candidates.joinToString()}.",
                            relation.participant.pada.sourceText,
                        ),
                    )
                    is FrameKarakaResolution.Unassigned -> add(
                        FrameDiagnostic(
                            FrameDiagnosticCode.UNASSIGNED_PARTICIPANT,
                            resolution.reason,
                            relation.participant.pada.sourceText,
                        ),
                    )
                    is FrameKarakaResolution.Resolved -> Unit
                }
            }
            warnings.forEach {
                add(FrameDiagnostic(FrameDiagnosticCode.AGREEMENT_MISMATCH, it, vakya.sourceText))
            }
        }
        val frame = KriyaFrame(
            id = frameId,
            vakya = vakya,
            kriya = KriyaHead(tingantaAnalysis, tingantaAnalysis.lexicalEntry),
            prayoga = prayoga,
            relations = relations,
            qualifications = qualifications,
            diagnostics = diagnostics,
        )

        val unadiAnalyses = subantas.mapNotNull { sub ->
            val stem = sub.lexicalEntry?.text ?: sub.pada.sourceText
            val result = UnadiAnalyzer.analyzeStem(stem)
            if (result.matches.isNotEmpty()) result else null
        }

        return VakyaAnalysis(
            vakya = vakya,
            padaAnalyses = padaAnalyses,
            prayoga = prayoga,
            karakas = karakas,
            warnings = warnings,
            unadiAnalyses = unadiAnalyses,
            frames = listOf(frame),
        )
    }

    private fun analyzeNamaVakya(
        vakya: NamaVakya,
    ): VakyaAnalysis {
        val analyses = vakya.padas.map(padaAnalyzer::analyze)

        val subantas = analyses.flatMap { analysis ->
            when (analysis) {
                is AnalyzedSubanta -> listOf(analysis.analysis)
                is AnalyzedSamuccita -> analysis.members
                else -> emptyList()
            }
        }

        val unadiAnalyses = subantas.mapNotNull { sub ->
            val stem = sub.lexicalEntry?.text ?: sub.pada.sourceText
            val result = UnadiAnalyzer.analyzeStem(stem)
            if (result.matches.isNotEmpty()) result else null
        }

        return VakyaAnalysis(
            vakya = vakya,
            padaAnalyses = analyses,
            prayoga = Prayoga.ANIRDHARITA,
            karakas = emptyList(),
            warnings = listOf(
                "नामवाक्ये अध्याहृतक्रियायाः पृथक् विश्लेषणम् अपेक्षितम्।",
            ),
            unadiAnalyses = unadiAnalyses,
        )
    }

    private fun inferPrayoga(
        tinganta: TingantaAnalysis,
    ): Prayoga {
        val text = tinganta.pada.sourceText
        val isCausative = text.contains("णिच्") || text.contains("इ") || text.contains("यि")
        if (isCausative) return Prayoga.CAUSATIVE

        val isKarmaniOrBhave = text.contains("यक्") || text.contains("चिण्")
        if (isKarmaniOrBhave) {
            val isAkarmaka = tinganta.lexicalEntry?.karmatva == Karmatva.AKARMAKA
            return if (isAkarmaka) Prayoga.BHAVE else Prayoga.KARMANI
        }

        return Prayoga.KARTARI
    }

    private fun analyzeKarakas(
        kriyaId: KriyaId,
        subantas: List<SubantaAnalysis>,
        tinganta: TingantaAnalysis,
        prayoga: Prayoga,
    ): List<KarakaRelation> {
        val dhatuSurface = tinganta.lexicalEntry?.sourceSurface ?: tinganta.pada.dhatu.mulaDhatu
        val profile = DhatuKarakaProfiles.forSurface(dhatuSurface)
        val allParticipants = subantas.mapIndexed { index, sub ->
            val possibleVibhaktis = sub.supCandidates.mapTo(mutableSetOf()) { it.vibhakti }
            val relations = ParticipantRelationInferrer.infer(
                lexicalEntry = sub.lexicalEntry,
                possibleVibhaktis = possibleVibhaktis,
                dhatuRelations = profile?.relations.orEmpty(),
            )
            ParticipantFacts(
                id = "p_$index",
                expression = sub.pada,
                possibleVibhaktis = possibleVibhaktis,
                semanticRelations = relations,
                linga = sub.lexicalEntry?.linga.orEmpty(),
                categories = sub.lexicalEntry?.categories.orEmpty(),
            )
        }
        return subantas.mapIndexed { index, sub ->
            assignKaraka(kriyaId, sub, tinganta, prayoga, allParticipants[index], allParticipants)
        }
    }

    private fun assignKaraka(
        kriyaId: KriyaId,
        subanta: SubantaAnalysis,
        tinganta: TingantaAnalysis,
        prayoga: Prayoga,
        participant: ParticipantFacts,
        allParticipants: List<ParticipantFacts>,
    ): KarakaRelation {
        if (prayoga == Prayoga.BHAVE || prayoga == Prayoga.ANIRDHARITA) {
            return KarakaRelation(
                kriyaId,
                subanta,
                FrameKarakaResolution.Unassigned("Kāraka assignment is unavailable for $prayoga."),
            )
        }
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(
                    surface = tinganta.lexicalEntry?.sourceSurface ?: tinganta.pada.dhatu.mulaDhatu,
                    sakarmaka = tinganta.lexicalEntry?.karmatva != Karmatva.AKARMAKA,
                ),
                participant = participant,
                allParticipants = allParticipants,
                prayoga = prayoga,
                verbNode = tinganta.pada,
                baseDhatu = tinganta.lexicalEntry,
            ),
        )
        val resolvedKaraka = resolution.resolved
        val frameResolution = when {
            resolvedKaraka != null -> FrameKarakaResolution.Resolved(resolvedKaraka)
            resolution.candidates.size > 1 -> FrameKarakaResolution.Ambiguous(resolution.candidates)
            else -> FrameKarakaResolution.Unassigned("No kāraka rule resolved this participant.")
        }
        return KarakaRelation(
            kriyaId = kriyaId,
            participant = subanta,
            resolution = frameResolution,
            evidence = resolution.evidence,
        )
    }

    private fun analyzeQualifications(
        kriyaId: KriyaId,
        analyses: List<PadaAnalysis>,
    ): List<KriyaQualification> =
        analyses.filterIsInstance<AnalyzedAvyaya>().map { analysis ->
            val form = analysis.pada.form
            val kind = when (form) {
                "मा" -> KriyaQualificationKind.NEGATION
                "कृपया" -> KriyaQualificationKind.COURTESY
                "पुनः", "वारम्", "सकृत्", "प्रत्येकम्" -> KriyaQualificationKind.FREQUENCY
                "भृशम्", "अत्यन्तम्" -> KriyaQualificationKind.INTENSITY
                "शीघ्रम्", "शनैः" -> KriyaQualificationKind.MANNER
                "यावत्", "तावत्" -> KriyaQualificationKind.TEMPORAL_EXTENT
                else -> KriyaQualificationKind.OTHER
            }
            KriyaQualification(kriyaId, analysis, kind, form)
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
