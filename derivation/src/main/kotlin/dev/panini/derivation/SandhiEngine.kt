package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya8.pada2.JhalamJashonteSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.MonusvarahSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna

/** Applies the implemented external-sandhi rules to two fully formed padas. */
class SandhiEngine(
    private val engine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras)
) {
    fun join(
        left: String,
        right: String,
        config: DerivationConfig = DerivationConfig(),
    ): DerivationResult {
        require(left.isNotBlank() && right.isNotBlank()) { "Two words are required for sandhi." }

        val initial = padaBoundaryState(left, right)
        return engine.derive(initial, config)
    }

    /**
     * Applies external sandhi while retaining the pada boundary in the rendered text.
     * The ordinary [join] result concatenates its terms because that is useful for
     * derivational surfaces; sentence rendering instead needs the transformed padas.
     */
    fun joinPadas(left: String, right: String): String {
        var leftState = singlePadaState(left)
        // Readable sentence rendering currently licenses these two mandatory
        // external operations. Applying the whole derivational catalogue here
        // would reopen the already completed internal phonology of each pada.
        val follower = right.trim().firstOrNull()
        val jashEnvironment = follower != null &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, follower)
        if (left.trim().endsWith("त्") && jashEnvironment && JhalamJashonteSutra.matches(leftState)) {
            leftState = JhalamJashonteSutra.apply(leftState).state
        }
        var state = padaBoundaryState(leftState.terms.single().surface, right)
        if (MonusvarahSutra.matches(state)) state = MonusvarahSutra.apply(state).state
        return state.terms
            .map { it.surface }
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    private fun singlePadaState(surface: String): DerivationState {
        val term = DerivationTerm("sandhi_left", surface.trim(), TermKind.PRATIPADIKA, upadesha = surface.trim())
        return DerivationState(
            terms = listOf(term),
            samjnas = setOf(SamjnaAssignment(term.id, Samjna.PADA)),
            stage = DerivationStage.PADA_FORMED,
        )
    }

    private fun padaBoundaryState(left: String, right: String): DerivationState {
        require(left.isNotBlank() && right.isNotBlank()) { "Two words are required for sandhi." }
        val leftTerm = DerivationTerm("sandhi_left", left.trim(), TermKind.PRATIPADIKA, upadesha = left.trim())
        val rightTerm = DerivationTerm("sandhi_right", right.trim(), TermKind.PRATIPADIKA, upadesha = right.trim())
        return DerivationState(
            terms = listOf(leftTerm, rightTerm),
            samjnas = setOf(
                SamjnaAssignment(leftTerm.id, Samjna.PADA),
                SamjnaAssignment(rightTerm.id, Samjna.PADA),
            ),
            stage = DerivationStage.PADA_FORMED,
        )
    }
}
