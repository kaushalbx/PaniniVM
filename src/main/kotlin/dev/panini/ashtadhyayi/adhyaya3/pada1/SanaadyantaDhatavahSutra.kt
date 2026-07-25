package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.32 सनाद्यन्ता धातवः.
 * Stems ending in san-ādi affixes are designated as dhātu (roots).
 */
object SanaadyantaDhatavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.32", text = "सनाद्यन्ता धातवः",
    hindiExplanation = "सन् आदि प्रत्यय जिनके अन्त में हों उनकी 'धातु' संज्ञा होती है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310032,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha in setOf("सन्", "क्यच्", "काम्यच्", "क्यङ्", "क्यष्", "णिङ्", "यङ्", "यक्", "आय", "इयङ्", "णिच्") } &&
        context.samjnas.none { it.samjna == Samjna.DHATU }

    override fun apply(context: DerivationState): DerivationChange {
        val targetTerm = context.allEffectiveTerms.last()
        val newSamjna = SamjnaAssignment(targetTerm.id, Samjna.DHATU)
        return DerivationChange(
            state = context.withSamjnas(setOf(newSamjna)),
            explanation = "3.1.32 assigns dhātu saṃjñā to san-ādi ending stems.",
        )
    }
}
