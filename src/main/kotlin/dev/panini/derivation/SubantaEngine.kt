package dev.panini.derivation

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.SupAffix
import dev.panini.core.Lakara
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.analysis.KarakaRuleEngine
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.DhatuIdentity
import dev.panini.vyakaranam.analysis.DhatuKarakaProfiles
import dev.panini.vyakaranam.analysis.ParticipantFacts
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingPratyaya

class SubantaEngine(
    private val engine: DerivationEngine = DerivationEngine(),
) {
    fun derive(request: SubantaDerivationRequest): DerivationResult {
        val plan = requireNotNull(SubantaFormPlans.find(request.vibhakti, request.vacana)) {
            "No complete downstream plan exists for ${SupAffix.select(request.vibhakti, request.vacana).upadesha}."
        }
        return engine.derive(request.initialState()).apply {
            verifyDerivation("4.1.2", plan.affix.upadesha, plan.requiredSutras, plan.finalStage)
        }
    }

    fun deriveSupportedParadigm(
        pratipadika: String,
        stemClass: SubantaStemClass = SubantaStemClass.guess(pratipadika),
    ): SubantaParadigm = SubantaParadigm(
        pratipadika = pratipadika,
        stemClass = stemClass,
        forms = SubantaFormPlans.all().associate { plan ->
            plan.affix to try {
                derive(SubantaDerivationRequest(pratipadika, plan.affix.vibhakti, plan.affix.vacana, stemClass))
            } catch (exception: IllegalArgumentException) {
                throw IllegalArgumentException("Failed to derive ${plan.affix}: ${exception.message}", exception)
            }
        },
    )

    fun deriveFromKaraka(request: KarakaSubantaDerivationRequest): DerivationResult {
        val allVibhaktis = Vibhakti.entries.toSet()
        val profile = DhatuKarakaProfiles.forSurface(request.dhatu)
        val participant = ParticipantFacts(
            id = request.pratipadika,
            expression = AvyayaPada(request.pratipadika, request.pratipadika),
            possibleVibhaktis = allVibhaktis,
            semanticRelations = profile?.relations.orEmpty()
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
                allParticipants = listOf(participant),
                prayoga = request.prayoga,
                candidates = setOf(request.karaka),
                verbNode = verbNode,
                baseDhatu = dhatuEntry
            )
        )
        val resolvedVibhakti = resolution.resolvedVibhakti ?: Vibhakti.PRATHAMA

        return derive(
            SubantaDerivationRequest(
                pratipadika = request.pratipadika,
                vibhakti = resolvedVibhakti,
                vacana = request.vacana,
                stemClass = SubantaStemClass.guess(request.pratipadika)
            )
        )
    }
}

/** The executable portion of a nominal paradigm, retaining its rule traces. */
data class SubantaParadigm(
    val pratipadika: String,
    val stemClass: SubantaStemClass,
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
