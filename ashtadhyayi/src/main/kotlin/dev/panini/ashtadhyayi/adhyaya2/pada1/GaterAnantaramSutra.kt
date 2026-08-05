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

import dev.panini.ganapatha.PradiGana

/**
 * Sūtra 2.1.45: गतेर् अनन्तरम्.
 * Prescribes Saptamī Tatpuruṣa compounding a gati prefix with an immediate kta-participle.
 * Example: प्राप्नुवन् अनन्तरम् = प्रानन्तरम्.
 */
object GaterAnantaramSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.45",
    text = "गतेर् अनन्तरम्",
    hindiExplanation = "गति उपसर्ग का अनन्तर क्तान्त सुबन्त के साथ सप्तमी तत्पुरुष समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210045,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            (PradiGana.contains(purva) || PradiGana.members.any { it.text.startsWith(purva) }) &&
            uttara == "अनन्तर"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.45 forms Gati-anantara Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
