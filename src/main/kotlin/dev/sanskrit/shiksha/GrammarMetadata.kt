package dev.sanskrit.shiksha

enum class Samjna {
    VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, ANUNASIKA, SAVARNA,
    DHATU, PRATYAYA, ANGA, PADA, PRAGRHYA, SARVANAMA, BHA, GHI,
    NADI, APRUKTA, SAMBUDDHI, SARVANAMASTHANA, GHU, PRATIPADIKA,
    AVYAYA, NIPATA, GATI, UPASARGA
}

enum class SemanticFeature {
    // Gender (Liṅga)
    PUMS, STRI, NAPUMSAKA,

    // Kāraka / Kṛt
    KARTARI, KARMANI, BHAVE,

    // Tense/Mood (Lakāra)
    VARTAMANA, LAT,

    // Person (Puruṣa)
    PRATHAMA_PURUSHA,

    // Case (Vibhakti)
    PRATHAMA, DVITIYA, TRTIYA, CHATURTHI, PANCHAMI, SASTHI, SAPTAMI, SAMBODHANA,

    // Number (Vacana)
    EKAVACANA, DVIVACANA, BAHUVACANA,

    // Other
    GUNA_REQUEST, VRDDHI_REQUEST, DHATU_LOPA, ARDHADHATUKA,
    /** The term denotes something other than a substantive (dravya). */
    ASATTVA,
    /** The term is construed with a verb; required by the upasarga/gati rules. */
    KRIYAYOGA,
    /** The subject directly experiences the quality or state (kartṛ-vedanā). */
    KARTR_VEDANA,
    /** The derivational sense is offspring or descendant (apatya). */
    APATYA,
    /** The derivational sense is a descendant from a grandson onward (gotra). */
    GOTRA,
    /** The derivational sense is an immediate descendant (anantara apatya). */
    ANANTARA_APATYA,
    /** The derivational sense is the ruler associated with a janapada (tadrāja). */
    TADRAJA,
    /** The derivational context is the northern region (udīc). */
    UDICYA,
    /** A taddhita sense governed by the prāg-dīvyatīya section (4.1.83). */
    PRAGDIVYATIYA,
    /** The stem denotes a body part (svāṅga). */
    SVANGA,
    /** The expression is used in the general future sense (bhaviṣyat). */
    BHAVISYAT,
    /** The derivation expresses an ablative relation (apādāna). */
    APADANA,
    /** A lexical formation is licensed by the Unādi tradition. */
    UNADI_LICENSED,
    /** The derivational sense is a collection or aggregate (samūha). */
    SAMUHA,
    /** The referent is the territory governed by the named group (viṣaya-deśa). */
    VISHAYA_DESE,
    /** The derivational sense is the place where the named thing resides (tasya nivāsa). */
    NIVASA,
    /** A taddhita derivation in the four senses carried through 4.2.67–4.2.100. */
    CHATURARTHIKA,
    /** The derivation denotes something born in, or originating from, the named place. */
    JATA,
    /** The derivation concerns a time-denoting expression (kālavṛtti). */
    KALAVRTTI,
    /** The derivation denotes something situated in the named place (tatra bhava). */
    TATRA_BHAVA,
    /** The derivational sense is an exposition of the named subject (vyākhyāna). */
    VYAKHYANA,
    /** The derivation denotes something coming from the named source (tata āgata). */
    TATAH_AGATA,
    /** The derivation denotes ancestral origin or lineage (abhijana). */
    ABHIJANA,
    /** The derivational sense is one who studies or knows the named text. */
    ADHYAYANA_VEDANA,
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
