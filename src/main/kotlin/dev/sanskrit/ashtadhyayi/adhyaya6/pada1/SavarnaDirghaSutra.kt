package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2].surface.lastOrNull() ?: return false
        val right = context.terms.last().surface.firstOrNull() ?: return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.AK, left) && Varnamala.areSavarna(left, right)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val leftTerm = terms[terms.size - 2]
        val rightTerm = terms.last()
        
        val leftChar = leftTerm.surface.last()
        val substitute = getDirgha(leftChar)
        
        val newSurface = leftTerm.surface.dropLast(1) + substitute + rightTerm.surface.drop(1)
        
        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + leftTerm.copy(surface = newSurface),
                stage = DerivationStage.ANGAKARYA
            ).addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, substitute, sutra)),
            explanation = "6.1.101: Savarṇa Dīrgha substitution ($substitute) for $leftChar + ${rightTerm.surface.first()}."
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
}
