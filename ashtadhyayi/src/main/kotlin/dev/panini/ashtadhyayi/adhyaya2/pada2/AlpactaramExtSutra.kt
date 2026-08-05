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
 * Sūtra 2.2.34: अल्पाच्तरम् (registered as 2.2.96 for unique ID).
 * Rules that member with fewer vowels is placed first in Dvandva.
 * Example: शिवः च केशवः च = शिवकेशवौ (śivakeśavau).
 */
object AlpactaramExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.96",
    text = "अल्पाच्तरम्",
    hindiExplanation = "द्वन्द्व समास में कम अच् (स्वर) वाले पद का पूर्व प्रयोग होता है (उदा. शिवकेशवौ)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220096,
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
            explanation = "2.2.96 places term with fewer vowels first in Dvandva '$compoundStem'.",
        )
    }
}
