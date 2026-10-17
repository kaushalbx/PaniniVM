package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 8.3.20: mandatory ordinary pada-final y-lopa after o before aś.
 * Reduced-effort variants under 8.3.18 need independent phonetic annotation. */
object OtoGargyasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.20", text = "ओतो गार्ग्यस्य",
    hindiExplanation = "ओकार के बाद पदान्त सामान्य यकार का अश् परे नित्य लोप होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830020,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun boundary(state: DerivationState): Int? = (0 until state.terms.lastIndex).firstOrNull { index ->
        val left = state.terms[index]
        val right = state.terms[index + 1]
        left.formedPadaRupa != null && right.formedPadaRupa != null &&
            state.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA } &&
            left.varnas.takeLast(2) == listOf(Svara.O, Vyanjana.YA) &&
            right.varnas.firstOrNull()?.let { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, it) } == true
    }
    override fun matches(context: DerivationState) = boundary(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val index = requireNotNull(boundary(context))
        val left = context.terms[index]
        val right = context.terms[index + 1]
        var state = context.deleteTermVarnas(left.id, left.varnas.lastIndex, 1, sutra)
        for (rule in listOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.101", "6.1.109"))
            state = state.blockAtBoundary(rule, left.id, right.id, sutra)
        return DerivationChange(state, "8.3.20 deletes ordinary final y after o, preserving tripadi hiatus.")
    }
}
