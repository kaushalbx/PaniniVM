package dev.panini.core

/** Canonical grammatical affix identity, independent of its current surface. */
sealed interface TypedAffix {
    val upadesha: String
    val initialSurface: String
    val itMarkers: Set<ItMarker>
    val alternateUpadeshas: Set<String> get() = emptySet()
}

enum class SanadiAffix(
    override val upadesha: String,
    override val initialSurface: String = upadesha,
    override val itMarkers: Set<ItMarker> = emptySet(),
) : TypedAffix {
    NIC("णिच्"),
    SAN("सन्"),
    YANG("यङ्"),
    KYAC("क्यच्"),
    KYANG("क्यङ्"),
    KAMYAC("काम्यच्");

    companion object {
        fun fromUpadesha(value: String): SanadiAffix? = entries.singleOrNull { it.upadesha == value.trim() }
    }
}

enum class KrtAffix(
    override val upadesha: String,
    override val initialSurface: String = upadesha,
    override val itMarkers: Set<ItMarker> = emptySet(),
    override val alternateUpadeshas: Set<String> = emptySet(),
) : TypedAffix {
    KTA("क्त"),
    KTAVATU("क्तवतुँ"),
    KTVA("क्त्वा"),
    NAMUL("णमुल्"),
    LYAP("ल्यप्"),
    TUMUN("तुमुँन्"),
    TAVYAT("तव्यत्"),
    ANIYAR("अनीयर्", alternateUpadeshas = setOf("अनीयर")),
    YAT("यत्", alternateUpadeshas = setOf("यत")),
    NYAT("ण्यत्"),
    NVUL("ण्वुल्"),
    TRC("तृच्"),
    GHAN("घञ्"),
    LYUT("ल्युट्");

    companion object {
        fun fromUpadesha(value: String): KrtAffix? = when (val normalized = value.trim()) {
            "अनीयर" -> ANIYAR
            "यत" -> YAT
            "अन" -> LYUT
            else -> entries.singleOrNull { it.upadesha == normalized }
        }
    }
}
