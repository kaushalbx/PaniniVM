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
        if (pratipadika.endsWith("त्मन्") || pratipadika.endsWith("आत्मन्")) {
            val stem = pratipadika.removeSuffix("न्").removeSuffix("न")
            return when {
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.EKAVACANA -> "${stem}ा"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.BAHUVACANA -> "${stem}ानः"
                else -> null
            }
        }
        return when (pratipadika) {
            "नदी" -> when {
                vibhakti == Vibhakti.TRTIYA && vacana == Vacana.EKAVACANA -> "नद्या"
                vibhakti == Vibhakti.TRTIYA && vacana == Vacana.BAHUVACANA -> "नदीभिः"
                else -> null
            }
            "राजन्" -> when {
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.EKAVACANA -> "राजा"
                vibhakti == Vibhakti.TRTIYA && vacana == Vacana.EKAVACANA -> "राज्ञा"
                else -> null
            }
            "वाच्" -> when {
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.EKAVACANA -> "वाक्"
                vibhakti == Vibhakti.TRTIYA && vacana == Vacana.EKAVACANA -> "वाचा"
                vibhakti == Vibhakti.TRTIYA && vacana == Vacana.BAHUVACANA -> "वाग्भिः"
                else -> null
            }
            "तद्" -> when {
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.EKAVACANA -> "सः"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.DVIVACANA -> "तौ"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.BAHUVACANA -> "ते"
                vibhakti == Vibhakti.DVITIYA && vacana == Vacana.EKAVACANA -> "तम्"
                vibhakti == Vibhakti.TRTIYA && vacana == Vacana.EKAVACANA -> "तेन"
                vibhakti == Vibhakti.CHATURTHI && vacana == Vacana.EKAVACANA -> "तस्मै"
                vibhakti == Vibhakti.PANCHAMI && vacana == Vacana.EKAVACANA -> "तस्मात्"
                vibhakti == Vibhakti.SASTHI && vacana == Vacana.EKAVACANA -> "तस्य"
                vibhakti == Vibhakti.SAPTAMI && vacana == Vacana.EKAVACANA -> "तस्मिन्"
                else -> null
            }
            "यद्" -> when {
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.EKAVACANA -> "यः"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.DVIVACANA -> "यौ"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.BAHUVACANA -> "ये"
                else -> null
            }
            "किम्" -> when {
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.EKAVACANA -> "कः"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.DVIVACANA -> "कौ"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.BAHUVACANA -> "के"
                else -> null
            }
            "इदम्" -> when {
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.EKAVACANA -> "अयम्"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.DVIVACANA -> "इमौ"
                vibhakti == Vibhakti.PRATHAMA && vacana == Vacana.BAHUVACANA -> "इमे"
                else -> null
            }
            "द्वि" -> when {
                vacana == Vacana.DVIVACANA -> "द्वी"
                else -> null
            }
            else -> null
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
