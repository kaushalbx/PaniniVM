package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.93: कृदतिङ्.
 * Non-Tiṅ affixes introduced after a verbal root are called 'kṛt'.
 */
object KrdAtinSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.93",
    text = "कृदतिङ्",
    hindiExplanation = "धातु से परे तिङ् से भिन्न प्रत्यय की कृत् संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310093,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val last = context.terms.lastOrNull() ?: return false
        val isKrtCandidate = last.kind == TermKind.PRATYAYA &&
            !last.id.contains("ting") && !last.id.contains("sup") &&
            context.samjnas.none { it.targetId == last.id && it.samjna == Samjna.KRT }
        return isKrtCandidate
    }

    override fun apply(context: DerivationState): DerivationChange {
        val last = context.terms.last()
        return DerivationChange(
            state = context.copy(
                samjnas = context.samjnas + SamjnaAssignment(last.id, Samjna.KRT)
            ),
            explanation = "3.1.93 assigns Kṛt samjñā to ${last.surface} (${last.upadesha})."
        )
    }
}
