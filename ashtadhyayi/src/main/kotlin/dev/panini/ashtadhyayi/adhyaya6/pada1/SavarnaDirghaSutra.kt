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
import dev.panini.shiksha.firstVarna
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toDirgha
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 6.1.101: akaḥ savarṇe dīrghaḥ. Adjacent homogeneous vowels combine into a single long vowel. */
object SavarnaDirghaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.101",
    text = "अकः सवर्णे दीर्घः",
    hindiExplanation = "अक् प्रत्याहार के स्वर के बाद उसी सवर्ण का स्वर आए तो दोनों के स्थान पर दीर्घ स्वर होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610101,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.VOWEL_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        val (leftIndex, rightIndex) = targetPair(context) ?: return false
        val leftTerm = context.terms[leftIndex]
        if (leftIndex > 0 && leftTerm.id == "shap") {
            val previous = context.terms[leftIndex - 1]
            val previousFinal = previous.surface.lastVarna()
            if (previous.upadesha == "णिच्" && previousFinal != null &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.EC, previousFinal)
            ) return false
        }
        if (leftTerm.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LOT && context.terms.last().upadesha == "झि") return false
        val left = leftTerm.surface.lastVarna() ?: return false
        val right = context.terms[rightIndex].surface.firstVarna() ?: return false

        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.AK, left) &&
            engine.contains(Pratyahara.AK, right) &&
            Varnamala.areSavarna(left, right)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val (leftIndex, rightIndex) = requireNotNull(targetPair(context))
        val leftTerm = terms[leftIndex]
        val rightTerm = terms[rightIndex]

        val leftVowel = requireNotNull(leftTerm.surface.lastVarna() as? Svara)
        val substitute = listOf(if (leftVowel in setOf(Svara.L, Svara.LL)) Svara.RR else leftVowel.toDirgha())
        val isBeginningAugment = leftTerm.kind == TermKind.AGAMA &&
            !leftTerm.mergeIntoAugmentTarget &&
            leftTerm.augmentTargetId == rightTerm.id &&
            "1.1.46" in leftTerm.establishedBySutras

        val rightVarnas = rightTerm.varnas
        val newSurface = if (isBeginningAugment) {
            (substitute + rightVarnas.drop(1)).toDevanagari()
        } else {
            (leftTerm.varnas.dropLast(1) + substitute + rightVarnas.drop(1)).toDevanagari()
        }
        val survivor = if (isBeginningAugment) rightTerm else leftTerm
        val consumedTerm = if (isBeginningAugment) leftTerm else rightTerm

        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                survivor.id, consumedTerm.id, newSurface, leftVowel, substitute, sutra,
            ).copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.101: Savarṇa Dīrgha substitution (${substitute.toDevanagari()}) " +
                "for $leftVowel + ${rightVarnas.first()}."
        )
    }

    private fun targetPair(context: DerivationState): Pair<Int, Int>? {
        if (context.terms.size < 2) return null
        context.terms.indices.firstOrNull { index ->
            if (index == context.terms.lastIndex) return@firstOrNull false
            val augment = context.terms[index + 1]
            augment.kind == TermKind.AGAMA &&
                !augment.mergeIntoAugmentTarget &&
                augment.augmentTargetId != null &&
                "1.1.46" in augment.establishedBySutras &&
                augment.surface.firstVarna() == Svara.AA
        }?.let { return it to it + 1 }
        if (context.terms.size > 2 && context.terms.all { it.id.startsWith("sankhya_") }) {
            return (0 until context.terms.lastIndex).firstOrNull { index ->
                val left = context.terms[index].surface.lastVarna() ?: return@firstOrNull false
                val right = context.terms[index + 1].surface.firstVarna() ?: return@firstOrNull false
                Varnamala.areSavarna(left, right)
            }?.let { it to it + 1 }
        }
        return (context.terms.lastIndex - 1) to context.terms.lastIndex
    }
}
