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
 * Sūtra 2.2.23: शेषो बहुव्रीहिः (registered as 2.2.94 for unique ID).
 * Header sūtra defining residual compounds as Bahuvrīhi.
 */
object SesoBahuvrihiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.94",
    text = "शेषो बहुव्रीहिः",
    hindiExplanation = "अधिकार सूत्र: शेष (पूर्व-समासों से भिन्न अर्थ वाले) समास बहुव्रीहि होते हैं।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220094,
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
            explanation = "2.2.94 classifies residual compound as Bahuvrīhi '$compoundStem'.",
        )
    }
}
