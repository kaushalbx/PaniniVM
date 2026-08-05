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
 * Sūtra 2.1.57: विशेषणं विशेष्येण बहुलम्.
 * Prescribes Karmadhāraya Tatpuruṣa compound between a qualifier (viseṣaṇa) and qualified noun (viseṣya).
 * Both members carry the same case (samānādhikaraṇa).
 * Example: नीलम ् उत्पलम् = नीलोत्पलम्.
 */
object VisesanamVisesyenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.57",
    text = "विशेषणं विशेष्येण बहुलम्",
    hindiExplanation = "विशेषणवाचक समर्थ सुबन्त का विशेष्यवाचक समर्थ सुबन्त के साथ बहुलता से कर्मधारय तत्पुरुष समास होता है (उदा. नीलोत्पलम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210057,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.KARMADHARAYA
    override val isGeneralFallback: Boolean = true
    override fun matches(context: SamasaRuleContext): Boolean = context.padas.size >= 2

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.57 forms Karmadhāraya Tatpuruṣa compound '$stem'.",
        )
    }
}
