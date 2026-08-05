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
 * Sūtra 6.3.4: अन्तरमपरमव्युक्तमनात्मनि.
 * Aluk of case affix for antara, apara when not referring to self.
 */
object AntaramAparavyoktamAnatmaniSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.4",
    text = "अन्तरमपरमव्युक्तमनात्मनि",
    hindiExplanation = "अनात्मन् अर्थ में अन्तर तथा अपर शब्द से विभक्ति का अलुक् होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630004,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return context.samasaType == SamasaType.ALUK_TATPURUSA && (first == "अन्तर" || first == "अपर")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.4 preserves case affix (Aluk) for antara/apara in '$compoundStem'.",
        )
    }
}
