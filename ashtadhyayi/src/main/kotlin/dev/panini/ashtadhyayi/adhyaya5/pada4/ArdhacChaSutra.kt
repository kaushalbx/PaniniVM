package dev.panini.ashtadhyayi.adhyaya5.pada4

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
 * Sūtra 5.4.100: अर्धाच्च.
 * Prescribes Samāsānta ac suffix after nau preceded by ardha.
 */
object ArdhacChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.100",
    text = "अर्धाच्च",
    hindiExplanation = "अर्ध शब्द पूर्व में होने पर नौ उत्तरपद से समासान्त अच् प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540100,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return first == "अर्ध" && (last == "नौ" || last == "नाव")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "अर्धनाव"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.100 adds Samāsānta ac for ardha + nau in '$compoundStem'.",
        )
    }
}
