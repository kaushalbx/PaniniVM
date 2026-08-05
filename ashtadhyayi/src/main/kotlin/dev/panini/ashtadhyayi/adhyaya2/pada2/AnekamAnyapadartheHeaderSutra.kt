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
 * Sūtra 2.2.24: अनेकमन्यपदार्थे (registered as 2.2.95 for unique ID).
 * Header sūtra prescribing Bahuvrīhi compounding when external referent is expressed.
 */
object AnekamAnyapadartheHeaderSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.95",
    text = "अनेकमन्यपदार्थे",
    hindiExplanation = "अन्य पद के अर्थ में अनेक प्रथमान्त सुबन्तों का विकल्प से बहुव्रीहि समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = true,
    kramaValue = 220095,
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
            explanation = "2.2.95 forms Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
