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
 * Sūtra 2.2.33: अजाद्यदन्तम् (registered as 2.2.101 for unique ID).
 * Rules Dvandva ordering placing vowel-initial, short-a ending term first.
 * Example: ईशः च कृष्णः च = ईशकृष्णौ (īśakṛṣṇau).
 */
object AjadyadantamExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.101",
    text = "अजाद्यदन्तम्",
    hindiExplanation = "द्वन्द्व समास में अजादि और अकारान्त सुबन्त पद का पूर्व प्रयोग होता है (उदा. ईशकृष्णौ)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220101,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.DVANDVA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.101 places vowel-initial, short-a ending term first in Dvandva '$compoundStem'.",
        )
    }
}
