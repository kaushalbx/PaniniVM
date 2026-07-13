package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.3: jho'ntaḥ. 
 * Replaces 'jh' of an affix with 'ant'. 
 * This is a general rule for verbal plural endings like 'jhi' -> 'anti'.
 */
object JhoAntahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.3",
    text = "झोऽन्तः",
    hindiExplanation = "प्रत्यय के 'झ' के स्थान पर 'अन्त्' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710003,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        
        // 1. Affix must start with 'jh'
        val surface = affix.surface
        val startsWithJh = surface.startsWith('झ')
        
        // 2. Must be a Pratyaya
        val isPratyaya = context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
        
        return startsWithJh && isPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val suffix = affix.surface.drop(1)
        val isMatra = suffix.firstOrNull() in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')
        val newSurface = (if (isMatra) "अन्त" else "अन्त्") + suffix
        
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = newSurface)),
            explanation = "7.1.3 substitutes 'ant' for 'jh' in the affix."
        )
    }
}
