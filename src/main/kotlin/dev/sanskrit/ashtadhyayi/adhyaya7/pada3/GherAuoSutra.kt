package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.3.123: gher au-auṭau. A Ghi stem lengthens before the two dual endings. */
object GherAuoSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.123",
    text = "घेरौऔटौ",
    hindiExplanation = "घि संज्ञक इ/उ-अन्त अङ्ग के बाद औ और औट् में अन्त्य स्वर दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730123,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("1.4.7", "6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI } &&
            affix.upadesha in setOf("औ", "औट्") &&
            stem.surface.lastOrNull() in setOf('इ', 'ि', 'उ', 'ु')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val longVowel = when (stem.surface.last()) {
            'इ' -> "ई"
            'ि' -> "ी"
            'उ' -> "ऊ"
            'ु' -> "ू"
            else -> error("GherAuoSutra matched a non-ik stem")
        }
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + longVowel),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED,
            ),
            explanation = "7.3.123: Lengthened the Ghi stem before ${affix.upadesha} dual.",
        )
    }
}
