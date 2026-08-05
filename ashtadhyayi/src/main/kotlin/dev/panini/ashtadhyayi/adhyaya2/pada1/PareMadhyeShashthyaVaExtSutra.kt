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
 * Sūtra 2.1.18: पारे मध्ये षष्ठ्या वा (registered as 2.1.103 for unique ID).
 * Prescribes Avyayībhāva compound with pāre / madhye optionally with ṣaṣṭhī.
 * Example: पारेगङ्गम् / मध्येगङ्गम्.
 */
object PareMadhyeShashthyaVaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.103",
    text = "पारे मध्ये षष्ठ्या वा",
    hindiExplanation = "पारे और मध्ये सप्पम्यन्त पदों का षष्ठ्यन्त के साथ विकल्प से अव्ययीभाव समास होता है (उदा. पारेगङ्गम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = true,
    kramaValue = 210103,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    private val targetPrefixes = setOf("पारे", "मध्ये")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA && targetPrefixes.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.103 forms Pāre/Madhye Avyayībhāva compound '$compoundStem'.",
        )
    }
}
