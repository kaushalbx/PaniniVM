package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.23 (registered as 6.3.123 for unique ID): द्विगुप्राप्तापन्नादि extension.
 * Pūrvapada rule for Dvigu extension.
 */
object DviguPraptapannaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.123",
    text = "द्विगुप्राप्तापन्नादि extension",
    hindiExplanation = "द्विगु तथा प्राप्तापन्न समास उत्तरपद प्रकरण नियम।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630123,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.DVIGU
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.123 applies Dvigu extension pūrvapada rule for '$compoundStem'.",
        )
    }
}
