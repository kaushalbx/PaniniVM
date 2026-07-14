package dev.sanskrit.ashtadhyayi.adhyaya8.pada3

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 8.3.59: ādeśapratyayayoḥ. 
 * Substitutes 'ṣ' for 's' if 's' is part of an ādeśa (substitute) or pratyaya (affix), 
 * and is preceded by a sound in the Iṇ pratyāhāra or Ku (ka-varga).
 */
object AdesapratyayayohSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.59",
    text = "आदेशप्रत्यययोः",
    hindiExplanation = "इण् (इ, उ, ऋ, लृ, ए, ओ, ऐ, औ, ह, य, व, र, ल) या कु (क-वर्ग) के बाद आदेश या प्रत्यय के 'स' का 'ष' होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830059,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // This rule applies in the Tripadi section (8.2.1 onwards)
        // It matches an 's' that is part of a suffix.
        val term = context.terms.lastOrNull() ?: return false
        if (term.kind != TermKind.PRATYAYA) return false
        val surface = term.surface
        
        // Find 's' and check the character before it
        val sIndex = surface.indexOf('स')
        if (sIndex == -1) return false
        
        val preChar = if (sIndex == 0) {
            if (context.terms.size < 2) return false
            val stemFinal = context.terms[context.terms.size - 2].surface.lastOrNull() ?: return false
            // An unmarked final consonant carries inherent अ.  Thus फल + स्य
            // has the environment -अस्य-, not -ल्स्य-.
            if (stemFinal !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) 'अ' else stemFinal
        } else {
            surface[sIndex - 1]
        }
        val engine = Ashtadhyayi.pratyaharaEngine
        
        // Check if preChar is in Iṇ or Ku
        val isInIn = engine.contains(Pratyahara.IN, preChar)
        val isInKu = isKu(preChar)
        
        return isInIn || isInKu
    }

    override fun apply(context: DerivationState): DerivationChange {
        val term = context.terms.last()
        val newSurface = term.surface.replace('स', 'ष')
        
        return DerivationChange(
            state = context.replaceTerm(term.id, term.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.3.59: Retroflexed 's' to 'ṣ' after Iṇ/Ku."
        )
    }

    private fun isKu(c: Char): Boolean = c in setOf('क', 'ख', 'ग', 'घ', 'ङ')
}
