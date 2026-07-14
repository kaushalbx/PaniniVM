package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 8.4.55: khari ca.
 * Substitutes car (unaspirated voiceless stops) for jhal (stops + fricatives) 
 * when followed by khar (voiceless sounds).
 */
object KhariCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.55",
    text = "खरि च",
    hindiExplanation = "झल् वर्णों के स्थान पर चर् आदेश होता है यदि बाद में खर् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840055,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        
        val left = context.terms[context.terms.size - 2].surface.lastOrNull() ?: return false
        val right = context.terms.last().surface.firstOrNull() ?: return false
        // A plain final consonant is followed by its inherent अ (फल + स्य),
        // so it is not a direct hal–khar contact.
        if (Varnamala.isConsonant(left)) return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, left) &&
            engine.contains(Pratyahara.KHAR, right) &&
            substituteFor(left) != left.toString()
    }

    override fun apply(context: DerivationState): DerivationChange {
        val leftTerm = context.terms[context.terms.size - 2]
        val leftChar = leftTerm.surface.last()
        
        val substitute = substituteFor(leftChar)
        
        val newSurface = leftTerm.surface.dropLast(1) + substitute
        
        return DerivationChange(
            state = context.replaceTerm(leftTerm.id, leftTerm.copy(surface = newSurface)),
            explanation = "8.4.55: Devoiced $leftChar to $substitute before voiceless sound."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, substitute, sutra))) }
    }

    private fun substituteFor(source: Char): String =
        SthaneAntaratamahSutra.selectBest(source, setOf("च", "ट", "त", "क", "प"))
}
