package dev.sanskrit.shiksha

enum class Sthana {
    KANTHA,    // Velar / Guttural
    TALU,      // Palatal
    MURDHA,    // Retroflex
    DANTA,     // Dental
    OSTHA,     // Labial
    NASIKA,    // Nasal
    KANTHATALU,
    KANTHOSTHA,
    DANTOSTHA;

    fun constituents(): Set<Sthana> = when (this) {
        KANTHATALU -> setOf(KANTHA, TALU)
        KANTHOSTHA -> setOf(KANTHA, OSTHA)
        DANTOSTHA -> setOf(DANTA, OSTHA)
        else -> setOf(this)
    }
}

