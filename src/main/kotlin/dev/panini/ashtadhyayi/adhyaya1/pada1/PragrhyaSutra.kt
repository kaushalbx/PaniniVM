package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.shiksha.Samjna
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.core.Vacana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        if (!HasMorphosyntax(vacana = Vacana.DVIVACANA).matches(context)) return false

        return context.terms.any { term ->
            val isNonNadiStem = term.kind == TermKind.PRATIPADIKA &&
                context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.NADI }
            if (term.kind != TermKind.PRATYAYA && !isNonNadiStem) return@any false
            val surface = term.surface
            val isEligibleVowel = surface.endsWith('ई') || surface.endsWith('ी') ||
                                 surface.endsWith('ऊ') || surface.endsWith('ू') ||
                                 surface.endsWith('ए') || surface.endsWith('े')

            isEligibleVowel && context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PRAGRHYA }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms.filter { term ->
            val isNonNadiStem = term.kind == TermKind.PRATIPADIKA &&
                context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.NADI }
            if (term.kind != TermKind.PRATYAYA && !isNonNadiStem) return@filter false
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
