package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.sutra.*

/**
 * 6.1.95 ओमाङोश्च — an अ/आ before ओम् or आङ् takes the following vowel as the single
 * replacement (पररूप, from 6.1.94): शिव + ओम् → शिवोम्, not the vṛddhi शिवौम् of 6.1.88.
 * Both are known by upadeśa: the indeclinable ओम् and the upasarga आङ्, not any word starting ओ/आ.
 */
object OmangoshcaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.95", text = "ओमाङोश्च",
    hindiExplanation = "अवर्ण से परे ओम् या आङ् हो तो पररूप एकादेश होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 1, optional = false, kramaValue = 610095,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.VOWEL_SANDHI, priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.87", "6.1.88", "6.1.101"),
), DerivationSutra {
    private fun target(state: DerivationState): Pair<DerivationTerm, DerivationTerm>? {
        if (state.stage == DerivationStage.FINAL) return null
        for (index in 0 until state.terms.lastIndex) {
            val left = state.terms[index]
            val right = state.terms[index + 1]
            if (left.varnas.lastOrNull() !in setOf(Svara.A, Svara.AA)) continue
            if (right.upadesha !in setOf("ओम्", "आङ्") || right.varnas.firstOrNull() !in setOf(Svara.O, Svara.AA)) continue
            if (state.isBlockedAtBoundary(sutra, left.id, right.id)) continue
            return left to right
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, right) = requireNotNull(target(context))
        return DerivationChange(context.pararupaAtBoundary(left.id, right.id, sutra),
            "6.1.95: पररूप for अवर्ण before ${right.upadesha}.")
    }
}
