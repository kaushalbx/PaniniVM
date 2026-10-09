package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 8.3.19: optional pada-final y/v-lopa after a/ā before aś. */
object LopahShakalyasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.19", text = "लोपः शाकल्यस्य", hindiExplanation = "अ/आ के बाद पदान्त य्/व् का अश् परे विकल्प से लोप होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true,
    kramaValue = 830019, role = SutraRole.Vidhi, action = SutraAction.LOPA,
    scope = SutraScope.PADA_BOUNDARY, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun boundary(context: DerivationState): Int? = (0 until context.terms.size - 1).firstOrNull { i ->
        val left = context.terms[i]
        val right = context.terms[i + 1]
        left.formedPadaRupa != null && right.formedPadaRupa != null &&
            context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA } &&
            left.varnas.lastOrNull() in setOf(Vyanjana.YA, Vyanjana.VA) &&
            left.varnas.dropLast(1).lastOrNull() in setOf(Svara.A, Svara.AA) &&
            right.varnas.firstOrNull()?.let { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, it) } == true
    }

    override fun matches(context: DerivationState) = boundary(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val index = requireNotNull(boundary(context))
        val left = context.terms[index]
        val right = context.terms[index + 1]
        var state = context.replaceTermVarna(left.id, left.varnas.lastIndex, emptyList(), sutra)
        // Tripadi lopa cannot reopen earlier vowel sandhi at this same boundary.
        for (rule in listOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.101"))
            state = state.blockAtBoundary(rule, left.id, right.id, sutra)
        return DerivationChange(state, "8.3.19: optional Śākalya y/v-lopa at the external pada boundary.")
    }
}
