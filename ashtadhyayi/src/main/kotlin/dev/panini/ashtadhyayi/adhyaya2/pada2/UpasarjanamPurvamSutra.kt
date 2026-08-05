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
 * Sūtra 2.2.30: उपसर्जनं पूर्वम्.
 * Rules that the Upasarjana (subordinate designation defined by Prathamā in compound-prescribing sūtra)
 * is placed first in the compound.
 */
object UpasarjanamPurvamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.30",
    text = "उपसर्जनं पूर्वम्",
    hindiExplanation = "समास में उपसर्जन संज्ञा वाला पद पूर्व (प्रथम स्थान पर) प्रयोग किया जाता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220030,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 100,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.30 places Upasarjana member first in compound '$compoundStem'.",
        )
    }
}
