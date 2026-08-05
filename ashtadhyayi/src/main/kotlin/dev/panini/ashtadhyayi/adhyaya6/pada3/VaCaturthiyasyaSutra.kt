package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 6.3.79: वा चतुर्थीयस्य.
 * Optional rule for dative forms.
 */
object VaCaturthiyasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.79",
    text = "वा चतुर्थीयस्य",
    hindiExplanation = "चतुर्थी-प्रत्ययान्त पूर्वपद का अलुक्/नियम विकल्प से होता है।",
    type = SutraType.VIBHASHA,
    chapter = 6,
    pada = 3,
    optional = true,
    kramaValue = 630079,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first()
        return first.vibhakti == Vibhakti.CHATURTHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.79 optionally applies dative pūrvapada rule in '$compoundStem'.",
        )
    }
}
