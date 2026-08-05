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

object UpapadamAtingExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.89",
    text = "उपपदम् अतिङ्",
    hindiExplanation = "उपपद सुबन्त का अतिङन्त समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. कुम्भकारः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220089,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.UPAPADA_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 &&
            (context.samasaType == SamasaType.UPAPADA_TATPURUSA || context.samasaType == SamasaType.TATPURUSA)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.89 forms Upapada Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
