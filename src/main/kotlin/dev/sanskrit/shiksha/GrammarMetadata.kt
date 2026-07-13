package dev.sanskrit.shiksha

enum class Samjna {
    VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, ANUNASIKA, SAVARNA,
    DHATU, PRATYAYA, ANGA, PADA, PRAGRHYA, SARVANAMA, BHA, GHI,
    NADI, APRUKTA, SAMBUDDHI, SARVANAMASTHANA, GHU, PRATIPADIKA,
    AVYAYA, NIPATA, GATI, UPASARGA
}

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
