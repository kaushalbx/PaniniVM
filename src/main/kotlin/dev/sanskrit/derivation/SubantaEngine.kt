package dev.sanskrit.derivation

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
