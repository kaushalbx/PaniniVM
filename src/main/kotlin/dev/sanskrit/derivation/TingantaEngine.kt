package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.DhatuPatha
import dev.sanskrit.dhatupatha.PadaType

class TingantaEngine(private val engine: DerivationEngine = DerivationEngine()) {

    fun derive(request: TingantaDerivationRequest): DerivationResult {
        val dhatu = findDhatu(request.dhatu)
        val targetPada = when (dhatu.pada) {
            PadaType.ATMANEPADA -> PadaType.ATMANEPADA
            else -> PadaType.PARASMAIPADA
        }
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
        val targetPada = pada ?: when (findDhatu(dhatu).pada) {
            PadaType.ATMANEPADA -> PadaType.ATMANEPADA
            else -> PadaType.PARASMAIPADA
        }
        val matchingPlans = TingantaFormPlans.all().filter { it.affix.pada == targetPada && it.lakara == lakara }
        return TingantaParadigm(
            dhatu = dhatu,
            pada = targetPada,
            forms = matchingPlans.associate { plan ->
                plan.affix to try {
                    derive(TingantaDerivationRequest(dhatu, plan.affix.vacana, plan.affix.purusha, lakara))
                } catch (exception: IllegalArgumentException) {
                    throw IllegalArgumentException("Failed to derive ${plan.affix}: ${exception.message}", exception)
                }
            }
        )
    }

    private fun findDhatu(dhatu: String) =
        DhatuPatha.all.firstOrNull { it.upadesha == dhatu || it.mula == dhatu }
            ?: throw IllegalArgumentException("Unknown dhatu: $dhatu")
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
