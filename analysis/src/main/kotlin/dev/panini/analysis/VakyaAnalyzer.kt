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
    private val grammarRegister: GrammarRegister = GrammarRegister.UNSPECIFIED,
) {

    fun analyze(
        vakya: Vakya,
        frameId: KriyaId = KriyaId("kriya-1"),
    ): KriyaFrame = analyze(vakya, frameId, VakyaSemanticContext())

    fun analyze(
        vakya: Vakya,
        frameId: KriyaId = KriyaId("kriya-1"),
        semanticContext: VakyaSemanticContext,
    ): KriyaFrame =
        when (vakya) {
            is AkhyataVakya -> analyzeAkhyataVakya(vakya, frameId, semanticContext)
            is NamaVakya -> analyzeNamaVakya(vakya, frameId, semanticContext)
        }

    private fun analyzeAkhyataVakya(
        vakya: AkhyataVakya,
        frameId: KriyaId,
        semanticContext: VakyaSemanticContext,
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

        semanticContext.validate(subantas.size, vakya.padas)
        val karakaAnalysis = analyzeKarakas(
            kriyaId = frameId,
            subantas = subantas,
            tinganta = tingantaAnalysis,
            prayoga = prayoga,
            semanticContext = semanticContext,
            coordinations = valencyCoordinations(padaAnalyses),
            writtenPadas = vakya.padas,
        )
        val relations = karakaAnalysis.relations
        val finiteRelations = relations.filterIndexed { index, _ ->
            index !in semanticContext.krtGovernors && index !in semanticContext.purposeGovernors
        }
        val warnings = agreementWarnings(
            prayoga = prayoga,
            coordinations = padaAnalyses.filterIsInstance<AnalyzedSamuccita>(),
            tinganta = tingantaAnalysis,
            relations = finiteRelations,
        )
        val qualifications = AdverbialAnalyzer.analyze(frameId, padaAnalyses)
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
            relations.forEachIndexed { nominalIndex, relation ->
                when (val resolution = relation.resolution) {
                    is FrameKarakaResolution.Ambiguous -> add(
                        FrameDiagnostic(
                            FrameDiagnosticCode.AMBIGUOUS_KARAKA,
                            ambiguousKarakaMessage(relation, resolution),
                            relation.participant.pada.sourceText,
                            nominalIndex,
                        ),
                    )
                    is FrameKarakaResolution.Unassigned -> add(
                        FrameDiagnostic(
                            FrameDiagnosticCode.UNASSIGNED_PARTICIPANT,
                            resolution.reason,
                            relation.participant.pada.sourceText,
                            nominalIndex,
                        ),
                    )
                    is FrameKarakaResolution.Resolved -> Unit
                }
                if (relation.unresolvedObjectMediators.isNotEmpty()) {
                    add(FrameDiagnostic(FrameDiagnosticCode.AMBIGUOUS_OBJECT_MEDIATION,
                        "Object mediation depends on unresolved participant readings: ${relation.unresolvedObjectMediators}.",
                        relation.participant.pada.sourceText, nominalIndex))
                }
                val optionalAlternatives = relation.alternatives.filter {
                    it.kind in setOf(KarakaAlternativeKind.OPTIONAL_DECLINE, KarakaAlternativeKind.OPTIONAL_APPLY)
                }
                val caseAlternatives = relation.alternatives.filter {
                    it.kind in setOf(KarakaAlternativeKind.OPTIONAL_VIBHAKTI, KarakaAlternativeKind.COEXISTENT_CASE)
                }
                val competingMeaningsWithoutCase = relation.resolution is FrameKarakaResolution.Ambiguous &&
                    relation.evidence.any { it.sutra.startsWith("1.4.") } &&
                    relation.alternatives.any { it.kind == KarakaAlternativeKind.COMPETING_ANALYSIS } &&
                    relation.alternatives.filter { it.kind == KarakaAlternativeKind.COMPETING_ANALYSIS }.none { it.matchesObservedMorphology }
                val optionalMeaningsWithoutCase = relation.resolution is FrameKarakaResolution.Ambiguous &&
                    optionalAlternatives.isNotEmpty() && optionalAlternatives.none { it.matchesObservedMorphology } &&
                    relation.evidence.lastOrNull()?.sutra?.startsWith("2.3.") != true
                if ((relation.resolution is FrameKarakaResolution.Resolved || competingMeaningsWithoutCase || optionalMeaningsWithoutCase) && relation.resolvedVibhakti == null &&
                    caseAlternatives.none { it.matchesObservedMorphology }) {
                    val cases = relation.participant.supCandidates.map { it.vibhakti }.distinct().joinToString("/")
                    val meaning = when (val result = relation.resolution) {
                        is FrameKarakaResolution.Resolved -> "${result.karaka.sanskritName} इति सम्बन्धः सिद्धः"
                        is FrameKarakaResolution.Ambiguous -> "${result.candidates.joinToString("/") { it.sanskritName }} इति सम्बन्धविकल्पाः सिद्धाः"
                        else -> error("Unvalidated case requires established meaning")
                    }
                    add(FrameDiagnostic(FrameDiagnosticCode.UNVALIDATED_VIBHAKTI,
                        "$meaning; $cases इति विभक्तेः विधानं न निर्धारितम्।",
                        relation.participant.pada.sourceText, nominalIndex))
                }
                if (caseAlternatives.isNotEmpty()) {
                    val ambiguous = relation.resolvedVibhakti == null &&
                        caseAlternatives.any { it.matchesObservedMorphology }
                    add(FrameDiagnostic(
                        if (ambiguous) FrameDiagnosticCode.AMBIGUOUS_VIBHAKTI else FrameDiagnosticCode.OPTIONAL_VIBHAKTI,
                        caseAlternatives.joinToString("; ") {
                            "${it.karaka.sanskritName}: ${it.vibhakti}; " +
                                if (it.matchesObservedMorphology) "observed morphology compatible" else "alternative form only"
                        },
                        relation.participant.pada.sourceText,
                        nominalIndex,
                    ))
                }
                if (optionalAlternatives.isNotEmpty()) {
                    val alternatives = optionalAlternatives.joinToString("; ") { alternative ->
                        "${alternative.karaka.sanskritName} (${alternative.vibhakti ?: "विभक्तिरनिर्धारिता"})"
                    }
                    add(FrameDiagnostic(
                        FrameDiagnosticCode.OPTIONAL_KARAKA,
                        "वैकल्पिककारकनिर्णयः: $alternatives।",
                        relation.participant.pada.sourceText,
                        nominalIndex,
                    ))
                }
                val competing = relation.alternatives.filter {
                    it.kind == KarakaAlternativeKind.COMPETING_ANALYSIS
                }
                if (competing.isNotEmpty()) {
                    val sutras = (relation.evidence.take(1) + competing.flatMap { it.evidence.take(1) })
                        .joinToString(" अथवा ") { it.sutra }
                    add(FrameDiagnostic(
                        FrameDiagnosticCode.COMPETING_KARAKA_ANALYSIS,
                        "प्रतिस्पर्धिनौ कारकविश्लेषणौ: $sutras; अर्थसन्दर्भेण निर्णयः अपेक्षितः।",
                        relation.participant.pada.sourceText,
                        nominalIndex,
                    ))
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
            relations = finiteRelations,
            qualifications = qualifications,
            nominalCaseLinks = karakaAnalysis.nominalCaseLinks,
            nominalKarakas = karakaAnalysis.nominalKarakas,
            purposeKarakas = karakaAnalysis.purposeKarakas,
            diagnostics = diagnostics,
            nominalRelations = NominalRelationAnalyzer.analyze(
                subantas,
                permitsPredication = tingantaAnalysis.pada.dhatu.mulaDhatu in setOf("अस्", "भू"),
            ),
        )
    }

    private fun ambiguousKarakaMessage(
        relation: KarakaRelation,
        resolution: FrameKarakaResolution.Ambiguous,
    ): String {
        val affixes = relation.participant.supCandidates
        val sharedUpadesha = affixes.map { it.upadesha }.distinct().singleOrNull()
        val sharedVacana = affixes.map { it.vacana }.distinct().singleOrNull()
        fun Vibhakti.sanskritName(): String = when (this) {
            Vibhakti.PRATHAMA -> "प्रथमा"
            Vibhakti.DVITIYA -> "द्वितीया"
            Vibhakti.TRTIYA -> "तृतीया"
            Vibhakti.CHATURTHI -> "चतुर्थी"
            Vibhakti.PANCHAMI -> "पञ्चमी"
            Vibhakti.SASTHI -> "षष्ठी"
            Vibhakti.SAPTAMI -> "सप्तमी"
        }
        fun Vacana.sanskritName(): String = when (this) {
            Vacana.EKAVACANA -> "एकवचनम्"
            Vacana.DVIVACANA -> "द्विवचनम्"
            Vacana.BAHUVACANA -> "बहुवचनम्"
        }
        val morphology = if (sharedUpadesha != null && sharedVacana != null) {
            val cases = affixes.map { it.vibhakti }.distinct().joinToString("/") { it.sanskritName() }
            "$sharedUpadesha ${sharedVacana.sanskritName()} $cases-साधारणः सुप्"
        } else {
            "सुप्-रूपम् अनेकविभक्तिसाधारणम्"
        }
        val roles = resolution.candidates.joinToString(" अथवा ") { it.sanskritName }
        return "$morphology; $roles इति सम्बन्धः सन्दर्भेण न निर्धारितः।"
    }

    private fun analyzeNamaVakya(
        vakya: NamaVakya,
        frameId: KriyaId,
        semanticContext: VakyaSemanticContext,
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

        semanticContext.validate(subantas.size, vakya.padas)
        require(semanticContext.krtGovernors.isEmpty()) { "Kṛt-governed case analysis in a verb-less clause is not yet supported." }
        require(semanticContext.avyayaGovernors.isEmpty()) { "Avyaya-governed case analysis in a verb-less clause is not yet supported." }
        require(semanticContext.purposeGovernors.isEmpty()) { "Purpose-action case analysis in a verb-less clause is not yet supported." }
        return KriyaFrame(
            id = frameId,
            vakya = vakya,
            kriya = null,
            prayoga = Prayoga.ANIRDHARITA,
            nominalRelations = NominalRelationAnalyzer.analyze(subantas, permitsPredication = true),
            nominalCaseLinks = NominalCaseLinkAnalyzer.analyze(subantas, subantas.mapIndexed { index, sub ->
                ParticipantFacts("p_$index", sub.pada, sub.supCandidates.mapTo(mutableSetOf()) { it.vibhakti },
                    semanticRelations = sub.lexicalEntry?.semanticRelations.orEmpty() + semanticContext.participantRelations[index].orEmpty())
            }),
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

    /** Ṇic makes even an originally akarmaka root syntactically transitive. */
    private fun isEffectivelySakarmaka(tinganta: TingantaAnalysis): Boolean =
        SanadiAffix.NIC in tinganta.pada.dhatu.sanadiAffixes ||
            tinganta.lexicalEntry?.karmatva != Karmatva.AKARMAKA

    private data class KarakaAnalysis(
        val relations: List<KarakaRelation>,
        val nominalCaseLinks: NominalCaseLinkAnalysis,
        val nominalKarakas: List<NominalKarakaRelation>,
        val purposeKarakas: List<PurposeKarakaRelation>,
    )

    /** Preserve parser-owned occurrences while recognizing a final च group. */
    private fun valencyCoordinations(padas: List<PadaAnalysis>): List<AnalyzedSamuccita> {
        val groups = padas.filterIsInstance<AnalyzedSamuccita>().toMutableList()
        padas.forEachIndexed { index, pada ->
            if (pada !is AnalyzedAvyaya || pada.pada.form != "च") return@forEachIndexed
            val members = mutableListOf<SubantaAnalysis>()
            var cases: Set<Vibhakti>? = null
            for (previous in padas.take(index).asReversed()) {
                val nominal = (previous as? AnalyzedSubanta)?.analysis ?: break
                val currentCases = nominal.supCandidates.mapTo(mutableSetOf()) { it.vibhakti }
                if (cases != null && cases != currentCases) break
                cases = currentCases
                members.add(0, nominal)
            }
            if (members.size >= 2) groups += AnalyzedSamuccita(
                dev.panini.vyakaranam.ast.SamuccitaSubanta(
                    members.joinToString(" ") { it.pada.sourceText } + " च", members.map { it.pada },
                ), members,
            )
        }
        return groups
    }

    private fun analyzeKarakas(
        kriyaId: KriyaId,
        subantas: List<SubantaAnalysis>,
        tinganta: TingantaAnalysis,
        prayoga: Prayoga,
        semanticContext: VakyaSemanticContext,
        coordinations: List<AnalyzedSamuccita>,
        writtenPadas: List<dev.panini.vyakaranam.ast.Pada>,
    ): KarakaAnalysis {
        semanticContext.validate(subantas.size)
        fun krtGovernor(reading: KrtGovernorReading): dev.panini.vyakaranam.ast.KridantaPratipadika {
            return (subantas[reading.governorIndex].pada as? dev.panini.vyakaranam.ast.SubantaPada)
                ?.pratipadika as? dev.panini.vyakaranam.ast.KridantaPratipadika
                ?: throw IllegalArgumentException("A kṛt governor link must reference a kṛdanta nominal occurrence.")
        }
        semanticContext.krtGovernors.values.forEach(::krtGovernor)
        val profile = tinganta.lexicalEntry?.let(DhatuKarakaProfiles::forDhatu)
        val possibleVibhaktisByParticipant = subantas.map { sub ->
            sub.supCandidates.mapTo(mutableSetOf()) { it.vibhakti }
        }
        val ownerGroups = subantas.indices.groupBy { index ->
            semanticContext.purposeGovernors[index]?.let { "purpose/${it.id}" }
                ?: semanticContext.krtGovernors[index]?.let { "krt/${it.governorIndex}" }
        }
        fun ownerRoot(indices: List<Int>): String =
            semanticContext.purposeGovernors[indices.first()]?.dhatu?.mulaDhatu
                ?: semanticContext.krtGovernors[indices.first()]?.let { krtGovernor(it).dhatu.mulaDhatu }
                ?: tinganta.pada.dhatu.mulaDhatu
        val unresolvedDvikarmakaIndices = ownerGroups.flatMap { (owner, indices) ->
            val root = ownerRoot(indices)
            val accusatives = indices.filter { possibleVibhaktisByParticipant[it] == setOf(Vibhakti.DVITIYA) }
            if (!KarakaDhatuDomains.isDvikarmaka(DhatuIdentity(root)) || accusatives.size <= 1) emptyList()
            else accusatives.filter { index ->
                (subantas[index].lexicalEntry?.semanticRelations.orEmpty() + semanticContext.participantRelations[index].orEmpty()).none {
                    it == SemanticRelation.DESIRED_OBJECT || it == SemanticRelation.SECONDARY_OBJECT
                }
            }
        }.toSet()
        val distributedDhatuRelations = mutableMapOf<Int, Set<SemanticRelation>>()
        ownerGroups.forEach { (owner, indices) ->
            val relationsForOwner = if (owner == null) profile?.relations.orEmpty()
                else if (indices.first() in semanticContext.purposeGovernors) emptySet()
                else semanticContext.krtGovernors[indices.first()]?.let { krtGovernor(it) }
                    ?.let { padaAnalyzer.findDhatu(it.dhatu) }?.let(DhatuKarakaProfiles::forDhatu)?.relations.orEmpty()
            val coordinatedIndices = coordinations.mapNotNull { coordination ->
                val members = coordination.members.map { member -> subantas.indexOfFirst { it.pada === member.pada } }
                if (members.isEmpty() || members.any { it !in indices }) null
                else members.mapTo(mutableSetOf()) { indices.indexOf(it) }
            }
            val distributed = ParticipantRelationInferrer.distributeDhatuRelations(
                indices.map { possibleVibhaktisByParticipant[it] }, relationsForOwner, coordinatedIndices,
                alternativeInstrumentCases = buildSet {
                    val root = DhatuIdentity(ownerRoot(indices))
                    if (KarakaDhatuDomains.isDiv(root)) add(dev.panini.core.Vibhakti.DVITIYA)
                    if (indices.any { index -> SemanticRelation.FIXED_TERM_HIRING_PAYMENT in
                            (subantas[index].lexicalEntry?.semanticRelations.orEmpty() +
                                semanticContext.participantRelations[index].orEmpty()) })
                        add(dev.panini.core.Vibhakti.CHATURTHI)
                },
            )
            indices.forEachIndexed { localIndex, index -> distributedDhatuRelations[index] = distributed[localIndex] }
        }
        val actionResultRelations = ActionNominalRelationEvidence.infer(subantas, semanticContext)
        val collectionRelations = CollectionMembershipEvidence.infer(subantas, writtenPadas, tinganta.pada, semanticContext)
        val allParticipants = subantas.mapIndexed { index, sub ->
            val possibleVibhaktis = possibleVibhaktisByParticipant[index]
            val reading = semanticContext.krtGovernors[index]
            val purposeReading = semanticContext.purposeGovernors[index]
            val inferredEvidence = ParticipantRelationInferrer.inferEvidence(
                lexicalEntry = sub.lexicalEntry,
                possibleVibhaktis = possibleVibhaktis,
                dhatuRelations = distributedDhatuRelations[index].orEmpty(),
                prayoga = if (reading == null && purposeReading == null) prayoga else Prayoga.ANIRDHARITA,
                sakarmaka = isEffectivelySakarmaka(tinganta),
                baseAkarmaka = tinganta.lexicalEntry?.karmatva == Karmatva.AKARMAKA,
            ).mapTo(mutableSetOf()) { evidence ->
                if (evidence.source != SemanticEvidenceSource.DHATU_VALENCY) evidence else {
                    val root = reading?.let { krtGovernor(it).dhatu.mulaDhatu } ?: tinganta.pada.dhatu.mulaDhatu
                    val owner = reading?.let { "$kriyaId/krt_${it.governorIndex}" } ?: kriyaId.toString()
                    evidence.copy(detail = "${evidence.detail} Governing root: $root; action owner: $owner.")
                }
            }
            val ambiguityEvidence = if (index in unresolvedDvikarmakaIndices) setOf(
                SemanticRelationEvidence(
                    SemanticRelation.AMBIGUOUS_DVIKARMAKA_OBJECT,
                    SemanticEvidenceSource.CONSTRUCTION_AMBIGUITY,
                    "Multiple unannotated accusatives occur with a dvikarmaka root.",
                ),
            ) else emptySet()
            val contextEvidence = semanticContext.participantRelations[index].orEmpty().map {
                SemanticRelationEvidence(it, SemanticEvidenceSource.CLAUSE_CONTEXT,
                    "Supplied clause-local premise for $kriyaId nominal occurrence p_$index.")
            }
            val intendedObject = if (!semanticContext.intendedObjects.containsKey(index)) null
                else semanticContext.intendedObjects[index]?.let { IntendedObject.Written("p_$it") } ?: IntendedObject.Implied
            val bindingEvidence = intendedObject?.let { setOf(SemanticRelationEvidence(
                SemanticRelation.RECIPIENT, SemanticEvidenceSource.CLAUSE_CONTEXT,
                "Supplied object-mediated recipient link for $kriyaId p_$index: $it.")) }.orEmpty()
            val purposeEvidence = purposeReading?.let { setOf(SemanticRelationEvidence(
                SemanticRelation.PURPOSE_ACTION_PARTICIPANT, SemanticEvidenceSource.CLAUSE_CONTEXT,
                "Supplied purpose-action binding ${it.id}: ${it.dhatu}; prefixes ${it.upasargas}; ${it::class.simpleName}.")) }.orEmpty()
            val semanticEvidence = inferredEvidence + ambiguityEvidence + contextEvidence + bindingEvidence + purposeEvidence +
                listOfNotNull(actionResultRelations[index], collectionRelations[index])
            ParticipantFacts(
                id = "p_$index",
                expression = sub.pada,
                possibleVibhaktis = possibleVibhaktis,
                semanticRelations = semanticEvidence.mapTo(mutableSetOf()) { it.relation },
                linga = sub.lexicalEntry?.linga.orEmpty(),
                vacana = sub.supCandidates.map { it.vacana }.distinct().singleOrNull(),
                categories = sub.lexicalEntry?.categories.orEmpty(),
                semanticEvidence = semanticEvidence,
                intendedObject = intendedObject,
                governingAvyaya = semanticContext.avyayaGovernors[index],
                purposeGovernor = purposeReading,
                governingNominal = reading?.let { subantas[it.governorIndex].pada as dev.panini.vyakaranam.ast.SubantaPada },
            )
        }
        val links = NominalCaseLinkAnalyzer.analyze(subantas, allParticipants,
            copularComparison = tinganta.pada.dhatu.mulaDhatu in setOf("अस्", "असँ") &&
                tinganta.pada.upasargas.isEmpty() && tinganta.pada.dhatu.sanadiAffixes.isEmpty())
        val linkedParticipants = allParticipants.mapIndexed { index, participant ->
            val link = links.established.singleOrNull { it.dependent == index }
                ?: return@mapIndexed participant
            val governor = subantas[link.governor].pada as? dev.panini.vyakaranam.ast.SubantaPada
                ?: return@mapIndexed participant
            participant.copy(governingNominal = governor,
                semanticRelations = participant.semanticRelations + link.derivedSemanticPremises,
                semanticEvidence = participant.semanticEvidence + (participant.semanticRelations.filter {
                    it in link.requiredSemanticPremises
                } + link.derivedSemanticPremises).map { SemanticRelationEvidence(it, SemanticEvidenceSource.GOVERNING_CONSTRUCTION,
                    when (it) {
                        SemanticRelation.PRONOMINAL_HETU -> "Eligible sarvanāma stem under 1.1.27 with actual हेतु governor p_${link.governor}."
                        SemanticRelation.EXPLICIT_HETU_USE -> "Actual written हेतु governor p_${link.governor} for ${link.sutra}; cause meaning independently established."
                        else -> "Unique nominal governor p_${link.governor} for ${link.sutra}; required semantic premises already established."
                    }) })
        }
        val hetuLinkedParticipants = linkedParticipants.mapIndexed { index, participant ->
            val incoming = links.established.filter {
                it.governor == index && it.sutra in setOf("2.3.26", "2.3.27")
            }
            if (incoming.isEmpty()) participant else {
                val derived = setOf(SemanticRelation.CAUSE_HETU, SemanticRelation.EXPLICIT_HETU_USE) +
                    if (incoming.any { it.sutra == "2.3.27" }) setOf(SemanticRelation.PRONOMINAL_HETU) else emptySet()
                participant.copy(semanticRelations = participant.semanticRelations + derived,
                    semanticEvidence = participant.semanticEvidence + derived.map {
                        SemanticRelationEvidence(it, SemanticEvidenceSource.GOVERNING_CONSTRUCTION,
                            "Written हेतु shares the selected cause construction with established dependent(s) ${incoming.map { it.dependent }}; case agreement remains required.")
                    })
            }
        }
        val allRelations = subantas.mapIndexed { index, sub ->
            val reading = semanticContext.krtGovernors[index]
            val purposeReading = semanticContext.purposeGovernors[index]
            val owner = purposeReading?.let { "purpose/${it.id}" } ?: reading?.let { "krt/${it.governorIndex}" }
            val scoped = ownerGroups.getValue(owner).map { hetuLinkedParticipants[it] }
            if (purposeReading != null) assignPurposeKaraka(KriyaId("$kriyaId/purpose_${purposeReading.id}"),
                sub, hetuLinkedParticipants[index], scoped, purposeReading)
            else assignKaraka(reading?.let { KriyaId("$kriyaId/krt_${it.governorIndex}") } ?: kriyaId,
                sub, tinganta, prayoga, hetuLinkedParticipants[index], scoped,
                reading, reading?.let(::krtGovernor))
        }
        val completeLinks = links.copy(jointAlternatives = NominalCaseLinkAnalyzer.jointAlternatives(links, allRelations))
        return KarakaAnalysis(allRelations, completeLinks, semanticContext.krtGovernors.map { (dependent, reading) ->
            NominalKarakaRelation(dependent, reading.governorIndex, allRelations[dependent])
        }, semanticContext.purposeGovernors.map { (dependent, governor) ->
            PurposeKarakaRelation(dependent, governor, allRelations[dependent])
        })
    }

    private fun assignPurposeKaraka(kriyaId: KriyaId, subanta: SubantaAnalysis, participant: ParticipantFacts,
        participants: List<ParticipantFacts>, governor: PurposeActionGovernor): KarakaRelation {
        val lexical = requireNotNull(padaAnalyzer.findDhatu(governor.dhatu)) { "Unknown purpose-action root: ${governor.dhatu.mulaDhatu}" }
        require(governor.upasargas.all { dev.panini.core.Upasarga.fromUpadesha(it) != null } &&
            governor.dhatu.sanadiAffixes.none { it == null }) { "Unknown purpose-action morphology cannot be ignored." }
        val sakarmaka = SanadiAffix.NIC in governor.dhatu.sanadiAffixes || lexical.karmatva == Karmatva.SAKARMAKA
        val candidates = if (sakarmaka && participant.hasEstablishedMeaning(SemanticRelation.DESIRED_OBJECT))
            setOf(Karaka.KARMAN) else setOf(Karaka.ANIRDHARITA)
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(governor.dhatu.mulaDhatu, sakarmaka),
            participant, participants, Prayoga.ANIRDHARITA, candidates = candidates, baseDhatu = lexical))
        return frameRelation(kriyaId, subanta, resolution)
    }

    private fun assignKaraka(
        kriyaId: KriyaId,
        subanta: SubantaAnalysis,
        tinganta: TingantaAnalysis,
        prayoga: Prayoga,
        participant: ParticipantFacts,
        allParticipants: List<ParticipantFacts>,
        krtReading: KrtGovernorReading? = null,
        krtGovernor: dev.panini.vyakaranam.ast.KridantaPratipadika? = null,
    ): KarakaRelation {
        if (prayoga == Prayoga.ANIRDHARITA) {
            return KarakaRelation(
                kriyaId,
                subanta,
                FrameKarakaResolution.Unassigned("Kāraka assignment is unavailable for $prayoga."),
            )
        }
        val krtDhatu = krtGovernor?.let { requireNotNull(padaAnalyzer.findDhatu(it.dhatu)) {
            "The linked kṛt governor's root is absent from the clause lexicon: ${it.dhatu.mulaDhatu}"
        } }
        val inferredEligibility = if (krtGovernor != null && krtReading?.expressedRoles != null &&
            krtGovernor.upasargas.isEmpty() && krtGovernor.dhatu.sanadiAffixes.none { it == null }) {
            val transitive = SanadiAffix.NIC in krtGovernor.dhatu.sanadiAffixes || krtDhatu?.karmatva == Karmatva.SAKARMAKA
            when {
                transitive -> setOf(Karaka.KARTR, Karaka.KARMAN) - krtReading.expressedRoles
                krtDhatu?.karmatva == Karmatva.AKARMAKA -> setOf(Karaka.KARTR) - krtReading.expressedRoles
                else -> null
            }
        } else null
        require(krtReading?.genitiveEligibleRoles == null || inferredEligibility == null ||
            krtReading.genitiveEligibleRoles == inferredEligibility) {
            "Supplied kṛt genitive eligibility contradicts the established derivation reading."
        }
        val eligibleRoles = krtReading?.genitiveEligibleRoles ?: inferredEligibility
        val scopedParticipant = if (inferredEligibility != null && krtReading?.genitiveEligibleRoles == null) participant.copy(
            semanticEvidence = participant.semanticEvidence + SemanticRelationEvidence(
                SemanticRelation.KRT_GOVERNED_PARTICIPANT, SemanticEvidenceSource.GOVERNING_CONSTRUCTION,
                "Genitive eligibility $inferredEligibility follows lexical root ${krtGovernor?.dhatu?.mulaDhatu} " +
                    "(${krtDhatu?.karmatva}) and selected expressed roles ${krtReading?.expressedRoles}; arguments may be implied."))
            else participant
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(
                    surface = krtGovernor?.dhatu?.mulaDhatu ?: tinganta.lexicalEntry?.sourceSurface ?: tinganta.pada.dhatu.mulaDhatu,
                    sakarmaka = if (krtGovernor == null) isEffectivelySakarmaka(tinganta) else
                        SanadiAffix.NIC in krtGovernor.dhatu.sanadiAffixes || krtDhatu?.karmatva != Karmatva.AKARMAKA,
                ),
                participant = scopedParticipant,
                allParticipants = allParticipants.map { if (it.id == scopedParticipant.id) scopedParticipant else it },
                prayoga = prayoga,
                verbNode = tinganta.pada.takeIf { krtGovernor == null },
                baseDhatu = if (krtGovernor == null) tinganta.lexicalEntry else krtDhatu,
                register = grammarRegister,
                governingKrt = krtGovernor,
                governingKrtExpressedRoles = krtReading?.expressedRoles,
                governingKrtOccurrenceId = krtReading?.let { kriyaId.value },
                governingKrtGenitiveEligibility = eligibleRoles?.let {
                    KrtGenitiveEligibility(kriyaId.value, it)
                },
            ),
        )
        return frameRelation(kriyaId, subanta, resolution)
    }

    private fun frameRelation(kriyaId: KriyaId, subanta: SubantaAnalysis, resolution: KarakaResolution): KarakaRelation {
        val resolvedKaraka = resolution.resolved
        val frameResolution = when {
            resolvedKaraka != null -> FrameKarakaResolution.Resolved(resolvedKaraka)
            resolution.candidates.size > 1 -> FrameKarakaResolution.Ambiguous(resolution.candidates)
            else -> FrameKarakaResolution.Unassigned(
                "Morphology proposed ${resolution.candidates.singleOrNull() ?: Karaka.ANIRDHARITA}, " +
                    "but no Aṣṭādhyāyī 1.4 kāraka-saṃjñā rule matched the semantic evidence.",
                resolution.candidates,
            )
        }
        return KarakaRelation(
            kriyaId = kriyaId,
            participant = subanta,
            resolution = frameResolution,
            evidence = resolution.evidence,
            alternatives = resolution.alternatives,
            rejectedRules = resolution.rejectedRules,
            resolvedVibhakti = resolution.resolvedVibhakti,
            nominalDesignations = resolution.nominalDesignations,
            additionalDesignations = resolution.additionalDesignations,
            unresolvedObjectMediators = resolution.unresolvedObjectMediators,
            primaryOptionalDecision = resolution.primaryOptionalDecision,
            coexistingKarakas = resolution.coexistingKarakas,
        )
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
        fun Vacana.participantCount(): Int = when (this) {
            Vacana.EKAVACANA -> 1
            Vacana.DVIVACANA -> 2
            Vacana.BAHUVACANA -> 3
        }
        fun countVacana(count: Int): Vacana = when (count) {
            1 -> Vacana.EKAVACANA
            2 -> Vacana.DVIVACANA
            else -> Vacana.BAHUVACANA
        }
        val expectedVacanas = coordination?.members?.fold(setOf(0)) { counts, member ->
            val memberCounts = member.supCandidates.mapTo(mutableSetOf()) { it.vacana.participantCount() }
            counts.flatMapTo(mutableSetOf()) { count -> memberCounts.map { count + it } }
        }?.mapTo(mutableSetOf(), ::countVacana)
            ?: controllerAnalysis.supCandidates.mapTo(mutableSetOf()) { it.vacana }

        if (tinganta.ting.vacana !in expectedVacanas) {
            return listOf(
                buildString {
                    append(if (controllerKaraka == Karaka.KARMAN)
                        "कर्मक्रियावचनयोः विरोधः: कर्म " else "कर्तृक्रियावचनयोः विरोधः: कर्ता ")
                    append(expectedVacanas.joinToString("/"))
                    append(", क्रिया ")
                    append(tinganta.ting.vacana)
                    append('।')
                },
            )
        }

        return emptyList()
    }
}
