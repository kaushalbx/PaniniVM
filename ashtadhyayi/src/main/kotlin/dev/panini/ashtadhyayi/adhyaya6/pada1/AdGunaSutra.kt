package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.replaceLastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toVarnas
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
        val rutvaFollowUp = context.substitutions.lastOrNull()?.takeIf { it.sutra == "6.1.114" }
        if (rutvaFollowUp != null && context.terms.any { it.id == rutvaFollowUp.targetId && it.surface.lastVarna() == Svara.U }) return true
        if (context.terms.size < 2) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LOT && context.terms.last().upadesha == "झि") return false
        val engine = Ashtadhyayi.pratyaharaEngine
        return context.terms.indices.any { index ->
            if (index == context.terms.lastIndex) return@any false
            val leftTerm = context.terms[index]
            val rightTerm = context.terms[index + 1]
            if (rightTerm.upadesha == "इट्" && rightTerm.surface.endsWith("ट्")) return@any false
            val isTaddhita = "4.1.76" in context.activeAdhikaras ||
                rightTerm.upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्")
            if (isTaddhita) return@any false
            val right = rightTerm.varnas.firstOrNull() ?: return@any false
            val leftVarnas = leftTerm.varnas
            val isA = leftVarnas.lastOrNull() in setOf(Svara.A, Svara.AA)
            val previousEndsInEc = index > 0 && context.terms[index - 1].surface.lastVarna()?.let {
                engine.contains(Pratyahara.EC, it)
            } == true
            val isFutureSya = leftTerm.upadesha == "स्य" &&
                context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG)
            val isAdadiShap = leftTerm.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }
            !isAdadiShap && (!previousEndsInEc || isFutureSya) && leftVarnas.getOrNull(leftVarnas.lastIndex - 1) != Vyanjana.NA &&
                isA && engine.contains(Pratyahara.AC, right)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        if (context.substitutions.lastOrNull()?.sutra == "6.1.114") {
            val targetId = context.substitutions.last().targetId
            val target = context.terms.firstOrNull { it.id == targetId && it.surface.lastVarna() == Svara.U }
            if (target != null) return DerivationChange(
                state = context.substituteTermSurface(
                    target.id, target.surface.replaceLastVarna(Svara.U, listOf(Svara.O)),
                    Svara.U, listOf(Svara.O), sutra,
                ),
                explanation = "6.1.87: Guṇa substitution ओ for अ + उ from रुँ."
            )
        }
        val terms = context.terms
        val index = terms.indices.first { position ->
            position < terms.lastIndex &&
                terms[position].surface.lastVarna() in setOf(Svara.A, Svara.AA) &&
                !(terms[position].id == "shap" && terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }) &&
                !(terms[position + 1].upadesha == "इट्" && terms[position + 1].surface.endsWith("ट्")) &&
                !("4.1.76" in context.activeAdhikaras ||
                    terms[position + 1].upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्")) &&
                terms[position].varnas.let { it.getOrNull(it.lastIndex - 1) != Vyanjana.NA } &&
                (position == 0 ||
                    terms[position - 1].surface.lastVarna()?.let {
                        !Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.EC, it)
                    } != false ||
                    (terms[position].upadesha == "स्य" && context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG))) &&
                terms[position + 1].varnas.firstOrNull()?.let {
                    Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AC, it)
                } == true
        }
        val leftTerm = terms[index]
        val rightTerm = terms[index + 1]

        val leftVarnas = leftTerm.varnas
        val rightVarnas = rightTerm.varnas
        val leftVowel = leftVarnas.last() as Svara
        val rightVowel = rightVarnas.first() as Svara
        val substitute = Varnamala.getGuna(rightVowel) ?: listOf(Svara.A)
        val isBeginningAugment = leftTerm.kind == TermKind.AGAMA &&
            !leftTerm.mergeIntoAugmentTarget &&
            leftTerm.augmentTargetId == rightTerm.id &&
            "1.1.46" in leftTerm.establishedBySutras

        val newSurface = (if (isBeginningAugment) {
            substitute + rightVarnas.drop(1)
        } else {
            leftVarnas.dropLast(1) + substitute + rightVarnas.drop(1)
        }).toDevanagari()
        val survivor = if (isBeginningAugment) rightTerm else leftTerm
        val consumedTerm = if (isBeginningAugment) leftTerm else rightTerm

        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                survivor.id, consumedTerm.id, newSurface, leftVowel, substitute, sutra,
            ).copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.87: Guṇa substitution (${substitute.toDevanagari()}) for ${leftVowel.devanagari} + ${rightVowel.devanagari}."
        )
    }
}
