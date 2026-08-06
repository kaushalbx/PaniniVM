package dev.panini.linganushasanam

import dev.panini.core.SamasaType

/**
 * Grammatical context for evaluating Pāṇinian Liṅgānuśāsana rules.
 *
 * @property pratipadika Nominal stem text (e.g. "राजसभा", "हविस्", "पञ्चम्").
 * @property pratyaya Internal or ending affix upadeśa (e.g. "क्तिन्", "ल्युट्", "ङीप्").
 * @property padas List of constituent stem upadeśas if evaluating a compound.
 * @property samasaType Optional [SamasaType] if evaluating compound gender.
 */
data class LingaRuleContext(
    val pratipadika: String,
    val pratyaya: String? = null,
    val padas: List<String> = emptyList(),
    val samasaType: SamasaType? = null,
)
