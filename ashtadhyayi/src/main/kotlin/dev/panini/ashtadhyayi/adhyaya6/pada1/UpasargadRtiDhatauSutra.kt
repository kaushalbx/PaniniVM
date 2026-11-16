package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.adhyaya1.pada4.hasKriyayoga
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.*

/**
 * "उपसर्गात् … धातौ" (6.1.91, carried into 6.1.94): the left term is an upasarga and the right
 * one is the dhātu it is joined to — either the DHATU term itself inside a derivation, or, at a
 * completed boundary in क्रियायोग, the verbal form that follows it. UPASARGA alone is not enough:
 * प्र + ऋषभ is no prefix–root boundary and keeps 6.1.87 (प्रर्षभ).
 */
internal fun isUpasargaBeforeItsDhatu(state: DerivationState, left: DerivationTerm, right: DerivationTerm): Boolean {
    if (SamjnaAssignment(left.id, Samjna.UPASARGA) !in state.samjnas) return false
    if (right.kind == TermKind.DHATU) return true
    return hasKriyayoga(state) && SamjnaAssignment(right.id, Samjna.UPASARGA) !in state.samjnas
}

/**
 * 6.1.91 उपसर्गादृति धातौ — an अवर्ण-final upasarga before an ऋ-initial dhātu takes vṛddhi
 * (आर्) as the single substitute, against 6.1.87's guṇa: प्र + ऋच्छति → प्रार्च्छति.
 */
object UpasargadRtiDhatauSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.91", text = "उपसर्गादृति धातौ",
    hindiExplanation = "अवर्णान्त उपसर्ग से परे ऋकारादि धातु हो तो वृद्धि एकादेश होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 1, optional = false, kramaValue = 610091,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.VOWEL_SANDHI, priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.87"),
), DerivationSutra {
    private fun target(state: DerivationState): Int? {
        if (state.stage == DerivationStage.FINAL) return null
        return (0 until state.terms.lastIndex).firstOrNull { index ->
            val left = state.terms[index]
            val right = state.terms[index + 1]
            left.varnas.lastOrNull() in setOf(Svara.A, Svara.AA) &&
                right.varnas.firstOrNull() in setOf(Svara.R, Svara.RR) &&
                isUpasargaBeforeItsDhatu(state, left, right) &&
                !state.isBlockedAtBoundary(sutra, left.id, right.id)
        }
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val index = requireNotNull(target(context))
        val left = context.terms[index]
        val right = context.terms[index + 1]
        val substitute = requireNotNull(Varnamala.getVrddhi(right.varnas.first() as Svara))
        val composed = context.replaceTermVarna(left.id, left.varnas.lastIndex, substitute, sutra)
            .deleteTermVarnas(right.id, 0, 1, sutra)
            .concatenateAfterInitialVowelCoalescence(left.id, right, sutra,
                transferDeferredDesignations = true,
                coalescedLeftVarnaIndices = left.varnas.lastIndex until left.varnas.lastIndex + substitute.size)
        val state = if (right.kind == TermKind.DHATU) composed.copy(stage = DerivationStage.ANGAKARYA) else composed
        return DerivationChange(state, "6.1.91: vṛddhi (${substitute.toDevanagari()}) for upasarga अवर्ण + ऋ of the dhātu.")
    }
}
