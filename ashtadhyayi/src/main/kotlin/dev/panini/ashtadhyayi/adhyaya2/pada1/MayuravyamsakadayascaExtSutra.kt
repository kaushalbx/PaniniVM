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

import dev.panini.ganapatha.MayuravyamsakadiGana

/**
 * Sūtra 2.1.70: मयूरव्यंसकादयश्च (registered as 2.1.106 for unique ID).
 * Prescribes irregular Tatpuruṣa nipātana compounds.
 * Example: मयूरव्यंसकादयः = मयूरव्यंसकः (mayūravyamsakaḥ).
 */
object MayuravyamsakadayascaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.106",
    text = "मयूरव्यंसकादयश्च",
    hindiExplanation = "मयूरव्यंसक आदि समास निपातन से सिद्ध होते हैं (उदा. मयूरव्यंसकः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210106,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.MAYURAVYAMSAKADI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val stem = context.padas.joinToString("") { it.upadesha }
        return (context.samasaType == SamasaType.MAYURAVYAMSAKADI || context.samasaType == SamasaType.TATPURUSA) &&
            (MayuravyamsakadiGana.contains(stem) || context.padas.any { MayuravyamsakadiGana.contains(it.upadesha) })
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.106 forms Mayūravyamsakādi Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
