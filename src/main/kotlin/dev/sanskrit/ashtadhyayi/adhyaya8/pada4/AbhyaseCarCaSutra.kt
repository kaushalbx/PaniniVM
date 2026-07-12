package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

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
 * 8.4.54: abhyāse car ca.
 * In the reduplicated syllable (abhyāsa), a jhal sound is replaced by 
 * a car sound (voiceless) or a jaś sound (voiced).
 * Effectively: Aspirated becomes Unaspirated.
 */
object AbhyaseCarCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.54",
    text = "अभ्यासे चर्च",
    hindiExplanation = "अभ्यास में झल् वर्णों के स्थान पर चर् और जश् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840054,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Condition: The term must be an 'abhyāsa' (reduplication)
        // Simplified check: first term in a multi-term verbal stem
        if (context.terms.size < 2) return false
        val firstTerm = context.terms.first()
        
        // Target: Must contain a Jhal sound (aspirated or fricative)
        val lastChar = firstTerm.surface.lastOrNull() ?: return false
        val engine = Ashtadhyayi.pratyaharaEngine
        
        return engine.contains(Pratyahara.JHAL, lastChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first()
        val lastChar = abhyasa.surface.last()
        
        // Potential substitutes: JAŚ (voiced) + CAR (voiceless)
        val potentialSubstitutes = setOf("ज", "ब", "ग", "ड", "द", "च", "ट", "त", "क", "प")
        val substitute = SthaneAntaratamahSutra.selectBest(lastChar, potentialSubstitutes)
        
        val newSurface = abhyasa.surface.dropLast(1) + substitute
        
        return DerivationChange(
            state = context.replaceTerm(abhyasa.id, abhyasa.copy(surface = newSurface)),
            explanation = "8.4.54: Simplified aspirated sound in abhyāsa to $substitute."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(abhyasa.id, lastChar, substitute, sutra))) }
    }
}
