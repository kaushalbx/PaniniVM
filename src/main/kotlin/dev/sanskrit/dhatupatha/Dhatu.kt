package dev.sanskrit.dhatupatha

/** One source entry from the Pāṇinian Dhātupāṭha. */
data class Dhatu(
    val id: String,
    val krama: Int,
    val upadesha: String,
    val artha: String,
    val arthaHindi: String,
    val arthaEnglish: String,
    val gana: Gana,
    val pada: PadaType? = null,
)

/** The ten traditional gaṇas of the Dhātupāṭha. */
enum class Gana {
    BHVADI, ADADI, JUHOTYADI, DIVADI, SVADI,
    TUDADI, RUDHADI, TANADI, KRYADI, CURADI,
}

enum class PadaType { PARASMAIPADA, ATMANEPADA, UBHAYAPADA }
