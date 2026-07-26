package dev.panini.analysis

import dev.panini.core.Vibhakti

sealed interface VibhaktiRuleResult {
    data class Assigned(val vibhakti: Vibhakti, val evidence: KarakaEvidence) : VibhaktiRuleResult
}
