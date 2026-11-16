package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.*

/**
 * 6.1.94 एङि पररूपम् — an अवर्ण-final upasarga before an एङ्-initial dhātu takes the following
 * vowel (पररूप), against 6.1.88's vṛddhi and 6.1.87's guṇa: प्र + एजते → प्रेजते.
 */
object EngiPararupamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.94", text = "एङि पररूपम्",
    hindiExplanation = "अकारान्त उपसर्ग के बाद यदि एङ् (ए, ओ) से आरम्भ होने वाली धातु हो, तो पररूप एकादेश होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 1, optional = false, kramaValue = 610094,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.VOWEL_SANDHI, priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.87", "6.1.88"),
), DerivationSutra {
    private fun target(state: DerivationState): Pair<DerivationTerm, DerivationTerm>? {
        if (state.stage == DerivationStage.FINAL) return null
        for (index in 0 until state.terms.lastIndex) {
            val left = state.terms[index]
            val right = state.terms[index + 1]
            if (left.varnas.lastOrNull() !in setOf(Svara.A, Svara.AA)) continue
            if (right.varnas.firstOrNull() !in setOf(Svara.E, Svara.O)) continue
            if (!isUpasargaBeforeItsDhatu(state, left, right)) continue
            if (state.isBlockedAtBoundary(sutra, left.id, right.id)) continue
            return left to right
        }
        return null
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (prefix, root) = requireNotNull(target(context))
        val replacement = listOf(root.varnas.first())
        val merged = context.pararupaAtBoundary(prefix.id, root.id, sutra)
        return DerivationChange(
            state = if (root.kind == TermKind.DHATU) merged.copy(stage = DerivationStage.ANGAKARYA) else merged,
            explanation = "6.1.94: Pararūpa substitution (${replacement.toDevanagari()}) for prefix-a + root-e/o.")
    }
}
