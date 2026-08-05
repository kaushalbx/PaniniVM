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
 * Sūtra 5.4.98: उत्तरपदलोपी द्विगुः.
 * Prescribes Samāsānta for uttarapada-lopi Dvigu compounds.
 */
object UttarapadalopiDviguSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.98",
    text = "उत्तरपदलोपी द्विगुः",
    hindiExplanation = "उत्तरपद के लोप वाले द्विगु समास से समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540098,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.DVIGU
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.98 adds Samāsānta for uttarapadalopi Dvigu '$compoundStem'.",
        )
    }
}
