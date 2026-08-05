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
 * Sūtra 2.1.22: सामि.
 * Prescribes Dvitīyā Tatpuruṣa / Avyayībhāva compound when indeclinable 'sāmi' (half) compounds with a kta-participle.
 * Example: सामि कृतम् = सामिकृतम् (sāmikṛtam - half done).
 */
object SamiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.22",
    text = "सामि",
    hindiExplanation = "सामि (अर्ध) अव्यय का क्तान्त सुबन्त के साथ तत्पुरुष समास होता है (उदा. सामिकृतम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210022,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva == "सामि"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.22 forms Sāmi Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
