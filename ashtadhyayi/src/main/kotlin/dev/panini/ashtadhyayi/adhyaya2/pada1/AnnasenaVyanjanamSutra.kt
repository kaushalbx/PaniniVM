package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.34: अन्नेन व्यञ्जनम्.
 *
 * Seasoning/flavoring ingredient nominals in Tṛtīyā compound with food nominals in Tatpuruṣa.
 */
object AnnasenaVyanjanamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.34",
    text = "अन्नेन व्यञ्जनम्",
    hindiExplanation = "व्यञ्जनवाचि सुबन्तं तृतीयान्तम् अन्नवाचिना सुबन्तेन सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210034,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val foodWords = setOf("ओदन", "सक्तु", "धान्य", "अन्न", "पायस")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.TRTIYA &&
            uttara in foodWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.34: Formed Tṛtīyā Tatpuruṣa seasoning food compound ($compoundStem).",
        )
    }
}
