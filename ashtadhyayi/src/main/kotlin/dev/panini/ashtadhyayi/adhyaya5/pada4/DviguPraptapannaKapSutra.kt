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
 * Sūtra 5.4.128: द्विगुप्राप्तापन्नापन्नपरिमाणिभ्यः.
 * Prescribes Samāsānta -kap suffix in Dvigu and specific compounds.
 */
object DviguPraptapannaKapSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.128",
    text = "द्विगुप्राप्तापन्नापन्नपरिमाणिभ्यः",
    hindiExplanation = "द्विगु, प्राप्त, आपन्न तथा परिमाणिन् शब्दों से समासान्त कप् (क) प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540128,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return context.samasaType == SamasaType.DVIGU || first == "प्राप्त" || first == "आपन्न"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "क"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.128 adds Samāsānta kap ('ka') in '$compoundStem'.",
        )
    }
}
