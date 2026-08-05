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

object AmaivavyayenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.20",
    text = "अमैवाव्ययेन",
    hindiExplanation = "अम् तथा एव अव्ययान्त उपपद का समर्थ अव्यय के साथ नित्य समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220020,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return (context.samasaType == SamasaType.UPAPADA_TATPURUSA || context.samasaType == SamasaType.TATPURUSA) &&
            context.padas.any { it.upadesha.contains("स्वाहा") || it.upadesha.endsWith("अम्") }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.20 forms mandatory Upapada compound '$compoundStem'.",
        )
    }
}
