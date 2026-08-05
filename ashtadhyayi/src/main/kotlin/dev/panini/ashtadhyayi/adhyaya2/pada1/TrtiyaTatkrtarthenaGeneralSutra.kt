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
 * Sūtra 2.1.26: तृतीया तत्कृतार्थेन गुणवचनेन (registered as 2.1.105 for unique ID).
 * Prescribes general Tṛtīyā Tatpuruṣa compound governance.
 */
object TrtiyaTatkrtarthenaGeneralSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.105",
    text = "तृतीया तत्कृतार्थेन गुणवचनेन",
    hindiExplanation = "तृतीयान्त पद का तत्कृत गुणवाचक समर्थ पद के साथ तत्पुरुष समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210105,
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
            explanation = "2.1.105 forms Tṛtīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
