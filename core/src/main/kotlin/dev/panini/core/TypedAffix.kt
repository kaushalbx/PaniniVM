package dev.panini.core

/** Canonical grammatical affix identity, independent of its current surface. */
sealed interface TypedAffix {
    val upadesha: String
    val initialSurface: String
    val itMarkers: Set<ItMarker>
    val alternateUpadeshas: Set<String> get() = emptySet()
    val pronunciationOnlyVarnaIndices: Set<Int> get() = emptySet()
}

/** Kit augments whose source vowel is pronunciation-only, not a phonological segment. */
enum class KitAugment(override val upadesha: String, override val pronunciationOnlyVarnaIndices: Set<Int>) : TypedAffix {
    YUK("युक्", setOf(1)),
    MUK("मुक्", setOf(1));
    override val initialSurface: String get() = upadesha
    override val itMarkers: Set<ItMarker> get() = emptySet()
}

/** Mit augments retain pronunciation-only material in their source upadeśa. */
enum class MitAugment(override val upadesha: String, override val pronunciationOnlyVarnaIndices: Set<Int>) : TypedAffix {
    NUM("नुम्", setOf(1));
    override val initialSurface: String get() = upadesha
    override val itMarkers: Set<ItMarker> get() = emptySet()
}

/** Taddhita source identities; recognition does not imply every derivation rule is implemented. */
enum class TaddhitaAffix(override val upadesha: String) : TypedAffix {
    AN("अण्"), INY("इञ्"), YANY("यञ्"), PHAK("फक्"), DHAK("ढक्"),
    VAT("वत्"), TASIL("तसिल्"), TRAL("त्रल्"),
    MATUP("मतुप्"), VATUP("वतुप्"), TVA("त्व"), TAL("तल्"), TARAP("तरप्"), TAMAP("तमप्");

    override val initialSurface: String get() = upadesha
    override val itMarkers: Set<ItMarker> get() = emptySet()

    companion object {
        fun fromUpadesha(text: String): TaddhitaAffix? = when (val normalized = text.trim()) {
            // Legacy source notation, not an additional original affix.
            "मत्" -> MATUP
            else -> entries.singleOrNull { it.upadesha == normalized }
        }
    }
}

/** Current substitute identities, not additional original taddhita affixes. */
enum class TaddhitaAdesha(override val upadesha: String) : TypedAffix {
    AYAN("आयन्"), EY("एय्"), IN("ईन्"), IY("ईय्"), IYA("इय्");

    override val initialSurface: String get() = upadesha
    override val itMarkers: Set<ItMarker> get() = emptySet()
}

/** Frequency affixes; their pronunciation-only vowels are excluded at term construction. */
enum class FrequencyAffix(
    override val upadesha: String,
    override val pronunciationOnlyVarnaIndices: Set<Int>,
) : TypedAffix {
    KRTVASUC("कृत्वसुच्", setOf(6)),
    SUC("सुच्", setOf(1));

    override val initialSurface: String get() = upadesha
    override val itMarkers: Set<ItMarker> get() = emptySet()
}

enum class SanadiAffix(
    override val upadesha: String,
    override val initialSurface: String = upadesha,
    override val itMarkers: Set<ItMarker> = emptySet(),
) : TypedAffix {
    NIC("णिच्"),
    SAN("सन्"),
    YANG("यङ्"),
    IYANG("ईयङ्"),
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
    override val pronunciationOnlyVarnaIndices: Set<Int> = emptySet(),
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
    LYUT("ल्युट्"),
    KVIP("क्विप्", pronunciationOnlyVarnaIndices = setOf(2));

    companion object {
        fun fromUpadesha(value: String): KrtAffix? = when (val normalized = value.trim()) {
            "अन" -> LYUT
            else -> entries.singleOrNull { it.upadesha == normalized || normalized in it.alternateUpadeshas }
        }
    }
}
