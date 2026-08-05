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
 * Sūtra 2.1.13: मर्यादाभिविध्योः (registered as 2.1.102 for unique ID).
 * Prescribes Avyayībhāva compound with āṅ in limit (maryādā) or inclusive (abhividhi) sense.
 * Example: आ कुमारम् = आकुमारम् (ākumāram).
 */
object MaryadabhividhyohExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.102",
    text = "मर्यादाभिविध्योः",
    hindiExplanation = "मर्यादा और अभिविधि अर्थ में आङ् अव्यय का समर्थ पञ्चम्यन्त के साथ अव्ययीभाव समास होता है (उदा. आकुमारम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210102,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA && purva == "आ"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.102 forms Āṅ Avyayībhāva compound '$compoundStem'.",
        )
    }
}
