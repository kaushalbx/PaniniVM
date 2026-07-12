package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.3.103: substitutes ए for final अ before plural झल-initial sup affix. */
object BahuvacaneJhalyetSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.103",
    text = "बहुवचने झल्येत्",
    hindiExplanation = "झलादि बहुवचन सुप् के परे अकारान्त अङ्ग का अकार एकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730103,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isAEnding = stem.surface.endsWith('अ') || stem.surface.endsWith('ा')
        val firstChar = affix.surface.firstOrNull() ?: return false
        
        val isPlural = SemanticFeature.BAHUVACANA in context.semanticFeatures
        
        return isAEnding && isPlural && isJhal(firstChar) && 
                context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val stem = terms[terms.size - 2]
        val oldChar = stem.surface.last()
        val newSurface = stem.surface.dropLast(1) + "े"
        
        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + stem.copy(surface = newSurface) + terms.last(),
                stage = DerivationStage.ANGAKARYA
            ).addSubstitution(VarnaSubstitution(stem.id, oldChar, "े", sutra)),
            explanation = "7.3.103: Substituted 'e' for final 'a' before plural jhal-initial sup."
        )
    }

    private fun isJhal(c: Char): Boolean = c in setOf(
        'झ', 'भ', 'घ', 'ढ', 'ध', 'ज', 'ब', 'ग', 'ड', 'द', 'ख', 'फ', 'छ', 'ठ', 'थ', 'च', 'ट', 'त', 'क', 'प', 'श', 'ष', 'स', 'ह'
    )
}
