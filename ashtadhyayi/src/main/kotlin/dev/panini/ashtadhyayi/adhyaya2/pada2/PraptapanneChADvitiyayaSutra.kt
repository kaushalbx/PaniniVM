package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.5: प्राप्तापन्ने च द्वितीयायाः.
 * Prescribes option for prāpta and āpanna compounds with dvitīyā subanta.
 * Example: प्राप्तः जीविकाम् = प्राप्तजीविकः / जीविकाप्राप्तः.
 */
object PraptapanneChADvitiyayaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.5",
    text = "प्राप्तापन्ने च द्वितीयायाः",
    hindiExplanation = "प्राप्त तथा आपन्न शब्द का द्वितीयान्त समर्थ सुबन्त के साथ विकल्प से तत्पुरुष समास होता है (उदा. प्राप्तजीविकः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = true,
    kramaValue = 220005,
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
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            (praptaWords.contains(purva) || praptaWords.contains(uttara))
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.5 forms Prāpta/Āpanna Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
