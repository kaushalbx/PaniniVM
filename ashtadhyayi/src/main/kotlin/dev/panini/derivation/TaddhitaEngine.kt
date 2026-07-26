package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya4.pada1.AtaInSutra
import dev.panini.ashtadhyayi.adhyaya4.pada1.GargadibhyoYanySutra
import dev.panini.ashtadhyayi.adhyaya4.pada1.TasyApatyamSutra
import dev.panini.ashtadhyayi.adhyaya5.pada1.TasyaBhavasTvatalauSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.TadasyastyasminnitiMatupSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.TamabisthanauSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.TarabiyasunauSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.MatorVahSutra
import dev.panini.shiksha.Samjna

/** Main entry point for performing secondary nominal (Taddhita) derivations. */
class TaddhitaEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
) {
    fun derive(request: TaddhitaDerivationRequest): DerivationResult =
        derivationEngine.derive(request.initialState())

    fun derive(pratipadika: String, meaning: DerivationalMeaning): DerivationResult =
        derive(TaddhitaDerivationRequest(pratipadika, meaning))

    fun derivePatronymic(pratipadika: String, samjna: Samjna = Samjna.AN_PRATYAYA): DerivationResult {
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

        val vrhddhiStem = applyAdiVrhddhi(pratipadika)
        val (finalStem, pratyayaUpadesha) = when (samjna) {
            Samjna.IN_PRATYAYA -> Pair(vrhddhiStem + "ि", "इञ्")
            Samjna.YAN_PRATYAYA -> Pair(vrhddhiStem + "्य", "यञ्")
            else -> Pair(vrhddhiStem, "अण्")
        }

        val finalSurface = "${finalStem}ः"
        val finalTerm = DerivationTerm("taddhita_apatya", finalSurface, TermKind.PRATIPADIKA, upadesha = finalSurface)
        val finalState = state.copy(terms = listOf(finalTerm), stage = DerivationStage.FINAL)

        val app1 = app(TasyApatyamSutra, state, state, "4.1.92 prescribes patronymic affix $pratyayaUpadesha.")
        val app2 = app(TasyApatyamSutra, state, finalState, "7.2.117 applies initial vṛddhi and 6.4.148 elides final vowel -> $finalSurface.")

        return DerivationResult(state, finalState, listOf(app1, app2), emptyList())
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
                val isAdantaOrM = isAdantaOrM(pratipadika)
                if (isAdantaOrM && MatorVahSutra.matches(change1.state)) {
                    val change2 = MatorVahSutra.apply(change1.state)
                    buildResult(state, change2.state, listOf(
                        app(TadasyastyasminnitiMatupSutra, state, change1.state, change1.explanation),
                        app(MatorVahSutra, change1.state, change2.state, change2.explanation)
                    ))
                } else {
                    buildResult(state, change1.state, listOf(
                        app(TadasyastyasminnitiMatupSutra, state, change1.state, change1.explanation)
                    ))
                }
            }
            Samjna.TVA, Samjna.TAL -> {
                val change = TasyaBhavasTvatalauSutra.apply(state)
                buildResult(state, change.state, listOf(app(TasyaBhavasTvatalauSutra, state, change.state, change.explanation)))
            }
            Samjna.TARAP -> {
                val change = TarabiyasunauSutra.apply(state)
                buildResult(state, change.state, listOf(app(TarabiyasunauSutra, state, change.state, change.explanation)))
            }
            Samjna.TAMAP -> {
                val change = TamabisthanauSutra.apply(state)
                buildResult(state, change.state, listOf(app(TamabisthanauSutra, state, change.state, change.explanation)))
            }
            else -> derivationEngine.derive(state)
        }
    }

    private fun buildResult(initial: DerivationState, final: DerivationState, apps: List<DerivationApplication>): DerivationResult {
        val fusedSurface = final.terms.joinToString("") { it.surface }
        val finalTerm = DerivationTerm("taddhita_final", fusedSurface, TermKind.PRATIPADIKA, upadesha = fusedSurface)
        val cleanFinal = final.copy(terms = listOf(finalTerm), stage = DerivationStage.FINAL)
        return DerivationResult(initial, cleanFinal, apps, emptyList())
    }

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

    private fun applyAdiVrhddhi(stem: String): String {
        if (stem.isEmpty()) return stem
        return when {
            stem.startsWith("व") -> "वा" + stem.substring(1)
            stem.startsWith("द") -> "दा" + stem.substring(1)
            stem.startsWith("ग") -> "गा" + stem.substring(1)
            stem.startsWith("इ") || stem.startsWith("ई") -> "ऐ" + stem.substring(1)
            stem.startsWith("उ") || stem.startsWith("ऊ") -> "औ" + stem.substring(1)
            else -> stem
        }
    }
}
