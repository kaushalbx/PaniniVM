package dev.panini.ashtadhyayi.adhyaya5.pada4

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
 * Sūtra 5.4.91: राजाहःसखिभ्यष्टच्.
 * Prescribes Samāsānta ṭac (-a) suffix after rājan, ahan, sakhi.
 * Example: महाराजाः, परमराजन, परमसखा.
 */
object RajahahSakhibhyasTacSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.91",
    text = "राजाहःसखिभ्यष्टच्",
    hindiExplanation = "राजन्, अहन् तथा सखि उत्तरपद वाले तत्पुरुष समास से नित्य समासान्त 'अ' (टच्) प्रत्यय होता है (उदा. महाराजः, परमसखः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540091,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return (context.samasaType == SamasaType.TATPURUSA || context.samasaType == SamasaType.KARMADHARAYA) &&
            (last == "राजन्" || last == "अहन्" || last == "सखि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val lastPada = context.padas.last().upadesha
        val convertedLast = when {
            lastPada == "राजन्" || lastPada == "राजन" || lastPada == "rajan" -> "राज"
            lastPada == "अहन्" || lastPada == "अहन" || lastPada == "ahan" -> "अह"
            lastPada == "सखि" || lastPada == "sakhi" -> "सख"
            else -> if (lastPada.endsWith("न्")) lastPada.dropLast(2) + "अ" else lastPada + "अ"
        }
        val leadingPadas = context.padas.dropLast(1).joinToString("") { it.upadesha }
        val compoundStem = leadingPadas + convertedLast
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.91 adds Samāsānta ṭac ('a') suffix after rājan/ahan/sakhi yielding stem '$compoundStem'.",
        )
    }
}
