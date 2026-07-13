package dev.sanskrit.derivation

class SubantaEngine(
    private val engine: DerivationEngine = DerivationEngine(),
) {
    fun derive(request: SubantaDerivationRequest): DerivationResult {
        require(request.stemClass == SubantaStemClass.A_STEM_MASCULINE) {
            "Only a-stem masculine nominal morphology is executable today."
        }
        val plan = requireNotNull(SubantaFormPlans.find(request.vibhakti, request.vacana)) {
            "No complete downstream plan exists for ${SupAffix.select(request.vibhakti, request.vacana).upadesha}."
        }
        val result = engine.derive(request.initialState())
        val appliedSutras = result.applications.mapTo(mutableSetOf()) { it.sutra }
        val selectedAffix = result.applications
            .singleOrNull { it.sutra == "4.1.2" }
            ?.delta
            ?.addedTerms
            ?.singleOrNull()
            ?.upadesha
        require(selectedAffix == plan.affix.upadesha) {
            "4.1.2 selected $selectedAffix, but ${plan.affix.upadesha} was required."
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
        pratipadika: String,
        stemClass: SubantaStemClass = SubantaStemClass.A_STEM_MASCULINE,
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
