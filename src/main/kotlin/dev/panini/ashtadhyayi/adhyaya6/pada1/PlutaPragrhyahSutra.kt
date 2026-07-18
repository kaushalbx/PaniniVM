package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.125: pluta-pragṛhyā aci nityam.
 * Pluta and pragṛhya vowels remain as they are (prakṛtibhāva) when followed by a vowel.
 * This rule blocks general sandhi rules like 6.1.77 (Iko yaṇ aci).
 */
object PlutaPragrhyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.125",
    text = "प्लुतप्रगृह्या अचि नित्यम्",
    hindiExplanation = "प्लुत और प्रगृह्य स्वर के बाद अच् (कोई स्वर) होने पर प्रकृतिभाव होता है (सन्धि नहीं होती)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610125,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    blocks = setOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.101")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.FINAL) return false
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        // 1. Check if left term is designated as Pragṛhya
        val isPragrhya = context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PRAGRHYA }
        if (!isPragrhya) return false

        // 2. Check if right term starts with a vowel (Ac)
        val firstChar = right.surface.firstOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AC, firstChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        // Prakṛtibhāva means the state remains unchanged, but we move the stage forward
        // to indicate that sandhi has been "processed" (or rather, bypassed).
        return DerivationChange(
            state = context.copy(stage = DerivationStage.FINAL),
            explanation = "6.1.125: Pragṛhya status prevents sandhi (prakṛtibhāva)."
        )
    }
}
