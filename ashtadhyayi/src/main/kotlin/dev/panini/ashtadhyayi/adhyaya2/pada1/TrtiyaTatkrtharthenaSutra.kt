package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.30: तृतीया तत्कृतार्थेन गुणवचनेन.
 * Prescribes Tṛtīyā Tatpuruṣa compound: pūrvapada must be in tṛtīyā (instrumental) case.
 * Matching: purely on purvaPadaVibhakti == TRTIYA — no surface-string check.
 */
object TrtiyaTatkrtharthenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.30",
    text = "तृतीया तत्कृतार्थेन गुणवचनेन",
    hindiExplanation = "तृतीयान्त समर्थ सुबन्त का तत्कृत अर्थ वाले गुणवाचक शब्द के साथ तत्पुरुष समास होता है (उदा. शङ्कुलाखण्डः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210030,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    // Authentic Pāṇinian condition: pūrvapada bears tṛtīyā vibhakti
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && context.purvaPadaVibhakti == Vibhakti.TRTIYA

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.30 forms Tṛtīyā Tatpuruṣa compound '$stem'.",
        )
    }
}
