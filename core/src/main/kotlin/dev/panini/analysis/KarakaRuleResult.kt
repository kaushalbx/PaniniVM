package dev.panini.analysis

import dev.panini.core.Karaka

/** Co-designations licensed by the assigning saṃjñā rule, not additional kāraka slots. */
enum class KarakaDesignation { HETU }

sealed interface KarakaRuleResult {
    data class Assigned(
        val karaka: Karaka,
        val evidence: KarakaEvidence,
        val additionalDesignations: Set<KarakaDesignation> = emptySet(),
        /** Simultaneous kāraka names, rather than competing interpretations. */
        val coexistingKarakas: Set<dev.panini.core.Karaka> = emptySet(),
    ) : KarakaRuleResult
}
