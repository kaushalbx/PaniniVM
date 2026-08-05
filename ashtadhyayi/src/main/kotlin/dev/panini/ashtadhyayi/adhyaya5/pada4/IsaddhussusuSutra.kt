package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 5.4.114: ईषद्दुःसुषु कृच्छ्राकृच्छ्रार्थेषु.
 * Prescribes Samāsānta suffix after īṣat, dur, su.
 */
object IsaddhussusuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.114",
    text = "ईषद्दुःसुषु कृच्छ्राकृच्छ्रार्थेषु",
    hindiExplanation = "ईषद्, दुर् तथा सु पूर्व में होने पर कृच्छ्र/अकृच्छ्र अर्थ में समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540114,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "ईषत्" || first == "दुर्" || first == "सु"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.114 adds Samāsānta 'a' after īṣat/dur/su in '$compoundStem'.",
        )
    }
}
