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
 * 2.1.28: कालाः अत्यन्तसंयोगे.
 *
 * Dvitiyā-subanta denoting continuous time duration compounds with matching subanta in Tatpuruṣa.
 */
object KalaAtyantasamyogeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.28",
    text = "कालाः अत्यन्तसंयोगे",
    hindiExplanation = "कालवाचिनः सुबन्ताः द्वितीयान्ताः अत्यन्तसंयोगे गम्यमाने सुबन्तेन सह समस्यन्ते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210028,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val kalaWords = setOf("मास", "अहोरात्र", "संवत्सर", "अहः", "रात्र", "मुहूर्त")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.DVITIYA &&
            (purva.upadesha in kalaWords || purva.surface in kalaWords)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.28: Formed Dvitīyā Tatpuruṣa time duration compound ($compoundStem).",
        )
    }
}
