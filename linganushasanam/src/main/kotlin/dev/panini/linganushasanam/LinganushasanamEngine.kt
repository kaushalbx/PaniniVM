package dev.panini.linganushasanam

import dev.panini.core.Linga

/**
 * Pāṇinian engine for Sūtra-driven nominal gender resolution (लिङ्गानुशासनम्).
 */
class LinganushasanamEngine(
    private val sutras: List<LinganushasanaSutra> = LinganushasanamRegistry.sutras
) {
    fun resolveOrNull(context: LingaRuleContext): LingaRuleResult.Matched? {
        for (sutra in sutras) {
            if (sutra.matches(context)) {
                val result = sutra.apply(context)
                if (result is LingaRuleResult.Matched) return result
            }
        }
        return null
    }

    /**
     * Resolves the Pāṇinian gender for the given [LingaRuleContext].
     * Defaults to [Linga.PUMS] if no specific Sūtra matches.
     */
    fun resolve(context: LingaRuleContext): LingaRuleResult.Matched {
        resolveOrNull(context)?.let { return it }
        return LingaRuleResult.Matched(
            linga = Linga.PUMS,
            ruleId = "LINGA_DEFAULT",
            explanation = "Default masculine assignment for '${context.pratipadika}'.",
        )
    }
}
