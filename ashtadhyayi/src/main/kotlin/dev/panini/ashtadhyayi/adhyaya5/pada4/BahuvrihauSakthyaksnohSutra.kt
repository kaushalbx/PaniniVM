package dev.panini.ashtadhyayi.adhyaya5.pada4

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
 * Sūtra 5.4.113: बहुव्रीहौ सक्थ्यक्ष्णोः स्वाङ्गात् षच्.
 * Prescribes Samāsānta ṣac (-a) suffix for sakthi and akṣi denoting body parts in Bahuvrīhi.
 * Example: कमलाक्षः (kamalākṣaḥ), दीर्घसक्थः (dīrghasakthaḥ).
 */
object BahuvrihauSakthyaksnohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.113",
    text = "बहुव्रीहौ सक्थ्यक्ष्णोः स्वाङ्गात् षच्",
    hindiExplanation = "स्वाङ्गवाची सक्थि तथा अक्षि उत्तरपद वाले बहुव्रीहि समास से नित्य समासान्त 'अ' (षच्) प्रत्यय होता है (उदा. कमलाक्षः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540113,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.BAHUVRIHI && (last == "सक्थि" || last == "अक्षि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.113 adds Samāsānta ṣac ('a') for sakthi/akṣi in '$compoundStem'.",
        )
    }
}
