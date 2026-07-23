package dev.panini.vyakaranam.analysis

data class ProhibitionContext(
    val targetSutraNumber: String,
    val affixItMarkers: Set<Char> = emptySet(),
    val isKitOrNgitAffix: Boolean = false,
)

sealed class NishedhaRuleResult {
    data class Blocked(
        val blockerSutraNumber: String,
        val blockerSutraText: String,
        val blockedTargetSutraNumber: String,
        val evidence: KarakaEvidence,
    ) : NishedhaRuleResult()

    object Allowed : NishedhaRuleResult()
}

object NishedhaRuleEngine {
    fun evaluateProhibition(context: ProhibitionContext): NishedhaRuleResult {
        val isBlockedByKngitiCa = context.isKitOrNgitAffix ||
            'क' in context.affixItMarkers ||
            'ङ' in context.affixItMarkers

        return if (isBlockedByKngitiCa && context.targetSutraNumber in setOf("1.1.1", "1.1.2", "1.1.3", "7.3.84")) {
            NishedhaRuleResult.Blocked(
                blockerSutraNumber = "1.1.5",
                blockerSutraText = "क्ङिति च",
                blockedTargetSutraNumber = context.targetSutraNumber,
                evidence = KarakaEvidence("1.1.5", "क्ङिति च", "Prohibits Guṇa/Vṛddhi sūtra ${context.targetSutraNumber} before K-it/Ṅ-it affix."),
            )
        } else {
            NishedhaRuleResult.Allowed
        }
    }
}
