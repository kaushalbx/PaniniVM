package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.28: तेन सहेति तुल्ययोगे (registered as 2.2.104 for unique ID).
 * Governance rule for saha Bahuvrīhi compounds.
 * Example: सह पत्न्या = सपत्नीकः (sapatnīkaḥ).
 */
object TenaSahetiHeaderSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.104",
    text = "तेन सहेति तुल्ययोगे",
    hindiExplanation = "तुल्ययोग अर्थ में सह शब्द का तृतीयान्त पद के साथ बहुव्रीहि समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220104,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.BAHUVRIHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.104 forms Saha Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
