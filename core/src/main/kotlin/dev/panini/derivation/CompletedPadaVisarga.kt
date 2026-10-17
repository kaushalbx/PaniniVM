package dev.panini.derivation

/** Explicit grammatical source supplied by lexical/morphological analysis.
 * S/R describe underlying sources; established ru requires already-derived
 * occurrence evidence. Written visarga alone supplies none of these analyses. */
enum class VisargaSource {
    S, R,
    /** Already-derived ru, requiring exact occurrence evidence; not a spelling-based analysis. */
    ESTABLISHED_RU,
}

/** Input provenance, not a sūtra application or a phonological sign. */
data class CompletedPadaVisargaInput(val surface: String, val source: VisargaSource)
