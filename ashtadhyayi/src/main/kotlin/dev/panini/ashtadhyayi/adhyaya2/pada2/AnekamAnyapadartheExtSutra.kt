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

object AnekamAnyapadartheExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.90",
    text = "अनेकमन्यपदार्थे",
    hindiExplanation = "अन्य पद के अर्थ में अनेक सुबन्तों का बहुव्रीहि समास होता है (उदा. पीताम्बरः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220090,
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
            explanation = "2.2.90 forms Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
