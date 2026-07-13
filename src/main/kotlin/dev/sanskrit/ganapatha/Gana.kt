package dev.sanskrit.ganapatha

abstract class Gana(
    val id: GanaIds,
    val name: String,
    val sourceIndex: Int,
    val sutra: String,
    val sutraId: String,
    val sutraText: String,
    val sutraTransliteration: String,
    val sutraType: String,
    val kind: GanaKind,
    val sanskritMeaning: String = "",
    val hindiMeaning: String = "",
    val englishMeaning: String = "",
    val members: List<GanaMember>,
    val source: GanaSource = GanaSource.GANAPATHA_DATA,
    val sourceUrl: String = GanaPathaSources.DATA_URL,
    val vartika: String = "",
    val rawWords: String = members.joinToString(" । ", postfix = " ॥") { it.text },
    val notes: String? = null,
) {
    private val membersByNormalized: Map<String, GanaMember> =
        members.associateBy { it.normalized }

    val memberTexts: List<String>
        get() = members.map { it.text }

    val normalizedMemberTexts: Set<String>
        get() = membersByNormalized.keys

    fun contains(text: String): Boolean =
        findMember(text) != null

    fun findMember(text: String): GanaMember? =
        membersByNormalized[GanaNormalizer.normalize(text)]

    fun requireMember(text: String): GanaMember =
        findMember(text) ?: error("Member $text is not present in gana ${id.key}.")

    fun hasMeaning(): Boolean =
        sanskritMeaning.isNotBlank() || hindiMeaning.isNotBlank() || englishMeaning.isNotBlank()
}

enum class GanaSource {
    GANAPATHA_DATA,
}

enum class GanaKind {
    PATHA,
    AKRTI,
}

enum class GanaIds(val key: String) {
    SARVADI("sarvadi"),
    SVARADI("svaradi"),
    CHADI("chadi"),
    PRADI("pradi"),
    URYADI("uryadi"),
    SAKSHATPRABHRTI("sakshatprabhrti"),
    TISHTHADGUPRABHRTI("tishthadguprabhrti"),
    SHAUNDADI("shaundadi"),
    PATRESAMITADI("patresamitadi"),
    VYAGHRADI("vyaghradi"),
    SHRENYADI("shrenyadi"),
    KRTADI("krtadi"),
    SHAKAPARTHIVADI("shakaparthivadi"),
    SHRAMANADI("shramanadi"),
    MAYURAVYAMSAKADI("mayuravyamsakadi"),
    YAJAKADI("yajakadi"),
    RAJADANTADI("rajadantadi"),
    AHITAGNYADI("ahitagnyadi"),
    KADARADI("kadaradi"),
    NAVADI("navadi"),
    PRAKRTYADI("prakrtyadi"),
    PRATYADI("pratyadi"),
    GAVASHVADI("gavashvadi"),
    DADHIPAYASYADI("dadhipayasyadi"),
    ARDHARCADI("ardharcadi"),
    PAILADI("pailadi"),
    TAULVALYADI("taulvalyadi"),
    YASKADI("yaskadi"),
    GOPAVANADI("gopavanadi"),
    TIKAKITAVADI("tikakitavadi"),
    UPAKADI("upakadi"),
    BHRSHADI("bhrshadi"),
    LOHITADI("lohitadi"),
    SUKHADI("sukhadi"),
    KANDVADI("kandvadi"),
    NANDYADI("nandyadi"),
    GRAHYADI("grahyadi"),
    PACADI("pacadi"),
    MULAVIBHUJADI("mulavibhujadi"),
    PARSHVADI("parshvadi"),
    GAMYADI("gamyadi"),
    BHIDADI("bhidadi"),
    SAMPADADI("sampadadi"),
    BHIMADI("bhimadi"),
    AJADI("ajadi"),
    SVASRADI("svasradi"),
    SAMANADI("samanadi"),
    GAURADI("gauradi"),
    BAHVADI("bahvadi"),
    KRODADI("krodadi"),
    SHARNGARAVADI("sharngaravadi"),
    KRAUDYADI("kraudyadi"),
    ASHVAPATYADI("ashvapatyadi"),
    UTSADI("utsadi"),
    BAAHVADI("baahvadi"),
    KUNJADI("kunjadi"),
    NADADI("nadadi"),
    BIDADI("bidadi"),
    GARGADI("gargadi"),
    ASHVADI("ashvadi"),
    SHIVADI("shivadi"),
    SHUBHRADI("shubhradi"),
    KALYANYADI("kalyanyadi"),
    GRSHTYADI("grshtyadi"),
    REVATYADI("revatyadi"),
    KURVADI("kurvadi"),
    TIKADI("tikadi"),
    VAKINADI("vakinadi"),
    KAMBOJADI("kambojadi"),
    BHARGADI("bhargadi"),
    YAUDHEYADI("yaudheyadi"),
}

object GanaPathaSources {
    const val PUBLIC_PAGE_URL: String = "https://ashtadhyayi.com/ganapath"
    const val DATA_URL: String =
        "https://cdn.jsdelivr.net/gh/ashtadhyayi-com/data@4a63049/ganapath/data.txt"
}

object GanaNormalizer {
    fun normalize(value: String): String =
        value.trim()
}

