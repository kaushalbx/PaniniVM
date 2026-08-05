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
 * Sūtra 5.4.154: अक्ष्णोऽदर्शनात् (registered as 5.4.254).
 * Extended rule for non-seeing akṣi.
 */
object AksnoAdarsanatExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.254",
    text = "अक्ष्णोऽदर्शनात्",
    hindiExplanation = "अदर्शन (नेत्र से भिन्न ज्ञान/अक्ष) अर्थ में अक्षि शब्द से समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540254,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "अक्षि" || last == "अक्ष्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.154 adds Samāsānta 'a' for non-seeing akṣi in '$compoundStem'.",
        )
    }
}
