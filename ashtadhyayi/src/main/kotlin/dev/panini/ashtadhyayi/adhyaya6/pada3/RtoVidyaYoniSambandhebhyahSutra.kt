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
 * Sūtra 6.3.34: ऋतो विद्यायोनिसम्बन्धेभ्यः.
 * Pūrvapada ān substitution for ṛ-ending kinship/teacher relation words in Dvandva.
 * Example: होतापोतारौ (hotāpotārau).
 */
object RtoVidyaYoniSambandhebhyahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.34",
    text = "ऋतो विद्यायोनिसम्बन्धेभ्यः",
    hindiExplanation = "विद्या सम्बन्ध तथा योनिसम्बन्ध वाची ऋदन्त पूर्वपद को द्वन्द्व समास में आनङ् (आ) आदेश होता है (उदा. होतापोतारौ)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630034,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return context.samasaType == SamasaType.DVANDVA && (first == "होतृ" || first == "पोतृ" || first == "नेष्टृ" || first == "उद्गातृ")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.34 applies ānaṅ substitution for ṛ-ending relation word in '$compoundStem'.",
        )
    }
}
