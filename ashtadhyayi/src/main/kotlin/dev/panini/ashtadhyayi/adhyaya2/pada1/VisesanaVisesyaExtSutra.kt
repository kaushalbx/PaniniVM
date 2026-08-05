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

object VisesanaVisesyaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.94",
    text = "विशेषणं विशेष्येण बहुलम्",
    hindiExplanation = "विशेषण सुबन्त का विशेष्य समर्थ सुबन्त के साथ कर्मधारय तत्पुरुष समास बहुल रूप से होता है (उदा. नीलोत्पलम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210094,
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
            purva == "नील" && uttara.startsWith("उत्पल")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.94 forms Viśeṣaṇa-viśeṣya Karmadhāraya compound '$compoundStem'.",
        )
    }
}
