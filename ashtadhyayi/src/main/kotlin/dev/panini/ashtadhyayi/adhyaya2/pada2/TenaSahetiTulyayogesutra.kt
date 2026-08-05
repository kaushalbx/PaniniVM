package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * 2.2.28: तेन सहेति तुल्ययोगे.
 */
object TenaSahetiTulyayogesutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.28",
    text = "तेन सहेति तुल्ययोगे",
    hindiExplanation = "तुल्ययोगे तृतीयान्तं सह इत्येनेन समस्यते, स च बहुव्रीहिः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220028,
    role = SutraRole.Vidhi,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.BAHUVRIHI
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva == "सह"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        val adjustedPurva = if (purva == "सह") "स" else purva
        val compoundStem = adjustedPurva + uttara

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.28: Formed Bahuvrīhi compound with 'saha' -> 'sa-' ($compoundStem).",
        )
    }
}
