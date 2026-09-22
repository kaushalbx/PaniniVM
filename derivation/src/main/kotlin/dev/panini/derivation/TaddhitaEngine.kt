package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya5.pada1.TasyaBhavasTvatalauSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.TadasyastyasminnitiMatupSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.TamabisthanauSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.TarabiyasunauSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.MatorVahSutra
import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna

/** Main entry point for performing secondary nominal (Taddhita) derivations. */
class TaddhitaEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
    private val subantaEngine: SubantaEngine = SubantaEngine(),
) {
    private val itProcessingEngine = DerivationEngine(Ashtadhyayi.executableSutrasAt(dev.panini.sutra.SutraStage.IT_PROCESSING))
    fun derive(request: TaddhitaDerivationRequest): DerivationResult =
        derivationEngine.derive(request.initialState())

    fun derive(pratipadika: String, meaning: DerivationalMeaning): DerivationResult =
        derive(TaddhitaDerivationRequest(pratipadika, meaning))

    fun derivePatronymic(pratipadika: String, samjna: Samjna = Samjna.AN_PRATYAYA): DerivationResult {
        val (meaning, expectedAffix) = when (samjna) {
            Samjna.IN_PRATYAYA -> DerivationalMeaning.APATYA to "इञ्"
            Samjna.YAN_PRATYAYA -> DerivationalMeaning.GOTRA to "यञ्"
            Samjna.AN_PRATYAYA, Samjna.APATYA -> DerivationalMeaning.APATYA to "अण्"
            else -> error("Unsupported patronymic designation: $samjna")
        }
        val stemInitial = TaddhitaDerivationRequest(pratipadika, meaning).initialState()
        val stemResult = derivationEngine.derive(
            stemInitial,
            DerivationConfig(computeSvara = false),
        )
        val introduction = stemResult.applications.firstOrNull { application ->
            application.after.terms.any { it.kind == TermKind.PRATYAYA && it.upadesha == expectedAffix }
        }
        requireNotNull(introduction) {
            "Registered taddhita rules did not introduce $expectedAffix for $pratipadika."
        }

        val nominal = subantaEngine.derive(
            SubantaDerivationRequest(
                stemResult.final.surface,
                Vibhakti.PRATHAMA,
                Vacana.EKAVACANA,
                Linga.PUMS,
            ),
        )
        val applications = stemResult.applications + nominal.applications
        val finalState = nominal.final.copy(
            droppedTerms = stemResult.final.droppedTerms + nominal.final.droppedTerms,
            appliedSutras = stemResult.initial.appliedSutras + applications.map { it.sutra },
        )
        val events = stemResult.events.filterNot { it is DerivationEvent.Completed } +
            nominal.events.filterNot { it is DerivationEvent.Completed } +
            DerivationEvent.Completed(finalState, applications.size)
        return DerivationResult(
            initial = stemResult.initial,
            final = finalState,
            applications = applications,
            events = events,
            svaraResult = nominal.svaraResult,
        )
    }

    fun derive(pratipadika: String, samjna: Samjna): DerivationResult {
        if (samjna in setOf(Samjna.AN_PRATYAYA, Samjna.IN_PRATYAYA, Samjna.YAN_PRATYAYA, Samjna.APATYA)) {
            return derivePatronymic(pratipadika, samjna)
        }

        val stemTerm = DerivationTerm("pratipadika", pratipadika, TermKind.PRATIPADIKA)
        val state = DerivationState(
            terms = listOf(stemTerm),
            samjnas = setOf(
                SamjnaAssignment(stemTerm.id, Samjna.PRATIPADIKA),
                SamjnaAssignment(stemTerm.id, samjna),
            ),
            activeAdhikaras = setOf("4.1.76"),
            stage = DerivationStage.INITIAL,
        )

        return when (samjna) {
            Samjna.MATUP -> {
                val change1 = TadasyastyasminnitiMatupSutra.apply(state)
                val processed = processIt(change1.state)
                val isAdantaOrM = isAdantaOrM(pratipadika)
                if (isAdantaOrM && MatorVahSutra.matches(processed.final)) {
                    val change2 = MatorVahSutra.apply(processed.final)
                    buildResult(state, change2.state, listOf(
                        app(TadasyastyasminnitiMatupSutra, state, change1.state, change1.explanation)
                    ) + processed.applications + app(MatorVahSutra, processed.final, change2.state, change2.explanation))
                } else {
                    buildResult(state, processed.final, listOf(
                        app(TadasyastyasminnitiMatupSutra, state, change1.state, change1.explanation)
                    ) + processed.applications)
                }
            }
            Samjna.TVA, Samjna.TAL -> {
                val change = TasyaBhavasTvatalauSutra.apply(state)
                buildResult(state, change.state, listOf(app(TasyaBhavasTvatalauSutra, state, change.state, change.explanation)))
            }
            Samjna.TARAP -> {
                val change = TarabiyasunauSutra.apply(state)
                val processed = processIt(change.state)
                buildResult(state, processed.final, listOf(app(TarabiyasunauSutra, state, change.state, change.explanation)) + processed.applications)
            }
            Samjna.TAMAP -> {
                val change = TamabisthanauSutra.apply(state)
                val processed = processIt(change.state)
                buildResult(state, processed.final, listOf(app(TamabisthanauSutra, state, change.state, change.explanation)) + processed.applications)
            }
            else -> derivationEngine.derive(state)
        }
    }

    private fun buildResult(initial: DerivationState, final: DerivationState, apps: List<DerivationApplication>): DerivationResult {
        val fusedSurface = final.terms.joinToString("") { it.surface }
        val finalTerm = DerivationTerm("taddhita_final", fusedSurface, TermKind.PRATIPADIKA, upadesha = fusedSurface)
        val cleanFinal = final.copy(
            terms = listOf(finalTerm),
            stage = DerivationStage.FINAL,
            appliedSutras = initial.appliedSutras + apps.map { it.sutra },
        )
        return DerivationResult(initial, cleanFinal, apps, emptyList())
            .completeSvara(SvaraContext.from(final))
    }

    private fun processIt(state: DerivationState): DerivationResult = itProcessingEngine.derive(
        state,
        DerivationConfig(validateFinalItProcessing = false, computeSvara = false),
    )

    private fun app(sutra: DerivationSutra, before: DerivationState, after: DerivationState, explanation: String): DerivationApplication =
        DerivationApplication(
            sutra = sutra.sutra, role = sutra.role, action = sutra.action, scope = sutra.scope,
            trace = sutra.renderTrace(), before = before, after = after, explanation = explanation
        )

    private fun isAdantaOrM(stem: String): Boolean {
        if (stem.isEmpty()) return false
        if (stem.endsWith("म्") || stem.endsWith("म")) return true
        val matras = setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'े', 'ै', 'ो', 'ौ', 'ं', 'ः', '्')
        return stem.last() !in matras
    }

}
