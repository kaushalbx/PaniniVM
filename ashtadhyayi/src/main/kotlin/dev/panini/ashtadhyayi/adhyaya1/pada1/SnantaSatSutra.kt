package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.24: ṣṇāntā ṣaṭ.
 * Numerals ending in 'ṣ' or 'n' are designated as 'ṣaṭ'.
 */
object SnantaSatSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.24",
    text = "ष्णान्ता षट्",
    hindiExplanation = "षकारान्त और नकारान्त संख्यावाचक शब्दों की 'षट्' संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110024,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val SHAT_NUMERALS = setOf("पञ्चन्", "षट्", "सप्तन्", "अष्टन्", "नवन्", "दशन्")

    override fun matches(context: DerivationState): Boolean =
        context.terms.any { isEligibleShatTerm(context, it) }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { isEligibleShatTerm(context, it) }
            .map { SamjnaAssignment(it.id, Samjna.SHAT) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.24: Assigned 'ṣaṭ' saṃjñā to numeral stem."
        )
    }

    private fun isEligibleShatTerm(context: DerivationState, term: DerivationTerm): Boolean {
        if (term.kind != TermKind.PRATIPADIKA) return false
        if (context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.SHAT }) return false

        val surface = term.surface
        val upadesha = term.upadesha
        return upadesha in SHAT_NUMERALS || surface in SHAT_NUMERALS
    }
}
