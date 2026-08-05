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
 * Sūtra 2.1.28: कालात्.
 * Prescribes Dvitīyā Tatpuruṣa compound for time duration words (kāla-vācī subanta)
 * with a compatible following subanta denoting state/quality over time.
 * Example: मासम् कल्याणी = माskल्याणी (māsakalyāṇī).
 */
object KalatSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.29",
    text = "कालात्",
    hindiExplanation = "कालवाची द्वितीयान्त सुबन्त का समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. मासकल्याणी)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210029,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 5,
), SamasaSutra {
    private val kalaWords = setOf("मास", "संवत्सर", "अहोरात्र", "अहः", "रात्र", "दिन")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            kalaWords.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.28 forms Kāla-Dvitīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
