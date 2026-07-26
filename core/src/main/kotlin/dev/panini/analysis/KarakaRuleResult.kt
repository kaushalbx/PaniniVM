package dev.panini.analysis

import dev.panini.core.Karaka

sealed interface KarakaRuleResult {
    data class Assigned(val karaka: Karaka, val evidence: KarakaEvidence) : KarakaRuleResult
}
