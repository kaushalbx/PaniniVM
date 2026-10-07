package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.27: तत्र तेनेदमिति सरूपे.
 * Prescribes Bahuvrīhi compound of identical words denoting mutual fight/combat.
 * Example: केशाकेशि, दण्डादण्डि, मुष्टामुष्टि.
 */
object TatraTenedamitiSarupeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.27",
    text = "तत्र तेनेदमिति सरूपे",
    hindiExplanation = "युद्ध अर्थ में समान रूप वाले शब्दों का बहुव्रीहि समास होता है (उदा. केशाकेशि, दण्डादण्डि)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220027,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    private val combatWords = setOf("केश", "दण्ड", "मुष्टि", "बाहु", "अङ्ग")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.BAHUVRIHI &&
            purva == uttara &&
            purva in combatWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        // Classification does not perform the separate 6.3.137 and 5.4.127 operations.
        val compoundStem = context.padas.flatMap { it.varnas }.toDevanagari()
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.27 forms Combat Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
