package dev.sanskrit.shiksha

enum class Svara(
    override val devanagari: String,
    val matra: String?,
) : Varna {
    A("अ", null),
    AA("आ", "ा"),
    I("इ", "ि"),
    II("ई", "ी"),
    U("उ", "ु"),
    UU("ऊ", "ू"),
    R("ऋ", "ृ"),
    RR("ॠ", "ॄ"),
    L("ऌ", "ॢ"),
    LL("ॡ", "ॣ"),
    E("ए", "े"),
    AI("ऐ", "ै"),
    O("ओ", "ो"),
    AU("औ", "ौ");

    companion object {
        fun fromIndependent(value: Char): Svara? =
            entries.firstOrNull { it.devanagari == value.toString() }

        fun fromMatra(value: Char): Svara? =
            entries.firstOrNull { it.matra == value.toString() }
    }
}
