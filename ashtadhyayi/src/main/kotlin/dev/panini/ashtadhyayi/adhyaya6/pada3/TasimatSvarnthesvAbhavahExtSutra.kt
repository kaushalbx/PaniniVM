package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.96: तसिमत्स्वर्थेष्वभावः (Ext registered as 6.3.96).
 * Extended tasi/mat/svar rule.
 */
object TasimatSvarnthesvAbhavahExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.96",
    text = "तसिमत्स्वर्थेष्वभावः",
    hindiExplanation = "तसि, मतुप् आदि प्रत्ययों के अर्थों में पुंवद्भाव का अभाव (निषेध) होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630096,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "तसि" || last == "मत्" || last == "स्व"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.96 blocks pumvadbhāva before tasi/mat in '$compoundStem'.",
        )
    }
}
