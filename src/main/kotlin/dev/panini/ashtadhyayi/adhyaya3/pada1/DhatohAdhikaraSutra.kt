package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.91: dhātoḥ.
 * Heading rule for rules 3.1.91 to the end of the 3rd adhyāya.
 * These suffixes are added to a 'dhātu' (root).
 */
object DhatohAdhikaraSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.91",
    text = "धातोः",
    hindiExplanation = "यह एक अधिकार सूत्र है। यहाँ से तीसरे अध्याय के अन्त तक प्रत्ययों का विधान 'धातु' के बाद होता है।",
    type = SutraType.ADHIKARA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310091,
    role = SutraRole.Adhikara,
    action = SutraAction.ADHIKARA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        "3.1.91" !in context.activeAdhikaras &&
        context.terms.any { it.kind == TermKind.DHATU || context.samjnas.any { s -> s.targetId == it.id && s.samjna == Samjna.DHATU } }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.activateAdhikara("3.1.91"),
        explanation = "3.1.91 (Dhātoḥ) adhikāra activated."
    )
}
