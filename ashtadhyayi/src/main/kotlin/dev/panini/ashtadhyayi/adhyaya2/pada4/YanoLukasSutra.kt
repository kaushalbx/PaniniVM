package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
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
 * 2.4.74: यङोऽचि च.
 * Prescribes 'luk' elision of the frequentative 'यङ्' affix before non-ac affixes.
 */
object YanoLukasSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.74",
    text = "यङोऽचि च",
    hindiExplanation = "अच् से भिन्न प्रत्यय परे होने पर यङ् का लुक् होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240074,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val isYanLukRequested = context.samjnas.any { it.samjna == Samjna.YAN_LUK }
        val yanTerm = context.terms.firstOrNull { it.upadesha == "यङ्" || it.id.contains("yan") }
        return isYanLukRequested && yanTerm != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val yanTerm = context.terms.first { it.upadesha == "यङ्" || it.id.contains("yan") }
        val newTerms = context.terms.filterNot { it.id == yanTerm.id }
        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = maxOf(context.stage, DerivationStage.ANGAKARYA),
            ),
            explanation = "2.4.74 performs luk-elision of यङ् affix."
        )
    }
}
