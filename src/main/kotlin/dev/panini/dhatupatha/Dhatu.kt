package dev.panini.dhatupatha

import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** One source entry from the Pāṇinian Dhātupāṭha. */
data class Dhatu(
    val id: String,
    val krama: Int,
    val upadesha: String,
    private val sourceSurface: String,
    val artha: String,
    val arthaHindi: String,
    val arthaEnglish: String,
    val gana: Gana,
    val pada: PadaType? = null,
    val itStatus: ItStatus? = null,
    val karmatva: Karmatva? = null,
    val svara: Accent? = null,
) {
    /** Normalized root spelling used only by the derivation engine. */
    internal val derivationalSurface: String get() = sourceSurface
}

/** The ten traditional gaṇas of the Dhātupāṭha. */
enum class Gana {
    BHVADI, ADADI, JUHOTYADI, DIVADI, SVADI,
    TUDADI, RUDHADI, TANADI, KRYADI, CURADI,
}

enum class PadaType { PARASMAIPADA, ATMANEPADA, UBHAYAPADA }
