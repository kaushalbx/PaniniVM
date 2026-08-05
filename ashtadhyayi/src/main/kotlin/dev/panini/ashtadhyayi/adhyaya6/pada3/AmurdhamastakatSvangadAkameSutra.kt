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
 * Sūtra 6.3.5: अमूर्द्धमस्तकात् स्वाङ्गादकामे.
 * Prescribes Aluk (non-elision) for body-part words except mūrdhan and mastaka when not expressing desire.
 */
object AmurdhamastakatSvangadAkameSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.5",
    text = "अमूर्द्धमस्तकात् स्वाङ्गादकामे",
    hindiExplanation = "मूर्द्धन् तथा मस्तक को छोड़कर अन्य स्वाङ्ग वाची शब्दों से विभक्ति का अलुक् होता है (काम अर्थ को छोड़कर)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630005,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return context.samasaType == SamasaType.ALUK_TATPURUSA && first != "मूर्द्धन्" && first != "मस्तक"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.5 preserves case affix (Aluk) for body-part stem in '$compoundStem'.",
        )
    }
}
