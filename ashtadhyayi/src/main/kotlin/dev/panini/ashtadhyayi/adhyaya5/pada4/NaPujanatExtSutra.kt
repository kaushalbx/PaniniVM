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
 * Sūtra 5.4.145: न पूजनात् (Ext registered as 5.4.145).
 * Extended non-pūjana block rule.
 */
object NaPujanatExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.145",
    text = "न पूजनात्",
    hindiExplanation = "पूजावाची (सु, अति आदि) पूर्वपद से परे समासान्त कप् प्रत्यय नहीं होता है।",
    type = SutraType.NISHEDHA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540145,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "सु" || first == "अति"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.145 blocks Samāsānta kap after pūjana prefix in '$compoundStem'.",
        )
    }
}
