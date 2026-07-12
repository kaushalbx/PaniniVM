package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 6.1.79: vānto yi pratyaye.
 * Substitutes 'av' and 'āv' (vānta) for 'o' and 'au' respectively 
 * when followed by a suffix (pratyaya) beginning with 'y'.
 */
object VantoYiPratyayeSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.79",
    text = "वान्तो यि प्रत्यये",
    hindiExplanation = "यकारादि प्रत्यय परे होने पर ओकार को 'अव्' और औकार को 'आव्' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610079,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    nimittaScope = NimittaScope.EXTERNAL
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Affix must be a Pratyaya and start with 'y'
        val isYPratyaya = affix.surface.startsWith('य') && 
                        context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
        if (!isYPratyaya) return false

        // 2. Stem must end in 'o' or 'au'
        val lastChar = stem.surface.lastOrNull() ?: return false
        return lastChar == 'ओ' || lastChar == 'ो' || lastChar == 'औ' || lastChar == 'ौ'
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        
        val replacement = when (lastChar) {
            'ओ', 'ो' -> "अव्"
            'औ', 'ौ' -> "आव्"
            else -> ""
        }

        val newSurface = stem.surface.dropLast(1) + replacement
        
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.1.79: Substituted '$replacement' for '$lastChar' before y-initial affix."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(stem.id, lastChar, replacement, sutra))) }
    }
}
