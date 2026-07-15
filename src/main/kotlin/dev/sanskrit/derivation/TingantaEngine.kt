package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.DhatuPatha
import dev.sanskrit.dhatupatha.PadaType

class TingantaEngine(private val engine: DerivationEngine = DerivationEngine()) {

    fun derive(request: TingantaDerivationRequest): DerivationResult {
        val dhatus = DhatuPatha.all.filter { it.upadesha == request.dhatu || it.mula == request.dhatu }
        require(dhatus.isNotEmpty()) {
            "Unknown dhatu: ${request.dhatu}"
        }
        val dhatu = dhatus.first()
        val targetPada = when (dhatu.pada ?: PadaType.PARASMAIPADA) {
            PadaType.PARASMAIPADA -> PadaType.PARASMAIPADA
            PadaType.ATMANEPADA -> PadaType.ATMANEPADA
            PadaType.UBHAYAPADA -> PadaType.PARASMAIPADA
        }
        
        val plan = requireNotNull(TingantaFormPlans.find(request.purusha, request.vacana, targetPada, request.lakara)) {
            "No complete downstream plan exists for ${TingAffix.select(request.purusha, request.vacana, targetPada)?.upadesha}."
        }
        
        val result = engine.derive(request.initialState(dhatu))
        val appliedSutras = result.applications.mapTo(mutableSetOf()) { it.sutra }
        
        val selectedAffix = result.applications
            .singleOrNull { it.sutra == "3.4.78" }
            ?.delta
            ?.addedTerms
            ?.singleOrNull()
            ?.upadesha
            
        require(selectedAffix == plan.affix.upadesha) {
            "3.4.78 selected $selectedAffix, but ${plan.affix.upadesha} was required."
        }
        require(plan.requiredSutras.all { it in appliedSutras }) {
            "Incomplete derivation for ${plan.affix.upadesha}; missing ${plan.requiredSutras - appliedSutras}."
        }
        require(result.final.stage == plan.finalStage) {
            "Incomplete derivation for ${plan.affix.upadesha}; expected ${plan.finalStage}, reached ${result.final.stage}."
        }
        
        return result
    }

    fun deriveSupportedParadigm(
        dhatu: String,
        pada: PadaType? = null,
        lakara: Lakara = Lakara.LAT,
    ): TingantaParadigm {
        val targetPada = pada ?: run {
            val dhatus = DhatuPatha.all.filter { it.upadesha == dhatu || it.mula == dhatu }
            require(dhatus.isNotEmpty()) { "Unknown dhatu: $dhatu" }
            val p = dhatus.first().pada ?: PadaType.PARASMAIPADA
            when (p) {
                PadaType.PARASMAIPADA -> PadaType.PARASMAIPADA
                PadaType.ATMANEPADA -> PadaType.ATMANEPADA
                PadaType.UBHAYAPADA -> PadaType.PARASMAIPADA
            }
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
