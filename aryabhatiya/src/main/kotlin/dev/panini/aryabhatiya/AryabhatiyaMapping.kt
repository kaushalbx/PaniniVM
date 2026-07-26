package dev.panini.aryabhatiya

/**
 * Mapping tables for Aryabhatiya alphanumerical notation (Āryabhaṭīya Geetika Pada 2).
 */
object AryabhatiyaMapping {

    enum class ConsonantType { VARGA, AVARGA }

    private val vargaMap: Map<Char, Long> = mapOf(
        'क' to 1L, 'ख' to 2L, 'ग' to 3L, 'घ' to 4L, 'ङ' to 5L,
        'च' to 6L, 'छ' to 7L, 'ज' to 8L, 'झ' to 9L, 'ञ' to 10L,
        'ट' to 11L, 'ठ' to 12L, 'ड' to 13L, 'ढ' to 14L, 'ण' to 15L,
        'त' to 16L, 'थ' to 17L, 'द' to 18L, 'ध' to 19L, 'न' to 20L,
        'प' to 21L, 'फ' to 22L, 'ब' to 23L, 'भ' to 24L, 'म' to 25L
    )

    private val avargaMap: Map<Char, Long> = mapOf(
        'य' to 30L, 'र' to 40L, 'ल' to 50L, 'व' to 60L,
        'श' to 70L, 'ष' to 80L, 'स' to 90L, 'ह' to 100L
    )

    private val vowelPowerMap: Map<Char, Int> = mapOf(
        'अ' to 0, 'आ' to 0,
        'इ' to 1, 'ई' to 1, 'ि' to 1, 'ी' to 1,
        'उ' to 2, 'ऊ' to 2, 'ु' to 2, 'ू' to 2,
        'ऋ' to 3, 'ॠ' to 3, 'ृ' to 3, 'ॄ' to 3,
        'ल' to 4, // Note: vowel lri
        'ए' to 5, 'ऐ' to 5, 'े' to 5, 'ै' to 5,
        'ओ' to 6, 'औ' to 6, 'ो' to 6, 'ौ' to 6
    )

    fun getConsonantValue(ch: Char): Pair<Long, ConsonantType>? {
        vargaMap[ch]?.let { return Pair(it, ConsonantType.VARGA) }
        avargaMap[ch]?.let { return Pair(it, ConsonantType.AVARGA) }
        return null
    }

    fun getVowelPower(ch: Char): Int? = vowelPowerMap[ch]

    fun isConsonant(ch: Char): Boolean = ch in vargaMap || ch in avargaMap
}
