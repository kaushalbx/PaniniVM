package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope

/**
 * 6.3.82: वोपसर्जनस्य.
 *
 * In a Bahuvrīhi compound, 'saha' in the pūrvapada optionally (optionally or obligatorily in context)
 * gets substituted by 'sa'.
 */
object VopasarjanasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.82",
    text = "वोपसर्जनस्य",
    hindiExplanation = "सहस्य सादेशः स्याद् बहुव्रीहौ।",
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630082,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva == "सह"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val uttara = context.uttaraPada.upadesha
        val compoundStem = "स" + uttara

        return SamasaRuleResult.Formed(
            type = SamasaType.BAHUVRIHI,
            compoundStem = compoundStem,
            sutra = number,
            description = "6.3.82: Replaced 'saha' with 'sa' in Bahuvrīhi (e.g. $compoundStem).",
        )
    }
}
