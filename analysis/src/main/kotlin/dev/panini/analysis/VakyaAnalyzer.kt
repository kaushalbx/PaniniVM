package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.SanadiAffix
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.core.Purusha
import dev.panini.vyakaranam.ast.Vikarana
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.Vakya

class VakyaAnalyzer(
    private val padaAnalyzer: PadaAnalyzer,
) {

    fun analyze(
        vakya: Vakya,
        frameId: KriyaId = KriyaId("kriya-1"),
    ): KriyaFrame =
        when (vakya) {
            is AkhyataVakya -> analyzeAkhyataVakya(vakya, frameId)
            is NamaVakya -> analyzeNamaVakya(vakya, frameId)
        }

    private fun analyzeAkhyataVakya(
        vakya: AkhyataVakya,
        frameId: KriyaId,
    ): KriyaFrame {
        val padaAnalyses = vakya.padas.map(padaAnalyzer::analyze)

        val tingantaAnalysis =
            padaAnalyses
                .filterIsInstance<AnalyzedTinganta>()
                .single()
                .analysis

        val prayoga = inferPrayoga(tingantaAnalysis)

        val subantas = buildList {
            for (analysis in padaAnalyses) {
                when (analysis) {
                    is AnalyzedSubanta -> add(analysis.analysis)
                    is AnalyzedSamuccita -> addAll(analysis.members)
                    else -> Unit
                }
            }
        }

        val relations = analyzeKarakas(
            kriyaId = frameId,
            subantas = subantas,
            tinganta = tingantaAnalysis,
            prayoga = prayoga,
        )
        val warnings = agreementWarnings(
            prayoga = prayoga,
            coordinations = padaAnalyses.filterIsInstance<AnalyzedSamuccita>(),
            tinganta = tingantaAnalysis,
            relations = relations,
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
        return KriyaFrame(
            id = frameId,
            vakya = vakya,
            kriya = KriyaHead(tingantaAnalysis, tingantaAnalysis.lexicalEntry),
            prayoga = prayoga,
            relations = relations,
            qualifications = qualifications,
            diagnostics = diagnostics,
        )
    }

    private fun analyzeNamaVakya(
        vakya: NamaVakya,
        frameId: KriyaId,
    ): KriyaFrame {
        val padaAnalyses = vakya.padas.map(padaAnalyzer::analyze)

        val subantas = buildList {
            for (analysis in padaAnalyses) {
                when (analysis) {
                    is AnalyzedSubanta -> add(analysis.analysis)
                    is AnalyzedSamuccita -> addAll(analysis.members)
                    else -> Unit
                }
            }
        }

        return KriyaFrame(
            id = frameId,
            vakya = vakya,
            kriya = null,
            prayoga = Prayoga.ANIRDHARITA,
            relations = emptyList(),
            qualifications = emptyList(),
            diagnostics = listOf(
                FrameDiagnostic(
                    FrameDiagnosticCode.UNCLASSIFIED_PADA,
                    "Nāma-vākya acknowledged without finite verb processing.",
                    vakya.sourceText,
                ),
            ),
        )
    }

    private fun inferPrayoga(
        tinganta: TingantaAnalysis,
    ): Prayoga {
        val isCausative = tinganta.pada.dhatu.sanadiPratyayas.any {
            SanadiAffix.fromUpadesha(it) == SanadiAffix.NIC
        }
        val isKarmaniOrBhave = tinganta.pada.vikarana == Vikarana.YAK
        if (isKarmaniOrBhave) {
            // A causative introduces an object even when its base root is
            // intransitive. Sanadi morphology remains on the verb AST; it must
            // not override the passive voice selected by yak.
            val isAkarmaka = !isCausative && tinganta.lexicalEntry?.karmatva == Karmatva.AKARMAKA
            return if (isAkarmaka) Prayoga.BHAVE else Prayoga.KARMANI
        }
        if (isCausative) return Prayoga.CAUSATIVE

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
        if (prayoga == Prayoga.ANIRDHARITA) {
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
                "पुनः", "पुनर्", "वारम्", "सकृत्", "प्रत्येकम्" -> KriyaQualificationKind.FREQUENCY
                "भृशम्", "अत्यन्तम्" -> KriyaQualificationKind.INTENSITY
                "शीघ्रम्", "शनैः" -> KriyaQualificationKind.MANNER
                "यावत्", "तावत्" -> KriyaQualificationKind.TEMPORAL_EXTENT
                else -> KriyaQualificationKind.OTHER
            }
            KriyaQualification(kriyaId, analysis, kind, form)
        }

    private fun agreementWarnings(
        prayoga: Prayoga,
        coordinations: List<AnalyzedSamuccita>,
        tinganta: TingantaAnalysis,
        relations: List<KarakaRelation>,
    ): List<String> {
        if (tinganta.pada.priorAction != null) return emptyList()
        if (prayoga == Prayoga.BHAVE) {
            return if (tinganta.ting.vacana == Vacana.EKAVACANA &&
                tinganta.ting.purusha == Purusha.PRATHAMA
            ) emptyList() else listOf(
                "भावे प्रथमपुरुषैकवचनम् अपेक्षितम्: क्रिया ${tinganta.ting.purusha}, ${tinganta.ting.vacana}।",
            )
        }
        // In karmani, the expressed nominative object controls agreement, not
        // the instrumental agent or an accusative secondary object.
        val controllerKaraka = when (prayoga) {
            Prayoga.KARMANI -> Karaka.KARMAN
            Prayoga.KARTARI, Prayoga.CAUSATIVE -> Karaka.KARTR
            else -> return emptyList()
        }
        fun controlsAgreement(relation: KarakaRelation): Boolean =
            (relation.resolution as? FrameKarakaResolution.Resolved)?.karaka == controllerKaraka &&
                (prayoga != Prayoga.KARMANI ||
                    relation.participant.supCandidates.map { it.vibhakti }.toSet() == setOf(Vibhakti.PRATHAMA))
        val controllerRelation = relations.firstOrNull(::controlsAgreement) ?: return emptyList()

        val controllerAnalysis = controllerRelation.participant
        val coordination = coordinations.firstOrNull { group ->
            controllerAnalysis in group.members && group.members.all { member ->
                relations.any { relation ->
                    relation.participant == member &&
                        controlsAgreement(relation)
                }
            }
        }
        // Count only an explicit, fully resolved coordination, not adjacent nouns
        // which may instead be appositions or have different grammatical roles.
        val expectedVacana = coordination?.members?.sumOf { member ->
            when (member.sup.vacana) {
                Vacana.EKAVACANA -> 1
                Vacana.DVIVACANA -> 2
                Vacana.BAHUVACANA -> 3
            }
        }?.let { count ->
            when (count) {
                1 -> Vacana.EKAVACANA
                2 -> Vacana.DVIVACANA
                else -> Vacana.BAHUVACANA
            }
        } ?: controllerAnalysis.sup.vacana

        if (expectedVacana != tinganta.ting.vacana) {
            return listOf(
                buildString {
                    append(if (controllerKaraka == Karaka.KARMAN)
                        "कर्मक्रियावचनयोः विरोधः: कर्म " else "कर्तृक्रियावचनयोः विरोधः: कर्ता ")
                    append(expectedVacana)
                    append(", क्रिया ")
                    append(tinganta.ting.vacana)
                    append('।')
                },
            )
        }

        return emptyList()
    }
}
