package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
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

/** 6.1.87: a/ā + ac -> guṇa. */
object AdGunaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.87",
    text = "आद्गुणः",
    hindiExplanation = "अ या आ के बाद अच् आए तो पूर्व और पर के स्थान पर एक गुणादेश होता है।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610087,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        val leftTerm = context.terms[context.terms.size - 2]
        val right = context.terms.last().surface.firstOrNull() ?: return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        // Match a/ā
        val isA = dev.sanskrit.shiksha.Varnamala.endsWithA(leftTerm.surface) || dev.sanskrit.shiksha.Varnamala.endsWithAA(leftTerm.surface)
        return isA && engine.contains(Pratyahara.AC, right)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val leftTerm = terms[terms.size - 2]
        val rightTerm = terms.last()
        
        val leftChar = leftTerm.surface.last()
        val rightChar = rightTerm.surface.first()
        
        val substitute = getGuna(rightChar)
        
        val newSurface = if (leftChar !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) {
            leftTerm.surface + substitute + rightTerm.surface.drop(1)
        } else {
            leftTerm.surface.dropLast(1) + substitute + rightTerm.surface.drop(1)
        }
        
        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + leftTerm.copy(surface = newSurface),
                droppedTerms = context.droppedTerms + terms.last().copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            ).addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, substitute, sutra)),
            explanation = "6.1.87: Guṇa substitution ($substitute) for $leftChar + $rightChar."
        )
    }

    private fun getGuna(right: Char): String = when (right) {
        'इ', 'ई', 'ि', 'ी' -> "े"
        'उ', 'ऊ', 'ु', 'ू' -> "ो"
        'ऋ', 'ॠ', 'ृ', 'ॄ' -> "अर्"
        else -> "अ" 
    }
}
