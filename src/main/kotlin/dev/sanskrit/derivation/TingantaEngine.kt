package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.DhatuPatha
import dev.sanskrit.dhatupatha.PadaType

class TingantaEngine(private val engine: DerivationEngine = DerivationEngine()) {

    fun derive(request: TingantaDerivationRequest): DerivationResult {
        val dhatu = findDhatu(request.dhatu)
        val targetPada = resolvePada(requireNotNull(dhatu.pada), request.pada)
        val plan = requireNotNull(TingantaFormPlans.find(request.purusha, request.vacana, targetPada, request.lakara)) {
            "No complete downstream plan exists for ${TingAffix.select(request.purusha, request.vacana, targetPada)?.upadesha}."
        }
        return engine.derive(request.initialState(dhatu)).apply {
            verifyDerivation("3.4.78", plan.affix.upadesha, plan.requiredSutras, plan.finalStage)
        }
    }

    fun deriveSupportedParadigm(
        dhatu: String,
        pada: PadaType? = null,
        lakara: Lakara = Lakara.LAT,
    ): TingantaParadigm {
        val targetPada = resolvePada(requireNotNull(findDhatu(dhatu).pada), pada)
        val matchingPlans = TingantaFormPlans.all().filter { it.affix.pada == targetPada && it.lakara == lakara }
        return TingantaParadigm(
            dhatu = dhatu,
            pada = targetPada,
            forms = matchingPlans.associate { plan ->
                plan.affix to try {
                    derive(TingantaDerivationRequest(dhatu, plan.affix.vacana, plan.affix.purusha, lakara, pada = targetPada))
                } catch (exception: IllegalArgumentException) {
                    throw IllegalArgumentException("Failed to derive ${plan.affix}: ${exception.message}", exception)
                }
            }
        )
    }

    private fun findDhatu(dhatu: String) =
        DhatuPatha.all.firstOrNull { it.upadesha == dhatu || it.derivationalSurface == dhatu }
            ?: throw IllegalArgumentException("Unknown dhatu: $dhatu")

    private fun resolvePada(dhatuPada: PadaType, requestedPada: PadaType?): PadaType {
        require(requestedPada != PadaType.UBHAYAPADA) { "A derivation must select a concrete pada." }
        return when (dhatuPada) {
            PadaType.PARASMAIPADA -> {
                require(requestedPada == null || requestedPada == PadaType.PARASMAIPADA) { "This dhātu is Parasmaipada only." }
                PadaType.PARASMAIPADA
            }
            PadaType.ATMANEPADA -> {
                require(requestedPada == null || requestedPada == PadaType.ATMANEPADA) { "This dhātu is Ātmanepada only." }
                PadaType.ATMANEPADA
            }
            PadaType.UBHAYAPADA -> requestedPada ?: PadaType.PARASMAIPADA
        }
    }
}

/** The executable portion of a verbal paradigm, retaining its rule traces. */
data class TingantaParadigm(
    val dhatu: String,
    val pada: PadaType,
    val forms: Map<TingAffix, DerivationResult>,
) {
    val derivationSurfaces: Map<TingAffix, String>
        get() = forms.mapValues { (_, result) -> result.final.surface }

    val surfaces: Map<TingAffix, String>
        get() = derivationSurfaces

    val coverage: List<TingantaCoverageRow>
        get() = forms.map { (affix, result) ->
            val actual = result.final.surface
            TingantaCoverageRow(
                affix = affix,
                actualSurface = actual,
                appliedSutras = result.applications.map { it.sutra },
                note = "derived",
            )
        }
}

data class TingantaCoverageRow(
    val affix: TingAffix,
    val actualSurface: String,
    val appliedSutras: List<String>,
    val note: String,
)
