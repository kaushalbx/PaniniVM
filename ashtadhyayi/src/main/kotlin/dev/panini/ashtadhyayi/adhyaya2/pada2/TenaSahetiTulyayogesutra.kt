package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.core.SamasaRuleContext
import dev.panini.core.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope

/**
 * 2.2.28: तेन सहेति तुल्ययोगे.
 *
 * A word with instrumental vibhakti compounds with 'saha' when expressing equal association (tulyayoga),
 * forming a Bahuvrīhi compound.
 */
object TenaSahetiTulyayogesutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.28",
    text = "तेन सहेति तुल्ययोगे",
    hindiExplanation = "तुल्ययोगे तृतीयान्तं सह इत्येनेन समस्यते, स च बहुव्रीहिः।",
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220028,
    role = SutraRole.Vidhi,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva == "सह" || context.samasaType == SamasaType.BAHUVRIHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha

        val adjustedPurva = if (purva == "सह") "स" else purva
        val compoundStem = adjustedPurva + uttara

        return SamasaRuleResult.Formed(
            type = SamasaType.BAHUVRIHI,
            compoundStem = compoundStem,
            sutra = number,
            description = "2.2.28: Formed Bahuvrīhi compound with 'saha' -> 'sa-' (e.g. $compoundStem).",
        )
    }
}
