package dev.sanskrit.derivation

/** Executable first-set parasmaipada tiṅ inventory used by 3.4.78. */
enum class TingAffix(
    val purusha: Purusha,
    val vacana: Vacana,
    val upadesha: String,
) {
    TIP(Purusha.PRATHAMA, Vacana.EKAVACANA, "तिप्"),
    TAS(Purusha.PRATHAMA, Vacana.DVIVACANA, "तस्"),
    JHI(Purusha.PRATHAMA, Vacana.BAHUVACANA, "झि"),
    ;

    val termId: String get() = "ting-" + name.lowercase()

    fun term(): DerivationTerm = DerivationTerm(termId, upadesha, TermKind.PRATYAYA, upadesha = upadesha)

    companion object {
        fun select(purusha: Purusha, vacana: Vacana): TingAffix? =
            entries.singleOrNull { it.purusha == purusha && it.vacana == vacana }
    }
}
