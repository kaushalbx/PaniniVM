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
        val engine = Ashtadhyayi.pratyaharaEngine
        for (i in 0 until context.terms.size) {
            val term = context.terms[i]
            if (term.kind != TermKind.PRATYAYA) continue
            val surface = term.surface
            
            val sIndex = surface.indexOf('स')
            if (sIndex == -1) continue
            
            val preChar = if (sIndex == 0) {
                if (i == 0) continue
                val stemFinal = context.terms[i - 1].surface.lastOrNull() ?: continue
                if (stemFinal !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) 'अ' else stemFinal
            } else {
                surface[sIndex - 1]
            }
            
            val isInIn = engine.contains(Pratyahara.IN, preChar)
            val isInKu = isKu(preChar)
            
            if (isInIn || isInKu) return true
        }
        return false
    }

    override fun apply(context: DerivationState): DerivationChange {
        val engine = Ashtadhyayi.pratyaharaEngine
        for (i in 0 until context.terms.size) {
            val term = context.terms[i]
            if (term.kind != TermKind.PRATYAYA) continue
            val surface = term.surface
            
            val sIndex = surface.indexOf('स')
            if (sIndex == -1) continue
            
            val preChar = if (sIndex == 0) {
                if (i == 0) continue
                val stemFinal = context.terms[i - 1].surface.lastOrNull() ?: continue
                if (stemFinal !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) 'अ' else stemFinal
            } else {
                surface[sIndex - 1]
            }
            
            val isInIn = engine.contains(Pratyahara.IN, preChar)
            val isInKu = isKu(preChar)
            
            if (isInIn || isInKu) {
                val newSurface = term.surface.replace('स', 'ष')
                return DerivationChange(
                    state = context.replaceTerm(term.id, term.copy(surface = newSurface))
                        .copy(stage = DerivationStage.FINAL),
                    explanation = "8.3.59: Retroflexed 's' to 'ṣ' after Iṇ/Ku."
                )
            }
        }
        return DerivationChange(context, "8.3.59: No match found")
    }

    private fun isKu(c: Char): Boolean = c in setOf('क', 'ख', 'ग', 'घ', 'ङ')
}
