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
 * Sūtra 5.4.93: ग्रामकातक्षाभ्यां ष्टच्.
 * Prescribes Samāsānta ṭac (-a) suffix after grāmaka and takṣan.
 */
object GramakataksabhyamTacSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.93",
    text = "ग्रामकातक्षाभ्यां ष्टच्",
    hindiExplanation = "ग्रामक तथा तक्षन् उत्तरपद से समासान्त ष्टच् (अ) प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540093,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "ग्रामक" || last == "तक्षन्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.93 adds Samāsānta ṭac ('a') suffix in '$compoundStem'.",
        )
    }
}
