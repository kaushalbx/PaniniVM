package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDevanagari
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
    text = "राजाऽहस्सखिभ्यष्टच्",
    hindiExplanation = "राजन्, अहन् तथा सखि उत्तरपद वाले तत्पुरुष समास से नित्य समासान्त 'अ' (टच्) प्रत्यय होता है (उदा. महाराजः, परमसखः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540091,
    role = SutraRole.Vidhi,
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
        val lastPada = context.uttaraPada
        require(lastPada.upadesha in setOf("राजन्", "अहन्", "सखि"))
        val replacement = lastPada.varnas.dropLast(1) +
            if (lastPada.varnas.lastOrNull() == Svara.I) listOf(Svara.A) else emptyList()
        val convertedLast = replacement.toDevanagari()
        // 6.3.46 owns mahat -> mahā; this rule edits only its samāsānta member.
        val compoundStem = (context.padas.dropLast(1).flatMap { it.varnas } + replacement).toDevanagari()
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.91 adds Samāsānta ṭac ('a') suffix after rājan/ahan/sakhi yielding stem '$compoundStem'.",
            memberEdits = mapOf(context.padas.lastIndex to convertedLast),
        )
    }
}
