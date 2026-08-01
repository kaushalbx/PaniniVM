package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.91: धातोः.
 * Adhikāra governing all suffixes introduced after a verbal root (dhātu) through 3.4.117.
 */
object DhatohSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.91",
    text = "धातोः",
    hindiExplanation = "३.४.११७ तक प्रत्ययों का विधान धातु से परे होता है।",
    type = SutraType.ADHIKARA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310091,
    role = SutraRole.Adhikara(endKrama = 340117),
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL) return false
        return "3.1.91" !in context.activeAdhikaras &&
            context.terms.any { it.kind == TermKind.DHATU }
    }

    override fun apply(context: DerivationState): DerivationChange {
        return DerivationChange(
            state = context.copy(activeAdhikaras = context.activeAdhikaras + "3.1.91"),
            explanation = "3.1.91 establishes the Dhātoḥ adhikāra."
        )
    }
}
