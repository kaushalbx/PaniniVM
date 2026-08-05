package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.51: तद्धितार्थोत्तरपदसमाहारे च.
 * Prescribes Dvigu / Tatpuruṣa compound when a taddhita suffix follows, or an uttarapada follows, or in collective (samāhāra) sense.
 * Example: पञ्चकपालः, पञ्चगवम्.
 */
object TaddhitarthaUttarapadaSamahareChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.51",
    text = "तद्धितार्थोत्तरपदसमाहारे च",
    hindiExplanation = "तद्धितार्थ, उत्तरपद परे होने पर तथा समाहार अर्थ में सङ्ख्यावाचक का समर्थ सुबन्त के साथ तत्पुरुष द्विगु समास होता है (उदा. पञ्चकपालः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210051,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 5,
), SamasaSutra {
    private val digits = setOf("द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return (context.samasaType == SamasaType.DVIGU || context.samasaType == SamasaType.TATPURUSA) &&
            purva in digits
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.51 forms Taddhitārtha / Samāhāra Dvigu compound '$compoundStem'.",
        )
    }
}
