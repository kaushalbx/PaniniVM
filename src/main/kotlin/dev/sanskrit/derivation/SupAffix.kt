package dev.sanskrit.derivation

import dev.sanskrit.shiksha.Svara

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

        fun fromFeatures(features: Set<dev.sanskrit.shiksha.SemanticFeature>): SupAffix? {
            val vibhakti = when {
                dev.sanskrit.shiksha.SemanticFeature.PRATHAMA in features -> Vibhakti.PRATHAMA
                dev.sanskrit.shiksha.SemanticFeature.DVITIYA in features -> Vibhakti.DVITIYA
                dev.sanskrit.shiksha.SemanticFeature.TRTIYA in features -> Vibhakti.TRTIYA
                dev.sanskrit.shiksha.SemanticFeature.CHATURTHI in features -> Vibhakti.CHATURTHI
                dev.sanskrit.shiksha.SemanticFeature.PANCHAMI in features -> Vibhakti.PANCHAMI
                dev.sanskrit.shiksha.SemanticFeature.SASTHI in features -> Vibhakti.SASTHI
                dev.sanskrit.shiksha.SemanticFeature.SAPTAMI in features -> Vibhakti.SAPTAMI
                else -> null
            }
            val vacana = when {
                dev.sanskrit.shiksha.SemanticFeature.EKAVACANA in features -> Vacana.EKAVACANA
                dev.sanskrit.shiksha.SemanticFeature.DVIVACANA in features -> Vacana.DVIVACANA
                dev.sanskrit.shiksha.SemanticFeature.BAHUVACANA in features -> Vacana.BAHUVACANA
                else -> null
            }
            if (vibhakti == null || vacana == null) return null
            val expectedFeaturesCount = (if (features.contains(dev.sanskrit.shiksha.SemanticFeature.PRATHAMA) ||
                features.contains(dev.sanskrit.shiksha.SemanticFeature.DVITIYA) ||
                features.contains(dev.sanskrit.shiksha.SemanticFeature.TRTIYA) ||
                features.contains(dev.sanskrit.shiksha.SemanticFeature.CHATURTHI) ||
                features.contains(dev.sanskrit.shiksha.SemanticFeature.PANCHAMI) ||
                features.contains(dev.sanskrit.shiksha.SemanticFeature.SASTHI) ||
                features.contains(dev.sanskrit.shiksha.SemanticFeature.SAPTAMI)
            ) 1 else 0) +
                    (if (features.contains(dev.sanskrit.shiksha.SemanticFeature.EKAVACANA) ||
                        features.contains(dev.sanskrit.shiksha.SemanticFeature.DVIVACANA) ||
                        features.contains(dev.sanskrit.shiksha.SemanticFeature.BAHUVACANA)
                    ) 1 else 0)
            if (features.size > expectedFeaturesCount) return null
            return entries.singleOrNull { it.vibhakti == vibhakti && it.vacana == vacana }
        }

    }
}
