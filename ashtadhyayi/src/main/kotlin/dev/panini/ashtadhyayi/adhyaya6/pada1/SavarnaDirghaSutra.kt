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
import dev.panini.shiksha.Varnamala
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
        if (leftTerm.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LOT && context.terms.last().upadesha == "झि") return false
        val leftChar = leftTerm.surface.lastOrNull() ?: return false
        val right = context.terms[rightIndex].surface.firstOrNull() ?: return false

        val leftPhoneme = if (leftChar !in Varnamala.independentVowelsOrMarks) 'अ' else leftChar
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.AK, leftPhoneme) &&
            engine.contains(Pratyahara.AK, right) &&
            normalize(leftPhoneme) == normalize(right) &&
            Varnamala.areSavarna(leftPhoneme, right)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val (leftIndex, rightIndex) = requireNotNull(targetPair(context))
        val leftTerm = terms[leftIndex]
        val rightTerm = terms[rightIndex]

        val leftChar = leftTerm.surface.last()
        val leftPhoneme = if (leftChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) 'अ' else leftChar
        val substitute = getDirgha(leftPhoneme)

        val newSurface = if (leftChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
            leftTerm.surface + substitute + rightTerm.surface.drop(1)
        } else {
            leftTerm.surface.dropLast(1) + substitute + rightTerm.surface.drop(1)
        }

        return DerivationChange(
            state = context.copy(
                terms = terms.take(leftIndex) + leftTerm.copy(surface = newSurface) + terms.drop(rightIndex + 1),
                droppedTerms = context.droppedTerms + rightTerm.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            ).addSubstitution(VarnaSubstitution(leftTerm.id, leftPhoneme, substitute, sutra)),
            explanation = "6.1.101: Savarṇa Dīrgha substitution ($substitute) for $leftPhoneme + ${rightTerm.surface.first()}."
        )
    }

    private fun getDirgha(c: Char): String = when (normalize(c)) {
        'अ' -> "ा"
        'इ' -> "ी"
        'उ' -> "ू"
        'ऋ' -> "ॄ"
        'ऌ' -> "ॄ" // ऌ doesn't have a dīrgha; ऋ is its savarṇa equivalent.
        else -> c.toString()
    }

    private fun normalize(c: Char): Char = when (c) {
        'अ', 'आ', 'ा' -> 'अ'
        'इ', 'ई', 'ि', 'ी' -> 'इ'
        'उ', 'ऊ', 'ु', 'ू' -> 'उ'
        'ऋ', 'ॠ', 'ृ', 'ॄ' -> 'ऋ'
        'ऌ', 'ॢ' -> 'ऌ'
        else -> c
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
                augment.surface.firstOrNull() in setOf('आ', 'ा')
        }?.let { return it to it + 1 }
        if (context.terms.size > 2 && context.terms.all { it.id.startsWith("sankhya_") }) {
            return (0 until context.terms.lastIndex).firstOrNull { index ->
                val left = context.terms[index].surface.lastOrNull() ?: return@firstOrNull false
                val right = context.terms[index + 1].surface.firstOrNull() ?: return@firstOrNull false
                val leftPhoneme = if (left !in Varnamala.independentVowelsOrMarks) 'अ' else left
                normalize(leftPhoneme) == normalize(right) && Varnamala.areSavarna(leftPhoneme, right)
            }?.let { it to it + 1 }
        }
        return (context.terms.lastIndex - 1) to context.terms.lastIndex
    }
}
