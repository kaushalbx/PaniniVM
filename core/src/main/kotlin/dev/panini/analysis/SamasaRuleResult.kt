package dev.panini.analysis

/**
 * The result returned by every Samāsa Sūtra after application.
 *
 * A sealed interface mirrors the KarakaRuleResult pattern: a Sūtra either
 * forms a compound (Formed) or does not apply (NotApplicable).
 */
sealed interface SamasaRuleResult {
    /**
     * The Sūtra successfully formed a compound stem.
     *
     * @param compoundStem  The joined stem without case ending (e.g. "राजपुरुष").
     * @param explanation   Human-readable Pāṇinian trace of what happened.
     */
    data class Formed(
        val compoundStem: String,
        val explanation: String,
    ) : SamasaRuleResult

    /**
     * The Sūtra's conditions were not met — no compound was formed.
     */
    data object NotApplicable : SamasaRuleResult
}
