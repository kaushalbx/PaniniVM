package dev.panini.derivation

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Svara

/** The 21 sup slots of 4.1.2; each enum entry is its one executable source. */
enum class SupAffix(
    val vibhakti: Vibhakti,
    val vacana: Vacana,
    val upadesha: String,
    val initialSurface: String = upadesha,
    val itMarkers: Set<ItMarker> = emptySet(),
) {
    SU(Vibhakti.PRATHAMA, Vacana.EKAVACANA, "सुँ"),
    AU(Vibhakti.PRATHAMA, Vacana.DVIVACANA, Svara.AU.devanagari),
    JAS(Vibhakti.PRATHAMA, Vacana.BAHUVACANA, "जस्", itMarkers = setOf(ItMarker.J)),
    AM(Vibhakti.DVITIYA, Vacana.EKAVACANA, "अम्"),
    AUT(Vibhakti.DVITIYA, Vacana.DVIVACANA, "औट्"),
    SAS(Vibhakti.DVITIYA, Vacana.BAHUVACANA, "शस्"),
    TA(Vibhakti.TRTIYA, Vacana.EKAVACANA, "टा"),
    BHYAM_3(Vibhakti.TRTIYA, Vacana.DVIVACANA, "भ्याम्"),
    BHIS(Vibhakti.TRTIYA, Vacana.BAHUVACANA, "भिस्"),
    NGE(Vibhakti.CHATURTHI, Vacana.EKAVACANA, "ङे"),
    BHYAM_4(Vibhakti.CHATURTHI, Vacana.DVIVACANA, "भ्याम्"),
    BHYAS_4(Vibhakti.CHATURTHI, Vacana.BAHUVACANA, "भ्यस्"),
    NGASI(Vibhakti.PANCHAMI, Vacana.EKAVACANA, "ङसि", itMarkers = setOf(ItMarker.U)),
    BHYAM_5(Vibhakti.PANCHAMI, Vacana.DVIVACANA, "भ्याम्"),
    BHYAS_5(Vibhakti.PANCHAMI, Vacana.BAHUVACANA, "भ्यस्"),
    NGAS(Vibhakti.SASTHI, Vacana.EKAVACANA, "ङस्"),
    OS_6(Vibhakti.SASTHI, Vacana.DVIVACANA, "ओस्"),
    AM_6(Vibhakti.SASTHI, Vacana.BAHUVACANA, "आम्"),
    NGI(Vibhakti.SAPTAMI, Vacana.EKAVACANA, "ङि"),
    OS_7(Vibhakti.SAPTAMI, Vacana.DVIVACANA, "ओस्"),
    SUP(Vibhakti.SAPTAMI, Vacana.BAHUVACANA, "सुप्");

    val id: String get() = "sup-" + name.lowercase().replace('_', '-')

    fun term(): DerivationTerm = DerivationTerm(id, initialSurface, TermKind.PRATYAYA, itMarkers, upadesha)

    companion object {
        fun select(vibhakti: Vibhakti, vacana: Vacana): SupAffix =
            entries.single { it.vibhakti == vibhakti && it.vacana == vacana }

        fun fromContext(context: DerivationalContext): SupAffix? {
            val vibhakti = context.rupa.vibhakti ?: return null
            val vacana = context.rupa.vacana ?: return null
            return entries.singleOrNull { it.vibhakti == vibhakti && it.vacana == vacana }
        }

    }
}
