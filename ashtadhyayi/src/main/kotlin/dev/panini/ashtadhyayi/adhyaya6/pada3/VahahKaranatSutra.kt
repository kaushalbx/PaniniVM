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
 * Sūtra 6.3.13: वाहः कारणात्.
 * Prescribes Aluk of case affix after vāha when denoting instrument / cause.
 */
object VahahKaranatSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.13",
    text = "वाहः कारणात्",
    hindiExplanation = "करण (साधन) अर्थ में वाह् उत्तरपद से पूर्व विभक्ति का अलुक् होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630013,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.ALUK_TATPURUSA && last == "वाह्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.13 preserves case affix (Aluk) before vāha in '$compoundStem'.",
        )
    }
}
