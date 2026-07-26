package dev.panini.analysis

sealed class NishedhaRuleResult {
    data class Blocked(
        val blockerSutraNumber: String,
        val blockerSutraText: String,
        val blockedTargetSutraNumber: String,
        val evidence: KarakaEvidence,
    ) : NishedhaRuleResult()

    object Allowed : NishedhaRuleResult()
}
