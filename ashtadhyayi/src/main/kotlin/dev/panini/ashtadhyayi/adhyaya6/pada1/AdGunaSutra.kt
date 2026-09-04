package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

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
    stage = SutraStage.POST_RUTVA,
    blocks = setOf("6.1.77"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.substitutions.lastOrNull()?.sutra == "6.1.114" &&
            context.terms.any { term -> term.surface.endsWith("ु") && context.samjnas.any { it.targetId == term.id && it.samjna == dev.panini.shiksha.Samjna.SANKHYA } }) return true
        if (context.terms.size < 2) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LOT && context.terms.last().upadesha == "झि") return false
        val engine = Ashtadhyayi.pratyaharaEngine
        return context.terms.indices.any { index ->
            if (index == context.terms.lastIndex) return@any false
            val leftTerm = context.terms[index]
            val rightTerm = context.terms[index + 1]
            if (rightTerm.upadesha == "इट्" && rightTerm.surface.endsWith("ट्")) return@any false
            val isTaddhita = "4.1.76" in context.activeAdhikaras ||
                rightTerm.upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्") ||
                rightTerm.id.contains("apatya") || rightTerm.id.contains("taddhita")
            if (isTaddhita) return@any false
            val right = rightTerm.surface.firstOrNull() ?: return@any false
            val isA = dev.panini.shiksha.Varnamala.endsWithA(leftTerm.surface) ||
                dev.panini.shiksha.Varnamala.endsWithAA(leftTerm.surface)
            val previousEndsInEc = index > 0 && engine.contains(Pratyahara.EC, context.terms[index - 1].surface.lastOrNull() ?: return@any false)
            val isFutureSya = leftTerm.upadesha == "स्य" &&
                context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG)
            val isAdadiShap = leftTerm.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }
            !isAdadiShap && (!previousEndsInEc || isFutureSya) && !leftTerm.surface.endsWith('न') &&
                isA && engine.contains(Pratyahara.AC, right)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        if (context.substitutions.lastOrNull()?.sutra == "6.1.114") {
            val target = context.terms.firstOrNull { term ->
                term.surface.endsWith("ु") && context.samjnas.any { it.targetId == term.id && it.samjna == dev.panini.shiksha.Samjna.SANKHYA }
            }
            if (target != null) return DerivationChange(
                state = context.replaceTerm(target.id, target.copy(surface = target.surface.dropLast(1) + "ो"))
                    .addSubstitution(VarnaSubstitution(target.id, 'ु', "ो", sutra)),
                explanation = "6.1.87: Guṇa substitution ओ for अ + उ from रुँ."
            )
        }
        val terms = context.terms
        val index = terms.indices.first { position ->
            position < terms.lastIndex &&
                (dev.panini.shiksha.Varnamala.endsWithA(terms[position].surface) ||
                    dev.panini.shiksha.Varnamala.endsWithAA(terms[position].surface)) &&
                !(terms[position].id == "shap" && terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }) &&
                !(terms[position + 1].upadesha == "इट्" && terms[position + 1].surface.endsWith("ट्")) &&
                !("4.1.76" in context.activeAdhikaras ||
                    terms[position + 1].upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्") ||
                    terms[position + 1].id.contains("apatya") || terms[position + 1].id.contains("taddhita")) &&
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
        val isBeginningAugment = leftTerm.kind == TermKind.AGAMA &&
            !leftTerm.mergeIntoAugmentTarget &&
            leftTerm.augmentTargetId == rightTerm.id &&
            "1.1.46" in leftTerm.establishedBySutras

        val newSurface = if (isBeginningAugment) {
            val initial = when (substitute) {
                "ा" -> "आ"
                "े" -> "ए"
                "ो" -> "ओ"
                else -> substitute
            }
            initial + rightTerm.surface.drop(1)
        } else if (leftChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
            if (substitute == "अ") leftTerm.surface + rightTerm.surface.drop(1)
            else leftTerm.surface + substitute + rightTerm.surface.drop(1)
        } else {
            leftTerm.surface.dropLast(1) + substitute + rightTerm.surface.drop(1)
        }
        val mergedTerm = if (isBeginningAugment) rightTerm.copy(surface = newSurface) else leftTerm.copy(surface = newSurface)
        val consumedTerm = if (isBeginningAugment) leftTerm else rightTerm

        return DerivationChange(
            state = context.copy(
                terms = terms.take(index) + mergedTerm + terms.drop(index + 2),
                droppedTerms = context.droppedTerms + consumedTerm.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            ).addSubstitution(VarnaSubstitution(mergedTerm.id, leftChar, substitute, sutra)),
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
