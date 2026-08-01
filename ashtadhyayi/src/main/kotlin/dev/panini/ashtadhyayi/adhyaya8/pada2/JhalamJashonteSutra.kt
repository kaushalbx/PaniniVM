package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
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
        val finalConsonant = getFinalConsonant(lastTerm.surface) ?: return false
        if (finalConsonant == 'स') return false
        if (finalConsonant in setOf('ज', 'ब', 'ग', 'ड', 'द')) return false
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, finalConsonant)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = internalSankhyaTerm(context) ?: context.terms.last()
        val finalConsonant = getFinalConsonant(lastTerm.surface)!!

        // Use 1.1.50 logic to pick the best voiced substitute
        val potentialSubstitutes = setOf("ज", "ब", "ग", "ड", "द")
        val substitute = SthaneAntaratamahSutra.selectBest(finalConsonant, potentialSubstitutes)

        val newSurface = if (lastTerm.surface.endsWith('र')) {
            lastTerm.surface.dropLast(1) + substitute
        } else {
            lastTerm.surface.dropLast(2) + substitute + '्'
        }

        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.2.39: Voiced the final consonant $finalConsonant to $substitute."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(lastTerm.id, finalConsonant, substitute, sutra))) }
    }

    private fun getFinalConsonant(surface: String): Char? {
        if (surface.isEmpty()) return null
        if (surface.endsWith('र')) return 'र'
        if (surface.endsWith('्') && surface.length >= 2) {
            return surface[surface.length - 2]
        }
        return null
    }

    private fun internalSankhyaTerm(context: DerivationState) = context.terms.firstOrNull { term ->
        term != context.terms.last() && context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.SANKHYA } &&
            getFinalConsonant(term.surface) != null
    }

    private fun isSankhyaCompound(context: DerivationState): Boolean = context.terms.size > 1 &&
        context.terms.all { term -> context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.SANKHYA } }
}
