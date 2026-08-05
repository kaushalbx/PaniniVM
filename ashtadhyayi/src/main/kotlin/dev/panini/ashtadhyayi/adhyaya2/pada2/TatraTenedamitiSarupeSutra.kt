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
 * Sūtra 2.2.23: तत्र तेनेदमिति सरूपे.
 * Prescribes Bahuvrīhi compound of identical words denoting mutual fight/combat.
 * Example: केशाकेशि, दण्डादण्डि, मुष्टामुष्टि.
 */
object TatraTenedamitiSarupeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.23",
    text = "तत्र तेनेदमिति सरूपे",
    hindiExplanation = "युद्ध अर्थ में समान रूप वाले शब्दों का बहुव्रीहि समास होता है (उदा. केशाकेशि, दण्डादण्डि)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220023,
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
            (purva == uttara || (purva in combatWords && uttara in combatWords))
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purvaStem = context.purvaPada.upadesha.dropLast(1) + "ा"
        val uttaraStem = context.uttaraPada.upadesha.dropLast(1) + "ि"
        val compoundStem = purvaStem + uttaraStem
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.23 forms Combat Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
