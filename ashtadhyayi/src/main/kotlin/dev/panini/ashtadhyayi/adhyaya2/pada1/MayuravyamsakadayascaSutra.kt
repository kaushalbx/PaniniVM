package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sutra.SamasaSutra

/**
 * Sūtra 2.1.72: मयूरव्यंसकादयश्च.
 * Prescribes Nitya Tatpuruṣa compounding for irregular words belonging to the Mayūravyamsakādi gaṇa.
 * Examples: मयूरव्यंसकः (कपटमयूरः), उच्चावचम् (उच्चं च अवचं च).
 */
object MayuravyamsakadayascaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.72",
    text = "मयूरव्यंसकादयश्च",
    hindiExplanation = "मयूरव्यंसक आदि शब्दों का नित्य तत्पुरुष समास निपातित होता है (उदा. मयूरव्यंसकः, उच्चावचम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210072,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.MAYURAVYAMSAKADI
    private val mayuravyamsakadiGana = setOf("मयूरव्यंसक", "उच्चावच", "चिन्मात्र", "अन्यराजा")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        if (context.samasaType == SamasaType.MAYURAVYAMSAKADI) return true
        val stem = context.padas.joinToString("") { it.upadesha }
        return stem in mayuravyamsakadiGana
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.72 (मयूरव्यंसकादयश्च) forms Nitya Tatpuruṣa compound '$stem'.",
        )
    }
}
