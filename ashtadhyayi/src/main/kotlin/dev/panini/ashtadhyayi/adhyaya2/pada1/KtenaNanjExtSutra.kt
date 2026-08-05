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

object KtenaNanjExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.95",
    text = "क्तेन नञ्विशिष्टेन",
    hindiExplanation = "क्तान्त सुबन्त का नञ्-विशिष्ट क्तान्त समर्थ सुबन्त के साथ कर्मधारय समास होता है (उदा. कृताकृतम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210095,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA &&
            purva == "कृत" && uttara.startsWith("अकृत")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.95 forms Kta-Nañ Karmadhāraya compound '$compoundStem'.",
        )
    }
}
