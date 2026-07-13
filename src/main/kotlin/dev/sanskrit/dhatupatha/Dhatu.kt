package dev.sanskrit.dhatupatha

import dev.sanskrit.shiksha.Accent
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.shiksha.Karmatva

/** One source entry from the Pāṇinian Dhātupāṭha. */
data class Dhatu(
    val id: String,
    val krama: Int,
    val upadesha: String,
    val mula: String,
    val artha: String,
    val arthaHindi: String,
    val arthaEnglish: String,
    val gana: Gana,
    val pada: PadaType? = null,
    val itStatus: ItStatus? = null,
    val karmatva: Karmatva? = null,
    val svara: Accent? = null,
)

/** The ten traditional gaṇas of the Dhātupāṭha. */
enum class Gana {
    BHVADI, ADADI, JUHOTYADI, DIVADI, SVADI,
    TUDADI, RUDHADI, TANADI, KRYADI, CURADI,
}

enum class PadaType { PARASMAIPADA, ATMANEPADA, UBHAYAPADA }
