package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.175: कम्बोजाल्लुक्. */
object KambojalLukSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.175", text = "कम्बोजाल्लुक्",
    hindiExplanation = "तद्राज के अर्थ में कम्बोजादि के बाद आए अञ् प्रत्यय का लुक् होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410175,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.TADRAJA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(69, it.surface, it.lexicalUses) } &&
            context.terms.any { it.kind == TermKind.PRATYAYA && it.upadesha == "अञ्" }

    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms.first { it.kind == TermKind.PRATYAYA && it.upadesha == "अञ्" }
        return DerivationChange(
            state = context.removeTerm(target.id),
            explanation = "4.1.175 applies लुक् to अञ् after an eligible कम्बोजादि term in the तद्राज sense.",
        )
    }
}
