package dev.sanskrit.derivation

/** Executable first-set parasmaipada tiṅ inventory used by 3.4.78. */
enum class TingAffix(
    val purusha: SemanticFeature,
    val vacana: Vacana,
    val upadesha: String,
) {
    TIP(SemanticFeature.PRATHAMA_PURUSHA, Vacana.EKAVACANA, "तिप्"),
    TAS(SemanticFeature.PRATHAMA_PURUSHA, Vacana.DVIVACANA, "तस्"),
    JHI(SemanticFeature.PRATHAMA_PURUSHA, Vacana.BAHUVACANA, "झि"),
    ;

    val termId: String get() = "ting-" + name.lowercase()

    fun term(): DerivationTerm = DerivationTerm(termId, upadesha, TermKind.PRATYAYA, upadesha = upadesha)

    companion object {
        fun fromFeatures(features: Set<SemanticFeature>): TingAffix? =
            entries.singleOrNull { it.purusha in features && it.vacana.semanticFeature in features }
    }
}
