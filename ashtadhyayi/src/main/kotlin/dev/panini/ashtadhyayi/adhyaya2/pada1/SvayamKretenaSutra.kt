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
 * Sūtra 2.1.27: स्वयं कृतेन.
 * Prescribes Tṛtīyā Tatpuruṣa compound of the indeclinable 'svayam' with a kṛt-ending word.
 * Example: स्वयं कृतम् = स्वयंकृतम्.
 */
object SvayamKretenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.27",
    text = "स्वयं कृतेन",
    hindiExplanation = "स्वयम् अव्यय का कृत प्रत्ययान्त सुबन्त के साथ तृतीया तत्पुरुष समास होता है (उदा. स्वयंकृतम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210027,
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
            (purva == "स्वयम्" || purva == "स्वयं")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.27 forms Svayam Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
