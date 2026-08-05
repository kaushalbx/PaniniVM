package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.ganapatha.PatresamitadiGana
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.48: पात्रेसमितादयश्च.
 * Irregular Saptamī Tatpuruṣa compounds denoting reproach/censure (aluk-samāsa and irregular ordering).
 * Examples: पात्रेसमिताः (pātresamitāḥ), कूपमण्डूकः (kūpamaṇḍūkaḥ), गेहेशूरः (geheśūraḥ), गोष्ठेशूरः.
 */
object PatresamitadiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.148",
    text = "पात्रेसमितादयश्च",
    hindiExplanation = "पात्रेसमित आदि समास निपातन से सिद्ध होते हैं (उदा. पात्रेसमिताः, कूपमण्डूकः, गेहेशूरः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210148,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val stem = context.padas.joinToString("") { it.upadesha }
        return (context.samasaType == SamasaType.TATPURUSA) &&
                (PatresamitadiGana.contains(stem) || PatresamitadiGana.contains(stem + "ः") ||
                 PatresamitadiGana.members.any { it.text.startsWith(stem) } ||
                 context.padas.any { PatresamitadiGana.contains(it.upadesha) })
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.48 forms Pātresamitādi Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
