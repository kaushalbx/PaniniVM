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
 * Sūtra 5.4.122: नित्यं षच्च.
 * Mandatory -ṣac suffix rule in Bahuvrīhi.
 */
object NityamSacChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.122",
    text = "नित्यं षच्च",
    hindiExplanation = "बहुव्रीहि समास में षच् ('अ') प्रत्यय नित्य (अनिवार्य) होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540122,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.BAHUVRIHI && (last == "मूर्धन्" || last == "अक्ष्" || last == "सक्थि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.122 adds mandatory Samāsānta ṣac ('a') in '$compoundStem'.",
        )
    }
}
