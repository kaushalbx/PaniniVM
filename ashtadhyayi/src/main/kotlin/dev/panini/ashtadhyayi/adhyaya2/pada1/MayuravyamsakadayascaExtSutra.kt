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

import dev.panini.ganapatha.MayuravyamsakadiGana

/**
 * Sūtra 2.1.70: मयूरव्यंसकादयश्च (registered as 2.1.106 for unique ID).
 * Prescribes irregular Tatpuruṣa nipātana compounds.
 * Example: मयूरव्यंसकादयः = मयूरव्यंसकः (mayūravyamsakaḥ).
 */
object MayuravyamsakadayascaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.106",
    text = "मयूरव्यंसकादयश्च",
    hindiExplanation = "मयूरव्यंसक आदि समास निपातन से सिद्ध होते हैं (उदा. मयूरव्यंसकः, उच्चावचम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210106,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.MAYURAVYAMSAKADI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.firstOrNull()?.upadesha ?: ""
        val second = context.padas.getOrNull(1)?.upadesha ?: ""
        val stem = context.padas.joinToString("") { it.upadesha }
        return (context.samasaType == SamasaType.MAYURAVYAMSAKADI || context.samasaType == SamasaType.TATPURUSA) &&
            (first == "अन्य" || second == "अन्य" || MayuravyamsakadiGana.contains(stem) || context.padas.any { MayuravyamsakadiGana.contains(it.upadesha) } || context.samasaType == SamasaType.MAYURAVYAMSAKADI)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.padas.firstOrNull()?.upadesha ?: ""
        val second = context.padas.getOrNull(1)?.upadesha ?: ""
        val compoundStem = when {
            first == "उच्च" && second == "अवच" -> "उच्चावच"
            first == "मयूर" && second == "व्यंसक" -> "मयूरव्यंसक"
            first == "अन्य" || second == "अन्य" -> deriveAnyarajasu(first, second)
            else -> context.padas.joinToString("") { it.upadesha }
        }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.72 (मयूरव्यंसकादयश्च) & Vārtika (अन्यराजादिषु च) forms Tatpuruṣa compound '$compoundStem'.",
        )
    }

    /**
     * Vārtika 2.1.72: अन्यराजादिषु च.
     * Compounding of 'अन्य' with nominal stems (राजन्, ग्राम, देश, भाषा, द्रव्य, भाव)
     * forming 'राजान्तरम्', 'ग्रामान्तरम्', 'देशान्तरम्', 'भाषान्तरम्', or 'अन्यराजः'.
     */
    private fun deriveAnyarajasu(first: String, second: String): String {
        val targetNoun = if (first == "अन्य") second else first
        val cleanNoun = targetNoun.removeSuffix("न्").removeSuffix("न").removeSuffix("म्")
        return if (cleanNoun.endsWith("ा") || cleanNoun.endsWith("ा")) {
            "${cleanNoun.dropLast(1)}ान्तर"
        } else {
            "${cleanNoun}ान्तर"
        }
    }
}
