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
 * Sūtra 2.2.18: कुगतिप्रादयः (registered as 2.2.98 for unique ID).
 * Prescribes Tatpuruṣa compound of ku, gati, and prādi prefixes with subanta.
 * Example: कु पुरुषः = कुपुरुषः, सु गन्धः = सुगन्धिः.
 */
object KuGatiPradayahExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.98",
    text = "कुगतिप्रादयः",
    hindiExplanation = "कु, गतिसंज्ञक और प्र आदि प्रादि अव्ययों का समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. कुपुरुषः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220098,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val kuGatiPradi = setOf("कु", "सु", "दुर्", "प्र", "परा", "अप", "सम्", "अनु", "अव", "निस्", "निर्")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA && kuGatiPradi.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.98 forms Ku-Gati-Prādi Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
