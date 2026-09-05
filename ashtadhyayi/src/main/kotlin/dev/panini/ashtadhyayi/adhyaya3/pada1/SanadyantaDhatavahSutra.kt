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
 * 3.1.32: सनाद्यन्ता धातवः.
 * Affixes starting with 'san' through 'kyaṣ' (3.1.7 - 3.1.31) turn the underlying stem into a secondary dhātu.
 */
object SanadyantaDhatavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.32",
    text = "सनाद्यन्ता धातवः",
    hindiExplanation = "सन् आदि प्रत्यय जिसके अन्त में हों उस शब्द रूप की धातु संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310032,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val sanadiAffixes = setOf("णिच्", "सन्", "यङ्", "क्यच्", "क्यङ्", "काम्यच्")

    override fun matches(context: DerivationState): Boolean {
        val lastPratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val isSanadi = lastPratyaya.upadesha in sanadiAffixes
        val alreadyDhatu = context.samjnas.any { it.targetId == lastPratyaya.id && it.samjna == Samjna.DHATU }
        return isSanadi && !alreadyDhatu
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastPratyaya = context.terms.last { it.kind == TermKind.PRATYAYA }
        return DerivationChange(
            state = context.copy(
                samjnas = context.samjnas + SamjnaAssignment(lastPratyaya.id, Samjna.DHATU),
            ),
            explanation = "3.1.32 assigns Dhātu samjñā to the secondary root formed with ${lastPratyaya.upadesha}."
        )
    }
}

internal fun DerivationState.hasSanadyantaDhatu(): Boolean =
    samjnas.any { assignment ->
        assignment.samjna == Samjna.DHATU &&
            terms.any { it.id == assignment.targetId && it.kind == TermKind.PRATYAYA }
    }
