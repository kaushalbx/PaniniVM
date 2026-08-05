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
 * Sūtra 2.1.36: कवचहरदक्षिणक्षीरेषु.
 * Prescribes Tṛtīyā Tatpuruṣa compound when the uttara-pada is one of 'kavaca', 'hara', 'dakṣiṇā', or 'kṣīra'.
 * Example: वयसा अनुगतः कवचहरः = कवचहरः (kavacaharaḥ).
 */
object KavacaharaDaksinaKsiresuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.48",
    text = "कवचहरदक्षिणक्षीरेषु",
    hindiExplanation = "तृतीयान्त सुबन्त का कवच, हर, दक्षिणा तथा क्षीर शब्दों के साथ तत्पुरुष समास होता है (उदा. कवचहरः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210048,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val targetUttaraPadas = setOf("कवचहर", "कवच", "हर", "दक्षिणा", "क्षीर")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            targetUttaraPadas.contains(uttara)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.36 forms Tṛtīyā Tatpuruṣa compound '$compoundStem' with Kavaca-hara etc.",
        )
    }
}
