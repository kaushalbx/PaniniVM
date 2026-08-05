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
 * Sūtra 6.3.10: गोष्पदगोष्ठश्विनदारीजङ्घाकुलौक्षहृदयानि खमे.
 * Prescribes Aluk (non-elision) of case affix for goṣpada, goṣṭha, etc. before kham.
 */
object GospadaGosthaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.10",
    text = "गोष्पदगोष्ठश्विनदारीजङ्घाकुलौक्षहृदयानि खमे",
    hindiExplanation = "गोष्पद, गोष्ठ आदि शब्दों की विभक्ति का खम् उत्तरपद परे होने पर अलुक् होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630010,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return (context.samasaType == SamasaType.ALUK_TATPURUSA || context.samasaType == SamasaType.TATPURUSA) &&
            (first == "गोष्पद" || first == "गोष्ठ" || first == "श्विन्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.10 preserves case affix (Aluk) for goṣpada/goṣṭha in '$compoundStem'.",
        )
    }
}
