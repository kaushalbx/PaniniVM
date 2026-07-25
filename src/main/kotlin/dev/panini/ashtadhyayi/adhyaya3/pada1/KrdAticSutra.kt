package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.93 कृदतिङ्.
 * Assigns Kṛt saṃjñā to non-Tiङ् affixes after a root.
 */
object KrdAticSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.93", text = "कृदतिङ्",
    hindiExplanation = "धातु से विहित तिङ्-भिन्न प्रत्ययों की 'कृत' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 3, pada = 1, optional = false, kramaValue = 310093,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == dev.panini.derivation.TermKind.PRATYAYA &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PRATYAYA }
        }

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(state = context, explanation = "3.1.93 assigns Kṛt saṃjñā to non-Tiङ् affixes.")
}
