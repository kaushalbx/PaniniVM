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
 * Sūtra 2.2.27: तत्र तेनेदमिति सरूपे (registered as 2.2.100 for unique ID).
 * Prescribes Bahuvrīhi compound in combat with identical weapon/instrument terms.
 * Example: केशेषु केशेषु गृहीत्वा इदं युद्ध प्रवृत्तम् = केशाकेशि (keśākeśi).
 */
object TatraTenedamitiSarupeExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.100",
    text = "तत्र तेनेदमिति सरूपे",
    hindiExplanation = "युद्ध (प्रहरण) अर्थ में समान रूप वाले पदों का सप्तम्यन्त और तृतीयान्त में बहुव्रीहि समास होता है (उदा. केशाकेशि)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220100,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.BAHUVRIHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.100 forms Sarūpa Combat Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
