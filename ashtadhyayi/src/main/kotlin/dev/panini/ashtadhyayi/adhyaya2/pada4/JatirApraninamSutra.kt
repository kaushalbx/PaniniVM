package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sutra.SamasaSutra

/**
 * 2.4.6: जातिरप्राणिनाम्.
 */
object JatirApraninamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.4.6",
    text = "जातिरप्राणिनाम्",
    hindiExplanation = "अपाणिनां जातिवाचिनां द्वन्द्वः एकवद् भवति।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.DVANDVA
    private val apraniJatiWords = setOf("धाना", "शष्कुलि", "बदर", "ामलक")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val padas = context.padas.map { it.upadesha }
        return context.samasaType == SamasaType.DVANDVA && padas.any { it in apraniJatiWords }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.4.6: Formed Samāhāra Dvandva (neuter singular) for non-living species ($compoundStem).",
        )
    }
}
