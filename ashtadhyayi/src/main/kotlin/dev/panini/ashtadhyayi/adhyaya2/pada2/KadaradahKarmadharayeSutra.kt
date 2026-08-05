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

import dev.panini.ganapatha.KadaradiGana

/**
 * Sūtra 2.2.38: कडाराः कर्मधारये.
 * Rules optional ordering for terms in kaḍāra group in Karmadhāraya.
 * Example: कडारः हाटकः = कडारहाटकः / हाटककडारः.
 */
object KadaradahKarmadharayeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.38",
    text = "कडाराः कर्मधारये",
    hindiExplanation = "कर्मधारय समास में कडार आदि शब्दों का विकल्प से पूर्व प्रयोग होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = true,
    kramaValue = 220038,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.KARMADHARAYA &&
            context.padas.any { KadaradiGana.contains(it.upadesha) }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.38 applies optional ordering for Kaḍāra group in Karmadhāraya '$compoundStem'.",
        )
    }
}
