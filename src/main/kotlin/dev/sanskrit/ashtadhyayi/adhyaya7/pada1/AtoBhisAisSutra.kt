package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.9: Ato bhis ais. 
 * Replaces instrumental-plural 'bhis' with 'ais' after an a-final stem.
 * This is an Apavāda to the general rule (not yet implemented) that would keep 'bhis'.
 */
object AtoBhisAisSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.9",
    text = "अतो भिस ऐस्",
    hindiExplanation = "अकारान्त अङ्ग के बाद भिस् के स्थान पर ऐस् होता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710009,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        
        val isAEnding = dev.sanskrit.shiksha.Varnamala.endsWithA(stem.surface) ||
            dev.sanskrit.shiksha.Varnamala.endsWithAA(stem.surface)
        
        return isAEnding && affix.surface == "भिस्" && 
                context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "ऐस्"))
                .addSubstitution(VarnaSubstitution(affix.id, 'भ', "ऐस्", sutra)),
            explanation = "7.1.9 substitutes ऐस् for instrumental-plural भिस्."
        )
    }
}
