package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Must be a Pada (per 1.4.14)
        val lastTerm = context.terms.lastOrNull() ?: return false
        val finalConsonant = getFinalConsonant(lastTerm.surface) ?: return false
        if (finalConsonant == 'स') return false
        if (finalConsonant in setOf('ज', 'ब', 'ग', 'ड', 'द')) return false
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, finalConsonant)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
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
}
