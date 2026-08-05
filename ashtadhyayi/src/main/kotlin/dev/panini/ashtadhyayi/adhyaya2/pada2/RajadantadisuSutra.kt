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

import dev.panini.ganapatha.RajadantadiGana

/**
 * Sūtra 2.2.31: राजदन्तादिषु परम्.
 * Reverses expected pūrvapada placement for terms in the rājadanta group.
 * Example: दन्तानाम् राजा = राजदन्तम् / राजदन्तः (dantānāṁ rājā = rājadantaḥ).
 */
object RajadantadisuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.31",
    text = "राजदन्तादिषु परम्",
    hindiExplanation = "राजदन्त आदि गण में उपसर्जन पद का पर प्रयोग होता है (उदा. राजदन्तः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220031,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val stem = context.padas.joinToString("") { it.upadesha }
        return context.samasaType == SamasaType.TATPURUSA &&
            (RajadantadiGana.contains(stem) ||
             RajadantadiGana.contains(stem + "ः") ||
             RajadantadiGana.members.any { it.text.startsWith(stem) })
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.31 applies irregular word order for Rājadanta group '$compoundStem'.",
        )
    }
}
