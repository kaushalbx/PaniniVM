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

/**
 * Sūtra 2.1.27: तृतीया तत्कृतार्थेन गुणवचनेन (registered as 2.1.100 for unique ID).
 * Prescribes Tṛtīyā Tatpuruṣa compound with quality words denoting cause/effect.
 */
object TrtiyaTatkrtarthenaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.100",
    text = "तृतीया तत्कृतार्थेन गुणवचनेन",
    hindiExplanation = "तृतीयान्त सुबन्त का तत्कृत गुणवाचक समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. शङ्कुलाखण्डः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210100,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.100 forms Tṛtīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
