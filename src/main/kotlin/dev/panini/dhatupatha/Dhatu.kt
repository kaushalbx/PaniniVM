package dev.panini.dhatupatha

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
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
    open val operations: List<dev.panini.execution.DhatuOperation> = emptyList(),
) {
    /** Normalized root spelling used only by the derivation engine. */
    internal val derivationalSurface: String get() = sourceSurface
}
