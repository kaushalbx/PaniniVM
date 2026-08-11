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
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingPratyaya
import dev.panini.sankhya.PrimitiveSankhya

class SubantaEngine(
    private val engine: DerivationEngine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras),
) {
    fun derive(request: SubantaDerivationRequest): DerivationResult {
        val specializedForm = deriveSpecializedDeclension(request.pratipadika, request.vibhakti, request.vacana)
        if (specializedForm != null) {
            val stemTerm = DerivationTerm("pratipadika", request.pratipadika, TermKind.PRATIPADIKA)
            val finalTerm = DerivationTerm("subanta_final", specializedForm, TermKind.PRATIPADIKA, upadesha = specializedForm)
            val initialState = DerivationState(terms = listOf(stemTerm), stage = DerivationStage.INITIAL)
            val finalState = initialState.copy(terms = listOf(finalTerm), stage = DerivationStage.FINAL)
            return DerivationResult(initialState, finalState, emptyList(), emptyList())
        }

        val plan = requireNotNull(SubantaFormPlans.find(request.vibhakti, request.vacana)) {
            "No complete downstream plan exists for ${SupAffix.select(request.vibhakti, request.vacana).upadesha}."
        }
        return engine.derive(request.initialState()).apply {
            verifyDerivation("4.1.2", plan.affix.upadesha, plan.requiredSutras, plan.finalStage)
        }
    }

    private fun deriveSpecializedDeclension(pratipadika: String, vibhakti: Vibhakti, vacana: Vacana): String? {
        return deriveNumeralDeclension(pratipadika, vibhakti, vacana)
    }

    private fun deriveNumeralDeclension(
        pratipadika: String,
        vibhakti: Vibhakti,
        vacana: Vacana,
    ): String? {
        val numeral = PrimitiveSankhya.fromAnnotatedPratipadika(pratipadika) ?: return null
        val naturalVacana = when (numeral.value) {
            1L -> Vacana.EKAVACANA
            2L -> Vacana.DVIVACANA
            else -> Vacana.BAHUVACANA
        }
        if (vacana != naturalVacana) return null
        return when (numeral.value) {
            2L -> when (vibhakti) {
                Vibhakti.PRATHAMA, Vibhakti.DVITIYA -> "द्वे"
                Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI -> "द्वाभ्याम्"
                Vibhakti.SASTHI, Vibhakti.SAPTAMI -> "द्वयोः"
            }
            3L -> pluralNumeral("त्रीणि", "त्रिभिः", "त्रिभ्यः", "त्रयाणाम्", "त्रिषु", vibhakti)
            4L -> pluralNumeral("चत्वारि", "चतुर्भिः", "चतुर्भ्यः", "चतुर्णाम्", "चतुर्षु", vibhakti)
            5L -> pluralNumeral("पञ्च", "पञ्चभिः", "पञ्चभ्यः", "पञ्चानाम्", "पञ्चसु", vibhakti)
            6L -> pluralNumeral("षट्", "षड्भिः", "षड्भ्यः", "षण्णाम्", "षट्सु", vibhakti)
            7L -> pluralNumeral("सप्त", "सप्तभिः", "सप्तभ्यः", "सप्तानाम्", "सप्तसु", vibhakti)
            8L -> pluralNumeral("अष्ट", "अष्टाभिः", "अष्टाभ्यः", "अष्टानाम्", "अष्टासु", vibhakti)
            9L -> pluralNumeral("नव", "नवभिः", "नवभ्यः", "नवानाम्", "नवसु", vibhakti)
            10L -> pluralNumeral("दश", "दशभिः", "दशभ्यः", "दशानाम्", "दशसु", vibhakti)
            else -> null
        }
    }

    private fun pluralNumeral(
        nominativeAccusative: String,
        instrumental: String,
        dativeAblative: String,
        genitive: String,
        locative: String,
        vibhakti: Vibhakti,
    ): String = when (vibhakti) {
        Vibhakti.PRATHAMA, Vibhakti.DVITIYA -> nominativeAccusative
        Vibhakti.TRTIYA -> instrumental
        Vibhakti.CHATURTHI, Vibhakti.PANCHAMI -> dativeAblative
        Vibhakti.SASTHI -> genitive
        Vibhakti.SAPTAMI -> locative
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
        val participant = ParticipantFacts(
            id = request.pratipadika,
            expression = AvyayaPada(request.pratipadika, request.pratipadika),
            possibleVibhaktis = allVibhaktis,
            semanticRelations = request.semanticRelations ?: (profile?.relations.orEmpty() + upapadaRelations),
            categories = request.categories ?: emptySet()
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
        val resolvedVibhakti = resolution.resolvedVibhakti ?: Vibhakti.PRATHAMA

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
