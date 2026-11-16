package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.*

/**
 * 6.1.89 एत्येधत्यूठ्सु — अवर्ण followed by an एच् of एति (इण्), एधति (एधँ) or the ऊठ् augment takes vṛddhi.
 * It overrides 6.1.94 एङि पररूपम्: उप + एति → उपैति, उप + एधते → उपैधते (not उपेति, उपेधते).
 * The roots are known by their Dhātupāṭha upadeśa, not by a surface spelling ए.
 */
object EtyedhatyuthSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.89", text = "एत्येधत्यूठ्सु",
    hindiExplanation = "अवर्ण से परे एति (इण्), एधति (एध्) के एच् या ऊठ् हो तो वृद्धि एकादेश होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 1, optional = false, kramaValue = 610089,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.VOWEL_SANDHI, priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.94", "6.1.87"),
), DerivationSutra {
    private val roots = setOf("इण्", "एधँ")

    /** A DHATU term by its own upadeśa; a completed verbal form by the dhātu it was derived from. */
    private fun isRoot(term: DerivationTerm): Boolean =
        if (term.kind == TermKind.DHATU) term.upadesha in roots
        else term.sourceDhatuId?.let { DhatuPatha.find(it)?.upadesha } in roots

    private fun target(state: DerivationState): Int? = if (state.stage == DerivationStage.FINAL) null else
        (0 until state.terms.lastIndex).firstOrNull { index ->
        val left = state.terms[index]
        val right = state.terms[index + 1]
        val first = right.varnas.firstOrNull()
        left.varnas.lastOrNull() in setOf(Svara.A, Svara.AA) &&
            first != null && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AC, first) &&
            ((right.kind != TermKind.AGAMA && isRoot(right) &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.EC, first)) ||
                (right.kind == TermKind.AGAMA && right.upadesha == "ऊठ्")) &&
            !state.isBlockedAtBoundary(sutra, left.id, right.id)
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val index = requireNotNull(target(context))
        val left = context.terms[index]
        val right = context.terms[index + 1]
        val rightVowel = right.varnas.first() as Svara
        val substitute = requireNotNull(Varnamala.getVrddhi(rightVowel))
        val composed = context.replaceTermVarna(left.id, left.varnas.lastIndex, substitute, sutra)
            .deleteTermVarnas(right.id, 0, 1, sutra)
            .concatenateAfterInitialVowelCoalescence(left.id, right, sutra,
                transferDeferredDesignations = true,
                coalescedLeftVarnaIndices = left.varnas.lastIndex until left.varnas.lastIndex + substitute.size)
        return DerivationChange(if (right.kind == TermKind.DHATU) composed.copy(stage = DerivationStage.ANGAKARYA) else composed,
            "6.1.89: vṛddhi (${substitute.toDevanagari()}) for अवर्ण + ${right.upadesha}.")
    }
}
