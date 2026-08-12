package dev.panini.linganushasanam

import dev.panini.core.Linga

/**
 * Result of evaluating a Liṅgānuśāsanam gender rule.
 */
sealed interface LingaRuleResult {
    data class Matched(
        val linga: Linga,
        val ruleId: String,
        val explanation: String,
    ) : LingaRuleResult

    object Unmatched : LingaRuleResult
}

/**
 * Pāṇinian gender rule interface according to the classical Liṅgānuśāsanam.
 */
interface LinganushasanamRule {
    val ruleId: String
    val description: String
    val priority: Int get() = 0

    fun matches(context: LingaRuleContext): Boolean
    fun apply(context: LingaRuleContext): LingaRuleResult
}
