package dev.panini.dhatupatha

import dev.panini.analysis.SemanticRelation
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** One source entry from the Pāṇinian Dhātupāṭha. */
open class Dhatu(
    val id: String,
    val krama: Int,
    val upadesha: String,
    val sourceSurface: String,
    val artha: String,
    val arthaHindi: String,
    val arthaEnglish: String,
    val gana: DhatuGana,
    val pada: PadaType? = null,
    val itStatus: ItStatus? = null,
    val karmatva: Karmatva? = null,
    val svara: Accent? = null,
    /** Lexically blocks the regular ṇic-conditioned guṇa of 7.3.86. */
    val blocksNicGuna: Boolean = false,
    open val operations: List<DhatuOperation> = emptyList(),
    open val semanticRelations: Set<SemanticRelation> = emptySet(),
    open val surfaceAliases: Set<String> = emptySet(),
) {
    /** Normalized root spelling used only by the derivation engine. */
    val derivationalSurface: String get() = sourceSurface
}
