package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti

enum class KarakaAlternativeKind { OPTIONAL_DECLINE, OPTIONAL_APPLY, COEXISTENT_CASE, COMPETING_ANALYSIS, OPTIONAL_VIBHAKTI }

data class RejectedKarakaRule(
    val targetSutra: String,
    val prohibition: KarakaEvidence,
)

data class KarakaAlternative(
    val karaka: Karaka,
    val vibhakti: Vibhakti?,
    val evidence: List<KarakaEvidence>,
    val kind: KarakaAlternativeKind = KarakaAlternativeKind.COMPETING_ANALYSIS,
    /** Distinguishes a generated grammatical option from a reading of the written form. */
    val matchesObservedMorphology: Boolean = true,
    /** The optional decision is separate from the rule proving this designation. */
    val optionalDecision: KarakaEvidence? = null,
)

data class KarakaResolution(
    val candidates: Set<Karaka>,
    val resolved: Karaka?,
    val possibleVibhaktis: Set<Vibhakti>,
    val evidence: List<KarakaEvidence>,
    val resolvedVibhakti: Vibhakti? = null,
    /** Counter-branches licensed by optional saṃjñā rules. */
    val alternatives: List<KarakaAlternative> = emptyList(),
    val rejectedRules: List<RejectedKarakaRule> = emptyList(),
    val nominalDesignations: List<NominalDesignationResult> = emptyList(),
    val additionalDesignations: Set<KarakaDesignation> = emptySet(),
    /** A possible object reading remains unsettled in the referenced participant. */
    val unresolvedObjectMediators: Map<String, Set<Karaka>> = emptyMap(),
    /** Describes the primary branch, even when both branches remain possible. */
    val primaryOptionalDecision: KarakaEvidence? = null,
    val coexistingKarakas: Set<Karaka> = emptySet(),
)
