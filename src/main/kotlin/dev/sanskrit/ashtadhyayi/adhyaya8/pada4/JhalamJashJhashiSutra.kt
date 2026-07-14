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
 * 8.4.53: jhalāṃ jaś jhaśi.
 * Substitutes jaś (voiced unaspirated stops) for jhal (stops + fricatives) 
 * when followed by jhaś (voiced stops).
 */
object JhalamJashJhashiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.53",
    text = "झलां जश् झशि",
    hindiExplanation = "झल् वर्णों के स्थान पर जश् आदेश होता है यदि बाद में झश् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840053,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Matches adjacent consonants within a word or at word boundaries.
        // For simplicity, we check across term boundaries.
        if (context.terms.size < 2) return false
        
        val left = context.terms[context.terms.size - 2].surface.lastOrNull() ?: return false
        val right = context.terms.last().surface.firstOrNull() ?: return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, left) &&
            engine.contains(Pratyahara.JHASH, right) &&
            substituteFor(left) != left.toString()
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val leftTerm = terms[terms.size - 2]
        val rightTerm = terms.last()
        
        val leftChar = leftTerm.surface.last()
        
        val substitute = substituteFor(leftChar)
        
        val newSurface = leftTerm.surface.dropLast(1) + substitute
        
        return DerivationChange(
            state = context.replaceTerm(leftTerm.id, leftTerm.copy(surface = newSurface))
                .addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, substitute, sutra)),
            explanation = "8.4.53: Substituted voiced $substitute for $leftChar before ${rightTerm.surface.first()}."
        )
    }

    private fun substituteFor(source: Char): String =
        SthaneAntaratamahSutra.selectBest(source, setOf("ज", "ब", "ग", "ड", "द"))
}
