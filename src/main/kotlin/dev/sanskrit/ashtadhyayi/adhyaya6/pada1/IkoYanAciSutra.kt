package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 6.1.77: Substituting yan for ik vowels before an ac (vowel). */
object IkoYanAciSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.77",
    text = "इको यणचि",
    hindiExplanation = "इक् (इ, उ, ऋ, लृ) के स्थान पर यण् (य्, व्, र्, ल्) होता है यदि बाद में अच् (कोई स्वर) हो।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610077,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.ANGAKARYA && context.stage != DerivationStage.PADA_FORMED) return false
        
        val terms = context.terms
        if (terms.size < 2) return false
        
        val left = terms[terms.size - 2].surface.lastOrNull() ?: return false
        val right = terms.last().surface.firstOrNull() ?: return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.IK, left) && 
               engine.contains(Pratyahara.AC, right) && 
               !isSavarna(left, right) // Savarna-dirgha (6.1.101) takes precedence
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val leftTerm = terms[terms.size - 2]
        val rightTerm = terms.last()
        
        val leftVowel = leftTerm.surface.last()
        val replacement = yanFor(leftVowel)
        
        val isMatra = leftVowel in setOf('ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ')
        val newLeftSurface = if (isMatra) {
            leftTerm.surface.dropLast(1) + "्" + replacement
        } else {
            leftTerm.surface.dropLast(1) + replacement
        }
        val mergedSurface = merge(newLeftSurface, rightTerm.surface)
        
        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + leftTerm.copy(surface = mergedSurface),
                droppedTerms = context.droppedTerms + rightTerm.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            ).addSubstitution(VarnaSubstitution(leftTerm.id, leftVowel, replacement, sutra)),
            explanation = "6.1.77: substituted $replacement for $leftVowel before vowel and merged terms."
        )
    }

    private fun merge(left: String, right: String): String {
        if (left.endsWith('्') && right.isNotEmpty()) {
            val first = right.first()
            val matra = when (first) {
                'अ' -> ""
                'आ' -> "ा"
                'इ' -> "ि"
                'ई' -> "ी"
                'उ' -> "ु"
                'ऊ' -> "ू"
                'ऋ' -> "ृ"
                'ॠ' -> "ॄ"
                'ऌ' -> "ॢ"
                'ए' -> "े"
                'ऐ' -> "ै"
                'ओ' -> "ो"
                'औ' -> "ौ"
                else -> null
            }
            if (matra != null) {
                return left.dropLast(1) + matra + right.drop(1)
            }
        }
        return left + right
    }

    private fun isSavarna(left: Char, right: Char): Boolean {
        // Simple savarna check: same vowel family
        val iks = setOf('इ', 'ई', 'ि', 'ी')
        val uks = setOf('उ', 'ऊ', 'ु', 'ू')
        val rks = setOf('ऋ', 'ॠ', 'ृ', 'ॄ')
        return (left in iks && right in iks) || (left in uks && right in uks) || (left in rks && right in rks)
    }
    
    private fun yanFor(c: Char): String = when(c) {
        'इ', 'ई', 'ि', 'ी' -> Vyanjana.YA.halanta
        'उ', 'ऊ', 'ु', 'ू' -> Vyanjana.VA.halanta
        'ऋ', 'ॠ', 'ृ', 'ॄ' -> Vyanjana.RA.halanta
        'ऌ', 'ॢ' -> Vyanjana.LA.halanta
        else -> ""
    }
}
