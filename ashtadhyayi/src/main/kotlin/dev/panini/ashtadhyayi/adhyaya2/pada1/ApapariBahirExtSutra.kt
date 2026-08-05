package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.12: अपपरिबहिरञ्चवः पञ्चम्याः (registered as 2.1.97 for unique ID).
 * Prescribes Avyayībhāva compound with apa, pari, bahis, añcu words.
 */
object ApapariBahirExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.97",
    text = "अपपरिबहिरञ्चवः पञ्चम्याः",
    hindiExplanation = "अप, परि, बहिस्, अञ्चु उत्तरपद का पञ्चम्यन्त समर्थ सुबन्त के साथ अव्ययीभाव समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210097,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    private val prefixes = setOf("अप", "परि", "बहिस्", "प्राक्", "प्रत्यक्")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA && prefixes.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.97 forms Apa-pari-bahis Avyayībhāva compound '$compoundStem'.",
        )
    }
}
