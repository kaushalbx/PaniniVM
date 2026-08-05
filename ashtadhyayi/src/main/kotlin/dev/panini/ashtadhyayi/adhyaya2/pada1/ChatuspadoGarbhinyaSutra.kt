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
 * 2.1.69: चतुष्पादो गर्भिण्या.
 *
 * A four-legged animal stem compounds with 'garbhiṇī' in Karmadhāraya.
 */
object ChatuspadoGarbhinyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.69",
    text = "चतुष्पादो गर्भिण्या",
    hindiExplanation = "चतुष्पादः सुबन्तः गर्भिणीशब्देन सह समस्यते, सोऽपि कर्मधारयः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210069,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.KARMADHARAYA
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA &&
            uttara == "गर्भिणी"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.69: Formed Chatuṣpāda Karmadhāraya compound ($compoundStem).",
        )
    }
}
