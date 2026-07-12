package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 6.1.109: eṅaḥ padāntādati.
 * When a pada-final 'e' or 'o' (Eṅ) is followed by short 'a' (at), 
 * a single substitute of the former (pūrvarūpa) replaces both.
 */
object EngahPadantadatiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.109",
    text = "एङः पदान्तादति",
    hindiExplanation = "पदान्त एङ् (ए, ओ) के बाद ह्रस्व अकार आने पर पूर्वरूप एकादेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610109,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        // 1. Left term must be a 'pada' (per 1.4.14)
        val isPada = context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA }
        if (!isPada) return false

        // 2. Left term must end in 'e' or 'o'
        val lastChar = left.surface.lastOrNull() ?: return false
        val engine = Ashtadhyayi.pratyaharaEngine
        if (!engine.contains(Pratyahara.EC, lastChar) || (lastChar != 'ए' && lastChar != 'ओ' && lastChar != 'े' && lastChar != 'ो')) return false

        // 3. Right term must start with short 'a'
        val nextChar = right.surface.firstOrNull() ?: return false
        return nextChar == 'अ'
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val left = terms[terms.size - 2]
        val right = terms.last()
        
        // Pūrvarūpa: the first vowel stays, the second disappears (represented by avagraha in modern script)
        val replacement = left.surface.last().toString()
        val newRightSurface = "ऽ" + right.surface.drop(1)
        
        return DerivationChange(
            state = context.replaceTerm(right.id, right.copy(surface = newRightSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "6.1.109: Pūrvarūpa substitution for final ${left.surface.last()} + अ."
        )
    }
}
