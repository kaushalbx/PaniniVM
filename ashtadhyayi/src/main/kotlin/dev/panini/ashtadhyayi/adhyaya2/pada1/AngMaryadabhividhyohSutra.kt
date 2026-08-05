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
 * 2.1.13: आङ् मर्यादाभिविध्योः.
 *
 * Āṅ in the sense of limit (maryādā) or inclusion (abhividhi) compounds with Pañcamī in Avyayībhāva.
 */
object AngMaryadabhividhyohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.13",
    text = "आङ् मर्यादाभिविध्योः",
    hindiExplanation = "मर्यादा तथा अभिविधि अर्थ में आङ् अव्यय पञ्चम्यन्त के साथ समस्यते, सोऽव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210013,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.AVYAYIBHAVA

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
