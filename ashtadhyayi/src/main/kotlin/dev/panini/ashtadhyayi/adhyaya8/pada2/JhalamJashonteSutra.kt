package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignationRemap
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.2.39: jhalāṃ jaśo'nte.
 * At the end of a pada (word), jhal sounds (stops and fricatives)
 * are replaced by jaś sounds (voiced unaspirated stops).
 */
object JhalamJashonteSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.39",
    text = "झलां जशोऽन्ते",
    hindiExplanation = "पदान्त में झल् वर्णों के स्थान पर जश् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820039,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = dev.panini.sutra.SutraStage.FINAL_CONSONANT_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Must be a Pada (per 1.4.14)
        val internal = internalSankhyaTerm(context)
        if (isSankhyaCompound(context) && internal == null) return false
        val lastTerm = internal ?: context.terms.lastOrNull() ?: return false
        val finalConsonant = getFinalConsonant(lastTerm) ?: return false
        if (finalConsonant == Vyanjana.SA) return false
        if (finalConsonant in jash) return false
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, finalConsonant.devanagari.single())
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = internalSankhyaTerm(context) ?: context.terms.last()
        val finalConsonant = requireNotNull(getFinalConsonant(lastTerm))

        // Use 1.1.50 logic to pick the best voiced substitute
        val potentialSubstitutes = setOf("ज", "ब", "ग", "ड", "द")
        val substituteText = SthaneAntaratamahSutra.selectBest(finalConsonant.devanagari.single(), potentialSubstitutes)
        val substitute = requireNotNull(Vyanjana.fromDevanagari(substituteText.single()))
        val result = lastTerm.varnas.dropLast(1) + substitute

        val changed = if (lastTerm.kind in setOf(TermKind.PRATYAYA, TermKind.AGAMA, TermKind.AUGMENT)) {
            val remaps = (lastTerm.itDesignations + lastTerm.deferredItDesignations).map { designation ->
                ItDesignationRemap(
                    designation.start, designation.endExclusive,
                    designation.start, designation.endExclusive,
                )
            }
            context.replaceWholeAffix(
                id = lastTerm.id,
                varnas = result,
                sutra = sutra,
                policy = WholeAffixDesignationPolicy.PreserveAndRemap(remaps),
            ).addSubstitution(VarnaSubstitution(lastTerm.id, finalConsonant.devanagari.single(), substitute.devanagari, sutra))
        } else {
            context.substituteTermVarnas(lastTerm.id, result, finalConsonant, listOf(substitute), sutra)
        }

        return DerivationChange(
            state = changed.copy(stage = DerivationStage.FINAL),
            explanation = "8.2.39: Voiced the final consonant $finalConsonant to $substitute."
        )
    }

    private fun getFinalConsonant(term: dev.panini.derivation.DerivationTerm): Vyanjana? = term.varnas.lastOrNull() as? Vyanjana

    private fun internalSankhyaTerm(context: DerivationState) = context.terms.firstOrNull { term ->
        term != context.terms.last() && context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.SANKHYA } &&
            getFinalConsonant(term) != null
    }

    private fun isSankhyaCompound(context: DerivationState): Boolean = context.terms.size > 1 &&
        context.terms.all { term -> context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.SANKHYA } }

    private val jash = setOf(Vyanjana.JA, Vyanjana.BA, Vyanjana.GA, Vyanjana.DDA, Vyanjana.DA)
}
