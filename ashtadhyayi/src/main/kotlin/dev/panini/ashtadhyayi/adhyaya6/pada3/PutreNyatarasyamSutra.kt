package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 6.3.9: पुत्रेऽन्यतरस्याम्.
 * Prescribes optional Aluk of Ṣaṣṭhī (6th case) for pūrvapada before 'putra'.
 * Example: देवयान्याः पुत्रः = देवयान्याःपुत्रः.
 */
object PutreNyatarasyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.9",
    text = "पुत्रेऽन्यतरस्याम्",
    hindiExplanation = "पुत्र शब्द उत्तरपद परे होने पर षष्ठी विभक्ति का विकल्प से अलुक् होता है (उदा. देवयान्याःपुत्रः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = true,
    kramaValue = 630009,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return uttara == "पुत्र" || context.samasaType == SamasaType.ALUK_TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "6.3.9 (पुत्रेऽन्यतरस्याम्) preserves Ṣaṣṭhī vibhakti before 'putra' for '$stem'.",
        )
    }
}
