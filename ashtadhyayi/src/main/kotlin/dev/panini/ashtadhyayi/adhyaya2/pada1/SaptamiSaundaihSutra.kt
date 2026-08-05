package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.40: सप्तमी शौण्डैः.
 * Prescribes Saptamī Tatpuruṣa compound: pūrvapada must be in saptamī (locative) case.
 * Matching: purely on purvaPadaVibhakti == SAPTAMI.
 */
object SaptamiSaundaihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.40",
    text = "सप्तमी शौण्डैः",
    hindiExplanation = "सप्तम्यन्त समर्थ सुबन्त का 'शौण्ड' (चतुर) आदि गण-पठित शब्दों के साथ तत्पुरुष समास होता है (उदा. अक्षशौण्डः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210040,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    isGeneralFallback = true,
), dev.panini.sutra.SamasaSutra {
    // Authentic Pāṇinian condition: pūrvapada bears saptamī vibhakti
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && context.purvaPadaVibhakti == Vibhakti.SAPTAMI

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.40 forms Saptamī Tatpuruṣa compound '$stem'.",
        )
    }
}
