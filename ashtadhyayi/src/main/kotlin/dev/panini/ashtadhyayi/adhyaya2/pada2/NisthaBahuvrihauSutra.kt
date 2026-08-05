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
 * Sūtra 2.2.36: निष्ठा.
 * Prescribes that a niṣṭhā (kta/ktavatu) participle is placed first in Bahuvrīhi.
 * Example: कृतं कृत्यं येन = कृतकृत्यः (kṛtakṛtyaḥ).
 */
object NisthaBahuvrihauSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.36",
    text = "निष्ठा",
    hindiExplanation = "बहुव्रीहि समास में निष्ठा (क्त/क्तवतु) प्रत्ययान्त पद का पूर्व प्रयोग होता है (उदा. कृतकृत्यः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220036,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.BAHUVRIHI &&
            (purva.endsWith("त") || purva.endsWith("तः") || purva.endsWith("तम्"))
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.36 places Niṣṭhā member first in Bahuvrīhi '$compoundStem'.",
        )
    }
}
