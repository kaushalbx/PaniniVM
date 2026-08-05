package dev.panini.shiksha

/**
 * Classifier for Nadī (river) names used in Samāsa sūtras (2.1.20, 2.1.21).
 */
object NadiClassifier {
    private val riverNames = setOf(
        "गङ्गा", "यमुना", "सरस्वती", "शोण", "नर्मदा", "सरयू",
        "सिन्धु", "कावेरी", "गोदावरी", "कृष्णा", "इरावती", "वितस्ता",
        "विपाशा", "शतद्रु", "चंद्रभागा", "तुङ्गभद्रा", "नद", "नदी"
    )

    fun isRiverName(stem: String, samjnas: Set<Samjna> = emptySet()): Boolean {
        if (samjnas.contains(Samjna.NADI)) return true
        if (stem in riverNames) return true
        return stem.endsWith("नदी") || stem.endsWith("वती")
    }
}
