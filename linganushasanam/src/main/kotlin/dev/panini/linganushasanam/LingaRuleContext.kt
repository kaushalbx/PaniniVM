package dev.panini.linganushasanam

import dev.panini.core.SamasaType
import dev.panini.core.LingaSemanticClass
import dev.panini.core.Linga
import dev.panini.core.Vacana

/**
 * Grammatical context for evaluating Pāṇinian Liṅgānuśāsana rules.
 *
 * @property pratipadika Nominal stem text (e.g. "राजसभा", "हविस्", "पञ्चम्").
 * @property pratyaya Internal or ending affix upadeśa (e.g. "क्तिन्", "ल्युट्", "ङीप्").
 * @property padas List of constituent stem upadeśas if evaluating a compound.
 * @property samasaType Optional [SamasaType] if evaluating compound gender.
 * @property applicableSutraNumbers Canonical rules whose full grammatical
 * applicability has already been established by an upstream derivation or
 * lexical authority. This is the lossless path for conditions that cannot be
 * inferred from the surface spelling alone.
 */
data class LingaRuleContext(
    val pratipadika: String,
    val pratyaya: String? = null,
    val padas: List<String> = emptyList(),
    val samasaType: SamasaType? = null,
    val semanticClasses: Set<LingaSemanticClass> = emptySet(),
    val applicableSutraNumbers: Set<String> = emptySet(),
    /** Explicit derivational facts used only where the canonical rule names them. */
    val syllableCount: Int? = null,
    val numericalValue: Long? = null,
    val upadha: String? = null,
    val vacana: Vacana? = null,
    val isIndeclinable: Boolean = false,
    val isQualityExpression: Boolean = false,
    val isPronoun: Boolean = false,
    /** Gender of the qualified/referred expression for agreement-sensitive rules. */
    val governingLinga: Linga? = null,
    val isDhatuDerivative: Boolean = false,
    val isVedicUsage: Boolean = false,
)
