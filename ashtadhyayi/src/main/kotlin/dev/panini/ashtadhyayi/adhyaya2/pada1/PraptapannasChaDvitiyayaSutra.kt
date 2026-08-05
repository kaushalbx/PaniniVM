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
 * Sūtra 2.1.26: प्राप्तापन्ने च द्वितीयया.
 * Prescribes Tatpuruṣa compound of 'prāpta' or 'āpanna' with dvitīyā subanta.
 * Example: प्राप्तो जीविकाम् = प्राप्तजीविकः, आपन्नो जीविकाम् = आपन्नजीविकः.
 */
object PraptapannasChaDvitiyayaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.26",
    text = "प्राप्तापन्ने च द्वितीयया",
    hindiExplanation = "प्राप्त और आपन्न शब्द का द्वितीयान्त समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. प्राप्तजीविकः, आपन्नजीविकः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210026,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val praptaWords = setOf("प्राप्त", "आपन्न")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva in praptaWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.26 forms Prāptāpanna Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
