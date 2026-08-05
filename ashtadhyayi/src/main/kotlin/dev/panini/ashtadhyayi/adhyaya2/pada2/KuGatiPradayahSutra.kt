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
 * Sūtra 2.2.18: कुगतिप्रादयः.
 * Prescribes Tatpuruṣa compound of 'ku', gati words, and 'pra' prefixes with समर्थ subantas.
 * Example: कुपुरुषः, सुपुरुषः, प्राचार्यः.
 */
object KuGatiPradayahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.18",
    text = "कुगतिप्रादयः",
    hindiExplanation = "कु, गति तथा प्र आदि प्रादि उपसर्गों का समर्थ सुबन्त के साथ नित्य तत्पुरुष समास होता है (उदा. कुपुरुषः, प्राचार्यः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220018,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val kuGatiPraPrefixes = setOf("कु", "सु", "प्र", "अति", "दुर्", "दुः", "निर्", "निः")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return (context.samasaType == SamasaType.TATPURUSA || context.samasaType == SamasaType.KARMADHARAYA) &&
            purva in kuGatiPraPrefixes
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.18 forms Ku-Gati-Prādaya Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
