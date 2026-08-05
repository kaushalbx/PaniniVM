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
 * Sūtra 6.3.12: ख्यात्याै नक्षत्रे.
 * Prescribes Aluk of case affix in constellation / star names.
 */
object KhyatyaiNaksatreSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.12",
    text = "ख्यात्याै नक्षत्रे",
    hindiExplanation = "नक्षत्र नाम अर्थ में पूर्वपद की विभक्ति का अलुक् (अलोप) होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630012,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.ALUK_TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.12 preserves case affix (Aluk) in constellation name '$compoundStem'.",
        )
    }
}
