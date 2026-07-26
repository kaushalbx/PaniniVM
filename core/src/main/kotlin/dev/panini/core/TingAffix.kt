package dev.panini.core

/** Executable tiṅ inventory used by 3.4.78. */
enum class TingAffix(
    val purusha: Purusha,
    val vacana: Vacana,
    val pada: PadaType,
    val upadesha: String,
) {
    // Parasmaipada (9)
    TIP(Purusha.PRATHAMA, Vacana.EKAVACANA, PadaType.PARASMAIPADA, "तिप्"),
    TAS(Purusha.PRATHAMA, Vacana.DVIVACANA, PadaType.PARASMAIPADA, "तस्"),
    JHI(Purusha.PRATHAMA, Vacana.BAHUVACANA, PadaType.PARASMAIPADA, "झि"),
    SIP(Purusha.MADHYAMA, Vacana.EKAVACANA, PadaType.PARASMAIPADA, "सिप्"),
    THAS(Purusha.MADHYAMA, Vacana.DVIVACANA, PadaType.PARASMAIPADA, "थस्"),
    THA(Purusha.MADHYAMA, Vacana.BAHUVACANA, PadaType.PARASMAIPADA, "थ"),
    MIP(Purusha.UTTAMA, Vacana.EKAVACANA, PadaType.PARASMAIPADA, "मिप्"),
    VAS(Purusha.UTTAMA, Vacana.DVIVACANA, PadaType.PARASMAIPADA, "वस्"),
    MAS(Purusha.UTTAMA, Vacana.BAHUVACANA, PadaType.PARASMAIPADA, "मस्"),

    // Atmanepada (9)
    TA(Purusha.PRATHAMA, Vacana.EKAVACANA, PadaType.ATMANEPADA, "त"),
    ATAM(Purusha.PRATHAMA, Vacana.DVIVACANA, PadaType.ATMANEPADA, "आताम्"),
    JHA(Purusha.PRATHAMA, Vacana.BAHUVACANA, PadaType.ATMANEPADA, "झ"),
    THAS_A(Purusha.MADHYAMA, Vacana.EKAVACANA, PadaType.ATMANEPADA, "थास्"),
    ATHAM(Purusha.MADHYAMA, Vacana.DVIVACANA, PadaType.ATMANEPADA, "आथाम्"),
    DHVAM(Purusha.MADHYAMA, Vacana.BAHUVACANA, PadaType.ATMANEPADA, "ध्वम्"),
    IT(Purusha.UTTAMA, Vacana.EKAVACANA, PadaType.ATMANEPADA, "इट्"),
    VAHI(Purusha.UTTAMA, Vacana.DVIVACANA, PadaType.ATMANEPADA, "वहि"),
    MAHING(Purusha.UTTAMA, Vacana.BAHUVACANA, PadaType.ATMANEPADA, "महिङ्"),
    ;

    val termId: String get() = "ting-" + name.lowercase().replace('_', '-')

    companion object {
        fun fromUpadesha(upadesha: String): TingAffix? =
            entries.singleOrNull { it.upadesha == upadesha.trim() }

        fun select(purusha: Purusha, vacana: Vacana, pada: PadaType = PadaType.PARASMAIPADA): TingAffix? =
            entries.singleOrNull { it.purusha == purusha && it.vacana == vacana && it.pada == pada }
    }
}
