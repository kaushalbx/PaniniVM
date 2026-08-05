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

import dev.panini.ganapatha.TishthadguPrabhrtiGana

/**
 * Sūtra 2.1.17: तिष्ठद्गुप्रभृतीनि च (registered as 2.1.98 for unique ID).
 * Prescribes irregular nipātana Avyayībhāva compounds in the tiṣṭhadgu group.
 * Example: तिष्ठन्त्यो गावः यस्मिन् काले = तिष्ठद्गु (tiṣṭhadgu).
 */
object TisthadguprabhrtiniSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.98",
    text = "तिष्ठद्गुप्रभृतीनि च",
    hindiExplanation = "तिष्ठद्गु आदि शब्द अव्ययीभाव समास निपातन से सिद्ध होते हैं (उदा. तिष्ठद्गु)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210098,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val stem = context.padas.joinToString("") { it.upadesha }
        return context.samasaType == SamasaType.AVYAYIBHAVA &&
                (TishthadguPrabhrtiGana.contains(stem) || context.padas.any { TishthadguPrabhrtiGana.contains(it.upadesha) })
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.98 forms Tiṣṭhadgu Avyayībhāva compound '$compoundStem'.",
        )
    }
}
