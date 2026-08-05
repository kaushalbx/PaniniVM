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
 * Sūtra 2.2.32: अभ्यर्हितं च (registered as 2.2.105 for unique ID).
 * Rules Dvandva ordering placing most revered/honored member first.
 * Example: माता च पिता च = मातापितरौ (mātāpitarau).
 */
object AbhyarhitamChaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.105",
    text = "अभ्यर्हितं च",
    hindiExplanation = "द्वन्द्व समास में अभ्यर्हित (पूज्य/श्रेष्ठ) पद का पूर्व प्रयोग होता है (उदा. मातापितरौ)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220105,
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
            explanation = "2.2.105 places most revered term first in Dvandva '$compoundStem'.",
        )
    }
}
