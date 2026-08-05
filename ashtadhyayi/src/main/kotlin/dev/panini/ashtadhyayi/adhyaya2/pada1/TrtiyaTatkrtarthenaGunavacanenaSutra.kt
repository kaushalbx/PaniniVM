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
 * 2.1.30: तृतीया तत्कृतार्थेन गुणवचनेन.
 *
 * Tṛtīyā-subanta compounds with quality-denoting words caused thereby or 'artha' in Tatpuruṣa.
 */
object TrtiyaTatkrtarthenaGunavacanenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.30",
    text = "तृतीया तत्कृतार्थेन गुणवचनेन",
    hindiExplanation = "तृतीयान्तः सुबन्तः तत्कृतगुणवचनेन सुबन्तेन अर्थशब्देन च सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210030,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.TRTIYA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.30: Formed Tṛtīyā Tatpuruṣa compound ($compoundStem).",
        )
    }
}
