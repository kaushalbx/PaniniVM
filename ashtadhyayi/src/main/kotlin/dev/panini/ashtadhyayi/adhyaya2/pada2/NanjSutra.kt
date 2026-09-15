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
 * Sūtra 2.2.6: नञ्.
 * Prescribes Nañ Tatpuruṣa compound for negative prefix 'nañ' (na) with a subanta.
 * This rule only forms the compound. The later substitutions are performed by
 * their own rules, 6.3.73–75.
 */
object NanjSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.6",
    text = "नञ्",
    hindiExplanation = "नञ् समर्थेन सुबन्तेन सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.NAN_TATPURUSA,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        if (context.samasaType == SamasaType.NAN_TATPURUSA) return true
        val purva = context.purvaPada.upadesha
        return purva == "न" || purva == "नञ्" || purva == "अ"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.6 forms the Nañ Tatpuruṣa compound; subsequent nañ changes are governed by 6.3.73 ff.",
        )
    }
}
