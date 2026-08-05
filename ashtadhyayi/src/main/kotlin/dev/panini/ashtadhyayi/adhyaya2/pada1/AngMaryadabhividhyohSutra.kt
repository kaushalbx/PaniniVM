package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.13: आङ् मर्यादाभिविध्योः.
 *
 * Compounds 'āṅ' ('ā') in the sense of limit (maryādā) or inclusive extent (abhividhi) with a Pañcamī nominal.
 */
object AngMaryadabhividhyohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.13",
    text = "आङ् मर्यादाभिविध्योः",
    hindiExplanation = "मर्यादाभिविध्योरर्थयोर्वर्तमान आङ् पञ्चम्यन्तेन सह समस्यते, सोऽव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210013,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva == "आ" || purva == "आङ्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "आ" + context.uttaraPada.upadesha

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.13: Formed Avyayībhāva compound with Āṅ ($compoundStem).",
        )
    }
}
