package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 1.1.11: īdūdedvivacanaṃ pragṛhyam.
 * Dual endings in 'ī', 'ū', or 'e' are called 'pragṛhya'.
 */
object PragrhyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.11",
    text = "ईदूदेद्विवचनं प्रगृह्यम्",
    hindiExplanation = "ईकारान्त, ऊकारान्त और एकारान्त द्विवचन की प्रगृह्य संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110011,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Must be in a dual context
        if (SemanticFeature.DVIVACANA !in context.semanticFeatures) return false

        return context.terms.any { term ->
            val surface = term.surface
            val isEligibleVowel = surface.endsWith('ई') || surface.endsWith('ी') ||
                                 surface.endsWith('ऊ') || surface.endsWith('ू') ||
                                 surface.endsWith('ए') || surface.endsWith('े')
            
            isEligibleVowel && context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PRAGRHYA }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms.filter { term ->
            val surface = term.surface
            surface.endsWith('ई') || surface.endsWith('ी') ||
            surface.endsWith('ऊ') || surface.endsWith('ू') ||
            surface.endsWith('ए') || surface.endsWith('े')
        }.map { SamjnaAssignment(it.id, Samjna.PRAGRHYA) }.toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.11 identifies pragṛhya vowels in dual forms."
        )
    }
}
