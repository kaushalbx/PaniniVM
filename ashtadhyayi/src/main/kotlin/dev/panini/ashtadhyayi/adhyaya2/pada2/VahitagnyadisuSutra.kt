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
 * Sūtra 2.2.37: वाहिताग्न्यादिषु.
 * Rules optional word ordering for terms in the āhitāgni group in Bahuvrīhi.
 * Example: आहिताग्निः / अग्निहितः (āhitāgniḥ / agnihitaḥ).
 */
object VahitagnyadisuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.37",
    text = "वाहिताग्न्यादिषु",
    hindiExplanation = "आहिताग्नि आदि गण में निष्ठा प्रत्ययान्त पद का विकल्प से उत्तर प्रयोग होता है (उदा. आहिताग्निः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = true,
    kramaValue = 220037,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    private val ahitagniGroup = setOf("आहिताग्नि", "अग्निहित", "जातपुत्र")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.BAHUVRIHI &&
            context.padas.any { ahitagniGroup.contains(it.upadesha) }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.37 applies optional word ordering for Āhitāgni group '$compoundStem'.",
        )
    }
}
