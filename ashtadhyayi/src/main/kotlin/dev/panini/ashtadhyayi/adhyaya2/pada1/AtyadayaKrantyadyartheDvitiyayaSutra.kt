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
 * Sūtra 2.1.25: अत्यादयः क्रान्त्याद्यर्थे द्वितीयया.
 * Prescribes Tatpuruṣa compound of prefixes like 'ati' denoting surpassing/crossing with dvitīyā subanta.
 * Example: अतिक्रान्तः कोकिलम् = अतिकोकिलः.
 */
object AtyadayaKrantyadyartheDvitiyayaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.25",
    text = "अत्यादयः क्रान्त्याद्यर्थे द्वितीयया",
    hindiExplanation = "अतिक्रान्त आदि अर्थों में अति आदि प्रादियों का द्वितीयान्त समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. अतिकोकिलः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210025,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val atiPrefixes = setOf("अति", "उत्", "अप", "परि")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva in atiPrefixes
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.25 forms Ati-Krānta Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
