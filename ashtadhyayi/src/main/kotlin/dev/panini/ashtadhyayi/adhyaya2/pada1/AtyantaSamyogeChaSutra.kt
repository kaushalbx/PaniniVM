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
 * Sūtra 2.1.49: अत्यन्तसंयोगे च.
 * Prescribes Dvitīyā Tatpuruṣa compound when continuous contact (atyanta-saṁyoga) in time/space is expressed.
 * Example: मुहूर्तम् सुखम् = मुहूर्तसुखम् (muhūrtasukham).
 */
object AtyantaSamyogeChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.49",
    text = "अत्यन्तसंयोगे च",
    hindiExplanation = "अत्यन्तसंयोग (निरन्तर संयोग) अर्थ में द्वितीयान्त का समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. मुहूर्तसुखम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210049,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val atyantaWords = setOf("मुहूर्त", "क्षण", "रात्र", "दिन")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            atyantaWords.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.49 forms Atyanta-saṁyoga Dvitīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
