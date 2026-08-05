package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

object SankhyapurvoDviguExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.92",
    text = "सङ्ख्यापूर्वो द्विगुः",
    hindiExplanation = "सङ्ख्या पूर्व में हो तो तत्पुरुष समास को द्विगु की संज्ञा होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210092,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.DVIGU
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.92 classifies numeral compound as Dvigu '$compoundStem'.",
        )
    }
}
