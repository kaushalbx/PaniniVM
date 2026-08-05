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
 * Sūtra 5.4.78: अनुपसर्गादद्ध्वनः.
 * Prescribes Samāsānta a-pratyaya for adhvan not preceded by Upasarga.
 * Example: रम्याध्वा (ramyādhvaḥ).
 */
object AnupasargadAdhvanahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.78",
    text = "अनुपसर्गादद्ध्वनः",
    hindiExplanation = "उपसर्ग रहित अध्वन् उत्तरपद वाले समास से समासान्त 'अ' (टच्) प्रत्यय होता है (उदा. रम्याध्वा)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540078,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        val first = context.padas.first().upadesha
        return last == "अध्वन्" && first !in setOf("प्र", "परा", "अप", "सम", "अनु", "अव", "निस", "निर", "दुस", "दुर्", "वि", "आ", "नि", "अधि", "अपि", "अति", "सु", "उद", "अभि", "प्रति", "परि", "उप")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.78 adds Samāsānta 'a' suffix after non-Upasarga adhvan in '$compoundStem'.",
        )
    }
}
