package dev.panini.shiksha

enum class Accent {
    UDATTA, ANUDATTA, SVARITA,
    ADYUDATTA, ANTODATTA, MADHYODATTA
}

enum class Recension {
    KASHIKA, SIDDHANTA_KAUMUDI, MAHABHASHYA, SHAKATAYANA
}

enum class ItStatus {
    SET, ANIT, VET
}

enum class Karmatva {
    SAKARMAKA, AKARMAKA, DVIKARMAKA
}

/** The lexical sense in which a particular term is being derived. */
enum class LexicalUse {
    RELATIVE_POSITION,
    PROPER_NAME,
    KINSHIP,
    WEALTH,
    EXTERIOR_ASSOCIATION,
    GARMENT,
}
