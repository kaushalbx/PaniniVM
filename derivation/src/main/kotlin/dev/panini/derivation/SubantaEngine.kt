package dev.panini.derivation

import dev.panini.core.SupAffix
import dev.panini.core.Lakara
import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.analysis.KarakaRuleEngine
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.DhatuIdentity
import dev.panini.analysis.DhatuKarakaProfiles
import dev.panini.analysis.ParticipantFacts
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.SemanticRelationEvidence
import dev.panini.analysis.SemanticEvidenceSource
import dev.panini.core.Karaka
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingPratyaya
import dev.panini.sankhya.PrimitiveSankhya

class SubantaEngine(
    private val engine: DerivationEngine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras),
) {
    fun derive(request: SubantaDerivationRequest): DerivationResult {
        val plan = requireNotNull(SubantaFormPlans.find(request.vibhakti, request.vacana)) {
            "No complete downstream plan exists for ${SupAffix.select(request.vibhakti, request.vacana).upadesha}."
        }
        return engine.derive(request.initialState()).apply {
            verifyDerivation("4.1.2", plan.affix.upadesha, plan.requiredSutras, plan.finalStage)
        }
    }

    /** Preserve distinct grammatical alternatives and validate every returned derivation. */
    fun deriveAll(request: SubantaDerivationRequest): List<DerivationResult> {
        val plan = requireNotNull(SubantaFormPlans.find(request.vibhakti, request.vacana)) {
            "No complete downstream plan exists for ${SupAffix.select(request.vibhakti, request.vacana).upadesha}."
        }
        return engine.deriveAll(request.initialState()).onEach {
            it.verifyDerivation("4.1.2", plan.affix.upadesha, plan.requiredSutras, plan.finalStage)
        }
    }

    fun deriveSupportedParadigm(
        pratipadika: String,
        linga: Linga = Linga.PUMS,
    ): SubantaParadigm = SubantaParadigm(
        pratipadika = pratipadika,
        linga = linga,
        forms = SubantaFormPlans.all().associate { plan ->
            plan.affix to try {
                derive(SubantaDerivationRequest(pratipadika, plan.affix.vibhakti, plan.affix.vacana, linga))
            } catch (exception: IllegalArgumentException) {
                throw IllegalArgumentException("Failed to derive ${plan.affix}: ${exception.message}", exception)
            }
        },
    )

    fun deriveFromKaraka(request: KarakaSubantaDerivationRequest): DerivationResult {
        val allVibhaktis = Vibhakti.entries.toSet()
        val profile = DhatuKarakaProfiles.forSurface(request.dhatu)
        val upapadaRelations: Set<SemanticRelation> = when (request.upapada?.lowercase()) {
            "सह", "साकम्", "सार्धम्", "समम्", "saha", "sakam", "sardham", "samam" -> setOf(SemanticRelation.ACCOMPANIMENT)
            else -> emptySet()
        }
        val relations = request.semanticRelations ?: (profile?.relations.orEmpty() + upapadaRelations)
        val requestedMeaning = when (request.karaka) {
            Karaka.KARTR -> SemanticRelation.INDEPENDENT_AGENT
            Karaka.KARMAN -> SemanticRelation.DESIRED_OBJECT
            Karaka.KARANA -> SemanticRelation.INSTRUMENT
            Karaka.SAMPRADANA -> SemanticRelation.RECIPIENT
            Karaka.APADANA -> SemanticRelation.SOURCE
            Karaka.ADHIKARANA -> SemanticRelation.LOCATION
            else -> null
        }
        val participant = ParticipantFacts(
            id = request.pratipadika,
            expression = AvyayaPada(request.pratipadika, request.pratipadika),
            possibleVibhaktis = allVibhaktis,
            semanticRelations = relations,
            // A verb profile describes several positions. The requested role
            // selects one participant; its other valencies are not equally
            // strong, explicitly supplied readings of that same participant.
            semanticEvidence = relations.mapTo(mutableSetOf()) { relation ->
                val supplied = request.semanticRelations != null || relation == requestedMeaning
                SemanticRelationEvidence(relation, when {
                    supplied -> SemanticEvidenceSource.CLAUSE_CONTEXT
                    relation in upapadaRelations -> SemanticEvidenceSource.GOVERNING_CONSTRUCTION
                    else -> SemanticEvidenceSource.DHATU_VALENCY
                }, if (supplied) "Meaning selected by the kāraka derivation request."
                    else "Unselected verbal valency or upapada context.")
            },
            categories = request.categories ?: emptySet(),
            intendedObject = request.intendedObject,
        )
        val dhatuEntry = DhatuPatha.all.firstOrNull {
            it.upadesha == request.dhatu || it.derivationalSurface == request.dhatu || it.sourceSurface == request.dhatu
        }
        val verbNode = TingantaPada(
            sourceText = request.dhatu,
            upasargas = emptyList(),
            dhatu = DhatuPrakriti(
                sourceText = request.dhatu,
                mulaDhatu = dhatuEntry?.sourceSurface ?: request.dhatu
            ),
            lakara = Lakara.LAT,
            ting = TingPratyaya("", "")
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(request.dhatu, request.isSakarmaka),
                participant = participant,
                allParticipants = listOf(participant) + request.otherParticipants.orEmpty(),
                prayoga = request.prayoga,
                candidates = setOf(request.karaka),
                verbNode = verbNode,
                baseDhatu = dhatuEntry
            )
        )
        val resolvedVibhakti = resolution.resolvedVibhakti
            ?: throw UnresolvedKarakaDerivationException(resolution)

        val result = derive(
            SubantaDerivationRequest(
                pratipadika = request.pratipadika,
                vibhakti = resolvedVibhakti,
                vacana = request.vacana,
                linga = request.linga
            )
        )
        return result.copy(karakaResolution = resolution)
    }
}

/** A missing or ambiguous case cannot silently become a nominative derivation. */
class UnresolvedKarakaDerivationException(
    val resolution: dev.panini.analysis.KarakaResolution,
) : IllegalArgumentException(
    "No unique case is licensed for this kāraka derivation; " +
        "resolved role=${resolution.resolved}, candidates=${resolution.candidates}, " +
        "case alternatives=${resolution.alternatives.map { it.vibhakti }}. " +
        "Supply the missing construction evidence or derive an explicitly selected case.",
)

/** The executable portion of a nominal paradigm, retaining its rule traces. */
data class SubantaParadigm(
    val pratipadika: String,
    val linga: Linga,
    val forms: Map<SupAffix, DerivationResult>,
) {
    val derivationSurfaces: Map<SupAffix, String>
        get() = forms.mapValues { (_, result) -> result.final.surface }

    val surfaces: Map<SupAffix, String>
        get() = derivationSurfaces

    val coverage: List<SubantaCoverageRow>
        get() = forms.map { (affix, result) ->
            val actual = result.final.surface
            SubantaCoverageRow(
                affix = affix,
                actualSurface = actual,
                appliedSutras = result.applications.map { it.sutra },
                note = "derived",
            )
        }
}

data class SubantaCoverageRow(
    val affix: SupAffix,
    val actualSurface: String,
    val appliedSutras: List<String>,
    val note: String,
)
