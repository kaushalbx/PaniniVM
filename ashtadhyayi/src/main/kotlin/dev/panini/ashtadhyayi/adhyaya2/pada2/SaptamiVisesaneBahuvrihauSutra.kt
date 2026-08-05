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
 * Sūtra 2.2.35: सप्तमीविशेषणे बहुव्रीहौ.
 * Prescribes that a locative-ending or adjective member is placed first in Bahuvrīhi.
 * Example: कण्ठे कालः अस्य = कण्ठेकालः (kaṇṭhekālaḥ).
 */
object SaptamiVisesaneBahuvrihauSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.35",
    text = "सप्तमीविशेषणे बहुव्रीहौ",
    hindiExplanation = "बहुव्रीहि समास में सप्तम्यन्त तथा विशेषण पद का पूर्व प्रयोग होता है (उदा. कण्ठेकालः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220035,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 &&
            context.samasaType == SamasaType.BAHUVRIHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.35 places Saptamī/Viśeṣaṇa member first in Bahuvrīhi '$compoundStem'.",
        )
    }
}
