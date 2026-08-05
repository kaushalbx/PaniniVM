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

object UpamanaSamanyaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.93",
    text = "उपमानानि सामान्यवचनैः",
    hindiExplanation = "उपमानवाचक सुबन्त का सामान्यवचन समर्थ सुबन्त के साथ कर्मधारय तत्पुरुष समास होता है (उदा. घनश्यामः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210093,
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
            purva == "घन" && uttara.startsWith("श्याम")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.93 forms Upamāna-sāmānya Karmadhāraya compound '$compoundStem'.",
        )
    }
}
