package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.shiksha.Samjna

/** Applies the implemented external-sandhi rules to two fully formed padas. */
class SandhiEngine(
    private val engine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras)
) {
    fun join(left: String, right: String): DerivationResult {
        require(left.isNotBlank() && right.isNotBlank()) { "Two words are required for sandhi." }

        val leftTerm = DerivationTerm("sandhi_left", left.trim(), TermKind.PRATIPADIKA, upadesha = left.trim())
        val rightTerm = DerivationTerm("sandhi_right", right.trim(), TermKind.PRATIPADIKA, upadesha = right.trim())
        val initial = DerivationState(
            terms = listOf(leftTerm, rightTerm),
            samjnas = setOf(
                SamjnaAssignment(leftTerm.id, Samjna.PADA),
                SamjnaAssignment(rightTerm.id, Samjna.PADA)
            ),
            stage = DerivationStage.PADA_FORMED
        )
        return engine.derive(initial)
    }
}
