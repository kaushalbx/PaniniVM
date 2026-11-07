package dev.panini.analysis

import dev.panini.ashtadhyayi.adhyaya2.pada3.CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.TulyarthairAtulopamabhyamTrtiyanatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.DurantikarthaihSasthiAnyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.AnyaraditarartedikshabdaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.NamahSvastiSvahaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.SasthiHetuprayogeSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.SarvanamnasTrtiyaCaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SarvanamaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.PancamiVibhakteSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.AyuktaKusalabhyamCaSevayamSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.PrasitotsukabhyamTrtiyaCaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.SadhuNipunabhyamSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.SwamyIsvaraAdhipatiSutra
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.isRecognizedStriAffix

/** Indices address the same flattened nominal list as frame.nominalRelations. */
data class NominalCaseLinkCandidate(
    val dependent: Int,
    val governor: Int,
    val sutra: String,
    val compatibleCases: Set<Vibhakti>,
    val requiredSemanticPremises: Set<SemanticRelation>,
    /** Conditions still needed before this conditional attachment may be used. */
    val missingSemanticPremises: Set<SemanticRelation>,
    /** Facts proved by the actual governor/category, rather than caller assertions. */
    val derivedSemanticPremises: Set<SemanticRelation> = emptySet(),
)

/** A case option requires reinflecting every listed nominal occurrence together. */
data class JointNominalCaseAlternative(
    val members: Set<Int>,
    val vibhakti: Vibhakti,
    val evidence: List<KarakaEvidence>,
) {
    init { require(members.size >= 2 && members.all { it >= 0 }) }
}

data class NominalCaseLinkAnalysis(
    val candidates: List<NominalCaseLinkCandidate>,
    val jointAlternatives: List<JointNominalCaseAlternative> = emptyList(),
) {
    /** Mere uniqueness of a case-compatible word is insufficient evidence. */
    val established: List<NominalCaseLinkCandidate> = candidates.groupBy { it.dependent }.values
        .mapNotNull { readings -> readings.filter { it.missingSemanticPremises.isEmpty() }.singleOrNull() }
}

object NominalCaseLinkAnalyzer {
    fun jointAlternatives(links: NominalCaseLinkAnalysis, relations: List<KarakaRelation>): List<JointNominalCaseAlternative> =
        links.established.groupBy { it.governor }.mapNotNull { (governor, incoming) ->
            // The complete linked group must support the optional rule. A
            // mandatory-genitive member prevents an isolated pair switch.
            if (incoming.any { it.sutra != "2.3.27" }) return@mapNotNull null
            val members = (incoming.map { it.dependent } + governor).toSet()
            val group = members.map { relations[it] }
            val current = group.map { it.resolvedVibhakti }.distinct().singleOrNull()
                ?: return@mapNotNull null
            if (group.any { it.evidence.lastOrNull()?.sutra != "2.3.27" }) return@mapNotNull null
            val alternative = (SarvanamnasTrtiyaCaSutra.permittedCases - current).singleOrNull()
                ?: return@mapNotNull null
            JointNominalCaseAlternative(members, alternative, listOf(KarakaEvidence(
                "2.3.27", SarvanamnasTrtiyaCaSutra.text,
                "Reinflect all nominal occurrences ${members.sorted()} together; the currently written partner cases are not retained.",
                optional = true, applied = false,
                semanticEvidence = group.flatMap { it.evidence }.flatMap { it.semanticEvidence }.toSet(),
            )))
        }

    private data class Construction(
        val rule: Sutra<VibhaktiRuleContext, VibhaktiRuleResult>,
        val premises: Set<SemanticRelation>,
        val derived: Set<SemanticRelation> = emptySet(),
        val eligible: (ParticipantFacts) -> Boolean = { true },
        val requiresCompatibleCase: Boolean = true,
        val candidateCases: ((VibhaktiRuleContext) -> Set<Vibhakti>)? = null,
    )
    private val constructions = listOf(
        Construction(SasthiHetuprayogeSutra, setOf(SemanticRelation.CAUSE_HETU),
            setOf(SemanticRelation.EXPLICIT_HETU_USE), { isPronominal(it) == false }, false, SasthiHetuprayogeSutra::candidateCases),
        Construction(SarvanamnasTrtiyaCaSutra, setOf(SemanticRelation.CAUSE_HETU),
            setOf(SemanticRelation.EXPLICIT_HETU_USE, SemanticRelation.PRONOMINAL_HETU), { isPronominal(it) == true }, false, SarvanamnasTrtiyaCaSutra::candidateCases),
        Construction(NamahSvastiSvahaSutra, setOf(SemanticRelation.SUFFICIENCY)),
        Construction(AnyaraditarartedikshabdaSutra,
            setOf(SemanticRelation.DIRECTIONAL_EXCLUSION)),
        Construction(DurantikarthaihSasthiAnyatarasyamSutra,
            setOf(SemanticRelation.DISTANCE_OR_PROXIMITY)),
        Construction(TulyarthairAtulopamabhyamTrtiyanatarasyamSutra,
            setOf(SemanticRelation.EQUAL_COMPARISON, SemanticRelation.SESHA_RELATION)),
        Construction(PancamiVibhakteSutra, setOf(SemanticRelation.COMPARATIVE_DISTINCTION,
            SemanticRelation.NIRDHARANA_REFERENCE, SemanticRelation.SEPARATE_COMPARISON_GROUPS)),
        Construction(CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra,
            setOf(SemanticRelation.BENEDICTION_WELLBEING, SemanticRelation.SESHA_RELATION)),
        Construction(SwamyIsvaraAdhipatiSutra, emptySet()),
        Construction(AyuktaKusalabhyamCaSevayamSutra, setOf(SemanticRelation.ENGROSSED_ATTACHMENT)),
        Construction(SadhuNipunabhyamSutra, setOf(SemanticRelation.ARCHA_RESPECT)),
        Construction(PrasitotsukabhyamTrtiyaCaSutra, setOf(SemanticRelation.ENGROSSED_ATTACHMENT)),
    )

    private fun isPronominal(participant: ParticipantFacts): Boolean? {
        if (SemanticRelation.PRONOMINAL_HETU in participant.semanticRelations) return true
        val stem = (participant.expression as? SubantaPada)?.pratipadika as? MulaPratipadika ?: return false
        if (!stem.vikaras.all { it.isRecognizedStriAffix }) return false
        if (SarvanamaSutra.isEligibleStem(stem.text)) return true
        return if (SarvanamaSutra.isListedStem(stem.text)) null else false
    }

    fun analyze(nominals: List<SubantaAnalysis>, participants: List<ParticipantFacts>,
        copularComparison: Boolean = false): NominalCaseLinkAnalysis {
        require(nominals.size == participants.size)
        val equalHeads = setOf("सम", "समान", "तुल्य", "सदृश")
        val orderHeads = setOf("न्यून", "अधिक")
        fun headStem(index: Int): String? {
            val stem = (nominals[index].pada as? SubantaPada)?.pratipadika as? MulaPratipadika ?: return null
            return stem.text.takeIf { stem.vikaras.all { it.isRecognizedStriAffix } }
        }
        val comparisonHead = if (!copularComparison) null else nominals.indices.filter {
            Vibhakti.PRATHAMA in participants[it].possibleVibhaktis && headStem(it) in equalHeads + orderHeads
        }.singleOrNull()
        val comparisonRule = comparisonHead?.let { if (headStem(it) in equalHeads) "2.3.72" else "2.3.42" }
        val standardCases = if (comparisonRule == "2.3.72") setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI)
            else setOf(Vibhakti.PANCHAMI)
        // In a comparison reading, an agreeing ordinal/property with one noun
        // head belongs to that nominal phrase, rather than adding an operand.
        fun isModifier(index: Int): Boolean = nominals[index].let {
            it.lexicalEntry?.nominalUse in setOf(NominalUse.MODIFIER, NominalUse.ENTITY_OR_MODIFIER) ||
                it.pada is dev.panini.vyakaranam.ast.SankhyaPuranaPada ||
                headStem(index)?.let(::isOrdinalWord) == true
        }
        val comparisonModifiers = if (comparisonHead == null) emptySet() else nominals.indices.filterTo(mutableSetOf()) { modifier ->
            modifier != comparisonHead && isModifier(modifier) && nominals.indices.filter { head ->
                head != modifier && head != comparisonHead && !isModifier(head) &&
                    nominals[modifier].supCandidates.any { m -> nominals[head].supCandidates.any { h ->
                        m.vibhakti == h.vibhakti && m.vacana == h.vacana
                    } } && (nominals[modifier].linga.isEmpty() || nominals[head].linga.isEmpty() ||
                    headStem(modifier)?.let(::isOrdinalWord) == true ||
                    nominals[modifier].linga.intersect(nominals[head].linga).isNotEmpty())
            }.size == 1
        }
        val comparisonSubject = comparisonHead?.let { head -> participants.indices.filter {
            it != head && it !in comparisonModifiers && Vibhakti.PRATHAMA in participants[it].possibleVibhaktis
        }.singleOrNull() }
        val resultHeads = nominals.indices.filter { index ->
            val stem = (nominals[index].pada as? SubantaPada)?.pratipadika as? MulaPratipadika
            stem?.lexicalIdentity == dev.panini.vyakaranam.ast.MulaPratipadikaIdentity.PHALA && stem.vikaras.isEmpty()
        }
        val resultAction = if (resultHeads.size != 1) null else nominals.indices.filter {
            Vibhakti.SASTHI in participants[it].possibleVibhaktis &&
                (nominals[it].pada as? SubantaPada)?.pratipadika is dev.panini.vyakaranam.ast.KridantaPratipadika
        }.singleOrNull()
        val comparisonStandard = if (comparisonSubject == null) null else participants.indices.filter {
            it != comparisonHead && it != comparisonSubject && it != resultAction && it !in comparisonModifiers &&
                participants[it].possibleVibhaktis.any(standardCases::contains) &&
                listOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.DESIRED_OBJECT,
                    SemanticRelation.INSTRUMENT, SemanticRelation.SOURCE, SemanticRelation.RECIPIENT,
                    SemanticRelation.LOCATION).none(participants[it]::hasEstablishedMeaning)
        }.singleOrNull()
        return NominalCaseLinkAnalysis(buildList {
            participants.forEachIndexed { dependent, participant ->
                if (SemanticRelation.DIRECT_ADDRESS in participant.semanticRelations || participant.governingAvyaya != null || participant.purposeGovernor != null) return@forEachIndexed
                nominals.forEachIndexed governorLoop@{ governor, nominal ->
                    if (dependent == governor) return@governorLoop
                    val governorPada = nominal.pada as? SubantaPada ?: return@governorLoop
                    // An existing explicit dependency must not be rebound by discovery.
                    if (participant.governingNominal != null && participant.governingNominal !== governorPada) return@governorLoop
                    for (construction in constructions) {
                        if (construction.rule === PancamiVibhakteSutra &&
                            (!copularComparison || headStem(governor) !in orderHeads)) continue
                        if (!construction.eligible(participant)) continue
                        val derived = construction.derived + if (dependent == comparisonStandard &&
                            governor == comparisonHead && construction.rule.number == comparisonRule)
                            construction.premises else emptySet()
                        val proposed = participant.copy(governingNominal = governorPada,
                            semanticRelations = participant.semanticRelations + construction.premises + construction.derived)
                        val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, participant.possibleVibhaktis, participant = proposed)
                        val allowed = construction.candidateCases?.invoke(context) ?: Vibhakti.entries.filterTo(mutableSetOf()) {
                            construction.rule.matches(context.copy(morphologicalCandidates = setOf(it)))
                        }
                        if (allowed.isEmpty() || (construction.requiresCompatibleCase && participant.possibleVibhaktis.intersect(allowed).isEmpty())) continue
                        add(NominalCaseLinkCandidate(dependent, governor, construction.rule.number,
                            participant.possibleVibhaktis.intersect(allowed).let {
                                if (construction.candidateCases == null) it else it.intersect(nominal.supCandidates.map { sup -> sup.vibhakti }.toSet())
                            },
                            construction.premises,
                            construction.premises - participant.semanticRelations - derived,
                            derived - participant.semanticRelations))
                    }
                }
            }
        })
    }
}
