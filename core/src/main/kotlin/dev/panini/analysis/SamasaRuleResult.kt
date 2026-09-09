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
     * @param memberEdits   Indexed replacements of compound members.  The
     *                      derivation engine composes these structurally and
     *                      joins the members only when sandhi is performed.
     * @param samasantaSuffix suffix appended after the transformed members.
     * @param wholeStemOverride true only for an indivisible irregular form.
     */
    data class Formed(
        val compoundStem: String,
        val explanation: String,
        val memberEdits: Map<Int, String> = emptyMap(),
        val samasantaSuffix: String? = null,
        val wholeStemOverride: Boolean = false,
    ) : SamasaRuleResult

    /**
     * The Sūtra's conditions were not met — no compound was formed.
     */
    data object NotApplicable : SamasaRuleResult
}
