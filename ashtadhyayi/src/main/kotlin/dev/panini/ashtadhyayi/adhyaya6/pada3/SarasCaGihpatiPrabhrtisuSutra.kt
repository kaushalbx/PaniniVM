package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.14 / 6.3.9 Ext: सरश्च गीःपतिप्रभृतिषु (registered as 6.3.109 for unique ID).
 * Prescribes Aluk of case affix for saras and gir in gīṣpati group.
 * Example: सरसिजम् (sarasijam), गीष्पतिः (gīṣpatiḥ).
 */
object SarasCaGihpatiPrabhrtisuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.109",
    text = "सरश्च गीःपतिप्रभृतिषु",
    hindiExplanation = "गीःपतिप्रभृति गण में सरस् तथा गिर् शब्दों की विभक्ति का अलुक् (अलोप) होता है (उदा. सरसिजम्, गीष्पतिः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630109,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return (context.samasaType == SamasaType.ALUK_TATPURUSA || context.samasaType == SamasaType.TATPURUSA) &&
            (first == "सरस्" || first == "सरसि" || first == "गिर्" || first == "गीः")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.109 preserves case affix (Aluk) for saras/gir in '$compoundStem'.",
        )
    }
}
