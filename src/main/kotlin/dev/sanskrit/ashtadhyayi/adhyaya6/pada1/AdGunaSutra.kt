package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
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
    blocks = setOf("6.1.77"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LOT && context.terms.last().upadesha == "झि") return false
        val engine = Ashtadhyayi.pratyaharaEngine
        return context.terms.indices.any { index ->
            if (index == context.terms.lastIndex) return@any false
            val leftTerm = context.terms[index]
            val rightTerm = context.terms[index + 1]
            if (rightTerm.upadesha == "इट्" && rightTerm.surface.endsWith("ट्")) return@any false
            val right = rightTerm.surface.firstOrNull() ?: return@any false
            val isA = dev.sanskrit.shiksha.Varnamala.endsWithA(leftTerm.surface) ||
                dev.sanskrit.shiksha.Varnamala.endsWithAA(leftTerm.surface)
            val previousEndsInEc = index > 0 && engine.contains(Pratyahara.EC, context.terms[index - 1].surface.lastOrNull() ?: return@any false)
            val isFutureSya = leftTerm.upadesha == "स्य" &&
                context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG)
            val isAdadiShap = leftTerm.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == Gana.ADADI }
            !isAdadiShap && (!previousEndsInEc || isFutureSya) && !leftTerm.surface.endsWith('न') &&
                isA && engine.contains(Pratyahara.AC, right)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val index = terms.indices.first { position ->
            position < terms.lastIndex &&
                (dev.sanskrit.shiksha.Varnamala.endsWithA(terms[position].surface) ||
                    dev.sanskrit.shiksha.Varnamala.endsWithAA(terms[position].surface)) &&
                !(terms[position].id == "shap" && terms.any { it.kind == TermKind.DHATU && it.gana == Gana.ADADI }) &&
                !(terms[position + 1].upadesha == "इट्" && terms[position + 1].surface.endsWith("ट्")) &&
                !terms[position].surface.endsWith('न') &&
                (position == 0 ||
                    !Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.EC, terms[position - 1].surface.lastOrNull() ?: return@first false) ||
                    (terms[position].upadesha == "स्य" && context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG))) &&
                terms[position + 1].surface.firstOrNull()?.let {
                    Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AC, it)
                } == true
        }
        val leftTerm = terms[index]
        val rightTerm = terms[index + 1]
        
        val leftChar = leftTerm.surface.last()
        val rightChar = rightTerm.surface.first()
        
        val substitute = getGuna(rightChar)
        
        val newSurface = if (leftChar !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) {
            if (substitute == "अ") leftTerm.surface + rightTerm.surface.drop(1)
            else leftTerm.surface + substitute + rightTerm.surface.drop(1)
        } else {
            leftTerm.surface.dropLast(1) + substitute + rightTerm.surface.drop(1)
        }
        
        return DerivationChange(
            state = context.copy(
                terms = terms.take(index) + leftTerm.copy(surface = newSurface) + terms.drop(index + 2),
                droppedTerms = context.droppedTerms + rightTerm.copy(surface = ""),
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
