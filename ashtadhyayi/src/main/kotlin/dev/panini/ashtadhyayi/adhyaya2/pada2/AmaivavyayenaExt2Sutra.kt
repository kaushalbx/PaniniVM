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
 * Sūtra 2.2.20: अमैवाव्ययेन (registered as 2.2.103 for unique ID).
 * Prescribes mandatory Upapada compound when ending in am or evā.
 * Example: स्वादुंकारम् (svādumkāram), अग्रेभोजम् (agrebhojam).
 */
object AmaivavyayenaExt2Sutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.103",
    text = "अमैवाव्ययेन",
    hindiExplanation = "अम् तथा एव अव्ययान्त उपपद का समर्थ अव्यय के साथ नित्य समास होता है (उदा. स्वादुंकारम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220103,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.UPAPADA_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.UPAPADA_TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.103 forms mandatory Am-Evā Upapada compound '$compoundStem'.",
        )
    }
}
