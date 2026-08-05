package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.51: तद्धितार्थोत्तरपदसमाहारे च (registered as 2.1.81 for unique ID).
 * Prescribes Dvigu / Karmadhāraya Tatpuruṣa in taddhita, uttarapada, or samāhāra contexts.
 * Example: पञ्च पूल्यः समाहृताः = पञ्चपूली (pañcapūlī).
 */
object TaddhitarthaUttarapadaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.81",
    text = "तद्धितार्थोत्तरपदसमाहारे च",
    hindiExplanation = "तद्धितार्थ, उत्तरपद परे होने पर तथा समाहार में सङ्ख्यापूर्वा द्विगु और कर्मधारय तत्पुरुष समास होता है (उदा. पञ्चपूली)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210081,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 &&
            (context.samasaType == SamasaType.DVIGU || context.samasaType == SamasaType.KARMADHARAYA)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.81 forms Taddhitārtha/Samāhāra Dvigu compound '$compoundStem'.",
        )
    }
}
